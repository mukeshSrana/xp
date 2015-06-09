package com.enonic.wem.repo.internal.dumper;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.codehaus.jparsec.util.Lists;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;

import com.enonic.wem.repo.internal.elasticsearch.ScanAndScrollParams;
import com.enonic.wem.repo.internal.index.IndexServiceInternal;
import com.enonic.wem.repo.internal.index.result.ScrollResult;
import com.enonic.wem.repo.internal.index.result.SearchResultEntries;
import com.enonic.wem.repo.internal.index.result.SearchResultEntry;
import com.enonic.wem.repo.internal.repository.IndexNameResolver;
import com.enonic.xp.index.IndexType;
import com.enonic.xp.repository.RepositoryId;

public class DumpCommand
{
    private final Path dumpPath;

    private final List<RepositoryId> repositoryIdList;

    private final int scrollSize;

    private final int openTimeSeconds;

    private final IndexServiceInternal indexService;

    private final DumpJsonSerializer serializer = DumpJsonSerializer.create( false );

    private DumpCommand( Builder builder )
    {
        dumpPath = builder.path;
        repositoryIdList = builder.repositoryIdList;
        scrollSize = builder.scrollSize;
        openTimeSeconds = builder.openTimeSeconds;
        indexService = builder.indexService;
    }

    public static Builder create()
    {
        return new Builder();
    }

    public void execute()
    {
        for ( final RepositoryId repositoryId : this.repositoryIdList )
        {
            dumpRepository( repositoryId );
        }
    }

    private void dumpRepository( final RepositoryId repositoryId )
    {
        dumpStorageIndex( repositoryId );
    }

    private void dumpStorageIndex( final RepositoryId repositoryId )
    {
        final String storageIndex = IndexNameResolver.resolveStorageIndexName( repositoryId );

        dumpType( storageIndex, IndexType.BRANCH.getName(), repositoryId );
        dumpType( storageIndex, IndexType.VERSION.getName(), repositoryId );
    }

    private void dumpType( final String indexName, final String indexType, final RepositoryId repositoryId )
    {
        final ScrollResult scrollResult = this.indexService.startScanScroll( ScanAndScrollParams.create().
            index( indexName ).
            type( indexType ).
            size( this.scrollSize ).
            keepAliveSecond( this.openTimeSeconds ).
            build() );

        final Writer writer = createWriter( repositoryId, indexType );
        doScroll( scrollResult.getScrollId(), writer );

        IOUtils.closeQuietly( writer );

    }

    private void doScroll( final String scrollId, final Writer writer )
    {
        final ScrollResult result = this.indexService.nextScanScroll( scrollId, this.openTimeSeconds );

        if ( result.getHits().getSize() > 0 )
        {
            write( result.getHits(), writer );

            doScroll( result.getScrollId(), writer );
        }
    }

    private void write( final SearchResultEntries entries, final Writer writer )
    {
        for ( final SearchResultEntry entry : entries )
        {
            try
            {
                writer.write( this.serializer.toString( entry ) );
            }
            catch ( Exception e )
            {
                throw new RuntimeException( "not able to serialize entry: " + entry );
            }
        }
    }

    private Writer createWriter( final RepositoryId repositoryId, final String indexType )
    {
        final Path storagePath = Paths.get( this.dumpPath.toString(), repositoryId.toString(), indexType );

        try
        {
            Files.createDirectories( storagePath );
            return Files.newBufferedWriter( Paths.get( storagePath.toString(), indexType ), Charsets.UTF_8 );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( "Not able to create file " + storagePath, e );
        }
    }

    public static final class Builder
    {
        private Path path;

        private List<RepositoryId> repositoryIdList = Lists.arrayList();

        private int scrollSize = 1000;

        private int openTimeSeconds = 2;

        private IndexServiceInternal indexService;

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

        public Builder indexService( IndexServiceInternal indexService )
        {
            this.indexService = indexService;
            return this;
        }

        private void validate()
        {
            Preconditions.checkNotNull( indexService, "IndexService must be set" );
            Preconditions.checkNotNull( path, "Dump-path must be set" );
            Preconditions.checkNotNull( repositoryIdList, "Repositories to dump must be given" );
        }

        public DumpCommand build()
        {
            validate();
            return new DumpCommand( this );
        }
    }
}
