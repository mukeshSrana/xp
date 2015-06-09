package com.enonic.wem.repo.internal.elasticsearch;

import java.util.Collection;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.snapshots.SnapshotInfo;

import com.enonic.wem.repo.internal.elasticsearch.document.DeleteDocument;
import com.enonic.wem.repo.internal.elasticsearch.document.StoreDocument;
import com.enonic.wem.repo.internal.elasticsearch.query.ElasticsearchQuery;
import com.enonic.wem.repo.internal.index.result.GetResult;
import com.enonic.wem.repo.internal.index.result.ScrollResult;
import com.enonic.wem.repo.internal.index.result.SearchResult;
import com.enonic.xp.snapshot.RestoreParams;
import com.enonic.xp.snapshot.RestoreResult;
import com.enonic.xp.snapshot.SnapshotParams;
import com.enonic.xp.snapshot.SnapshotResult;
import com.enonic.xp.snapshot.SnapshotResults;

public interface ElasticsearchDao
{
    public String store( IndexRequest indexRequest );

    public void store( Collection<StoreDocument> storeDocuments );

    public boolean delete( DeleteRequest deleteRequest );

    public boolean delete( DeleteDocument deleteDocument );

    public SearchResult find( ElasticsearchQuery query );

    public GetResult get( GetQuery getQuery );

    public ScrollResult startScroll( final ScanAndScrollParams params );

    public ScrollResult getScroll( final String scrollId, final int keepAliveSeconds );

    public long count( ElasticsearchQuery query );

    public SnapshotResult snapshot( final SnapshotParams params );

    public RestoreResult restoreSnapshot( final RestoreParams params );

    public SnapshotInfo getSnapshot( final String snapshotName );

    public void deleteSnapshot( final String snapshotName );

    public void deleteSnapshotRepository();

    public SnapshotResults listSnapshots();

}
