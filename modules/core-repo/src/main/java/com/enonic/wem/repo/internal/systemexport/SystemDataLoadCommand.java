package com.enonic.wem.repo.internal.systemexport;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import com.enonic.wem.repo.internal.elasticsearch.ElasticsearchDao;
import com.enonic.wem.repo.internal.repository.IndexNameResolver;
import com.enonic.wem.repo.internal.repository.RepositoryInitializer;
import com.enonic.xp.branch.Branch;
import com.enonic.xp.index.IndexService;
import com.enonic.xp.index.IndexType;
import com.enonic.xp.index.ReindexParams;
import com.enonic.xp.repository.RepositoryId;
import com.enonic.xp.vfs.VirtualFile;
import com.enonic.xp.vfs.VirtualFilePath;

public class SystemDataLoadCommand
    extends AbstractSystemDataCommand
{
    private final VirtualFile dumpPath;

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
        Preconditions.checkArgument( dumpPath.exists(), "Cannot find dump-root at path " + dumpPath.getPath() );

        dumpPath.getChildren().forEach( this::handleRepo );
    }

    private void handleRepo( final VirtualFile repoFolder )
    {
        final String repoName = repoFolder.getName();

        LOG.info( "Load system data: Initializing repo '" + repoName + "'" );

        repositoryInitializer.initializeRepository( RepositoryId.from( repoName ), true );

        readRepoFolder( repoFolder );

        internalIndexService.refresh( IndexNameResolver.resolveStorageIndexName( RepositoryId.from( repoName ) ) );

        // TODO: Reindex all branches, not hardcoded
        indexService.reindex( ReindexParams.create().
            addBranch( Branch.from( "draft" ) ).
            addBranch( Branch.from( "master" ) ).
            initialize( false ).
            repositoryId( RepositoryId.from( repoName ) ).
            build() );

        internalIndexService.refresh( IndexNameResolver.resolveSearchIndexName( RepositoryId.from( repoName ) ) );
    }

    private void readRepoFolder( final VirtualFile repoFolder )
    {
        final List<VirtualFile> indexFolders = repoFolder.getChildren();

        for ( final VirtualFile indexFolder : indexFolders )
        {
            indexFolder.getChildren().forEach( ( typeFolder ) -> handleType( typeFolder, repoFolder.getName() ) );
        }
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
        private VirtualFile dumpPath;

        private int scrollSize;

        private RepositoryInitializer repositoryInitializer;

        private ElasticsearchDao elasticsearchDao;

        private IndexService indexService;

        private Builder()
        {
        }

        public Builder dumpRoot( VirtualFile dumpPath )
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
