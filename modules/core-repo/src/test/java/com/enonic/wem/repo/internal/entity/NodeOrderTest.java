package com.enonic.wem.repo.internal.entity;

import java.util.Iterator;

import org.junit.Test;

import com.enonic.wem.api.content.ContentConstants;
import com.enonic.wem.api.context.ContextAccessor;
import com.enonic.wem.api.data.PropertyTree;
import com.enonic.wem.api.data.Value;
import com.enonic.wem.api.index.IndexPath;
import com.enonic.wem.api.index.PatternIndexConfigDocument;
import com.enonic.wem.api.node.CreateNodeParams;
import com.enonic.wem.api.node.Node;
import com.enonic.wem.api.node.NodeId;
import com.enonic.wem.api.node.NodePath;
import com.enonic.wem.api.node.NodeQuery;
import com.enonic.wem.api.query.expr.DynamicConstraintExpr;
import com.enonic.wem.api.query.expr.DynamicOrderExpr;
import com.enonic.wem.api.query.expr.FieldOrderExpr;
import com.enonic.wem.api.query.expr.FunctionExpr;
import com.enonic.wem.api.query.expr.OrderExpr;
import com.enonic.wem.api.query.expr.QueryExpr;
import com.enonic.wem.api.query.expr.ValueExpr;
import com.enonic.wem.api.util.GeoPoint;
import com.enonic.wem.repo.internal.index.IndexContext;
import com.enonic.wem.repo.internal.index.query.NodeQueryResult;

import static org.junit.Assert.*;

public class NodeOrderTest
    extends AbstractNodeTest
{

    @Test
    public void geo_distance_sorting()
        throws Exception
    {
        final Node node1 = createNode( "node1", Value.newGeoPoint( GeoPoint.from( "80,80" ) ) );
        final Node node2 = createNode( "node2", Value.newGeoPoint( GeoPoint.from( "81,80" ) ) );
        final Node node3 = createNode( "node3", Value.newGeoPoint( GeoPoint.from( "82,80" ) ) );
        final Node node4 = createNode( "node4", Value.newGeoPoint( GeoPoint.from( "83,80" ) ) );

        final NodeQuery distanceQuery = NodeQuery.create().
            query( QueryExpr.from( null, new DynamicOrderExpr(
                FunctionExpr.from( "geoDistance", ValueExpr.string( "my-value" ), ValueExpr.geoPoint( "83,80" ) ),
                OrderExpr.Direction.ASC ) ) ).
            build();

        final NodeQueryResult nodeQueryResult = queryService.find( distanceQuery, IndexContext.from( ContextAccessor.current() ) );

        final Iterator<NodeId> iterator = nodeQueryResult.getNodeIds().iterator();
        assertEquals( node4.id(), iterator.next() );
        assertEquals( node3.id(), iterator.next() );
        assertEquals( node2.id(), iterator.next() );
        assertEquals( node1.id(), iterator.next() );

    }

    @Test
    public void score_sorting()
        throws Exception
    {
        final Node node1 = createNode( "node1", Value.newString( "denne har en fisk" ) );
        final Node node2 = createNode( "node2", Value.newString( "denne har fisk og ost" ) );
        final Node node3 = createNode( "node3", Value.newString( "mens denne har både fisk, ost og pølse" ) );
        final Node node4 = createNode( "node4", Value.newString( "denne vinner, siden den har alle sammen: fisk, ost, pølse og pai" ) );

        final FunctionExpr fulltextExpression =
            FunctionExpr.from( "fulltext", ValueExpr.string( "my-value" ), ValueExpr.string( "pai fisk pølse ost" ),
                               ValueExpr.string( "OR" ) );

        final NodeQuery fulltextQuery = NodeQuery.create().
            query( QueryExpr.from( new DynamicConstraintExpr( fulltextExpression ),
                                   FieldOrderExpr.create( IndexPath.from( "_score" ), OrderExpr.Direction.DESC ) ) ).
            build();

        printContentRepoIndex();

        final NodeQueryResult nodeQueryResult = queryService.find( fulltextQuery, IndexContext.from( ContextAccessor.current() ) );

        assertEquals( 4, nodeQueryResult.getHits() );

        final Iterator<NodeId> iterator = nodeQueryResult.getNodeIds().iterator();
        assertEquals( node4.id(), iterator.next() );
        assertEquals( node3.id(), iterator.next() );
        assertEquals( node2.id(), iterator.next() );
        assertEquals( node1.id(), iterator.next() );
    }

    private Node createNode( final String name, final Value value )
    {
        final PropertyTree data = new PropertyTree( new PropertyTree.PredictivePropertyIdProvider() );
        data.setProperty( "my-value", value );

        return createNode( CreateNodeParams.create().
            name( name ).
            parent( NodePath.ROOT ).
            data( data ).
            setNodeId( NodeId.from( name ) ).
            indexConfigDocument( PatternIndexConfigDocument.create().
                analyzer( ContentConstants.CONTENT_DEFAULT_ANALYZER ).
                build() ).
            build() );
    }

}