package com.enonic.wem.repo.internal.systemexport;

import org.elasticsearch.action.index.IndexRequest;

import com.google.common.base.Preconditions;

import com.enonic.wem.repo.internal.elasticsearch.xcontent.BranchDumpXContentBuilderFactory;
import com.enonic.wem.repo.internal.repository.IndexNameResolver;
import com.enonic.xp.index.IndexType;
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
            type( IndexType.VERSION.getName() ).
            source( BranchDumpXContentBuilderFactory.create( branchDumpJson ) );
    }
}
