package com.enonic.xp.index;

import com.enonic.xp.vfs.VirtualFile;

public class LoadDataParams
{

    private final VirtualFile dumpRoot;

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

    public VirtualFile getDumpRoot()
    {
        return dumpRoot;
    }

    public int getBatchSize()
    {
        return batchSize;
    }

    public static final class Builder
    {
        private VirtualFile dumpRoot;

        private int batchSize;

        private Builder()
        {
        }

        public Builder dumpRoot( final VirtualFile dumpRoot )
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
