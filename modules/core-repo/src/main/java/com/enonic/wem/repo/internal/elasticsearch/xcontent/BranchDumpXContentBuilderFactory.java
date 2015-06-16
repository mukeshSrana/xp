package com.enonic.wem.repo.internal.elasticsearch.xcontent;

import java.time.Instant;

import org.elasticsearch.common.xcontent.XContentBuilder;

import com.enonic.wem.repo.internal.elasticsearch.branch.BranchIndexPath;
import com.enonic.wem.repo.internal.index.IndexException;
import com.enonic.wem.repo.internal.systemexport.BranchDumpJson;

public class BranchDumpXContentBuilderFactory
    extends AbstractXContentBuilderFactor
{

    public static XContentBuilder create( final BranchDumpJson branchDumpJson )
    {
        try
        {
            final XContentBuilder builder = startBuilder();

            addField( builder, BranchIndexPath.VERSION_ID.getPath(), branchDumpJson.getVersionId() );
            addField( builder, BranchIndexPath.BRANCH_NAME.getPath(), branchDumpJson.getBranch() );
            addField( builder, BranchIndexPath.TIMESTAMP.getPath(), Instant.parse( branchDumpJson.getTimestamp() ) );
            addField( builder, BranchIndexPath.NODE_ID.getPath(), branchDumpJson.getNodeId() );
            addField( builder, BranchIndexPath.STATE.getPath(), branchDumpJson.getState() );
            addField( builder, BranchIndexPath.PATH.getPath(), branchDumpJson.getPath() );

            endBuilder( builder );
            return builder;
        }
        catch ( Exception e )
        {
            throw new IndexException( "Failed to build xContent for VersionDump", e );
        }

    }
}