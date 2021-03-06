package com.enonic.xp.admin.impl.json.content;

import java.util.Set;

import com.google.common.collect.Sets;

import com.enonic.xp.content.ContentVersion;
import com.enonic.xp.content.ContentVersions;

public class ContentVersionsJson
{
    private Set<ContentVersionJson> contentVersions = Sets.newLinkedHashSet();

    public ContentVersionsJson( final ContentVersions contentVersions )
    {
        for ( final ContentVersion contentVersion : contentVersions )
        {
            this.contentVersions.add( new ContentVersionJson( contentVersion ) );
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public Set<ContentVersionJson> getContentVersions()
    {
        return contentVersions;
    }
}
