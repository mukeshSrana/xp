package com.enonic.wem.repo.internal.elasticsearch.xcontent;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import com.enonic.wem.repo.internal.index.IndexValueNormalizer;

public abstract class AbstractXContentBuilderFactor
{

    static XContentBuilder startBuilder()
        throws Exception
    {
        final XContentBuilder result = XContentFactory.jsonBuilder();
        result.startObject();

        return result;
    }

    static void addField( XContentBuilder result, String name, Object value )
        throws Exception
    {
        if ( value == null )
        {
            return;
        }

        if ( value instanceof String )
        {
            value = IndexValueNormalizer.normalize( (String) value );
        }

        result.field( name, value );
    }


    static void endBuilder( final XContentBuilder contentBuilder )
        throws Exception
    {
        contentBuilder.endObject();
    }


}
