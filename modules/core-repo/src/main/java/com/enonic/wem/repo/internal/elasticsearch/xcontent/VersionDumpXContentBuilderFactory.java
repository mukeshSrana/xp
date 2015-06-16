package com.enonic.wem.repo.internal.elasticsearch.xcontent;

import java.time.Instant;

import org.elasticsearch.common.xcontent.XContentBuilder;

import com.enonic.wem.repo.internal.index.IndexException;
import com.enonic.wem.repo.internal.systemexport.VersionDumpJson;
import com.enonic.wem.repo.internal.version.VersionIndexPath;

public class VersionDumpXContentBuilderFactory
    extends AbstractXContentBuilderFactor
{

    public static XContentBuilder create( final VersionDumpJson versionDumpJson )
    {
        try
        {
            final XContentBuilder builder = startBuilder();

            addField( builder, VersionIndexPath.VERSION_ID.getPath(), versionDumpJson.getVersionId() );
            addField( builder, VersionIndexPath.NODE_ID.getPath(), versionDumpJson.getNodeId() );
            addField( builder, VersionIndexPath.TIMESTAMP.getPath(), Instant.parse( versionDumpJson.getTimestamp() ) );
            addField( builder, VersionIndexPath.NODE_PATH.getPath(), versionDumpJson.getNodePath() );

            endBuilder( builder );
            return builder;
        }
        catch ( Exception e )
        {
            throw new IndexException( "Failed to build xContent for VersionDump", e );
        }

    }

}
