package com.enonic.wem.repo.internal.index.result;

public class ScrollResult
{
    private String scrollId;

    private SearchResultEntries hits;

    private ScrollResult( Builder builder )
    {
        scrollId = builder.scrollId;
        hits = builder.hits;
    }

    public static Builder create()
    {
        return new Builder();
    }

    public String getScrollId()
    {
        return scrollId;
    }

    public SearchResultEntries getHits()
    {
        return hits;
    }

    public static final class Builder
    {
        private String scrollId;

        private SearchResultEntries hits;

        private Builder()
        {
        }

        public Builder scrollId( String scrollId )
        {
            this.scrollId = scrollId;
            return this;
        }

        public Builder hits( SearchResultEntries hits )
        {
            this.hits = hits;
            return this;
        }

        public ScrollResult build()
        {
            return new ScrollResult( this );
        }
    }
}
