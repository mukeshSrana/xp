package com.enonic.wem.core.relationship;


import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.enonic.wem.api.content.ContentId;
import com.enonic.wem.api.data.DataPath;
import com.enonic.wem.api.relationship.Relationship;
import com.enonic.wem.api.schema.relationship.RelationshipTypeName;
import com.enonic.wem.api.support.serializer.AbstractJsonSerializer;
import com.enonic.wem.api.support.serializer.JsonParsingException;
import com.enonic.wem.api.support.serializer.JsonSerializerUtil;

public final class RelationshipJsonSerializer
    extends AbstractJsonSerializer<Relationship>
{
    private boolean includeCreator;

    private boolean includeCreatedTime;

    private boolean includeModifier;

    private boolean includeModifiedTime;

    public RelationshipJsonSerializer()
    {
    }

    public RelationshipJsonSerializer includeCreator( boolean value )
    {
        this.includeCreator = value;
        return this;
    }

    public RelationshipJsonSerializer includeCreatedTime( boolean value )
    {
        this.includeCreatedTime = value;
        return this;
    }

    public RelationshipJsonSerializer includeModifier( boolean value )
    {
        this.includeModifier = value;
        return this;
    }

    public RelationshipJsonSerializer includeModifiedTime( boolean value )
    {
        this.includeModifiedTime = value;
        return this;
    }

    public RelationshipJsonSerializer( final ObjectMapper objectMapper )
    {
        super( objectMapper );
    }

    public JsonNode serialize( final Relationship relationship )
    {
        final ObjectNode objectNode = objectMapper().createObjectNode();
        if ( includeCreator )
        {
            objectNode.put( "creator", relationship.getCreator().toString() );
        }
        if ( includeCreatedTime )
        {
            JsonSerializerUtil.setDateTimeValue( "createdTime", relationship.getCreatedTime(), objectNode );
        }
        if ( includeModifier )
        {
            objectNode.put( "modifier", relationship.getModifier().toString() );
        }
        if ( includeModifiedTime )
        {
            JsonSerializerUtil.setDateTimeValue( "modifiedTime", relationship.getModifiedTime(), objectNode );
        }
        objectNode.put( "type", relationship.getType().toString() );
        objectNode.put( "fromContent", relationship.getFromContent().toString() );
        objectNode.put( "toContent", relationship.getToContent().toString() );

        if ( relationship.getManagingData() != null )
        {
            objectNode.put( "managingData", relationship.getManagingData().toString() );
        }
        else
        {
            objectNode.putNull( "managingData" );
        }
        serializeProperties( relationship, objectNode );
        return objectNode;
    }

    private void serializeProperties( final Relationship relationship, final ObjectNode objectNode )
    {
        final Map<String, String> properties = relationship.getProperties();
        if ( properties.size() == 0 )
        {
            objectNode.putNull( "properties" );
        }
        else
        {
            final ObjectNode propertiesNode = objectNode.putObject( "properties" );
            for ( Map.Entry<String, String> entry : properties.entrySet() )
            {
                propertiesNode.put( entry.getKey(), entry.getValue() );
            }
        }
    }

    public Relationship toRelationship( final String json )
        throws JsonParsingException
    {
        return toObject( json );
    }

    protected Relationship parse( final JsonNode relationshipNode )
    {
        final Relationship.Builder builder = Relationship.newRelationship();

        if ( includeCreator )
        {
            builder.creator( JsonSerializerUtil.getUserKeyValue( "creator", relationshipNode ) );
        }
        if ( includeCreatedTime )
        {
            builder.createdTime( JsonSerializerUtil.getDateTimeValue( "createdTime", relationshipNode ) );
        }
        if ( includeModifier )
        {
            builder.modifier( JsonSerializerUtil.getUserKeyValue( "modifier", relationshipNode ) );
        }
        if ( includeModifiedTime )
        {
            builder.modifiedTime( JsonSerializerUtil.getDateTimeValue( "modifiedTime", relationshipNode ) );
        }
        builder.type( RelationshipTypeName.from( JsonSerializerUtil.getStringValue( "type", relationshipNode ) ) );
        builder.fromContent( ContentId.from( JsonSerializerUtil.getStringValue( "fromContent", relationshipNode ) ) );
        builder.toContent( ContentId.from( JsonSerializerUtil.getStringValue( "toContent", relationshipNode ) ) );
        if ( !relationshipNode.get( "managingData" ).isNull() )
        {
            builder.managed( DataPath.from( JsonSerializerUtil.getStringValue( "managingData", relationshipNode ) ) );
        }
        if ( !relationshipNode.get( "properties" ).isNull() )
        {
            final ObjectNode propertiesNode = (ObjectNode) relationshipNode.get( "properties" );
            final Iterator<String> it = propertiesNode.fieldNames();
            while ( it.hasNext() )
            {
                final String fieldName = it.next();
                builder.property( fieldName, propertiesNode.get( fieldName ).textValue() );
            }
        }
        return builder.build();
    }
}
