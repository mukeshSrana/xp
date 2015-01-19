package com.enonic.wem.api.security.auth;

import com.google.common.collect.ImmutableSet;

import com.enonic.wem.api.security.PrincipalKey;
import com.enonic.wem.api.security.PrincipalKeys;
import com.enonic.wem.api.security.RoleKeys;
import com.enonic.wem.api.security.User;

import static com.google.common.base.Preconditions.checkNotNull;

public final class AuthenticationInfo
{
    private final User user;

    private final PrincipalKeys principals;

    private final boolean authenticated;

    private AuthenticationInfo( final Builder builder )
    {
        this.authenticated = builder.authenticated;
        if ( builder.authenticated )
        {
            this.user = checkNotNull( builder.user, "AuthenticationInfo user cannot be null" );
            builder.principals.add( user.getKey() );
        }
        else
        {
            this.user = null;
        }
        this.principals = PrincipalKeys.from( builder.principals.build() );
    }

    public boolean isAuthenticated()
    {
        return authenticated;
    }

    public User getUser()
    {
        return user;
    }

    public PrincipalKeys getPrincipals()
    {
        return principals;
    }

    public boolean hasRole( final String role )
    {
        return principals.stream().anyMatch( principal -> principal.isRole() && principal.getId().equals( role ) );
    }

    public boolean hasRole( final PrincipalKey role )
    {
        return principals.stream().anyMatch( principal -> principal.isRole() && principal.equals( role ) );
    }

    public static Builder create()
    {
        return new Builder( true );
    }

    public static AuthenticationInfo unAuthenticated()
    {
        return new Builder( false ).principals( PrincipalKey.ofAnonymous(), RoleKeys.EVERYONE ).build();
    }

    public static class Builder
    {
        private User user;

        private final ImmutableSet.Builder<PrincipalKey> principals;

        private boolean authenticated;

        private Builder( final boolean authenticated )
        {
            this.principals = ImmutableSet.builder();
            this.authenticated = authenticated;
        }

        public Builder user( final User user )
        {
            this.user = user;
            return this;
        }

        public Builder principals( final Iterable<PrincipalKey> principals )
        {
            this.principals.addAll( principals );
            return this;
        }

        public Builder principals( final PrincipalKey... principals )
        {
            for ( PrincipalKey principal : principals )
            {
                this.principals.add( principal );
            }
            return this;
        }

        public AuthenticationInfo build()
        {
            return new AuthenticationInfo( this );
        }
    }

    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }

        final AuthenticationInfo that = (AuthenticationInfo) o;

        if ( authenticated != that.authenticated )
        {
            return false;
        }
        if ( principals != null ? !principals.equals( that.principals ) : that.principals != null )
        {
            return false;
        }
        if ( user != null ? !user.equals( that.user ) : that.user != null )
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + ( principals != null ? principals.hashCode() : 0 );
        result = 31 * result + ( authenticated ? 1 : 0 );
        return result;
    }
}