package com.enonic.wem.portal.internal.underscore;

import java.time.Instant;

import org.mockito.Mockito;

import com.google.common.io.ByteStreams;

import com.enonic.wem.api.account.UserKey;
import com.enonic.wem.api.blob.Blob;
import com.enonic.wem.api.blob.BlobKey;
import com.enonic.wem.api.blob.BlobService;
import com.enonic.wem.api.content.Content;
import com.enonic.wem.api.content.ContentId;
import com.enonic.wem.api.content.ContentPath;
import com.enonic.wem.api.content.ContentService;
import com.enonic.wem.api.content.attachment.Attachment;
import com.enonic.wem.api.content.attachment.AttachmentService;
import com.enonic.wem.api.content.attachment.Attachments;
import com.enonic.wem.api.content.attachment.GetAttachmentParameters;
import com.enonic.wem.api.context.Context;
import com.enonic.wem.api.schema.content.ContentTypeName;
import com.enonic.wem.core.blobstore.memory.MemoryBlobRecord;
import com.enonic.wem.core.image.filter.BuilderContext;
import com.enonic.wem.core.image.filter.ImageFilter;
import com.enonic.wem.core.image.filter.ImageFilterBuilder;
import com.enonic.wem.portal.internal.base.ModuleBaseResourceTest;

public abstract class ImageBaseResourceTest<T extends ImageBaseResource>
    extends ModuleBaseResourceTest<T>
{
    private ImageFilterBuilder imageFilterBuilder;

    private AttachmentService attachmentService;

    private BlobService blobService;

    protected ContentService contentService;

    @Override
    protected void configure()
        throws Exception
    {
        super.configure();

        this.imageFilterBuilder = Mockito.mock( ImageFilterBuilder.class );
        this.resource.imageFilterBuilder = imageFilterBuilder;

        this.attachmentService = Mockito.mock( AttachmentService.class );
        this.resource.attachmentService = attachmentService;

        this.blobService = Mockito.mock( BlobService.class );
        this.resource.blobService = blobService;

        this.contentService = Mockito.mock( ContentService.class );
        this.resource.contentService = contentService;
    }

    protected final void setupContent( final Context context )
    throws Exception
    {
        final ContentPath contentPath = ContentPath.from( "path/to/content" );
        final Content content = createContent( "content-id", contentPath, "image" );
        Mockito.when( this.contentService.getById( Mockito.eq( content.getId() ), Mockito.eq( context ) ) ).
            thenReturn( content );
        Mockito.when( this.contentService.getByPath( Mockito.eq( content.getPath() ), Mockito.eq( context ) ) ).
            thenReturn( content );

        final BlobKey blobKey = new BlobKey( "<blobkey-1>" );
        final Attachment attachment = Attachment.newAttachment().
            blobKey( blobKey ).
            name( "enonic-logo.png" ).
            mimeType( "image/png" ).
            label( "small" ).
            build();

        final byte[] imageData = ByteStreams.toByteArray( getClass().getResourceAsStream( "enonic-logo.png" ) );
        Mockito.when( this.attachmentService.get( Mockito.isA( GetAttachmentParameters.class ) ) ).
            thenReturn( attachment );
        Mockito.when( this.attachmentService.getAll( Mockito.isA( ContentId.class ), Mockito.eq( context ) ) ).
            thenReturn( Attachments.from( attachment ) );

        final Blob blob = new MemoryBlobRecord( blobKey, imageData );
        Mockito.when( this.blobService.get( Mockito.isA( BlobKey.class ) ) ).
            thenReturn( blob );
        Mockito.when( this.imageFilterBuilder.build( Mockito.isA( BuilderContext.class ), Mockito.isA( String.class ) ) ).
            thenReturn( getImageFilterBuilder() );
    }

    private ImageFilter getImageFilterBuilder()
    {
        return source -> source;
    }

    private Content createContent( final String id, final ContentPath contentPath, final String contentTypeName )
    {
        return Content.newContent().
            id( ContentId.from( id ) ).
            path( contentPath ).
            createdTime( Instant.now() ).
            owner( UserKey.from( "myStore:me" ) ).
            displayName( "My Content" ).
            modifiedTime( Instant.now() ).
            modifier( UserKey.superUser() ).
            type( ContentTypeName.from( contentTypeName ) ).
            build();
    }
}