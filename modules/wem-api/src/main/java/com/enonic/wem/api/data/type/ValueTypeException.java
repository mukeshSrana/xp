package com.enonic.wem.api.data.type;

public final class ValueTypeException
    extends RuntimeException
{
    public ValueTypeException( final String message, final Object... args )
    {
        super( String.format( message, args ) );
    }
}