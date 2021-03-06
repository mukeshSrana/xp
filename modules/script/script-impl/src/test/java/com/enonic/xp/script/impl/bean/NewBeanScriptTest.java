package com.enonic.xp.script.impl.bean;

import org.junit.Test;

import com.enonic.xp.script.impl.AbstractScriptTest;
import com.enonic.xp.script.ScriptExports;
import com.enonic.xp.resource.ResourceKey;

import static org.junit.Assert.*;

public class NewBeanScriptTest
    extends AbstractScriptTest
{
    @Test
    public void testEmpty()
    {
        final ResourceKey script = ResourceKey.from( "myapplication:/bean/simple-test.js" );
        final ScriptExports exports = runTestScript( script );
        assertNotNull( exports );
        assertSame( script, exports.getScript() );
    }
}
