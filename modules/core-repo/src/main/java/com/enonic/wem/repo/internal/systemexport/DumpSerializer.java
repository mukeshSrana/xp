package com.enonic.wem.repo.internal.systemexport;

import com.enonic.wem.repo.internal.index.result.SearchResultEntry;

public interface DumpSerializer<T extends AbstractDumpJson>
{
    public String toString( final SearchResultEntry searchResultEntry );

    public T toJson( final String string );


}
