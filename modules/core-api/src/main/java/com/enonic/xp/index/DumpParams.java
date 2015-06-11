package com.enonic.xp.index;

import java.nio.file.Path;
import java.util.List;

import com.google.common.collect.Lists;

import com.enonic.xp.repository.RepositoryId;

public class DumpParams
{
    private final List<RepositoryId> repositoryIds;

    private final int batchSize;

    private final int timeout;

    private final Path dumpPath;

    private DumpParams( Builder builder )
    {
        repositoryIds = builder.repositoryIds;
        batchSize = builder.batchSize;
        timeout = builder.timeout;
        dumpPath = builder.dumpPath;
    }

    public static Builder create()
    {
        return new Builder();
    }

    public List<RepositoryId> getRepositoryIds()
    {
        return repositoryIds;
    }

    public int getBatchSize()
    {
        return batchSize;
    }

    public int getTimeout()
    {
        return timeout;
    }

    public Path getDumpPath()
    {
        return dumpPath;
    }

    public static final class Builder
    {
        private List<RepositoryId> repositoryIds = Lists.newArrayList();

        private int batchSize = 1000;

        private int timeout = 2;

        private Path dumpPath;

        private Builder()
        {
        }

        public Builder setRepositories( List<RepositoryId> repositoryIds )
        {
            this.repositoryIds = repositoryIds;
            return this;
        }

        public Builder addRepository( final RepositoryId repositoryId )
        {
            this.repositoryIds.add( repositoryId );
            return this;
        }

        public Builder batchSize( int batchSize )
        {
            this.batchSize = batchSize;
            return this;
        }

        public Builder timeout( int timeout )
        {
            this.timeout = timeout;
            return this;
        }

        public Builder dumpPath( Path dumpPath )
        {
            this.dumpPath = dumpPath;
            return this;
        }

        public DumpParams build()
        {
            return new DumpParams( this );
        }
    }
}
