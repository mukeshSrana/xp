package com.enonic.wem.repo.internal.entity;

import com.enonic.xp.security.PrincipalKey;
import com.enonic.xp.security.acl.AccessControlEntry;
import com.enonic.xp.security.acl.AccessControlList;

public class NodeDefaultAclFactory
{
    public static AccessControlList create( final PrincipalKey creator )
    {


        return AccessControlList.create().
            add( AccessControlEntry.create().
                allowAll().
                principal( creator ).
                build() ).
            build();


    }
}
