package com.enonic.wem.api.data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.enonic.wem.api.data.type.ValueType;
import com.enonic.wem.api.data.type.ValueTypes;

public class PropertyJson
    extends DataJson<Property>
{
    private final Property property;

    public PropertyJson( final Property property )
    {
        super( property );
        this.property = property;
    }

    @SuppressWarnings("UnusedDeclaration")
    @JsonCreator
    public PropertyJson( @JsonProperty("name") final String name, @JsonProperty("type") final String type,
                         @JsonProperty(value = "value") final String value, @JsonProperty(value = "set") final List<DataJson> set )
    {
        super( newProperty( type, name, value, set ) );
        this.property = getData();
    }

    private static Property newProperty( final String type, final String name, final String stringValue, final List<DataJson> set )
    {
        ValueType valueType = ValueTypes.getByName( type );
        Value value = Value.newValue( valueType, stringValue != null ? stringValue : RootDataSetJson.dataJsonListToRootDataSet( set ) );
        return Property.newProperty( name, value );
    }

    public String getType()
    {
        return property.getValueType().getName();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getValue()
    {
        if ( !property.getValue().getType().equals( ValueTypes.DATA ) )
        {
            return property.getString();
        }
        else
        {
            return null;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<DataJson> getSet()
    {
        if ( property.getValue().getType().equals( ValueTypes.DATA ) )
        {
            final RootDataSet rootDataSet = property.getValue().asData();
            final RootDataSetJson rootDataSetJson = new RootDataSetJson( rootDataSet );
            return rootDataSetJson.getSet();
        }
        else
        {
            return null;
        }
    }
}