package com.enonic.wem.core.script.compiler;

import org.mozilla.javascript.Script;

public interface ScriptCache
{
    public Script get( String key );

    public void put( String key, Script script );
}
