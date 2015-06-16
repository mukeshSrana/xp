package com.enonic.wem.repo.internal.systemexport;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.enonic.wem.repo.internal.elasticsearch.branch.BranchIndexPath;
import com.enonic.wem.repo.internal.index.result.SearchResultEntry;

public class BranchDumpJson
    extends AbstractDumpJson
{
    @JsonProperty("_id")
    public String id;

    @JsonProperty("_timestamp")
    public String timestamp;

    @JsonProperty("versionId")
    public String versionId;

    @JsonProperty("branch")
    public String branch;

    @JsonProperty("nodeId")
    public String nodeId;

    @JsonProperty("state")
    public String state;

    @JsonProperty("path")
    public String path;

    @JsonProperty("routing")
    public String routing;


    public String getTimestamp()
    {
        return timestamp;
    }

    public String getVersionId()
    {
        return versionId;
    }

    public String getBranch()
    {
        return branch;
    }

    public String getNodeId()
    {
        return nodeId;
    }

    public String getState()
    {
        return state;
    }

    public String getPath()
    {
        return path;
    }

    public String getRouting()
    {
        return routing;
    }

    public String getId()
    {
        return id;
    }

    public static BranchDumpJson toJson( final SearchResultEntry searchResultEntry )
    {
        final BranchDumpJson branchDumpJson = new BranchDumpJson();

        branchDumpJson.id = searchResultEntry.getId();
        branchDumpJson.branch = getStringValue( searchResultEntry, BranchIndexPath.BRANCH_NAME.getPath() );
        branchDumpJson.versionId = getStringValue( searchResultEntry, BranchIndexPath.VERSION_ID.getPath() );
        branchDumpJson.nodeId = getStringValue( searchResultEntry, BranchIndexPath.NODE_ID.getPath() );
        branchDumpJson.state = getStringValue( searchResultEntry, BranchIndexPath.STATE.getPath() );
        branchDumpJson.path = getStringValue( searchResultEntry, BranchIndexPath.PATH.getPath() );
        branchDumpJson.routing = getStringValue( searchResultEntry, BranchIndexPath.ROUTING.getPath() );
        branchDumpJson.timestamp = getStringValue( searchResultEntry, BranchIndexPath.TIMESTAMP.getPath() );

        return branchDumpJson;
    }
}
