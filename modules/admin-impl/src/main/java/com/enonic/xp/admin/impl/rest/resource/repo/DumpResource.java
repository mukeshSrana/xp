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
import com.enonic.xp.index.DumpParams;
import com.enonic.xp.index.DumpService;
import com.enonic.xp.security.RoleKeys;

@Path(ResourceConstants.REST_ROOT + "repo")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed(RoleKeys.ADMIN_ID)
@Component(immediate = true)
public class DumpResource
    implements AdminResource
{
    private DumpService dumpService;

    private static final int DEFAULT_BATCH_SIZE = 1000;

    private static final int DEFAULT_TIMEOUT = 2;

    @POST
    @Path("dump")
    public void reindex( final DumpRequestJson request )
    {
        Preconditions.checkNotNull( request, "no request json provided" );

        this.dumpService.dump( DumpParams.create().
            setRepositories( request.getRepositoryIds() ).
            dumpPath( Paths.get( "dump" ) ).
            batchSize( request.getBatchSize() != null ? request.getBatchSize() : DEFAULT_BATCH_SIZE ).
            timeout( request.getTimeout() != null ? request.getTimeout() : DEFAULT_TIMEOUT ).
            build() );
    }

    @Reference
    public void setDumpService( final DumpService dumpService )
    {
        this.dumpService = dumpService;
    }
}
