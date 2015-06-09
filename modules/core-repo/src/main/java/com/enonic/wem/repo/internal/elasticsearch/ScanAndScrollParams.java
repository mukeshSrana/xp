package com.enonic.wem.repo.internal.elasticsearch;

import com.google.common.base.Preconditions;

public class ScanAndScrollParams
{
    private final String index;

    private final String type;

    private final int size;

    private final int keepAliveSeconds;

    private ScanAndScrollParams( Builder builder )
    {
        this.index = builder.index;
        this.type = builder.type;
        this.size = builder.size;
        this.keepAliveSeconds = builder.keepAliveSeconds;
    }

    public String getIndex()
    {
        return index;
    }

    public String getType()
    {
        return type;
    }

    public int getSize()
    {
        return size;
    }

    public int getKeepAliveSeconds()
    {
        return keepAliveSeconds;
    }

    public static Builder create()
    {
        return new Builder();
    }


    public static final class Builder
    {
        private String index;

        private String type;

        private int size = 1000;

        private int keepAliveSeconds = 60;

        private Builder()
        {
        }

        public Builder index( String index )
        {
            this.index = index;
            return this;
        }

        public Builder type( String type )
        {
            this.type = type;
            return this;
        }

        public Builder size( int size )
        {
            this.size = size;
            return this;
        }

        public Builder keepAliveSecond( int keepAliveSecond )
        {
            this.keepAliveSeconds = keepAliveSecond;
            return this;
        }

        private void validate()
        {
            Preconditions.checkNotNull( type, "Type must be set for scan and scroll" );
            Preconditions.checkNotNull( index, "Index must be set for scan and scroll" );
        }

        public ScanAndScrollParams build()
        {
            this.validate();
            return new ScanAndScrollParams( this );
        }
    }
}
