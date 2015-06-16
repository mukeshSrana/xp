package com.enonic.wem.repo.internal.systemexport;

import java.io.BufferedWriter;

import org.apache.commons.io.IOUtils;

import com.enonic.wem.repo.internal.index.result.SearchResultEntries;
import com.enonic.wem.repo.internal.index.result.SearchResultEntry;

public final class DumpWriter
{
    private final BufferedWriter writer;

    private final DumpSerializer dumpSerializer;

    public DumpWriter( final DumpSerializer dumpSerializer, final BufferedWriter writer )
    {
        this.dumpSerializer = dumpSerializer;
        this.writer = writer;
    }

    public void write( final SearchResultEntries entries )
    {
        for ( final SearchResultEntry entry : entries )
        {
            writeEntry( entry );
        }
    }

    private void writeEntry( final SearchResultEntry entry )
    {
        try
        {
            final String source = this.dumpSerializer.toString( entry );
            writer.write( source );
            writer.newLine();
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
