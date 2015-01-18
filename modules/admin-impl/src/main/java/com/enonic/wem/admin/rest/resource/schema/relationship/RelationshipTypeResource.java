package com.enonic.wem.admin.rest.resource.schema.relationship;

import java.awt.image.BufferedImage;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;

import com.enonic.wem.admin.AdminResource;
import com.enonic.wem.admin.json.schema.relationship.RelationshipTypeJson;
import com.enonic.wem.admin.json.schema.relationship.RelationshipTypeListJson;
import com.enonic.wem.admin.rest.resource.ResourceConstants;
import com.enonic.wem.admin.rest.resource.schema.SchemaImageHelper;
import com.enonic.wem.api.Icon;
import com.enonic.wem.api.module.ModuleKey;
import com.enonic.wem.api.schema.relationship.RelationshipType;
import com.enonic.wem.api.schema.relationship.RelationshipTypeName;
import com.enonic.wem.api.schema.relationship.RelationshipTypeService;
import com.enonic.wem.api.schema.relationship.RelationshipTypes;

@Path(ResourceConstants.REST_ROOT + "schema/relationship")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("admin-login")
public final class RelationshipTypeResource
    implements AdminResource
{
    private static final String DEFAULT_MIME_TYPE = "image/png";

    private static final SchemaImageHelper helper = new SchemaImageHelper();

    private RelationshipTypeService relationshipTypeService;

    private RelationshipTypeIconUrlResolver relationshipTypeIconUrlResolver;

    private RelationshipTypeIconResolver relationshipTypeIconResolver;

    @GET
    public RelationshipTypeJson get( @QueryParam("name") final String name )
    {
        final RelationshipTypeName relationshipTypeName = RelationshipTypeName.from( name );
        final RelationshipType relationshipType = fetchRelationshipType( relationshipTypeName );

        if ( relationshipType == null )
        {
            String message = String.format( "RelationshipType [%s] was not found.", relationshipTypeName );
            throw new WebApplicationException( Response.status( Response.Status.NOT_FOUND ).
                entity( message ).type( MediaType.TEXT_PLAIN_TYPE ).build() );
        }

        return new RelationshipTypeJson( relationshipType, this.relationshipTypeIconUrlResolver );
    }

    public RelationshipType fetchRelationshipType( final RelationshipTypeName name )
    {
        return relationshipTypeService.getByName( name );
    }

    @GET
    @Path("list")
    public RelationshipTypeListJson list()
    {
        final RelationshipTypes relationshipTypes = relationshipTypeService.getAll();

        return new RelationshipTypeListJson( relationshipTypes, this.relationshipTypeIconUrlResolver );
    }

    @GET
    @Path("byModule")
    public RelationshipTypeListJson getByModule( @QueryParam("moduleKey") final String moduleKey )
    {
        final RelationshipTypes relationshipTypes = relationshipTypeService.getByModule( ModuleKey.from( moduleKey ) );

        return new RelationshipTypeListJson( relationshipTypes, this.relationshipTypeIconUrlResolver );
    }

    @GET
    @Path("icon/{relationshipTypeName}")
    @Produces("image/*")
    public Response getIcon( @PathParam("relationshipTypeName") final String relationshipTypeStr,
                             @QueryParam("size") @DefaultValue("128") final int size, @QueryParam("hash") final String hash )
        throws Exception
    {
        final RelationshipTypeName relationshipTypeName = RelationshipTypeName.from( relationshipTypeStr );
        final Icon icon = this.relationshipTypeIconResolver.resolveIcon( relationshipTypeName );

        final Response.ResponseBuilder responseBuilder;
        if ( icon == null )
        {
            final BufferedImage defaultRelationshipTypeImage = helper.getDefaultRelationshipTypeImage( size );
            responseBuilder = Response.ok( defaultRelationshipTypeImage, DEFAULT_MIME_TYPE );
            applyMaxAge( Integer.MAX_VALUE, responseBuilder );
        }
        else
        {
            final BufferedImage image = helper.resizeImage( icon.asInputStream(), size );
            responseBuilder = Response.ok( image, icon.getMimeType() );
            if ( StringUtils.isNotEmpty( hash ) )
            {
                applyMaxAge( Integer.MAX_VALUE, responseBuilder );
            }
        }

        return responseBuilder.build();
    }

    private void applyMaxAge( int maxAge, final Response.ResponseBuilder responseBuilder )
    {
        final CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge( maxAge );
        responseBuilder.cacheControl( cacheControl );
    }

    public void setRelationshipTypeService( final RelationshipTypeService relationshipTypeService )
    {
        this.relationshipTypeService = relationshipTypeService;
        this.relationshipTypeIconResolver = new RelationshipTypeIconResolver( relationshipTypeService );
        this.relationshipTypeIconUrlResolver = new RelationshipTypeIconUrlResolver( this.relationshipTypeIconResolver );
    }
}