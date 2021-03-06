package com.enonic.wem.repo.internal.elasticsearch.function;

import java.util.List;

import com.enonic.wem.repo.internal.elasticsearch.query.builder.function.AbstractSimpleQueryStringFunction;
import com.enonic.wem.repo.internal.entity.NodeConstants;
import com.enonic.wem.repo.internal.index.query.IndexQueryFieldNameResolver;
import com.enonic.xp.query.expr.ValueExpr;

public class FulltextFunctionArguments
    extends AbstractSimpleQueryStringFunction
{
    private final String functionName = "fulltext";

    @Override
    protected String getDefaultAnalyzer()
    {
        return NodeConstants.DEFAULT_FULLTEXT_SEARCH_ANALYZER;
    }

    public FulltextFunctionArguments( final List<ValueExpr> arguments )
    {
        super( arguments );
    }

    @Override
    public String getFunctionName()
    {
        return functionName;
    }


    @Override
    public String resolveQueryFieldName( final String baseFieldName )
    {
        return IndexQueryFieldNameResolver.resolveAnalyzedFieldName( baseFieldName );
    }
}
