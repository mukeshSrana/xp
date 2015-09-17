package com.enonic.xp.jaxrs.swagger.impl;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Sets;

import io.swagger.jaxrs.Reader;
import io.swagger.models.Swagger;

import com.enonic.xp.web.handler.BaseWebHandler;
import com.enonic.xp.web.handler.WebHandler;
import com.enonic.xp.web.handler.WebHandlerChain;
import com.enonic.xp.web.jaxrs.JaxRsComponent;

@Component(immediate = true, service = WebHandler.class)
public final class SwaggerServlet
    extends BaseWebHandler
{
    private final static String BASE_PATH = "/swagger";

    private Swagger model;

    private boolean initialized;

    private final Set<Class<?>> resources;

    public SwaggerServlet()
    {
        setOrder( 10 );
        this.resources = Sets.newConcurrentHashSet();
    }

    @Override
    protected boolean canHandle( final HttpServletRequest req )
    {
        return req.getRequestURI().startsWith( BASE_PATH );
    }

    @Override
    protected void doHandle( final HttpServletRequest req, final HttpServletResponse res, final WebHandlerChain chain )
        throws Exception
    {
        final String path = req.getRequestURI();
        if ( path.equals( BASE_PATH + "/swagger.json" ) )
        {
            serveApiListing( req, res );
            return;
        }

        res.sendError( HttpServletResponse.SC_NOT_FOUND );
    }

    private void serveApiListing( final HttpServletRequest req, final HttpServletResponse res )
        throws Exception
    {
        final Swagger swagger = findApiModel();
        serialize( res, swagger );
    }

    private Swagger findApiModel()
    {
        if ( !this.initialized )
        {
            createApiModel();
        }

        return this.model;
    }

    private synchronized void createApiModel()
    {
        final Reader reader = new Reader( null );
        this.model = reader.read( this.resources );
        this.initialized = true;
    }

    private void serialize( final HttpServletResponse res, final Swagger swagger )
        throws Exception
    {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure( SerializationFeature.INDENT_OUTPUT, true );
        mapper.setSerializationInclusion( JsonInclude.Include.NON_NULL );

        res.setContentType( "application/json" );
        mapper.writeValue( res.getWriter(), swagger );
    }

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void addResource( final JaxRsComponent resource )
    {
        final Class<?> type = resource.getClass();
        if ( type.getAnnotation( Path.class ) != null )
        {
            this.resources.add( type );
            this.initialized = false;
        }
    }

    public void removeResource( final JaxRsComponent resource )
    {
        this.resources.remove( resource.getClass() );
        this.initialized = false;
    }
}
