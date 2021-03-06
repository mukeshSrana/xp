package com.enonic.xp.core.impl.content.validate;


import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import com.enonic.xp.content.Content;
import com.enonic.xp.content.ContentPath;
import com.enonic.xp.form.FieldSet;
import com.enonic.xp.form.FormItemSet;
import com.enonic.xp.form.Input;
import com.enonic.xp.inputtype.InputTypeName;
import com.enonic.xp.schema.content.ContentType;
import com.enonic.xp.schema.content.ContentTypeName;

import static org.junit.Assert.*;

public class OccurrenceValidatorTest
{
    private static final ContentPath MY_CONTENT_PATH = ContentPath.from( "/mycontent" );

    private ContentType contentType;

    @Before
    public void before()
    {
        contentType = ContentType.create().
            name( "myapplication:my_type" ).
            superType( ContentTypeName.structured() ).
            build();
    }

    private OccurrenceValidator newValidator( final ContentType type )
    {
        return new OccurrenceValidator( type.getForm() );
    }

    @Test
    public void given_input_with_maxOccur1_with_two_data_when_validate_then_MaximumOccurrencesValidationError()
    {
        contentType.getForm().addFormItem(
            Input.create().name( "myInput" ).label( "Input" ).inputType( InputTypeName.TEXT_LINE ).maximumOccurrences( 1 ).build() );
        Content content = Content.create().path( MY_CONTENT_PATH ).type( contentType.getName() ).build();
        content.getData().setString( "myInput[0]", "1" );
        content.getData().setString( "myInput[1]", "2" );

        // exercise
        DataValidationErrors validationResults = newValidator( contentType ).validate( content.getData().getRoot() );
        assertTrue( validationResults.hasErrors() );
        assertTrue( validationResults.getFirst() instanceof MaximumOccurrencesValidationError );
        assertEquals( "Input [myInput] allows maximum 1 occurrence: 2", validationResults.getFirst().getErrorMessage() );
    }

    @Test
    public void given_input_with_maxOccur2_with_three_data_when_validate_then_MaximumOccurrencesValidationError()
    {
        contentType.getForm().addFormItem(
            Input.create().name( "myInput" ).label( "Input" ).inputType( InputTypeName.TEXT_LINE ).maximumOccurrences( 2 ).build() );
        Content content = Content.create().path( MY_CONTENT_PATH ).type( contentType.getName() ).build();
        content.getData().setString( "myInput[0]", "1" );
        content.getData().setString( "myInput[1]", "2" );
        content.getData().setString( "myInput[2]", "3" );

        // exercise
        DataValidationErrors validationResults = newValidator( contentType ).validate( content.getData().getRoot() );
        assertTrue( validationResults.hasErrors() );
        assertTrue( validationResults.getFirst() instanceof MaximumOccurrencesValidationError );
        assertEquals( "Input [myInput] allows maximum 2 occurrences: 3", validationResults.getFirst().getErrorMessage() );
    }

    @Test
    public void given_required_input_with_data_when_validate_then_hasErrors_returns_false()
    {
        contentType.getForm().addFormItem(
            Input.create().name( "myInput" ).label( "Input" ).inputType( InputTypeName.TEXT_LINE ).required( true ).build() );
        Content content = Content.create().path( MY_CONTENT_PATH ).type( contentType.getName() ).build();
        content.getData().setString( "myInput", "value" );

        // exercise
        DataValidationErrors validationResults = newValidator( contentType ).validate( content.getData().getRoot() );
        assertFalse( validationResults.hasErrors() );
    }

    @Test
    public void given_required_input_with_no_data_when_validate_then_hasErrors_returns_true()
    {
        contentType.getForm().addFormItem(
            Input.create().name( "myInput" ).label( "Input" ).inputType( InputTypeName.TEXT_LINE ).required( true ).build() );
        Content content = Content.create().path( MY_CONTENT_PATH ).type( contentType.getName() ).build();

        // exercise
        DataValidationErrors validationResults = newValidator( contentType ).validate( content.getData().getRoot() );
        assertTrue( validationResults.hasErrors() );
    }

    @Test
    public void given_input_with_minOccur2_with_one_data_when_validate_then_MinimumOccurrencesValidationError()
    {
        contentType.getForm().addFormItem(
            Input.create().name( "myInput" ).label( "Input" ).inputType( InputTypeName.TEXT_LINE ).minimumOccurrences( 2 ).build() );
        Content content = Content.create().path( MY_CONTENT_PATH ).type( contentType.getName() ).build();
        content.getData().setString( "myInput", "value" );

        // exercise
        DataValidationErrors validationResults = newValidator( contentType ).validate( content.getData().getRoot() );
        assertTrue( validationResults.hasErrors() );
        assertTrue( validationResults.getFirst() instanceof MinimumOccurrencesValidationError );
        assertEquals( "Input [myInput] requires minimum 2 occurrences: 1", validationResults.getFirst().getErrorMessage() );
    }

    @Test
    public void given_input_with_minOccur3_with_two_data_when_validate_then_MinimumOccurrencesValidationError()
    {
        contentType.getForm().addFormItem(
            Input.create().name( "myInput" ).label( "Input" ).inputType( InputTypeName.TEXT_LINE ).minimumOccurrences( 3 ).build() );
        Content content = Content.create().path( MY_CONTENT_PATH ).type( contentType.getName() ).build();
        content.getData().setString( "myInput[0]", "value 1" );
        content.getData().setString( "myInput[1]", "value 2" );

        // exercise
        DataValidationErrors validationResults = newValidator( contentType ).validate( content.getData().getRoot() );
        assertTrue( validationResults.hasErrors() );
        assertTrue( validationResults.getFirst() instanceof MinimumOccurrencesValidationError );
        assertEquals( "Input [myInput] requires minimum 3 occurrences: 2", validationResults.getFirst().getErrorMessage() );
    }

    @Test
    public void given_required_field_with_no_data_within_layout_when_validate_then_MinimumOccurrencesValidationError()
    {

        contentType.getForm().addFormItem( FieldSet.create().label( "My layout" ).name( "myLayout" ).addFormItem(
            Input.create().name( "myField" ).label( "Field" ).inputType( InputTypeName.TEXT_LINE ).required( true ).build() ).build() );
        Content content = Content.create().path( MY_CONTENT_PATH ).build();

        // exercise
        DataValidationErrors validationResults = newValidator( contentType ).validate( content.getData().getRoot() );
        assertTrue( validationResults.hasErrors() );
        assertTrue( validationResults.getFirst() instanceof MinimumOccurrencesValidationError );
    }

    @Test
    public void given_required_input_with_no_data_within_layout_within_layout_when_validate_then_MinimumOccurrencesValidationError()
    {
        contentType.getForm().addFormItem( FieldSet.create().label( "My outer layout" ).name( "myOuterlayout" ).addFormItem(
            FieldSet.create().label( "My Layout" ).name( "myLayout" ).addFormItem(
                Input.create().name( "myInput" ).label( "Input" ).inputType( InputTypeName.TEXT_LINE ).required(
                    true ).build() ).build() ).build() );
        Content content = Content.create().path( MY_CONTENT_PATH ).type( contentType.getName() ).build();

        // exercise
        DataValidationErrors validationResults = newValidator( contentType ).validate( content.getData().getRoot() );
        assertTrue( validationResults.hasErrors() );
        assertTrue( validationResults.getFirst() instanceof MinimumOccurrencesValidationError );
    }

    @Test()
    public void given_required_set_with_data_when_validate_then_hasErrors_returns_false()
    {
        contentType.getForm().addFormItem( FormItemSet.create().name( "mySet" ).required( true ).addFormItem(
            Input.create().name( "myInput" ).label( "Input" ).inputType( InputTypeName.TEXT_LINE ).build() ).build() );
        Content content = Content.create().path( MY_CONTENT_PATH ).type( contentType.getName() ).build();
        content.getData().setString( "mySet.myInput", "value" );

        // exercise
        DataValidationErrors validationResults = newValidator( contentType ).validate( content.getData().getRoot() );
        assertFalse( validationResults.hasErrors() );
    }

    @Test
    public void given_required_set_with_no_data_when_validate_then_MinimumOccurrencesValidationError()
    {
        contentType.getForm().addFormItem( FormItemSet.create().name( "mySet" ).required( true ).addFormItem(
            Input.create().name( "myInput" ).label( "Input" ).inputType( InputTypeName.TEXT_LINE ).build() ).build() );
        Content content = Content.create().path( MY_CONTENT_PATH ).type( contentType.getName() ).build();

        // exercise
        DataValidationErrors validationResults = newValidator( contentType ).validate( content.getData().getRoot() );
        assertTrue( validationResults.hasErrors() );
        assertTrue( validationResults.getFirst() instanceof MinimumOccurrencesValidationError );
        assertEquals( "FormItemSet [mySet] requires minimum 1 occurrence: 0", validationResults.getFirst().getErrorMessage() );
    }

    @Test
    public void given_required_set_with_no_data_within_layout_when_validate_then_MinimumOccurrencesValidationError()
    {
        contentType.getForm().addFormItem( FieldSet.create().label( "My layout" ).name( "myLayout" ).addFormItem(
            FormItemSet.create().name( "mySet" ).required( true ).addFormItem(
                Input.create().name( "myInput" ).label( "Input" ).inputType( InputTypeName.TEXT_LINE ).build() ).build() ).build() );
        Content content = Content.create().path( MY_CONTENT_PATH ).type( contentType.getName() ).build();

        // exercise
        DataValidationErrors validationResults = newValidator( contentType ).validate( content.getData().getRoot() );
        assertTrue( validationResults.hasErrors() );
        assertTrue( validationResults.getFirst() instanceof MinimumOccurrencesValidationError );
    }

    @Test
    public void given_required_input_at_top_and_inside_formItemSet_and_formItemSet_have_other_unrequired_data_when_validate_then_two_errors_are_found()
    {
        Input myInput =
            Input.create().name( "myRequiredInput" ).label( "Input" ).inputType( InputTypeName.TEXT_LINE ).required( true ).build();
        FormItemSet mySet = FormItemSet.create().name( "mySet" ).required( false ).addFormItem( myInput ).build();
        contentType.getForm().addFormItem( mySet );
        contentType.getForm().addFormItem(
            Input.create().name( "myOtherRequiredInput" ).label( "Other input" ).inputType( InputTypeName.TEXT_LINE ).required(
                true ).build() );
        Content content = Content.create().path( MY_CONTENT_PATH ).type( contentType.getName() ).build();
        content.getData().setString( "mySet.myUnrequiredData", "1" );

        assertEquals( "mySet.myRequiredInput", mySet.getInput( "myRequiredInput" ).getPath().toString() );

        // exercise
        DataValidationErrors validationResults = newValidator( contentType ).validate( content.getData().getRoot() );
        assertTrue( validationResults.hasErrors() );
        assertEquals( 2, validationResults.size() );
        assertTrue( validationResults.getFirst() instanceof MinimumOccurrencesValidationError );

        Iterator<DataValidationError> dataValidationErrorIterator = validationResults.iterator();

        DataValidationError nextDataValidationError = dataValidationErrorIterator.next();
        assertTrue( nextDataValidationError instanceof MinimumOccurrencesValidationError );
        assertEquals( "Input [mySet.myRequiredInput] requires minimum 1 occurrence: 0", nextDataValidationError.getErrorMessage() );
        assertNotNull( nextDataValidationError.getPath() );
        nextDataValidationError = dataValidationErrorIterator.next();
        assertTrue( nextDataValidationError instanceof MinimumOccurrencesValidationError );
        assertEquals( "Input [myOtherRequiredInput] requires minimum 1 occurrence: 0", nextDataValidationError.getErrorMessage() );
    }

    @Test
    public void data_for_input_is_not_required_if_parent_data_set_does_not_exist()
    {
        Input myInput =
            Input.create().name( "myRequiredInput" ).label( "Input" ).inputType( InputTypeName.TEXT_LINE ).required( true ).build();
        FormItemSet mySet = FormItemSet.create().name( "mySet" ).required( false ).addFormItem( myInput ).build();
        contentType.getForm().addFormItem( mySet );
        Content content = Content.create().path( MY_CONTENT_PATH ).type( contentType.getName() ).build();
        content.getData().setString( "myData", "1" );

        // exercise
        DataValidationErrors validationResults = newValidator( contentType ).validate( content.getData().getRoot() );
        assertFalse( validationResults.hasErrors() );
        assertEquals( 0, validationResults.size() );
    }

    @Test
    public void given_required_set_with_no_data_and_other_set_with_data_when_validate_then_MinimumOccurrencesValidationError()
    {
        // setup
        contentType.getForm().addFormItem( Input.create().name( "name" ).label( "Input" ).inputType( InputTypeName.TEXT_LINE ).build() );

        FormItemSet personalia = FormItemSet.create().name( "personalia" ).multiple( false ).required( true ).build();
        personalia.add( Input.create().name( "eyeColour" ).label( "Eye color" ).inputType( InputTypeName.TEXT_LINE ).build() );
        personalia.add( Input.create().name( "hairColour" ).label( "Hair color" ).inputType( InputTypeName.TEXT_LINE ).build() );
        contentType.getForm().addFormItem( personalia );

        FormItemSet crimes = FormItemSet.create().name( "crimes" ).multiple( true ).build();
        contentType.getForm().addFormItem( crimes );
        crimes.add( Input.create().name( "description" ).label( "Description" ).inputType( InputTypeName.TEXT_LINE ).build() );
        crimes.add( Input.create().name( "year" ).label( "Year" ).inputType( InputTypeName.TEXT_LINE ).build() );

        Content content = Content.create().path( MY_CONTENT_PATH ).type( contentType.getName() ).build();

        content.getData().setString( "name", "Thomas" );
        content.getData().setString( "crimes[0].description", "Stole tomatoes from neighbour" );
        content.getData().setString( "crimes[0].year", "1989" );
        content.getData().setString( "crimes[1].description", "Stole a chocolate from the Matbua shop" );
        content.getData().setString( "crimes[1].year", "1990" );

        // exercise
        DataValidationErrors validationResults = newValidator( contentType ).validate( content.getData().getRoot() );
        assertTrue( validationResults.hasErrors() );
        assertTrue( validationResults.getFirst() instanceof MinimumOccurrencesValidationError );
        assertEquals( "FormItemSet [personalia] requires minimum 1 occurrence: 0", validationResults.getFirst().getErrorMessage() );
    }

}
