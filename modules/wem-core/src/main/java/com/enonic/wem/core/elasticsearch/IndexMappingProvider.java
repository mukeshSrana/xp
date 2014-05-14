package com.enonic.wem.core.elasticsearch;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;

import com.enonic.wem.core.index.Index;
import com.enonic.wem.core.index.IndexException;


public class IndexMappingProvider
{
    private final static String PREFIX = "/META-INF/index/mapping/";

    private final static String[] MAPPING_FILES = { //
        PREFIX + "nodb-node-mapping.json", //
        PREFIX + "store-entity-mapping.json" //
    };

    List<IndexMapping> getMappingsForIndex( final Index index )
    {
        final List<IndexMapping> indexMappings = Lists.newArrayList();
        for ( final String mappingFile : MAPPING_FILES )
        {
            try
            {
                final IndexMapping indexMapping = createIndexMapping( index, mappingFile );
                if ( indexMapping != null )
                {
                    indexMappings.add( indexMapping );
                }
            }
            catch ( IOException e )
            {
                throw new IndexException( "Failed to load mapping file: " + mappingFile, e );
            }
        }
        return indexMappings;
    }

    private IndexMapping createIndexMapping( final Index index, final String mappingFile )
        throws IOException
    {
        final String filename = mappingFile.substring( PREFIX.length() );

        final String[] parts = filename.split( "-" );
        if ( parts.length < 3 )
        {
            return null;
        }

        final String resourceIndexName = parts[0];
        if ( !index.getName().equals( resourceIndexName ) )
        {
            return null;
        }

        final String indexType = parts[1];
        final URL url = Resources.getResource( getClass(), mappingFile );
        final String mapping = Resources.toString( url, Charsets.UTF_8 );

        return new IndexMapping( index, indexType, mapping );
    }
}