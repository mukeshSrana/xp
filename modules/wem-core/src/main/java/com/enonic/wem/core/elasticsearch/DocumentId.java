package com.enonic.wem.core.elasticsearch;

public class DocumentId
{
    final String id;

    public DocumentId( final String idAsString )
    {
        this.id = idAsString;
    }

    public String getId()
    {
        return id;
    }
}