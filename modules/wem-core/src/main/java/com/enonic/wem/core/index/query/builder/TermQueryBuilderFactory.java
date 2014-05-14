package com.enonic.wem.core.index.query.builder;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import com.enonic.wem.api.data.Value;
import com.enonic.wem.api.query.expr.CompareExpr;
import com.enonic.wem.core.index.query.IndexQueryFieldNameResolver;

public class TermQueryBuilderFactory
    extends AbstractBuilderFactory
{
    public QueryBuilder create( final CompareExpr compareExpr )
    {
        final String queryFieldName = IndexQueryFieldNameResolver.resolve( compareExpr );

        final Value value = compareExpr.getFirstValue().getValue();
        return QueryBuilders.termQuery( queryFieldName, getValueAsType( value ) );
    }

    public QueryBuilder create( final String fieldName, final Value value )
    {
        return QueryBuilders.termQuery( fieldName, getValueAsType( value ) );
    }
}