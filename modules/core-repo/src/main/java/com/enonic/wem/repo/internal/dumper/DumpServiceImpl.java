package com.enonic.wem.repo.internal.dumper;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.enonic.wem.repo.internal.index.IndexServiceInternal;
import com.enonic.xp.index.DumpParams;
import com.enonic.xp.index.DumpService;

@Component
public class DumpServiceImpl
    implements DumpService
{
    private IndexServiceInternal indexServiceInternal;

    @Override
    public void dump( final DumpParams params )
    {
        DumpCommand.create().
            indexService( this.indexServiceInternal ).
            path( params.getDumpPath() ).
            openTimeSeconds( params.getTimeout() ).
            setRepositories( params.getRepositoryIds() ).
            scrollSize( params.getBatchSize() ).
            build().
            execute();
    }

    @Reference
    public void setIndexServiceInternal( final IndexServiceInternal indexServiceInternal )
    {
        this.indexServiceInternal = indexServiceInternal;
    }
}
