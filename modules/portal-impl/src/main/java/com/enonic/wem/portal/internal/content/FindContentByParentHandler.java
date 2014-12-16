package com.enonic.wem.portal.internal.content;

import com.enonic.wem.api.content.ContentPath;
import com.enonic.wem.api.content.ContentService;
import com.enonic.wem.api.content.FindContentByParentParams;
import com.enonic.wem.script.command.CommandHandler;
import com.enonic.wem.script.command.CommandRequest;

public final class FindContentByParentHandler
    implements CommandHandler
{
    private final ContentService contentService;

    public FindContentByParentHandler( final ContentService contentService )
    {
        this.contentService = contentService;
    }

    @Override
    public String getName()
    {
        return "content.findByParent";
    }

    @Override
    public Object execute( final CommandRequest req )
    {
        final FindContentByParentParams params = FindContentByParentParams.create().
            from( req.param( "from" ).value( Integer.class, 0 ) ).
            size( req.param( "size" ).value( Integer.class, 10 ) ).
            parentPath( req.param( "parentPath" ).required().value( ContentPath.class ) ).
            build();

        return this.contentService.findByParent( params ).getContents();
    }
}