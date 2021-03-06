package com.enonic.xp.node;

import java.time.Instant;

import com.google.common.annotations.Beta;

@Beta
public class NodeVersion
    implements Comparable<NodeVersion>
{
    private final NodeVersionId nodeVersionId;

    private final Instant timestamp;

    public NodeVersion( final NodeVersionId nodeVersionId, final Instant timestamp )
    {
        this.nodeVersionId = nodeVersionId;
        this.timestamp = timestamp;
    }

    public NodeVersionId getId()
    {
        return nodeVersionId;
    }

    public Instant getTimestamp()
    {
        return timestamp;
    }

    // Insert with newest first
    @Override
    public int compareTo( final NodeVersion o )
    {
        if ( this.timestamp == o.timestamp )
        {
            return 0;
        }

        if ( this.timestamp.isBefore( o.timestamp ) )
        {
            return 1;
        }

        return -1;
    }

    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }

        final NodeVersion that = (NodeVersion) o;

        if ( nodeVersionId != null ? !nodeVersionId.equals( that.nodeVersionId ) : that.nodeVersionId != null )
        {
            return false;
        }
        if ( timestamp != null ? !timestamp.equals( that.timestamp ) : that.timestamp != null )
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = nodeVersionId != null ? nodeVersionId.hashCode() : 0;
        result = 31 * result + ( timestamp != null ? timestamp.hashCode() : 0 );
        return result;
    }
}
