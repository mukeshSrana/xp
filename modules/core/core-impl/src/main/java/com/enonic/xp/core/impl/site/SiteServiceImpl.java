package com.enonic.xp.core.impl.site;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.enonic.xp.app.ApplicationKey;
import com.enonic.xp.site.SiteDescriptor;
import com.enonic.xp.site.SiteService;

@Component(immediate = true)
public class SiteServiceImpl
    implements SiteService
{
    private SiteDescriptorRegistry siteDescriptorRegistry;

    @Override
    public SiteDescriptor getDescriptor( final ApplicationKey applicationKey )
    {
        final SiteDescriptor siteDescriptor = this.siteDescriptorRegistry.get( applicationKey );
        if ( siteDescriptor == null )
        {
            return null;
        }
        return siteDescriptor;
    }

    @Reference
    public void setSiteDescriptorRegistry( final SiteDescriptorRegistry siteDescriptorRegistry )
    {
        this.siteDescriptorRegistry = siteDescriptorRegistry;
    }
}
