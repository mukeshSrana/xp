package com.enonic.wem.repo.internal.systemexport;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.enonic.wem.repo.internal.index.result.SearchResultEntry;

public class VersionDumpJson
    extends AbstractDumpJson
{
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

        versionDumpJson.versionId = searchResultEntry.getId();
        versionDumpJson.nodeId = searchResultEntry.getId();
        versionDumpJson.timestamp = getStringValue( searchResultEntry, "timestamp" );

        return versionDumpJson;
    }

}
