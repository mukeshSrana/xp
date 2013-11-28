package com.enonic.wem.admin.rest.resource.module;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;

import com.enonic.wem.admin.json.module.ListModuleJson;
import com.enonic.wem.admin.json.module.ModuleSummaryJson;
import com.enonic.wem.admin.rest.resource.AbstractResource;
import com.enonic.wem.admin.rest.resource.module.json.InstallModuleResultJson;
import com.enonic.wem.admin.rest.resource.module.json.ListModuleResultJson;
import com.enonic.wem.admin.rest.resource.module.json.ModuleDeleteParams;
import com.enonic.wem.admin.rest.resource.module.json.ModuleDeleteResultJson;
import com.enonic.wem.api.command.Commands;
import com.enonic.wem.api.command.module.CreateModule;
import com.enonic.wem.api.command.module.DeleteModule;
import com.enonic.wem.api.command.module.GetModule;
import com.enonic.wem.api.module.Module;
import com.enonic.wem.api.module.ModuleKey;
import com.enonic.wem.api.module.ModuleNotFoundException;
import com.enonic.wem.api.module.Modules;
import com.enonic.wem.core.exporters.ModuleExporter;
import com.enonic.wem.core.module.ModuleImporter;
import com.enonic.wem.core.servlet.MultipartHelper;

import static com.enonic.wem.api.command.Commands.module;

@javax.ws.rs.Path("module")
@Produces(MediaType.APPLICATION_JSON)
public class ModuleResource
    extends AbstractResource
{
    private static final String ZIP_MIME_TYPE = "application/zip";

    @GET
    @javax.ws.rs.Path("list")
    public ListModuleResultJson list()
    {
        Modules modules = client.execute( Commands.module().list() );
        return ListModuleResultJson.result( new ListModuleJson( modules ) );
    }


    @POST
    @javax.ws.rs.Path("delete")
    public ModuleDeleteResultJson delete( ModuleDeleteParams params )
    {
        DeleteModule command = Commands.module().delete().module( params.getModuleKey() );
        boolean deleted = client.execute( command );
        if ( deleted )
        {
            return ModuleDeleteResultJson.result( params.getModuleKey() );
        }
        else
        {
            return ModuleDeleteResultJson.error( "Module: '" + params.getModuleKey().toString() + "' as not found" );
        }
    }

    @POST
    @javax.ws.rs.Path("install")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public InstallModuleResultJson install( HttpServletRequest req )
        throws Exception
    {
        final Part part = req.getPart( "file" );
        final String fileName = MultipartHelper.getFileName( part );
        final Path tempDirectory = Files.createTempDirectory( "modules" );

        try
        {
            final Path tempZipFile = tempDirectory.resolve( fileName );
            Files.copy( part.getInputStream(), tempZipFile );
            final ModuleImporter moduleImporter = new ModuleImporter();
            final Module importedModule;
            try
            {
                importedModule = moduleImporter.importModuleFromZip( tempZipFile );
            }
            catch ( Exception e )
            {
                return InstallModuleResultJson.error( e.getMessage() );
            }

            final CreateModule createModuleCommand = CreateModule.fromModule( importedModule );
            final Module createdModule = client.execute( createModuleCommand );

            return InstallModuleResultJson.result( new ModuleSummaryJson( createdModule ) );
        }
        finally
        {
            FileUtils.deleteDirectory( tempDirectory.toFile() );
        }
    }

    @GET
    @javax.ws.rs.Path("export")
    public javax.ws.rs.core.Response export( @QueryParam("moduleKey") String moduleKeyParam )
        throws IOException
    {
        final ModuleKey moduleKey;
        try
        {
            moduleKey = ModuleKey.from( moduleKeyParam );
        }
        catch ( Exception e )
        {
            return Response.status( Response.Status.BAD_REQUEST ).build();
        }

        final GetModule getModuleCommand = module().get().module( moduleKey );
        final Module module;
        try
        {
            module = client.execute( getModuleCommand );
        }
        catch ( ModuleNotFoundException e )
        {
            return Response.status( Response.Status.NOT_FOUND ).build();
        }

        final Path tempDirectory = Files.createTempDirectory( "modules" );
        try
        {
            final ModuleExporter moduleExporter = new ModuleExporter();
            final Path moduleFilePath = moduleExporter.exportToZip( module, tempDirectory );
            final byte[] zipContents = Files.readAllBytes( moduleFilePath );

            final String fileName = moduleFilePath.getFileName().toString();
            return Response.ok( zipContents, ZIP_MIME_TYPE ).header( "Content-Disposition", "attachment; filename=" + fileName ).build();
        }
        finally
        {
            FileUtils.deleteDirectory( tempDirectory.toFile() );
        }
    }
}
