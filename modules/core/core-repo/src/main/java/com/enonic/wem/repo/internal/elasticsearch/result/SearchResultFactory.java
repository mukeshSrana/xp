package com.enonic.wem.repo.internal.elasticsearch.result;

import org.elasticsearch.action.search.SearchResponse;

import com.enonic.wem.repo.internal.elasticsearch.aggregation.AggregationsFactory;
import com.enonic.wem.repo.internal.index.result.SearchResult;

public class SearchResultFactory
{
    public static SearchResult create( final SearchResponse searchResponse )
    {
        return SearchResult.create().
            results( SearchResultEntriesFactory.create( searchResponse.getHits() ) ).
            aggregations( AggregationsFactory.create( searchResponse.getAggregations() ) ).
            build();
    }


}
