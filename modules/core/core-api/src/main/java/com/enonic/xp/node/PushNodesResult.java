package com.enonic.xp.node;

import java.util.Set;

import com.google.common.annotations.Beta;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

@Beta
public class PushNodesResult
{
    private final Nodes successfull;

    private final Nodes childrenSuccessfull;

    private final ImmutableSet<Failed> failed;

    private PushNodesResult( Builder builder )
    {
        successfull = Nodes.from( builder.successfull );
        childrenSuccessfull = Nodes.from( builder.childrenSuccessfull );
        failed = ImmutableSet.copyOf( builder.failed );
    }

    public Nodes getSuccessfull()
    {
        return successfull;
    }

    public Nodes getChildrenSuccessfull()
    {
        return childrenSuccessfull;
    }

    public ImmutableSet<Failed> getFailed()
    {
        return failed;
    }

    public static Builder create()
    {
        return new Builder();
    }

    public static final class Builder
    {
        private final Set<Node> successfull = Sets.newHashSet();

        private final Set<Failed> failed = Sets.newHashSet();

        private final Set<Node> childrenSuccessfull = Sets.newHashSet();

        private Builder()
        {
        }

        public Builder addSuccess( final Node success )
        {
            this.successfull.add( success );
            return this;
        }

        public Builder addChildSuccess( final Node success )
        {
            this.childrenSuccessfull.add( success );
            return this;
        }

        public Builder addFailed( final Node failed, final Reason reason )
        {
            this.failed.add( new Failed( failed, reason ) );
            return this;
        }

        public PushNodesResult build()
        {
            return new PushNodesResult( this );
        }
    }

    public enum Reason
    {
        PARENT_NOT_FOUND,
        ACCESS_DENIED
    }

    public static final class Failed
    {
        private final Node node;

        private final Reason reason;

        public Failed( final Node node, final Reason reason )
        {
            this.node = node;
            this.reason = reason;
        }

        public Node getNode()
        {
            return node;
        }

        public Reason getReason()
        {
            return reason;
        }
    }
}
