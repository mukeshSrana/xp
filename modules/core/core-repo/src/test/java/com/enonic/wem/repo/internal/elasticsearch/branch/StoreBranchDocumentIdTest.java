package com.enonic.wem.repo.internal.elasticsearch.branch;

import org.junit.Test;

import com.enonic.wem.repo.internal.branch.BranchDocumentId;

import static org.junit.Assert.*;

public class StoreBranchDocumentIdTest
{
    @Test
    public void from()
        throws Exception
    {
        final BranchDocumentId id = BranchDocumentId.from( "myBlobKey_myBranch" );
        assertEquals( "myBlobKey_myBranch", id.getValue() );
        assertEquals( "myBlobKey", id.getNodeId().toString() );
        assertEquals( "myBranch", id.getBranch().getName() );
    }

    @Test(expected = IllegalArgumentException.class)
    public void from_missing_ws()
        throws Exception
    {
        BranchDocumentId.from( "myBlobKey_" );
    }

    @Test(expected = IllegalArgumentException.class)
    public void from_missing_nodeId()
        throws Exception
    {
        BranchDocumentId.from( "_myBranch" );
    }


    @Test(expected = IllegalArgumentException.class)
    public void from_missing_separator()
        throws Exception
    {
        BranchDocumentId.from( "myBlobKeymyBranch" );
    }
}
