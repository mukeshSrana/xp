package com.enonic.xp.core.impl.content.page;

import java.util.ArrayList;
import java.util.List;

import com.enonic.xp.app.Application;
import com.enonic.xp.app.ApplicationKey;
import com.enonic.xp.app.ApplicationService;
import com.enonic.xp.page.DescriptorKey;
import com.enonic.xp.page.PageDescriptor;
import com.enonic.xp.page.PageDescriptors;
import com.enonic.xp.resource.ResourceKey;
import com.enonic.xp.resource.ResourceKeys;

final class GetPageDescriptorsByApplicationCommand
    extends AbstractGetPageDescriptorCommand<GetPageDescriptorsByApplicationCommand>
{
    private final static String PATH = "/site/pages";

    private ApplicationKey applicationKey;

    private ApplicationService applicationService;

    public PageDescriptors execute()
    {
        final Application application = this.applicationService.getApplication( this.applicationKey );
        return getDescriptorsFromApplication( application );
    }

    public GetPageDescriptorsByApplicationCommand applicationKey( final ApplicationKey applicationKey )
    {
        this.applicationKey = applicationKey;
        return this;
    }

    public final GetPageDescriptorsByApplicationCommand applicationService( final ApplicationService applicationService )
    {
        this.applicationService = applicationService;
        return this;
    }

    private PageDescriptors getDescriptorsFromApplication( final Application application )
    {
        final List<PageDescriptor> pageDescriptors = new ArrayList<>();
        final ResourceKeys resourceKeys = this.resourceService.findFolders( application.getKey(), PATH );

        for ( final ResourceKey resourceKey : resourceKeys )
        {
            final String descriptorName = resourceKey.getName();
            final DescriptorKey key = DescriptorKey.from( application.getKey(), descriptorName );
            final PageDescriptor pageDescriptor = getDescriptor( key );
            if ( pageDescriptor != null )
            {
                pageDescriptors.add( pageDescriptor );
            }

        }

        return PageDescriptors.from( pageDescriptors );
    }

}