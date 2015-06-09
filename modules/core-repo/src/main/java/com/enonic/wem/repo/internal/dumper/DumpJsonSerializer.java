package com.enonic.wem.repo.internal.dumper;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;

import com.enonic.wem.repo.internal.index.result.SearchResultEntry;
import com.enonic.xp.util.Exceptions;

public class DumpJsonSerializer
{
    protected final ObjectMapper mapper;

    private DumpJsonSerializer( final ObjectMapper mapper )
    {
        this.mapper = mapper;
    }

    public String toString( final SearchResultEntry searchResultEntry )
    {
        try
        {
            return this.mapper.writeValueAsString( searchResultEntry );

        }
        catch ( final JsonProcessingException e )
        {
            throw Exceptions.unchecked( e );
        }
    }


    public static DumpJsonSerializer create( final boolean indent )
    {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat( new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ) );
        mapper.disable( SerializationFeature.WRITE_DATES_AS_TIMESTAMPS );
        mapper.disable( SerializationFeature.FAIL_ON_EMPTY_BEANS );
        mapper.enable( MapperFeature.SORT_PROPERTIES_ALPHABETICALLY );
        mapper.enable( SerializationFeature.WRITE_NULL_MAP_VALUES );
        mapper.setSerializationInclusion( JsonInclude.Include.ALWAYS );
        mapper.registerModule( new JSR310Module() );

        if ( indent )
        {
            mapper.enable( SerializationFeature.INDENT_OUTPUT );
        }

        return new DumpJsonSerializer( mapper );
    }
}
