package com.enonic.wem.admin.rest.resource.security.json;

import com.enonic.wem.api.security.UserStore;

@SuppressWarnings("UnusedDeclaration")
public class UserStoreSummaryJson
{
    private final UserStore userStore;

    public UserStoreSummaryJson( final UserStore userStore )
    {

        this.userStore = userStore;
    }

    public String getDisplayName()
    {
        return userStore.getDisplayName();
    }

    public String getKey()
    {
        return userStore.getKey().toString();
    }
}