package com.enonic.xp.admin.impl.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract class JsonResult
    implements JsonSerializable
{
    protected final ObjectMapper objectMapper = createObjectMapper();

    private final boolean success;

    private final String error;

    public JsonResult()
    {
        this( true );
    }

    public JsonResult( final boolean success )
    {
        this.success = success;
        this.error = null;
    }

    protected JsonResult( final String error )
    {
        this.success = false;
        this.error = error;
    }

    protected JsonResult( final JsonResultBuilder builder )
    {
        this.success = builder.success;
        this.error = builder.error;
    }

    private ObjectMapper createObjectMapper()
    {
        return ObjectMapperHelper.create();
    }

    public boolean hasError()
    {
        return error != null;
    }

    protected abstract void serialize( ObjectNode json );


    @Override
    public final JsonNode toJson()
    {

        final ObjectNode json = objectMapper.createObjectNode();
        json.put( "success", this.success );

        if ( this.error != null )
        {
            json.put( "error", this.error );
        }

        serialize( json );
        return json;
    }

    protected final ObjectNode objectNode()
    {
        return objectMapper.createObjectNode();
    }

    protected final ArrayNode arrayNode()
    {
        return objectMapper.createArrayNode();
    }

    public static class JsonResultBuilder
    {
        private boolean success = true;

        private String error;

        public JsonResultBuilder success()
        {
            this.success = true;
            this.error = null;
            return this;
        }

        public JsonResultBuilder error( final String error )
        {
            this.error = error;
            this.success = false;
            return this;
        }
    }
}