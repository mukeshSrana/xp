package com.enonic.wem.repo.internal.entity;

import org.junit.Test;

import com.enonic.wem.api.node.CreateNodeParams;
import com.enonic.wem.api.node.FindNodesByParentParams;
import com.enonic.wem.api.node.FindNodesByParentResult;
import com.enonic.wem.api.node.Node;
import com.enonic.wem.api.node.NodePath;
import com.enonic.wem.api.node.Nodes;

import static org.junit.Assert.*;

public class DuplicateNodeCommandTest
    extends AbstractNodeTest
{

    @Test
    public void duplicate_single()
        throws Exception
    {
        final String nodeName = "my-node";
        final Node createdNode = createNode( CreateNodeParams.create().
            parent( NodePath.ROOT ).
            name( nodeName ).
            build() );

        final Node duplicatedNode = DuplicateNodeCommand.create().
            id( createdNode.id() ).
            versionService( versionService ).
            indexService( indexService ).
            nodeDao( nodeDao ).
            queryService( queryService ).
            workspaceService( workspaceService ).
            build().
            execute();

        assertEquals( nodeName + "-" + DuplicateValueResolver.COPY_TOKEN, duplicatedNode.name().toString() );
    }

    @Test
    public void duplicate_with_children()
        throws Exception
    {
        final String nodeName = "my-node";
        final Node createdNode = createNode( CreateNodeParams.create().
            parent( NodePath.ROOT ).
            name( nodeName ).
            build() );

        final Node childNode = createNode( CreateNodeParams.create().
            parent( createdNode.path() ).
            name( "my-child" ).
            build() );

        refresh();

        final Node duplicatedNode = DuplicateNodeCommand.create().
            id( createdNode.id() ).
            versionService( versionService ).
            indexService( indexService ).
            nodeDao( nodeDao ).
            queryService( queryService ).
            workspaceService( workspaceService ).
            build().
            execute();

        refresh();

        final FindNodesByParentResult children = findByParent( FindNodesByParentParams.create().
            parentPath( duplicatedNode.path() ).
            build() );

        final Nodes childNodes = children.getNodes();
        assertEquals( 1, childNodes.getSize() );
        assertEquals( childNode.name(), childNodes.first().name() );
    }

}