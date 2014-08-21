package com.enonic.wem.portal.internal.controller;

import com.enonic.wem.api.resource.ResourceKey;

public interface JsController
{
    public JsController scriptDir( ResourceKey dir );

    public JsController context( JsContext context );

    public void execute();
}