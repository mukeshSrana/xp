package com.enonic.xp.admin.impl.rest.resource.repo;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import com.enonic.xp.repository.RepositoryId;

public class DumpRequestJson
{
    private List<RepositoryId> repositoryIds = Lists.newArrayList();

    private Integer batchSize;

    private Integer timeout;

    @JsonCreator
    public DumpRequestJson( @JsonProperty("repositories") final String repositories, //
                            @JsonProperty("timeout") final Integer timeout, //
                            @JsonProperty("batchSize") final Integer batchSize )
    {
        Preconditions.checkArgument( !Strings.isNullOrEmpty( repositories ), "Repository name has to be given" );

        final List<String> repositoryArray = Arrays.asList( repositories.split( "," ) );

        repositoryArray.forEach( element -> repositoryIds.add( RepositoryId.from( element ) ) );

        this.batchSize = batchSize;
        this.timeout = timeout;
    }

    public List<RepositoryId> getRepositoryIds()
    {
        return repositoryIds;
    }

    public Integer getBatchSize()
    {
        return batchSize;
    }

    public Integer getTimeout()
    {
        return timeout;
    }
}
