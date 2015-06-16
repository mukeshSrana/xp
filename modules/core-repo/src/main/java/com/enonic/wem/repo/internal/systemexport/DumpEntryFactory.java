package com.enonic.wem.repo.internal.systemexport;

import org.elasticsearch.action.index.IndexRequest;

import com.enonic.xp.repository.RepositoryId;

public interface DumpEntryFactory
{
    public IndexRequest create( final RepositoryId repositoryId, final AbstractDumpJson json );

}
