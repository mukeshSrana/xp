package com.enonic.wem.repo.internal.elasticsearch.result;

import org.elasticsearch.action.search.SearchResponse;

import com.enonic.wem.repo.internal.index.result.ScrollResult;

public class ScrollResultFactory
{
    public static ScrollResult create( final SearchResponse searchResponse )
    {
        return ScrollResult.create().
            scrollId( searchResponse.getScrollId() ).
            hits( SearchResultEntriesFactory.create( searchResponse.getHits() ) ).
            build();
    }

}
