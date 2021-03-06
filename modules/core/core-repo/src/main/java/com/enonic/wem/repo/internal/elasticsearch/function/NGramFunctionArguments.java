package com.enonic.wem.repo.internal.elasticsearch.function;

import java.util.List;

import com.enonic.wem.repo.internal.elasticsearch.query.builder.function.AbstractSimpleQueryStringFunction;
import com.enonic.wem.repo.internal.entity.NodeConstants;
import com.enonic.wem.repo.internal.index.query.IndexQueryFieldNameResolver;
import com.enonic.xp.query.expr.ValueExpr;

public class NGramFunctionArguments
    extends AbstractSimpleQueryStringFunction
{

    @Override
    protected String getDefaultAnalyzer()
    {
        return NodeConstants.DEFAULT_NGRAM_SEARCH_ANALYZER;
    }

    @Override
    public String getFunctionName()
    {
        return "ngram";
    }

    public NGramFunctionArguments( final List<ValueExpr> arguments )
    {
        super( arguments );
    }

    @Override
    public String resolveQueryFieldName( final String baseFieldName )
    {
        return IndexQueryFieldNameResolver.resolveNGramFieldName( baseFieldName );
    }
}
