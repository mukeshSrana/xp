/*
 * Copyright 2000-2011 Enonic AS
 * http://www.enonic.com/license
 */
package com.enonic.xp.core.impl.image.command;

import com.jhlabs.image.EdgeFilter;

public final class EdgeFilterCommand
    extends FilterCommand
{
    public EdgeFilterCommand()
    {
        super( "edge" );
    }

    @Override
    protected Object doBuild( Object[] args )
    {
        return new EdgeFilter();
    }
}