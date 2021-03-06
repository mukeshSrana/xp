package com.enonic.xp.admin.impl.app;

import com.enonic.xp.security.PrincipalKeys;

public final class AdminApplication
{
    private final String id;

    private final PrincipalKeys requiredAccess;

    public AdminApplication( final String id, final PrincipalKeys requiredAccess )
    {
        this.id = id;
        this.requiredAccess = requiredAccess;
    }

    public String getId()
    {
        return id;
    }

    public boolean isAccessAllowed( final PrincipalKeys authPrincipals )
    {
        return authPrincipals.stream().anyMatch( requiredAccess::contains );
    }
}
