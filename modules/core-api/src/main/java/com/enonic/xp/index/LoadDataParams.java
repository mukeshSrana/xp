package com.enonic.xp.index;

import java.nio.file.Path;

public class LoadDataParams
{

    private final Path dumpRoot;

    private final int batchSize;

    private LoadDataParams( Builder builder )
    {
        dumpRoot = builder.dumpRoot;
        batchSize = builder.batchSize;
    }

    public static Builder create()
    {
        return new Builder();
    }

    public Path getDumpRoot()
    {
        return dumpRoot;
    }

    public int getBatchSize()
    {
        return batchSize;
    }

    public static final class Builder
    {
        private Path dumpRoot;

        private int batchSize;

        private Builder()
        {
        }

        public Builder dumpRoot( Path dumpRoot )
        {
            this.dumpRoot = dumpRoot;
            return this;
        }

        public Builder batchSize( int batchSize )
        {
            this.batchSize = batchSize;
            return this;
        }

        public LoadDataParams build()
        {
            return new LoadDataParams( this );
        }
    }
}
