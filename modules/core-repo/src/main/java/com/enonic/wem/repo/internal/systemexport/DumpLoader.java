package com.enonic.wem.repo.internal.systemexport;

import java.io.BufferedReader;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.index.IndexRequest;

import com.enonic.wem.repo.internal.elasticsearch.ElasticsearchDao;
import com.enonic.xp.repository.RepositoryId;
import com.enonic.xp.vfs.VirtualFile;

public class DumpLoader
{
    private DumpEntryFactory entryFactory;

    private DumpSerializer dumpSerializer;

    private VirtualFile dumpFile;

    private RepositoryId repositoryId;

    private final int batchSize = 100;

    private ElasticsearchDao elasticsearchDao;

    private DumpLoader( Builder builder )
    {
        entryFactory = builder.entryFactory;
        dumpSerializer = builder.dumpSerializer;
        dumpFile = builder.dumpFile;
        repositoryId = builder.repositoryId;
        elasticsearchDao = builder.elasticsearchDao;
    }

    public void load()
    {
        try
        {
            final BufferedReader reader = dumpFile.getCharSource().openBufferedStream();

            String source;

            while ( ( source = reader.readLine() ) != null )
            {
                storeEntry( source );
            }

            IOUtils.closeQuietly( reader );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    private void storeEntry( final String source )
    {
        final AbstractDumpJson json = dumpSerializer.toJson( source );

        final IndexRequest indexRequest = entryFactory.create( this.repositoryId, json );

        final String id = elasticsearchDao.store( indexRequest );
        System.out.println( "Stored id: " + id );
    }

    public static Builder create()
    {
        return new Builder();
    }

    public static final class Builder
    {
        private DumpEntryFactory entryFactory;

        private DumpSerializer dumpSerializer;

        private VirtualFile dumpFile;

        private RepositoryId repositoryId;

        private ElasticsearchDao elasticsearchDao;

        private Builder()
        {
        }

        public Builder entryFactory( DumpEntryFactory entryFactory )
        {
            this.entryFactory = entryFactory;
            return this;
        }

        public Builder dumpSerializer( DumpSerializer dumpSerializer )
        {
            this.dumpSerializer = dumpSerializer;
            return this;
        }

        public Builder dumpFile( VirtualFile dumpFile )
        {
            this.dumpFile = dumpFile;
            return this;
        }

        public Builder repositoryId( RepositoryId repositoryId )
        {
            this.repositoryId = repositoryId;
            return this;
        }

        public Builder elasticsearchDao( ElasticsearchDao elasticsearchDao )
        {
            this.elasticsearchDao = elasticsearchDao;
            return this;
        }

        public DumpLoader build()
        {
            return new DumpLoader( this );
        }
    }
}
