/*
 * Copyright 2000-2011 Enonic AS
 * http://www.enonic.com/license
 */
package com.enonic.xp.core.impl.image.command;

import com.enonic.xp.core.impl.image.ImageScaleFunction;
import com.enonic.xp.core.impl.image.effect.ScaleSquareFunction;
import com.enonic.xp.image.FocalPoint;

public final class ScaleSquareFunctionCommand
    extends ScaleCommand
{
    public ScaleSquareFunctionCommand()
    {
        super( "square" );
    }

    @Override
    protected ImageScaleFunction doBuild( Object[] args, FocalPoint focalPoint )
    {
        return new ScaleSquareFunction( getIntArg( args, 0, 0 ), focalPoint.xOffset(), focalPoint.yOffset() );
    }
}