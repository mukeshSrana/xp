package com.enonic.wem.repo.internal.systemexport;

import com.enonic.wem.repo.internal.index.IndexServiceInternal;

public abstract class AbstractSystemDataCommand
{
    final static String DUMP_FILE_NAME = "dump.json";

    protected final BranchDumpSerializer branchSerializer = BranchDumpSerializer.create( false );

    protected final VersionDumpSerializer versionSerializer = VersionDumpSerializer.create( false );

    protected final IndexServiceInternal internalIndexService;

    public AbstractSystemDataCommand( final Builder builder )
    {
        this.internalIndexService = builder.indexService;
    }

    protected static class Builder<B extends Builder>
    {
        protected IndexServiceInternal indexService;

        @SuppressWarnings("unchecked")
        public B indexService( final IndexServiceInternal indexService )
        {
            this.indexService = indexService;
            return (B) this;
        }

    }


}
