package com.enonic.wem.admin.json.content.page.part;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.enonic.wem.admin.json.content.page.PageComponentJson;
import com.enonic.wem.admin.json.data.DataJson;
import com.enonic.wem.admin.json.data.RootDataSetJson;
import com.enonic.wem.api.content.page.ComponentName;
import com.enonic.wem.api.content.page.part.PartComponent;
import com.enonic.wem.api.content.page.part.PartDescriptorKey;

import static com.enonic.wem.api.content.page.part.PartComponent.newPartComponent;

@SuppressWarnings("UnusedDeclaration")
public class PartComponentJson
    extends PageComponentJson<PartComponent>
{
    private final PartComponent part;

    public PartComponentJson( final PartComponent component )
    {
        super( component );
        this.part = component;
    }

    @JsonCreator
    public PartComponentJson( @JsonProperty("name") final String name, @JsonProperty("descriptor") final String descriptor,
                              @JsonProperty("config") final List<DataJson> config )
    {
        super( newPartComponent().
            name( ComponentName.from( name ) ).
            descriptor( descriptor != null ? PartDescriptorKey.from( descriptor ) : null ).
            config( config != null ? new RootDataSetJson( config ).getRootDataSet() : null ).
            build() );

        this.part = getComponent();
    }
}
