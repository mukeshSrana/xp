package com.enonic.wem.repo.internal.systemexport;

import org.elasticsearch.action.index.IndexRequest;

import com.google.common.base.Preconditions;

import com.enonic.wem.repo.internal.elasticsearch.xcontent.BranchDumpXContentBuilderFactory;
import com.enonic.wem.repo.internal.repository.IndexNameResolver;
import com.enonic.wem.repo.internal.version.NodeVersionDocumentId;
import com.enonic.xp.index.IndexType;
import com.enonic.xp.node.NodeId;
import com.enonic.xp.node.NodeVersionId;
import com.enonic.xp.repository.RepositoryId;

public class BranchEntryFactory
    implements DumpEntryFactory
{
    @Override
    public IndexRequest create( final RepositoryId repositoryId, final AbstractDumpJson json )
    {
        Preconditions.checkArgument( json instanceof BranchDumpJson );

        final BranchDumpJson branchDumpJson = (BranchDumpJson) json;

        return new IndexRequest( IndexNameResolver.resolveStorageIndexName( repositoryId ) ).
            type( IndexType.BRANCH.getName() ).
            routing( branchDumpJson.getRouting() ).
            id( branchDumpJson.getId() ).
            parent( new NodeVersionDocumentId( NodeId.from( branchDumpJson.getNodeId() ),
                                               NodeVersionId.from( branchDumpJson.getVersionId() ) ).toString() ).
            source( BranchDumpXContentBuilderFactory.create( branchDumpJson ) );
    }
}
