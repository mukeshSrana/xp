package com.enonic.xp.admin.impl.rest.resource.repo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.enonic.xp.repository.RepositoryId;

public class SnapshotRequestJson
{
    private RepositoryId repositoryId;

    private boolean skipIndexedData;

    @JsonCreator
    public SnapshotRequestJson( @JsonProperty("repositoryId") final String repositoryId,
                                @JsonProperty("skipIndexedData") final boolean skipIndexedData )
    {
        this.repositoryId = RepositoryId.from( repositoryId );
        this.skipIndexedData = skipIndexedData;
    }

    public RepositoryId getRepositoryId()
    {
        return repositoryId;
    }

    public boolean isSkipIndexedData()
    {
        return skipIndexedData;
    }
}
