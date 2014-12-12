package com.enonic.wem.api.query.aggregation;

import java.util.Collection;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

public abstract class AbstractRangeAggregationQuery<R extends Range>
    extends AggregationQuery
{
    private final String fieldName;

    private final ImmutableSet<R> ranges;

    AbstractRangeAggregationQuery( final Builder builder, final Collection<R> ranges )
    {
        super( builder.name );
        this.ranges = ImmutableSet.copyOf( ranges );
        this.fieldName = builder.fieldName;
    }

    public ImmutableSet<R> getRanges()
    {
        return ranges;
    }

    public String getFieldName()
    {
        return fieldName;
    }

    public static DateRangeAggregationQuery.Builder dateRangeQuery( final String name )
    {
        return new DateRangeAggregationQuery.Builder( name );
    }

    public static NumericRangeAggregationQuery.Builder numericRangeQuery( final String name )
    {
        return new NumericRangeAggregationQuery.Builder( name );
    }

    public abstract static class Builder<T extends Builder, R extends Range>
        extends AggregationQuery.Builder
    {
        private String fieldName;

        public Collection<R> ranges = Sets.newHashSet();

        public Builder( final String name )
        {
            super( name );
        }

        public T range( final R range )
        {
            ranges.add( range );
            return (T) this;
        }

        public T setRanges( final Collection<R> ranges )
        {
            this.ranges = ranges;
            return (T) this;
        }

        public T fieldName( final String fieldName )
        {
            this.fieldName = fieldName;
            return (T) this;
        }
    }
}
