package com.enonic.wem.repo.internal.systemexport;

import java.io.Writer;

import org.apache.commons.io.IOUtils;

import com.enonic.wem.repo.internal.index.result.SearchResultEntry;

public final class DumpWriter
{
    private final Writer writer;

    private final DumpSerializer dumpSerializer;

    public DumpWriter( final DumpSerializer dumpSerializer, final Writer writer )
    {
        this.dumpSerializer = dumpSerializer;
        this.writer = writer;
    }

    public void write(final SearchResultEntry entry)
    {
        try
        {
            writer.write( this.dumpSerializer.toString( entry ) );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( "not able to serialize entry: " + entry, e );
        }
    }

    public void close()
    {
        IOUtils.closeQuietly( this.writer );

    }
}
