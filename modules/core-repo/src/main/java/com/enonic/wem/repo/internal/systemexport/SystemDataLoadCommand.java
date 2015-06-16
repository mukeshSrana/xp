package com.enonic.wem.repo.internal.systemexport;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import com.enonic.wem.repo.internal.elasticsearch.ElasticsearchDao;
import com.enonic.wem.repo.internal.repository.RepositoryInitializer;
import com.enonic.xp.branch.Branch;
import com.enonic.xp.index.IndexService;
import com.enonic.xp.index.IndexType;
import com.enonic.xp.index.ReindexParams;
import com.enonic.xp.repository.RepositoryId;
import com.enonic.xp.vfs.VirtualFile;
import com.enonic.xp.vfs.VirtualFilePath;
import com.enonic.xp.vfs.VirtualFiles;

public class SystemDataLoadCommand
    extends AbstractSystemDataCommand
{
    private final Path dumpPath;

    private final int scrollSize;

    private final RepositoryInitializer repositoryInitializer;

    private final ElasticsearchDao elasticsearchDao;

    private final IndexService indexService;

    private final static Logger LOG = LoggerFactory.getLogger( SystemDataLoadCommand.class );

    private SystemDataLoadCommand( final Builder builder )
    {
        super( builder );

        this.repositoryInitializer = builder.repositoryInitializer;
        this.dumpPath = builder.dumpPath;
        this.scrollSize = builder.scrollSize;
        this.elasticsearchDao = builder.elasticsearchDao;
        this.indexService = builder.indexService;
    }

    public void execute()
    {
        final VirtualFile vfRoot = VirtualFiles.from( dumpPath );
        Preconditions.checkArgument( vfRoot.exists(), "Cannot find dump-root at path " + dumpPath );

        vfRoot.getChildren().forEach( this::handleRepo );
    }

    private void handleRepo( final VirtualFile repoFolder )
    {
        final String repoName = repoFolder.getName();

        LOG.info( "Load system data: Initializing repo '" + repoName + "'" );

        repositoryInitializer.initializeRepository( RepositoryId.from( repoName ), true );

        readDump( repoFolder );

        indexService.reindex( ReindexParams.create().
            addBranch( Branch.from( "draft" ) ).
            addBranch( Branch.from( "master" ) ).
            initialize( false ).
            repositoryId( RepositoryId.from( repoName ) ).
            build() );
    }

    private void readDump( final VirtualFile repoFolder )
    {
        repoFolder.getChildren().forEach( ( typeFolder ) -> handleType( typeFolder, repoFolder.getName() ) );
    }

    private void handleType( final VirtualFile typeFolder, final String repoName )
    {
        // Initialize reader with type-folder and repo

        final IndexType indexType = IndexType.fromString( typeFolder.getName() );

        LOG.info( "Load system data: Loading data for type '" + typeFolder.getName() + "'" );

        final VirtualFilePath dumpFilePath = typeFolder.getPath().join( DUMP_FILE_NAME );

        final VirtualFile dumpFile = typeFolder.resolve( dumpFilePath );

        Preconditions.checkArgument( dumpFile.exists(), "dump-file not found at path: " + dumpFilePath );

        DumpLoader.create().
            dumpFile( dumpFile ).
            repositoryId( RepositoryId.from( repoName ) ).
            dumpSerializer( getSerializer( indexType ) ).
            entryFactory( getEntryFactory( indexType ) ).
            elasticsearchDao( this.elasticsearchDao ).
            build().
            load();
    }

    private DumpSerializer getSerializer( final IndexType indexType )
    {
        switch ( indexType )
        {
            case BRANCH:
                return this.branchSerializer;
            case VERSION:
                return this.versionSerializer;
            default:
                throw new IllegalArgumentException( "Cannot read dump of index-type: '" + indexType + "'" );
        }
    }

    private DumpEntryFactory getEntryFactory( final IndexType indexType )
    {
        switch ( indexType )
        {
            case BRANCH:
                return new BranchEntryFactory();
            case VERSION:
                return new VersionEntryFactory();
            default:
                throw new IllegalArgumentException( "Cannot read dump of index-type: '" + indexType + "'" );
        }
    }

    public static Builder create()
    {
        return new Builder();
    }

    public static final class Builder
        extends AbstractSystemDataCommand.Builder<Builder>
    {
        private Path dumpPath;

        private int scrollSize;

        private RepositoryInitializer repositoryInitializer;

        private ElasticsearchDao elasticsearchDao;

        private IndexService indexService;

        private Builder()
        {
        }

        public Builder dumpRoot( Path dumpPath )
        {
            this.dumpPath = dumpPath;
            return this;
        }

        public Builder batchSize( int scrollSize )
        {
            this.scrollSize = scrollSize;
            return this;
        }

        public Builder repositoryIntitalizer( final RepositoryInitializer repositoryInitializer )
        {
            this.repositoryInitializer = repositoryInitializer;
            return this;
        }

        public Builder elasticsearchDao( final ElasticsearchDao elasticsearchDao )
        {
            this.elasticsearchDao = elasticsearchDao;
            return this;
        }

        public Builder indexService( final IndexService indexService )
        {
            this.indexService = indexService;
            return this;
        }

        public SystemDataLoadCommand build()
        {
            return new SystemDataLoadCommand( this );
        }
    }
}
