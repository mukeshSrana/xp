package com.enonic.xp.support.serializer;


public class JsonParsingException
    extends ParsingException
{
    public JsonParsingException( final String message, final Exception e )
    {
        super( message, e );
    }

    public JsonParsingException( final String message )
    {
        super( message );
    }
}