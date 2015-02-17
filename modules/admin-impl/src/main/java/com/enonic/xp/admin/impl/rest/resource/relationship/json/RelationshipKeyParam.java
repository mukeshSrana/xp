package com.enonic.xp.admin.impl.rest.resource.relationship.json;

import com.enonic.xp.content.ContentId;
import com.enonic.xp.relationship.RelationshipKey;
import com.enonic.xp.schema.relationship.RelationshipTypeName;

public class RelationshipKeyParam
{
    private String fromContent;

    private String toContent;

    private String type;

    public RelationshipKeyParam()
    {
    }

    public RelationshipKeyParam( String str )
    {
    }

    public String getFromContent()
    {
        return fromContent;
    }

    public void setFromContent( final String fromContent )
    {
        this.fromContent = fromContent;
    }

    public String getToContent()
    {
        return toContent;
    }

    public void setToContent( final String toContent )
    {
        this.toContent = toContent;
    }

    public String getType()
    {
        return type;
    }

    public void setType( final String type )
    {
        this.type = type;
    }

    public RelationshipKey from()
    {
        final RelationshipTypeName type = RelationshipTypeName.from( this.type );
        final ContentId fromContent = ContentId.from( this.fromContent );
        final ContentId toContent = ContentId.from( this.toContent );

        return RelationshipKey.from( type, fromContent, toContent );
    }
}