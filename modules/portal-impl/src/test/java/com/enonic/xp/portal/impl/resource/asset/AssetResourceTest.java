package com.enonic.xp.portal.impl.resource.asset;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.enonic.xp.portal.impl.resource.base.ModuleBaseResourceTest;

import static org.junit.Assert.*;

public class AssetResourceTest
    extends ModuleBaseResourceTest
{
    @Override
    protected void configure()
        throws Exception
    {
        configureModuleService();
    }

    @Test
    public void getPublicResource_found()
        throws Exception
    {
        addResource( "main.css", "demo:/public/css/main.css", "p {color:red;}" );

        final MockHttpServletRequest request = newGetRequest( "/live/prod/path/to/content/_/public/demo/css/main.css" );
        final MockHttpServletResponse response = executeRequest( request );

        assertEquals( 200, response.getStatus() );
        assertEquals( "text/css", response.getContentType() );
        assertEquals( "p {color:red;}", response.getContentAsString() );
    }

    @Test
    public void getPublicResource_moduleNotFound()
        throws Exception
    {
        final MockHttpServletRequest request = newGetRequest( "/live/prod/path/to/content/_/public/demo/css/main.css" );
        final MockHttpServletResponse response = executeRequest( request );

        assertEquals( 404, response.getStatus() );
        assertTrue( response.getContentAsString().contains( "Module [demo] not found" ) );
    }

    @Test
    public void getPublicResource_fileNotFound()
        throws Exception
    {
        addModule( "demo" );

        final MockHttpServletRequest request = newGetRequest( "/live/prod/path/to/content/_/public/demo/css/main.css" );
        final MockHttpServletResponse response = executeRequest( request );

        assertEquals( 404, response.getStatus() );
        assertTrue( response.getContentAsString().contains( "File [css/main.css] not found in module [demo]" ) );
    }

    @Test
    public void methodNotAllowed()
        throws Exception
    {
        final MockHttpServletRequest request = newPostRequest( "/live/prod/path/to/content/_/public/demo/css/main.css" );
        final MockHttpServletResponse response = executeRequest( request );
        assertEquals( 405, response.getStatus() );
    }
}