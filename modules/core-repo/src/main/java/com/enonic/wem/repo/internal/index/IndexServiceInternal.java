package com.enonic.wem.repo.internal.index;

import java.util.Set;

import org.elasticsearch.common.unit.TimeValue;

import com.enonic.wem.repo.internal.elasticsearch.ClusterHealthStatus;
import com.enonic.wem.repo.internal.elasticsearch.ScanAndScrollParams;
import com.enonic.wem.repo.internal.index.result.ScrollResult;
import com.enonic.xp.index.IndexType;
import com.enonic.xp.node.Node;
import com.enonic.xp.node.NodeId;
import com.enonic.xp.node.NodeVersionId;
import com.enonic.xp.repository.RepositoryId;

public interface IndexServiceInternal
{
    public void createIndex( final String indexName, final String settings );

    public void deleteIndices( final String... indexNames );

    public boolean indicesExists( final String... indices );

    public void store( final Node node, final NodeVersionId nodeVersionId, final IndexContext context );

    public void delete( final NodeId nodeId, final IndexContext context );

    public ScrollResult startScanScroll( final ScanAndScrollParams params);

    public ScrollResult nextScanScroll( final String scrollId, final int keepAliveSeconds);

    public Set<String> getAllRepositoryIndices( final RepositoryId repositoryId );

    public void applyMapping( final String indexName, final IndexType indexType, final String mapping );

    public ClusterHealthStatus getClusterHealth( final TimeValue timeout, final String... indexNames );

    public void refresh( final String... indexNames );

}

