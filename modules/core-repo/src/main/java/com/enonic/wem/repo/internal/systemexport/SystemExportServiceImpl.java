package com.enonic.wem.repo.internal.systemexport;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.enonic.wem.repo.internal.elasticsearch.ElasticsearchDao;
import com.enonic.wem.repo.internal.index.IndexServiceInternal;
import com.enonic.wem.repo.internal.repository.RepositoryInitializer;
import com.enonic.xp.index.DumpDataParams;
import com.enonic.xp.index.IndexService;
import com.enonic.xp.index.LoadDataParams;
import com.enonic.xp.index.SystemExportService;

@Component
public class SystemExportServiceImpl
    implements SystemExportService
{
    private IndexServiceInternal indexServiceInternal;

    private ElasticsearchDao elasticsearchDao;

    private IndexService indexService;

    @Override
    public void dump( final DumpDataParams params )
    {
        SystemDataDumpCommand.create().
            indexService( this.indexServiceInternal ).
            path( params.getDumpPath() ).
            openTimeSeconds( params.getTimeout() ).
            setRepositories( params.getRepositoryIds() ).
            scrollSize( params.getBatchSize() ).
            build().
            execute();
    }

    @Override
    public void load( final LoadDataParams params )
    {
        final RepositoryInitializer repositoryInitializer = new RepositoryInitializer( this.indexServiceInternal );

        SystemDataLoadCommand.create().
            indexService( this.indexServiceInternal ).
            dumpRoot( params.getDumpRoot() ).
            batchSize( params.getBatchSize() ).
            repositoryIntitalizer( repositoryInitializer ).
            elasticsearchDao( this.elasticsearchDao ).
            indexService( this.indexService ).
            build().
            execute();
    }

    @Reference
    public void setIndexServiceInternal( final IndexServiceInternal indexServiceInternal )
    {
        this.indexServiceInternal = indexServiceInternal;
    }

    @Reference
    public void setElasticsearchDao( final ElasticsearchDao elasticsearchDao )
    {
        this.elasticsearchDao = elasticsearchDao;
    }

    @Reference
    public void setIndexService( final IndexService indexService )
    {
        this.indexService = indexService;
    }
}
