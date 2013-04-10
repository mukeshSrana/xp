package com.enonic.wem.core.jcr.loader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.jcr.NamespaceRegistry;
import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.Workspace;

import org.apache.jackrabbit.commons.cnd.CndImporter;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.enonic.wem.core.jcr.JcrConstants;
import com.enonic.wem.core.jcr.JcrHelper;
import com.enonic.wem.core.jcr.provider.JcrSessionProvider;

@Component
public final class JcrInitializer
    implements JcrConstants
{
    private final JcrSessionProvider jcrSessionProvider;

    @Inject
    public JcrInitializer( final JcrSessionProvider jcrSessionProvider )
    {
        this.jcrSessionProvider = jcrSessionProvider;
    }

    public boolean initialize()
        throws Exception
    {
        return initialize( false );
    }

    public boolean initialize( final boolean reInit )
        throws Exception
    {
        final Session session = this.jcrSessionProvider.loginAdmin();

        if ( reInit )
        {
            removeData( session );
        }

        try
        {
            return initialize( session );
        }
        finally
        {
            session.logout();
        }
    }

    private boolean initialize( final Session session )
        throws Exception
    {
        if ( session.nodeExists( "/" + JcrConstants.ROOT_NODE ) )
        {
            return false;
        }

        registerNamespaces( session );
        registerNodeTypes( session, "/META-INF/jcr/node_types.cnd" );
        importContent( session, "/META-INF/jcr/init_content.xml" );
        return true;
    }

    private void registerNamespaces( final Session session )
        throws Exception
    {
        final Workspace workspace = session.getWorkspace();
        final NamespaceRegistry reg = workspace.getNamespaceRegistry();

        try
        {
            reg.registerNamespace( WEM_NS_PREFIX, WEM_NS );
        }
        catch ( final Exception e )
        {
            // Do nothing
        }
    }

    private void registerNodeTypes( final Session session, final String resource )
        throws Exception
    {
        final InputStream in = getClass().getResourceAsStream( resource );
        final Reader fileReader = new InputStreamReader( in );
        CndImporter.registerNodeTypes( fileReader, session, true );
    }

    private void importContent( final Session session, final String resource )
        throws Exception
    {
        final InputStream in = getClass().getResourceAsStream( resource );
        new JcrXmlLoader( session ).importContent( in );
    }

    private void removeData( final Session session )
        throws Exception
    {
        final Node node = JcrHelper.getNodeOrNull( session.getRootNode(), "wem" );
        if ( node != null )
        {
            node.remove();
            session.save();
        }
    }
}
