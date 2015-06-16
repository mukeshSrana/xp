package com.enonic.xp.admin.impl.rest.resource.repo;

import java.nio.file.Paths;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.google.common.base.Preconditions;

import com.enonic.xp.admin.impl.AdminResource;
import com.enonic.xp.admin.impl.rest.resource.ResourceConstants;
import com.enonic.xp.index.DumpDataParams;
import com.enonic.xp.index.LoadDataParams;
import com.enonic.xp.index.SystemExportService;
import com.enonic.xp.security.RoleKeys;
import com.enonic.xp.vfs.VirtualFiles;

@Path(ResourceConstants.REST_ROOT + "system")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed(RoleKeys.ADMIN_ID)
@Component(immediate = true)
public class SystemDataResource
    implements AdminResource
{
    private SystemExportService systemExportService;

    private static final int DEFAULT_BATCH_SIZE = 1000;

    private static final int DEFAULT_TIMEOUT = 2;

    @POST
    @Path("dump")
    public void dump( final DumpRequestJson request )
    {
        Preconditions.checkNotNull( request, "no request json provided" );

        this.systemExportService.dump( DumpDataParams.create().
            setRepositories( request.getRepositoryIds() ).
            dumpPath( Paths.get( "dump" ) ).
            batchSize( request.getBatchSize() != null ? request.getBatchSize() : DEFAULT_BATCH_SIZE ).
            timeout( request.getTimeout() != null ? request.getTimeout() : DEFAULT_TIMEOUT ).
            build() );
    }


    @POST
    @Path("load")
    public void load( final LoadDataRequestJson request )
    {
        Preconditions.checkNotNull( request, "no request json provided" );

        this.systemExportService.load( LoadDataParams.create().
            batchSize( 100 ).
            dumpRoot( VirtualFiles.from( Paths.get( request.getDumpRoot() ) )).
            build() );
    }


    @Reference
    public void setSystemExportService( final SystemExportService systemExportService )
    {
        this.systemExportService = systemExportService;
    }
}
