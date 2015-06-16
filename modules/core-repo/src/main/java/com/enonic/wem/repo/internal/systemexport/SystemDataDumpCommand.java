package com.enonic.wem.repo.internal.systemexport;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.codehaus.jparsec.util.Lists;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;

import com.enonic.wem.repo.internal.elasticsearch.ScanAndScrollParams;
import com.enonic.wem.repo.internal.index.result.ScrollResult;
import com.enonic.wem.repo.internal.repository.IndexNameResolver;
import com.enonic.xp.index.IndexType;
import com.enonic.xp.repository.RepositoryId;

public class SystemDataDumpCommand
    extends AbstractSystemDataCommand
{
    private final Path dumpPath;

    private final List<RepositoryId> repositoryIdList;

    private final int scrollSize;

    private final int openTimeSeconds;

    private SystemDataDumpCommand( final Builder builder )
    {
        super( builder );
        this.dumpPath = builder.path;
        this.repositoryIdList = builder.repositoryIdList;
        this.scrollSize = builder.scrollSize;
        this.openTimeSeconds = builder.openTimeSeconds;

    }

    public static Builder create()
    {
        return new Builder();
    }

    public void execute()
    {
        this.repositoryIdList.forEach( this::dumpRepository );
    }

    private void dumpRepository( final RepositoryId repositoryId )
    {
        dumpStorageIndex( repositoryId );
    }

    private void dumpStorageIndex( final RepositoryId repositoryId )
    {
        final String storageIndex = IndexNameResolver.resolveStorageIndexName( repositoryId );

        dumpType( storageIndex, IndexType.BRANCH, repositoryId );
        dumpType( storageIndex, IndexType.VERSION, repositoryId );
    }

    private void dumpType( final String indexName, final IndexType indexType, final RepositoryId repositoryId )
    {
        final ScrollResult scrollResult = this.internalIndexService.startScanScroll( ScanAndScrollParams.create().
            index( indexName ).
            type( indexType.getName() ).
            size( this.scrollSize ).
            keepAliveSecond( this.openTimeSeconds ).
            build() );

        final DumpWriter dumpWriter = createDumpWriter( repositoryId, indexType );

        doScroll( scrollResult.getScrollId(), dumpWriter );

        dumpWriter.close();
    }

    private void doScroll( final String scrollId, final DumpWriter writer )
    {
        final ScrollResult result = this.internalIndexService.nextScanScroll( scrollId, this.openTimeSeconds );

        if ( result.getHits().getSize() > 0 )
        {
            writer.write( result.getHits() );

            doScroll( result.getScrollId(), writer );
        }
    }

    private DumpWriter createDumpWriter( final RepositoryId repositoryId, final IndexType indexType )
    {
        switch ( indexType )
        {
            case VERSION:
                return new DumpWriter( this.versionSerializer, createWriter( repositoryId, indexType ) );
            case BRANCH:
                return new DumpWriter( this.branchSerializer, createWriter( repositoryId, indexType ) );
            default:
                throw new IllegalArgumentException( "Cannot serialize index of type: " + indexType.getName() );
        }
    }

    private BufferedWriter createWriter( final RepositoryId repositoryId, final IndexType indexType )
    {
        final Path storagePath =
            Paths.get( this.dumpPath.toString(), repositoryId.toString(), IndexNameResolver.resolveStorageIndexName( repositoryId ),
                       indexType.getName() );

        try
        {
            Files.createDirectories( storagePath );
            final BufferedWriter bufferedWriter =
                Files.newBufferedWriter( Paths.get( storagePath.toString(), DUMP_FILE_NAME ), Charsets.UTF_8 );
            return bufferedWriter;
        }
        catch ( IOException e )
        {
            throw new RuntimeException( "Not able to create file " + storagePath, e );
        }
    }

    public static class Builder
        extends AbstractSystemDataCommand.Builder<Builder>
    {
        private Path path;

        private List<RepositoryId> repositoryIdList = Lists.arrayList();

        private int scrollSize = 1000;

        private int openTimeSeconds = 2;

        private Builder()
        {
        }

        public Builder path( Path path )
        {
            this.path = path;
            return this;
        }

        public Builder setRepositories( final List<RepositoryId> repositoryIdList )
        {
            this.repositoryIdList = repositoryIdList;
            return this;
        }

        public Builder addRepository( final RepositoryId repositoryId )
        {
            this.repositoryIdList.add( repositoryId );
            return this;
        }

        public Builder scrollSize( int scrollSize )
        {
            this.scrollSize = scrollSize;
            return this;
        }

        public Builder openTimeSeconds( int openTimeSeconds )
        {
            this.openTimeSeconds = openTimeSeconds;
            return this;
        }


        private void validate()
        {
            Preconditions.checkNotNull( indexService, "IndexService must be set" );
            Preconditions.checkNotNull( path, "Dump-path must be set" );
            Preconditions.checkNotNull( repositoryIdList, "Repositories to dump must be given" );
        }

        public SystemDataDumpCommand build()
        {
            validate();
            return new SystemDataDumpCommand( this );
        }
    }
}
