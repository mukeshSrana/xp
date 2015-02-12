package com.enonic.wem.admin.rest.resource.auth;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.enonic.wem.admin.AdminResource;
import com.enonic.wem.admin.rest.resource.ResourceConstants;
import com.enonic.wem.admin.security.AuthHelper;
import com.enonic.wem.api.context.ContextAccessor;
import com.enonic.wem.api.security.SecurityService;
import com.enonic.wem.api.security.auth.AuthenticationInfo;
import com.enonic.wem.api.session.Session;

@Path(ResourceConstants.REST_ROOT + "auth")
@Produces(MediaType.APPLICATION_JSON)
@Component(immediate = true)
public final class AuthResource
    implements AdminResource
{
    private final AdminApplicationsRegistry appRegistry;

    private SecurityService securityService;

    public AuthResource()
    {
        this.appRegistry = new AdminApplicationsRegistry();
    }

    @POST
    @Path("login")
    public LoginResultJson login( final LoginJson login )
    {
        final AuthHelper helper = new AuthHelper( this.securityService );
        final AuthenticationInfo authInfo = helper.login( login.getUser(), login.getPassword(), login.isRememberMe() );

        if ( !authInfo.isAuthenticated() )
        {
            return new LoginResultJson( AuthenticationInfo.unAuthenticated() );
        }

        return new LoginResultJson( authInfo, this.appRegistry.getAllowedApplications( authInfo.getPrincipals() ) );
    }

    @POST
    @Path("logout")
    public void logout()
    {
        AuthHelper.logout();
    }

    @GET
    @Path("authenticated")
    public LoginResultJson isAuthenticated()
    {
        final Session session = ContextAccessor.current().getLocalScope().getSession();
        if ( session == null )
        {
            return new LoginResultJson( AuthenticationInfo.unAuthenticated() );
        }

        final AuthenticationInfo authInfo = ContextAccessor.current().getAuthInfo();
        return new LoginResultJson( authInfo, appRegistry.getAllowedApplications( authInfo.getPrincipals() ) );
    }

    @Reference
    public void setSecurityService( final SecurityService securityService )
    {
        this.securityService = securityService;
    }
}
