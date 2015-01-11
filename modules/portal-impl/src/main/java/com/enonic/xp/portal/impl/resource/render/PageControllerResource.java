package com.enonic.xp.portal.impl.resource.render;

import com.enonic.wem.api.content.Content;
import com.enonic.wem.portal.internal.controller.PortalContextImpl;
import com.enonic.wem.portal.internal.rendering.RenderResult;
import com.enonic.wem.portal.internal.rendering.Renderer;
import com.enonic.xp.portal.PortalContext;
import com.enonic.xp.portal.impl.resource.render.RendererControllerResource;

public final class PageControllerResource
    extends RendererControllerResource
{
    protected Renderer<Content, PortalContext> renderer;

    @Override
    protected RenderResult execute( final PortalContextImpl context )
        throws Exception
    {
        return this.renderer.render( this.content, context );
    }
}
