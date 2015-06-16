package com.enonic.wem.repo.internal.systemexport;

import org.elasticsearch.action.index.IndexRequest;

import com.google.common.base.Preconditions;

import com.enonic.wem.repo.internal.elasticsearch.xcontent.AbstractXContentBuilderFactor;
import com.enonic.wem.repo.internal.elasticsearch.xcontent.VersionDumpXContentBuilderFactory;
import com.enonic.wem.repo.internal.repository.IndexNameResolver;
import com.enonic.xp.index.IndexType;
import com.enonic.xp.repository.RepositoryId;

public class VersionEntryFactory
    extends AbstractXContentBuilderFactor
    implements DumpEntryFactory

{
    public IndexRequest create( final RepositoryId repositoryId, final AbstractDumpJson json )
    {
        Preconditions.checkArgument( json instanceof VersionDumpJson );

        final VersionDumpJson versionDumpJson = (VersionDumpJson) json;

        return new IndexRequest( IndexNameResolver.resolveStorageIndexName( repositoryId ) ).
            type( IndexType.VERSION.getName() ).
            id( versionDumpJson.id ).
            source( VersionDumpXContentBuilderFactory.create( versionDumpJson ) );
    }
}
