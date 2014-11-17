package com.enonic.wem.repo.internal.elasticsearch;

import com.enonic.wem.repo.internal.index.IndexFieldNameNormalizer;

class ReturnField
{
    private final String normalizedReturnFieldName;

    public ReturnField( final String fieldName )
    {
        this.normalizedReturnFieldName = IndexFieldNameNormalizer.normalize( fieldName );
    }

    public String getName()
    {
        return normalizedReturnFieldName;
    }

}
