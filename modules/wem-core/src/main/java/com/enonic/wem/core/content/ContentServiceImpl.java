package com.enonic.wem.core.content;

import com.enonic.wem.api.blob.BlobService;
import com.enonic.wem.api.content.CompareContentParams;
import com.enonic.wem.api.content.CompareContentResult;
import com.enonic.wem.api.content.CompareContentResults;
import com.enonic.wem.api.content.CompareContentsParams;
import com.enonic.wem.api.content.Content;
import com.enonic.wem.api.content.ContentId;
import com.enonic.wem.api.content.ContentPath;
import com.enonic.wem.api.content.ContentPaths;
import com.enonic.wem.api.content.ContentService;
import com.enonic.wem.api.content.Contents;
import com.enonic.wem.api.content.CreateContentParams;
import com.enonic.wem.api.content.DeleteContentParams;
import com.enonic.wem.api.content.DeleteContentResult;
import com.enonic.wem.api.content.FindContentByParentParams;
import com.enonic.wem.api.content.FindContentByParentResult;
import com.enonic.wem.api.content.FindContentByQueryParams;
import com.enonic.wem.api.content.FindContentByQueryResult;
import com.enonic.wem.api.content.FindContentVersionsParams;
import com.enonic.wem.api.content.FindContentVersionsResult;
import com.enonic.wem.api.content.GetActiveContentVersionsParams;
import com.enonic.wem.api.content.GetActiveContentVersionsResult;
import com.enonic.wem.api.content.GetContentByIdsParams;
import com.enonic.wem.api.content.PushContentParams;
import com.enonic.wem.api.content.RenameContentParams;
import com.enonic.wem.api.content.SetContentChildOrderParams;
import com.enonic.wem.api.content.UpdateContentParams;
import com.enonic.wem.api.content.ValidateContentData;
import com.enonic.wem.api.content.attachment.AttachmentService;
import com.enonic.wem.api.content.data.ContentData;
import com.enonic.wem.api.content.site.CreateSiteParams;
import com.enonic.wem.api.content.site.ModuleConfig;
import com.enonic.wem.api.content.site.ModuleConfigDataSerializer;
import com.enonic.wem.api.content.site.Site;
import com.enonic.wem.api.data.DataId;
import com.enonic.wem.api.data.Value;
import com.enonic.wem.api.schema.content.ContentTypeForms;
import com.enonic.wem.api.schema.content.ContentTypeName;
import com.enonic.wem.api.schema.content.ContentTypeService;
import com.enonic.wem.api.schema.content.validator.DataValidationErrors;
import com.enonic.wem.core.entity.Node;
import com.enonic.wem.core.entity.NodeId;
import com.enonic.wem.core.entity.NodeService;
import com.enonic.wem.core.entity.SetNodeChildOrderParams;
import com.enonic.wem.core.index.query.QueryService;

public class ContentServiceImpl
    implements ContentService
{
    private ContentTypeService contentTypeService;

    private NodeService nodeService;

    private BlobService blobService;

    private AttachmentService attachmentService;

    private QueryService queryService;

    private ContentNodeTranslator contentNodeTranslator;

    private final static ModuleConfigDataSerializer MODULE_CONFIG_DATA_SERIALIZER = new ModuleConfigDataSerializer();

    @Override
    public Content getById( final ContentId id )
    {
        return GetContentByIdCommand.create( id ).
            nodeService( this.nodeService ).
            contentTypeService( this.contentTypeService ).
            blobService( this.blobService ).
            translator( this.contentNodeTranslator ).
            build().
            execute();
    }

    @Override
    public Site getNearestSite( final ContentId contentId )
    {
        return GetNearestSiteCommand.create().
            contentService( this ).
            contentId( contentId ).
            build().
            execute();
    }

    @Override
    public Contents getByIds( final GetContentByIdsParams params )
    {
        return GetContentByIdsCommand.create( params ).
            nodeService( this.nodeService ).
            contentTypeService( this.contentTypeService ).
            blobService( this.blobService ).
            translator( this.contentNodeTranslator ).
            queryService( this.queryService ).
            build().
            execute();
    }

    @Override
    public Content getByPath( final ContentPath path )
    {
        return GetContentByPathCommand.create( path ).
            nodeService( this.nodeService ).
            contentTypeService( this.contentTypeService ).
            blobService( this.blobService ).
            translator( this.contentNodeTranslator ).
            build().
            execute();
    }

    @Override
    public Contents getByPaths( final ContentPaths paths )
    {
        return GetContentByPathsCommand.create( paths ).
            nodeService( this.nodeService ).
            contentTypeService( this.contentTypeService ).
            blobService( this.blobService ).
            translator( this.contentNodeTranslator ).
            build().
            execute();
    }

    @Override
    public FindContentByParentResult findByParent( final FindContentByParentParams params )
    {
        return FindContentByParentCommand.create( params ).
            queryService( this.queryService ).
            nodeService( this.nodeService ).
            contentTypeService( this.contentTypeService ).
            blobService( this.blobService ).
            translator( this.contentNodeTranslator ).
            build().
            execute();
    }

    @Override
    public Site create( final CreateSiteParams params )
    {

        // TODO: validate that PageTemplates are only created below  Site/templates

        final ContentData data = new ContentData();
        data.setProperty( DataId.from( "description", 0 ), Value.newString( params.getDescription() ) );
        for ( final ModuleConfig moduleConfig : params.getModuleConfigs() )
        {
            data.addProperty( "modules", Value.newData( MODULE_CONFIG_DATA_SERIALIZER.toData( moduleConfig ) ) );
        }

        final CreateContentParams createContentParams = new CreateContentParams().
            contentType( ContentTypeName.site() ).
            parent( params.getParentContentPath() ).
            name( params.getName() ).
            displayName( params.getDisplayName() ).
            form( ContentTypeForms.SITE ).
            contentData( data ).draft( params.isDraft() );

        final Site site = (Site) CreateContentCommand.create().
            nodeService( this.nodeService ).
            contentTypeService( this.contentTypeService ).
            blobService( this.blobService ).
            translator( this.contentNodeTranslator ).
            params( createContentParams ).
            build().
            execute();

        this.create( new CreateContentParams().
            owner( site.getOwner() ).
            displayName( "Templates" ).
            name( "templates" ).
            parent( site.getPath() ).
            contentType( ContentTypeName.folder() ).
            draft( false ).
            contentData( new ContentData() ) );

        return site;
    }

    @Override
    public Content create( final CreateContentParams params )
    {
        final Content content = CreateContentCommand.create().
            nodeService( this.nodeService ).
            contentTypeService( this.contentTypeService ).
            blobService( this.blobService ).
            translator( this.contentNodeTranslator ).
            params( params ).
            build().
            execute();

        if ( content instanceof Site )
        {
            this.create( new CreateContentParams().
                owner( content.getOwner() ).
                displayName( "Templates" ).
                name( "templates" ).
                parent( content.getPath() ).
                contentType( ContentTypeName.folder() ).
                draft( false ).
                contentData( new ContentData() ) );
        }

        return content;
    }

    @Override
    public Content update( final UpdateContentParams params )
    {
        return UpdateContentCommand.create( params ).
            nodeService( this.nodeService ).
            contentTypeService( this.contentTypeService ).
            blobService( this.blobService ).
            attachmentService( this.attachmentService ).
            translator( this.contentNodeTranslator ).
            build().
            execute();
    }

    @Override
    public DeleteContentResult delete( final DeleteContentParams params )
    {
        return DeleteContentCommand.create().
            nodeService( this.nodeService ).
            contentTypeService( this.contentTypeService ).
            blobService( this.blobService ).
            translator( this.contentNodeTranslator ).
            params( params ).
            build().
            execute();
    }

    @Override
    public Content push( final PushContentParams params )
    {
        params.getContentId();

        return PushContentCommand.create().
            nodeService( this.nodeService ).
            contentTypeService( this.contentTypeService ).
            blobService( this.blobService ).
            translator( this.contentNodeTranslator ).
            contentId( params.getContentId() ).
            target( params.getTarget() ).
            build().
            execute();
    }

    @Override
    public DataValidationErrors validate( final ValidateContentData data )
    {
        return new ValidateContentDataCommand().
            contentTypeService( this.contentTypeService ).
            data( data ).
            execute();
    }

    @Override
    public Content rename( final RenameContentParams params )
    {
        return RenameContentCommand.create( params ).
            nodeService( this.nodeService ).
            contentTypeService( this.contentTypeService ).
            blobService( this.blobService ).
            translator( this.contentNodeTranslator ).
            build().
            execute();
    }

    @Override
    public FindContentByQueryResult find( final FindContentByQueryParams params )
    {
        return FindContentByQueryCommand.create().
            params( params ).
            queryService( this.queryService ).
            nodeService( this.nodeService ).
            contentTypeService( this.contentTypeService ).
            blobService( this.blobService ).
            translator( this.contentNodeTranslator ).
            build().
            execute();
    }

    @Override
    public CompareContentResult compare( final CompareContentParams params )
    {
        return CompareContentCommand.create().
            nodeService( this.nodeService ).
            contentId( params.getContentId() ).
            target( params.getTarget() ).
            build().
            execute();
    }

    @Override
    public CompareContentResults compare( final CompareContentsParams params )
    {
        return CompareContentsCommand.create().
            nodeService( this.nodeService ).
            contentIds( params.getContentIds() ).
            target( params.getTarget() ).
            build().
            execute();
    }

    @Override
    public FindContentVersionsResult getVersions( final FindContentVersionsParams params )
    {
        return FindContentVersionsCommand.create().
            nodeService( this.nodeService ).
            contentTypeService( this.contentTypeService ).
            blobService( this.blobService ).
            translator( this.contentNodeTranslator ).
            contentId( params.getContentId() ).
            from( params.getFrom() ).
            size( params.getSize() ).
            build().
            execute();
    }


    @Override
    public GetActiveContentVersionsResult getActiveVersions( final GetActiveContentVersionsParams params )
    {
        return GetActiveContentVersionsCommand.create().
            nodeService( this.nodeService ).
            contentTypeService( this.contentTypeService ).
            blobService( this.blobService ).
            translator( this.contentNodeTranslator ).
            contentId( params.getContentId() ).
            workspaces( params.getWorkspaces() ).
            build().
            execute();
    }

    @Override
    public Content setChildOrder( final SetContentChildOrderParams params )
    {
        final Node node = nodeService.setChildOrder( SetNodeChildOrderParams.create().
            nodeId( NodeId.from( params.getContentId() ) ).
            childOrder( params.getChildOrder() ).
            build() );

        return contentNodeTranslator.fromNode( node );
    }

    @Override
    public String generateContentName( final String displayName )
    {
        return new ContentPathNameGenerator().generatePathName( displayName );
    }

    public void setContentTypeService( final ContentTypeService contentTypeService )
    {
        this.contentTypeService = contentTypeService;
    }

    public void setNodeService( final NodeService nodeService )
    {
        this.nodeService = nodeService;
    }

    public void setBlobService( final BlobService blobService )
    {
        this.blobService = blobService;
    }

    public void setAttachmentService( final AttachmentService attachmentService )
    {
        this.attachmentService = attachmentService;
    }

    public void setQueryService( final QueryService queryService )
    {
        this.queryService = queryService;
    }

    public void setContentNodeTranslator( final ContentNodeTranslator contentNodeTranslator )
    {
        this.contentNodeTranslator = contentNodeTranslator;
    }
}
