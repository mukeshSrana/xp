package com.enonic.xp.core.impl.content.page.part;

import org.junit.Assert;
import org.junit.Test;

import com.enonic.xp.content.page.region.PartDescriptors;
import com.enonic.xp.module.Module;
import com.enonic.xp.module.ModuleKey;
import com.enonic.xp.module.Modules;

public class PartDescriptorServiceImpl_getByModulesTest
    extends AbstractPartDescriptorServiceTest
{
    @Test
    public void getDescriptorsFromSingleModule()
        throws Exception
    {
        final Module module = createModule( "foomodule" );
        createDescriptors( "foomodule:foomodule-part-descr" );

        mockResourcePaths( module, "parts/foomodule-part-descr/part.xml" );
        final PartDescriptors result = this.service.getByModule( module.getKey() );

        Assert.assertNotNull( result );
        Assert.assertEquals( 1, result.getSize() );
    }

    @Test
    public void getDescriptorsFromMultipleModules()
        throws Exception
    {
        final Modules modules = createModules( "foomodule", "barmodule" );
        createDescriptors( "foomodule:foomodule-part-descr", "barmodule:barmodule-part-descr" );

        mockResourcePaths( modules.getModule( ModuleKey.from( "foomodule" ) ), "parts/foomodule-part-descr/part.xml" );
        mockResourcePaths( modules.getModule( ModuleKey.from( "barmodule" ) ), "parts/barmodule-part-descr/part.xml" );

        final PartDescriptors result = this.service.getByModules( modules.getModuleKeys() );

        Assert.assertNotNull( result );
        Assert.assertEquals( 2, result.getSize() );
    }
}
