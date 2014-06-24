package com.enonic.wem.portal.underscore;

import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;

import static org.junit.Assert.*;

public class ImageByNameResourceTest
    extends ImageBaseResourceTest<ImageByNameResource>
{
    @Override
    protected void configure()
        throws Exception
    {
        this.resource = new ImageByNameResource();
        super.configure();
    }

    @Test
    public void getImageFound()
        throws Exception
    {
        setupContent();

        final Request request = new Request( Method.GET, "/live/path/to/content/_/image/enonic-logo.png" );
        final Response response = executeRequest( request );

        assertNull( this.resource.filterParam );
        assertEquals( 85, this.resource.quality );
        assertEquals( 16777215, this.resource.backgroundColor );
        assertEquals( 200, response.getStatus().getCode() );
        assertEquals( "image/png", response.getEntity().getMediaType().toString() );
    }

    @Test
    public void getImageNotFound()
        throws Exception
    {
        Mockito.when( this.contentService.getByPath( Mockito.anyObject(), Mockito.anyObject() ) ).thenReturn( null );

        final Request request = new Request( Method.GET, "/live/path/to/content/_/image/enonic-logo.png" );
        final Response response = executeRequest( request );
        assertEquals( 404, response.getStatus().getCode() );
    }

    @Test
    public void getImageWithFilter()
        throws Exception
    {
        setupContent();

        final Request request =
            new Request( Method.GET, "/live/path/to/content/_/image/enonic-logo.png?filter=sepia()&quality=75&background=0x0" );
        final Response response = executeRequest( request );

        assertEquals( "sepia()", this.resource.filterParam );
        assertEquals( 75, this.resource.quality );
        assertEquals( 0, this.resource.backgroundColor );
        assertEquals( 200, response.getStatus().getCode() );
        assertEquals( "image/png", response.getEntity().getMediaType().toString() );
    }
}