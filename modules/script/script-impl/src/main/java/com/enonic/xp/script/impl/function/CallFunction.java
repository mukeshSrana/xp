package com.enonic.xp.script.impl.function;

import jdk.nashorn.api.scripting.JSObject;

import com.enonic.xp.script.impl.value.JsObjectConverter;

public final class CallFunction
    extends AbstractFunction
{
    public final static String NAME = "__call";

    public CallFunction()
    {
        super( NAME );
    }

    @Override
    public Object call( final Object thiz, final Object... args )
    {
        final JSObject func = (JSObject) args[0];
        final Object[] array = (Object[]) args[1];

        final Object[] jsArray = JsObjectConverter.toJsArray( array );
        return func.call( thiz, jsArray );
    }
}
