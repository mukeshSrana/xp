package com.enonic.wem.repo.internal.systemexport;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.enonic.wem.repo.internal.index.result.SearchResultEntry;
import com.enonic.wem.repo.internal.version.VersionIndexPath;

public class VersionDumpJson
    extends AbstractDumpJson
{
    @JsonProperty("_id")
    public String id;

    @JsonProperty("versionId")
    public String versionId;

    @JsonProperty("nodeId")
    public String nodeId;

    @JsonProperty("timestamp")
    public String timestamp;

    @JsonProperty("nodePath")
    public String nodePath;

    public String getVersionId()
    {
        return versionId;
    }

    public String getNodeId()
    {
        return nodeId;
    }

    public String getTimestamp()
    {
        return timestamp;
    }

    public String getNodePath()
    {
        return nodePath;
    }

    public static VersionDumpJson toJson( final SearchResultEntry searchResultEntry )
    {
        final VersionDumpJson versionDumpJson = new VersionDumpJson();

        versionDumpJson.id = searchResultEntry.getId();
        versionDumpJson.versionId = getStringValue( searchResultEntry, VersionIndexPath.VERSION_ID.getPath() );
        versionDumpJson.nodeId = getStringValue( searchResultEntry, VersionIndexPath.NODE_ID.getPath() );
        versionDumpJson.timestamp = getStringValue( searchResultEntry, VersionIndexPath.TIMESTAMP.getPath() );
        versionDumpJson.nodePath = getStringValue( searchResultEntry, VersionIndexPath.NODE_PATH.getPath() );

        return versionDumpJson;
    }

}
