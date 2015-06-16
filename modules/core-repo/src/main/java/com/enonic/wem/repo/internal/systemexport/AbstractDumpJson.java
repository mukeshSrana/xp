package com.enonic.wem.repo.internal.systemexport;

import com.enonic.wem.repo.internal.index.result.SearchResultEntry;

public abstract class AbstractDumpJson
{
    protected static String getStringValue( final SearchResultEntry searchResultEntry, final String path )
    {
        return searchResultEntry.getField( path, true ).getValue().toString();
    }

}
