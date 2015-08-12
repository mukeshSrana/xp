package com.enonic.xp.support.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.Beta;

@Beta
public abstract class AbstractJsonSerializer<T>
{
    private boolean prettyPrint = false;

    private final ObjectMapper defaultMapper;

    protected AbstractJsonSerializer()
    {
        defaultMapper = new ObjectMapper();
    }

    protected AbstractJsonSerializer( final ObjectMapper objectMapper )
    {
        this.defaultMapper = objectMapper;
    }

    protected ObjectMapper objectMapper()
    {
        return defaultMapper;
    }

    public void prettyPrint()
    {
        prettyPrint = true;
    }

    public String toString( T obj )
    {
        final JsonNode jsonNode = serialize( obj );
        if ( prettyPrint )
        {
            try
            {
                return objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString( jsonNode );
            }
            catch ( IOException e )
            {
                throw new RuntimeException( e );
            }
        }
        else
        {
            return jsonNode.toString();
        }
    }

    public T toObject( final String json )
    {
        try
        {
            final JsonFactory f = JsonFactoryHolder.DEFAULT_FACTORY;
            final JsonParser jp = f.createParser( json );

            final ObjectMapper mapper = new ObjectMapper();
            final JsonNode node = mapper.readValue( jp, JsonNode.class );

            try
            {
                return parse( node );
            }
            finally
            {
                jp.close();
            }

        }
        catch ( IOException e )
        {
            throw new RuntimeException( "Failed to read json", e );
        }
    }

    public JsonNode toJson( T obj )
    {
        return serialize( obj );
    }

    protected abstract JsonNode serialize( final T obj );

    protected abstract T parse( final JsonNode node );

}