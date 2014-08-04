package com.enonic.wem.api.xml.serializer;

import org.junit.Test;

import com.enonic.wem.api.content.page.layout.LayoutDescriptor;
import com.enonic.wem.api.content.page.layout.LayoutDescriptorKey;
import com.enonic.wem.api.form.Form;
import com.enonic.wem.api.xml.mapper.XmlLayoutDescriptorMapper;
import com.enonic.wem.api.xml.model.XmlLayoutDescriptor;

import static com.enonic.wem.api.content.page.region.RegionDescriptor.newRegionDescriptor;
import static com.enonic.wem.api.content.page.region.RegionDescriptors.newRegionDescriptors;
import static com.enonic.wem.api.form.Input.newInput;
import static com.enonic.wem.api.form.inputtype.InputTypes.DECIMAL_NUMBER;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class XmlLayoutDescriptorSerializerTest
    extends BaseXmlSerializer2Test
{

    @Test
    public void test_to_xml()
        throws Exception
    {
        Form configForm = Form.newForm().
            addFormItem( newInput().name( "width" ).inputType( DECIMAL_NUMBER ).label( "Column width" ).build() ).
            build();

        LayoutDescriptor layoutDescriptor = LayoutDescriptor.newLayoutDescriptor().
            displayName( "A Layout" ).
            name( "mylayout" ).
            config( configForm ).
            regions( newRegionDescriptors().
                add( newRegionDescriptor().name( "left" ).build() ).
                add( newRegionDescriptor().name( "right" ).build() ).
                build() ).
            key( LayoutDescriptorKey.from( "module-1.0.0:mylayout" ) ).
            build();

        XmlLayoutDescriptor xml = XmlLayoutDescriptorMapper.toXml( layoutDescriptor );
        final String result = XmlSerializers2.layoutDescriptor().serialize( xml );

        assertXml( "layout-descriptor.xml", result );
    }

    @Test
    public void test_from_xml()
        throws Exception
    {
        final String xml = readFromFile( "layout-descriptor.xml" );
        final LayoutDescriptor.Builder builder = LayoutDescriptor.newLayoutDescriptor();
        builder.key( LayoutDescriptorKey.from( "module-1.0.0:mylayout" ) );
        builder.name( "mylayout" );

        XmlLayoutDescriptor xmlObject = XmlSerializers2.layoutDescriptor().parse( xml );
        XmlLayoutDescriptorMapper.fromXml( xmlObject, builder );
        LayoutDescriptor layoutDescriptor = builder.build();

        assertEquals( "A Layout", layoutDescriptor.getDisplayName() );
        final Form config = layoutDescriptor.getConfig();
        assertNotNull( config );
        assertEquals( DECIMAL_NUMBER, config.getFormItem( "width" ).toInput().getInputType() );
        assertEquals( "Column width", config.getFormItem( "width" ).toInput().getLabel() );
    }

}