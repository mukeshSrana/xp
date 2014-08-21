package com.enonic.wem.portal.internal.base;

import javax.inject.Inject;

import org.restlet.resource.ResourceException;

import com.enonic.wem.api.content.ContentPath;
import com.enonic.wem.api.context.Context;
import com.enonic.wem.api.module.ModuleKey;
import com.enonic.wem.api.module.ModuleKeyResolver;
import com.enonic.wem.api.module.ModuleName;
import com.enonic.wem.api.module.ModuleNotFoundException;
import com.enonic.wem.api.module.ModuleService;
import com.enonic.wem.core.module.ModuleKeyResolverService;

public abstract class ModuleBaseResource
    extends WorkspaceBaseResource
{
    @Inject
    protected ModuleKeyResolverService moduleKeyResolverService;

    @Inject
    protected ModuleService moduleService;

    protected String contentPath;

    @Override
    protected void doInit()
        throws ResourceException
    {
        super.doInit();
        this.contentPath = getAttribute( "path" );
    }

    protected final ModuleKey resolveModule()
    {
        final String moduleName = getAttribute( "module" );
        final ContentPath path = ContentPath.from( this.contentPath );

        try
        {
            return ModuleKey.from( moduleName );
        }
        catch ( final Exception e )
        {
            return resolveModuleFromSite( path, moduleName );
        }
    }

    private ModuleKey resolveModuleFromSite( final ContentPath contentPath, final String moduleName )
    {
        try
        {
            final ModuleKeyResolver moduleKeyResolver =
                this.moduleKeyResolverService.forContent( contentPath, Context.create( this.workspace ) );
            return moduleKeyResolver.resolve( ModuleName.from( moduleName ) );
        }
        catch ( final ModuleNotFoundException e )
        {
            throw notFound( "Module [%s] not found", moduleName );
        }
    }
}