package com.enonic.xp.admin.impl.json.content;

import java.util.Set;

import com.google.common.collect.Sets;

import com.enonic.xp.content.ActiveContentVersionEntry;
import com.enonic.xp.content.GetActiveContentVersionsResult;

public class GetActiveContentVersionsResultJson
{
    private final Set<ActiveContentVersionEntryJson> activeContentVersions = Sets.newLinkedHashSet();

    public GetActiveContentVersionsResultJson( final GetActiveContentVersionsResult result )
    {
        for ( final ActiveContentVersionEntry activeContentVersionEntry : result.getActiveContentVersions() )
        {
            activeContentVersions.add( new ActiveContentVersionEntryJson( activeContentVersionEntry ) );
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public Set<ActiveContentVersionEntryJson> getActiveContentVersions()
    {
        return activeContentVersions;
    }
}
