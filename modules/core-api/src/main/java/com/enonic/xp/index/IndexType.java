package com.enonic.xp.index;

import com.google.common.annotations.Beta;

@Beta
public enum IndexType
{
    SEARCH,
    BRANCH,
    VERSION;

    public String getName()
    {
        return this.name().toLowerCase();
    }


    public static IndexType fromString( final String name )
    {
        if ( SEARCH.getName().equals( name ) )
        {
            return SEARCH;
        }

        if ( BRANCH.getName().equals( name ) )
        {
            return BRANCH;
        }

        if ( VERSION.getName().equals( name ) )
        {
            return VERSION;
        }

        throw new IllegalArgumentException( "Cannot map value '" + name + "' to IndexType" );
    }

}
