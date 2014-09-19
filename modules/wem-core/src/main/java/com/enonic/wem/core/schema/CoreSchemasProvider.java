package com.enonic.wem.core.schema;

import java.io.InputStream;
import java.time.Instant;
import java.util.List;

import org.apache.commons.lang.WordUtils;

import com.google.common.collect.Lists;

import com.enonic.wem.api.Icon;
import com.enonic.wem.api.form.Form;
import com.enonic.wem.api.form.Input;
import com.enonic.wem.api.form.inputtype.InputTypes;
import com.enonic.wem.api.schema.Schema;
import com.enonic.wem.api.schema.SchemaProvider;
import com.enonic.wem.api.schema.Schemas;
import com.enonic.wem.api.schema.content.ContentType;
import com.enonic.wem.api.schema.content.ContentTypeName;
import com.enonic.wem.api.schema.content.ContentTypeNames;
import com.enonic.wem.api.schema.relationship.RelationshipType;
import com.enonic.wem.api.schema.relationship.RelationshipTypeName;

import static com.enonic.wem.api.schema.content.ContentType.newContentType;
import static com.enonic.wem.api.schema.relationship.RelationshipType.newRelationshipType;

public final class CoreSchemasProvider
    implements SchemaProvider
{
    private static final String CONTENT_TYPES_FOLDER = "content-types";

    private static final String RELATIONSHIP_TYPES_FOLDER = "relationship-types";

    // System Content Types
    public static Form MEDIA_IMAGE_FORM = createMediaImageForm();

    private static final ContentType STRUCTURED = createSystemType( ContentTypeName.structured() ).
        setFinal( false ).setAbstract( true ).build();

    private static final ContentType UNSTRUCTURED = createSystemType( ContentTypeName.unstructured() ).
        setFinal( false ).setAbstract( false ).build();

    public static final ContentType FOLDER = createSystemType( ContentTypeName.folder() ).
        setFinal( false ).setAbstract( false ).build();

    private static final ContentType PAGE = createSystemType( ContentTypeName.page() ).setFinal( true ).setAbstract( false ).build();

    private static final ContentType SITE =
        createSystemType( ContentTypeName.site() ).description( "Root content for sites" ).setFinal( true ).setAbstract( false ).build();

    private static final ContentType SHORTCUT = createSystemType( ContentTypeName.shortcut() ).
        setFinal( true ).setAbstract( false ).build();

    private static final ContentType MEDIA = createSystemType( ContentTypeName.media() ).
        setFinal( false ).setAbstract( false ).allowChildContent( false ).build();

    private static final ContentType MEDIA_TEXT = createSystemType( ContentTypeName.textMedia() ).superType( ContentTypeName.media() ).
        setFinal( true ).setAbstract( false ).allowChildContent( false ).build();

    private static final ContentType MEDIA_DATA = createSystemType( ContentTypeName.dataMedia() ).superType( ContentTypeName.media() ).
        setFinal( true ).setAbstract( false ).allowChildContent( false ).build();

    private static final ContentType MEDIA_AUDIO = createSystemType( ContentTypeName.audioMedia() ).superType( ContentTypeName.media() ).
        setFinal( true ).setAbstract( false ).allowChildContent( false ).build();

    private static final ContentType MEDIA_VIDEO = createSystemType( ContentTypeName.videoMedia() ).superType( ContentTypeName.media() ).
        setFinal( true ).setAbstract( false ).allowChildContent( false ).build();

    private static final ContentType MEDIA_IMAGE = createSystemType( ContentTypeName.imageMedia() ).superType( ContentTypeName.media() ).
        setFinal( true ).setAbstract( false ).allowChildContent( false ).form( MEDIA_IMAGE_FORM ).build();

    private static final ContentType MEDIA_VECTOR = createSystemType( ContentTypeName.vectorMedia() ).superType( ContentTypeName.media() ).
        setFinal( true ).setAbstract( false ).allowChildContent( false ).build();

    private static final ContentType MEDIA_ARCHIVE =
        createSystemType( ContentTypeName.archiveMedia() ).superType( ContentTypeName.media() ).
            setFinal( true ).setAbstract( false ).allowChildContent( false ).build();

    private static final ContentType MEDIA_DOCUMENT =
        createSystemType( ContentTypeName.documentMedia() ).superType( ContentTypeName.media() ).
            setFinal( true ).setAbstract( false ).allowChildContent( false ).build();

    private static final ContentType MEDIA_SPREADSHEET =
        createSystemType( ContentTypeName.spreadsheetMedia() ).superType( ContentTypeName.media() ).
            setFinal( true ).setAbstract( false ).allowChildContent( false ).build();

    private static final ContentType MEDIA_PRESENTATION =
        createSystemType( ContentTypeName.presentationMedia() ).superType( ContentTypeName.media() ).
            setFinal( true ).setAbstract( false ).allowChildContent( false ).build();

    private static final ContentType MEDIA_CODE = createSystemType( ContentTypeName.codeMedia() ).superType( ContentTypeName.media() ).
        setFinal( true ).setAbstract( false ).allowChildContent( false ).build();

    private static final ContentType MEDIA_EXECUTABLE =
        createSystemType( ContentTypeName.executableMedia() ).superType( ContentTypeName.media() ).
            setFinal( true ).setAbstract( false ).allowChildContent( false ).build();

    private static final ContentType[] SYSTEM_TYPES =
        {UNSTRUCTURED, STRUCTURED, FOLDER, PAGE, SHORTCUT, MEDIA, MEDIA_TEXT, MEDIA_DATA, MEDIA_AUDIO, MEDIA_VIDEO, MEDIA_IMAGE,
            MEDIA_VECTOR, MEDIA_ARCHIVE, MEDIA_DOCUMENT, MEDIA_SPREADSHEET, MEDIA_PRESENTATION, MEDIA_CODE, MEDIA_EXECUTABLE, SITE};

    // System Relationship Types
    private static final RelationshipType DEFAULT =
        createRelationshipType( RelationshipTypeName.DEFAULT, "Default", "relates to", "related of" );

    private static final RelationshipType PARENT = createRelationshipType( RelationshipTypeName.PARENT, "Parent", "parent of", "child of" );

    private static final RelationshipType LINK = createRelationshipType( RelationshipTypeName.LINK, "Link", "links to", "linked by" );

    private static final RelationshipType LIKE = createRelationshipType( RelationshipTypeName.LIKE, "Like", "likes", "liked by" );

    private static final RelationshipType IMAGE =
        createRelationshipType( RelationshipTypeName.IMAGE, "Image", "relates to image", "related of image",
                                ContentTypeNames.from( ContentTypeName.imageMedia() ) );

    private static final RelationshipType[] RELATIONSHIP_TYPES = {DEFAULT, PARENT, LINK, LIKE, IMAGE};


    private static ContentType.Builder createSystemType( final ContentTypeName contentTypeName )
    {
        final String displayName = WordUtils.capitalize( contentTypeName.getLocalName() );
        return newContentType().
            name( contentTypeName ).
            displayName( displayName ).
            setBuiltIn();
    }

    private static Form createMediaImageForm()
    {
        return Form.newForm().
            addFormItem( Input.newInput().name( "image" ).
                inputType( InputTypes.IMAGE ).build() ).
            addFormItem( Input.newInput().name( "mimeType" ).
                inputType( InputTypes.TEXT_LINE ).
                label( "Mime type" ).
                occurrences( 1, 1 ).
                build() ).

            build();
    }

    private static RelationshipType createRelationshipType( final RelationshipTypeName relationshipTypeName, final String displayName,
                                                            final String fromSemantic, final String toSemantic )
    {
        return createRelationshipType( relationshipTypeName, displayName, fromSemantic, toSemantic, ContentTypeNames.empty() );
    }

    private static RelationshipType createRelationshipType( final RelationshipTypeName relationshipTypeName, final String displayName,
                                                            final String fromSemantic, final String toSemantic,
                                                            final ContentTypeNames toContentTypes )
    {
        return newRelationshipType().
            name( relationshipTypeName ).
            displayName( displayName ).
            fromSemantic( fromSemantic ).
            toSemantic( toSemantic ).
            addAllowedToTypes( toContentTypes ).
            build();
    }

    private List<ContentType> generateSystemContentTypes()
    {
        final List<ContentType> systemContentTypes = Lists.newArrayList();
        for ( ContentType contentType : SYSTEM_TYPES )
        {
            contentType = newContentType( contentType ).
                icon( loadSchemaIcon( CONTENT_TYPES_FOLDER, contentType.getName().getLocalName() ) ).
                build();
            systemContentTypes.add( contentType );
        }
        return systemContentTypes;
    }

    private List<RelationshipType> generateSystemRelationshipTypes()
    {
        final List<RelationshipType> relationshipTypes = Lists.newArrayList();
        for ( RelationshipType relationshipType : RELATIONSHIP_TYPES )
        {
            relationshipType = RelationshipType.newRelationshipType( relationshipType ).
                icon( loadSchemaIcon( RELATIONSHIP_TYPES_FOLDER, relationshipType.getName().getLocalName() ) ).
                build();
            relationshipTypes.add( relationshipType );
        }
        return relationshipTypes;
    }

    @Override
    public Schemas getSchemas()
    {
        final List<Schema> schemas = Lists.newArrayList();
        schemas.addAll( generateSystemContentTypes() );
        schemas.addAll( generateSystemRelationshipTypes() );
        return Schemas.from( schemas );
    }

    private Icon loadSchemaIcon( final String metaInfFolderName, final String name )
    {
        final String metaInfFolderBasePath = "/" + "META-INF" + "/" + metaInfFolderName;
        final String filePath = metaInfFolderBasePath + "/" + name.toLowerCase() + ".png";
        try
        {
            final InputStream stream = this.getClass().getResourceAsStream( filePath );
            if ( stream == null )
            {
                return null;
            }
            return Icon.from( stream, "image/png", Instant.now() );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( "Failed to load icon file: " + filePath, e );
        }
    }

}