package com.enonic.xp.schema.mixin;

import org.junit.Test;

import com.enonic.xp.form.FormItemSet;
import com.enonic.xp.form.inputtype.InputTypes;
import com.enonic.xp.schema.mixin.Mixin;

import static com.enonic.xp.form.FormItemSet.newFormItemSet;
import static com.enonic.xp.form.InlineMixin.newInlineMixin;
import static com.enonic.xp.form.Input.newInput;
import static com.enonic.xp.schema.mixin.Mixin.newMixin;
import static org.junit.Assert.*;

public class MixinTest
{

    @Test
    public void adding_a_formItemSetMixin_to_another_formItemSetMixin_throws_exception()
    {
        Mixin ageMixin = newMixin().name( "mymodule:age" ).addFormItem( newInput().name( "age" ).inputType( InputTypes.TEXT_LINE ).build() ).build();

        final FormItemSet personFormItemSet = newFormItemSet().name( "person" ).addFormItem(
            newInput().name( "name" ).inputType( InputTypes.TEXT_LINE ).build() ).addFormItem(
            newInlineMixin( ageMixin ).build() ).build();
        Mixin personMixin = newMixin().name( "mymodule:person" ).addFormItem( personFormItemSet ).build();

        Mixin addressMixin = newMixin().name( "mymodule:address" ).addFormItem( newFormItemSet().name( "address" ).addFormItem(
            newInput().inputType( InputTypes.TEXT_LINE ).name( "street" ).build() ).addFormItem(
            newInput().inputType( InputTypes.TEXT_LINE ).name( "postalCode" ).build() ).addFormItem(
            newInput().inputType( InputTypes.TEXT_LINE ).name( "postalPlace" ).build() ).build() ).build();

        try
        {
            personFormItemSet.add( newInlineMixin( addressMixin ).build() );
        }
        catch ( Exception e )
        {
            assertTrue( e instanceof IllegalArgumentException );
            assertEquals( "A Mixin cannot reference other Mixins unless it is of type InputMixin: FormItemSetMixin", e.getMessage() );
        }
    }
}