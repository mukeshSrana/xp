package com.enonic.xp.core.impl.content;

import com.google.common.base.Preconditions;

import com.enonic.xp.branch.Branch;
import com.enonic.xp.content.ContentId;
import com.enonic.xp.content.ContentIds;
import com.enonic.xp.content.ResolvePublishDependenciesResult;
import com.enonic.xp.node.NodeId;
import com.enonic.xp.node.ResolveSyncWorkResult;
import com.enonic.xp.node.ResolveSyncWorkResults;
import com.enonic.xp.node.SyncWorkResolverParams;

public class ResolvePublishDependenciesCommand
    extends AbstractContentCommand
{
    private final ContentIds contentIds;

    private final Branch target;

    private final ResolvePublishDependenciesResult.Builder resultBuilder;

    private final boolean includeChildren;

    private ResolvePublishDependenciesCommand( final Builder builder )
    {
        super( builder );
        this.contentIds = builder.contentIds;
        this.target = builder.target;
        this.resultBuilder = ResolvePublishDependenciesResult.create();
        this.includeChildren = builder.includeChildren;
    }

    ResolvePublishDependenciesResult execute()
    {
        resolveDependencies();
        return resultBuilder.build();
    }

    private void resolveDependencies()
    {
        final ResolveSyncWorkResults syncWorkResults = resolveSyncWorkResults( this.includeChildren );

        populateResults( syncWorkResults );
    }

    private ResolveSyncWorkResults resolveSyncWorkResults( boolean includeChildren )
    {
        final ResolveSyncWorkResults.Builder resultsBuilder = ResolveSyncWorkResults.create();

        for ( final ContentId contentId : this.contentIds )
        {
            final ResolveSyncWorkResult syncWorkResult = getWorkResult( contentId, includeChildren );

            resultsBuilder.add( syncWorkResult );
        }
        return resultsBuilder.build();
    }

    private ResolveSyncWorkResult getWorkResult( final ContentId contentId, boolean includeChildren )
    {
        return nodeService.resolveSyncWork( SyncWorkResolverParams.create().
            includeChildren( includeChildren ).
            nodeId( NodeId.from( contentId.toString() ) ).
            branch( this.target ).
            build() );
    }

    private void populateResults( final ResolveSyncWorkResults syncWorkResults )
    {
        this.resultBuilder.pushContentRequests( PushContentRequestsFactory.create().
            syncWorkResults( syncWorkResults ).
            forceInitialReasonInclusion( true ).
            build().
            createRequests() );
    }

    public static Builder create()
    {
        return new Builder();
    }

    public static class Builder
        extends AbstractContentCommand.Builder<Builder>
    {
        private ContentIds contentIds;

        private Branch target;

        private boolean includeChildren = true;

        public Builder contentIds( final ContentIds contentIds )
        {
            this.contentIds = contentIds;
            return this;
        }

        public Builder target( final Branch target )
        {
            this.target = target;
            return this;
        }

        public Builder includeChildren( final boolean includeChildren )
        {
            this.includeChildren = includeChildren;
            return this;
        }

        @Override
        void validate()
        {
            super.validate();
            Preconditions.checkNotNull( target );
            Preconditions.checkNotNull( contentIds );
        }

        public ResolvePublishDependenciesCommand build()
        {
            validate();
            return new ResolvePublishDependenciesCommand( this );
        }

    }
}
