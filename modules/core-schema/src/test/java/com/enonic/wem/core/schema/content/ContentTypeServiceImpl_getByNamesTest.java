package com.enonic.wem.core.schema.content;

import org.junit.Test;

import com.enonic.wem.api.schema.content.ContentTypeNames;
import com.enonic.wem.api.schema.content.ContentTypes;
import com.enonic.wem.api.schema.content.GetContentTypesParams;

import static org.junit.Assert.*;

public class ContentTypeServiceImpl_getByNamesTest
    extends AbstractContentTypeServiceTest
{
    @Test
    public void testGetByName()
        throws Exception
    {
        register( createContentType( "mymodule:my-contenttype-1", "DisplayName1" ),
                  createContentType( "mymodule:my-contenttype-2", "DisplayName2" ) );

        final GetContentTypesParams params =
            new GetContentTypesParams().contentTypeNames( ContentTypeNames.from( "mymodule:my-contenttype-1" ) );
        final ContentTypes result = this.service.getByNames( params );

        assertEquals( 1, result.getSize() );
        verifyContentType( "mymodule:my-contenttype-1", "DisplayName1", result );
    }
}