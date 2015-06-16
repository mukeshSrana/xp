package com.enonic.xp.admin.impl.rest.resource.repo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class LoadDataRequestJson
{
    private String dumpRoot;

    private Integer batchSize;

    private Integer timeout;

    @JsonCreator
    public LoadDataRequestJson( @JsonProperty("dumpRoot") final String dumpRoot, //
                                @JsonProperty("timeout") final Integer timeout, //
                                @JsonProperty("batchSize") final Integer batchSize )
    {
        Preconditions.checkArgument( !Strings.isNullOrEmpty( dumpRoot ), "Dump folder has to be given" );

        this.dumpRoot = dumpRoot;
        this.batchSize = batchSize;
        this.timeout = timeout;
    }

    public String getDumpRoot()
    {
        return dumpRoot;
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
