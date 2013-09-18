package com.enonic.wem.admin.jsonrpc;

import com.fasterxml.jackson.databind.JsonNode;

import com.enonic.wem.admin.json.JsonSerializable;

public interface JsonRpcContext
    extends Iterable<JsonRpcParam>
{
    public Iterable<JsonRpcParam> params();

    public JsonNode getResult();

    public boolean hasParam( final String name );

    public JsonRpcParam param( final String name );

    public void setResult( final String value );

    public void setResult( final JsonNode value );

    public void setResult( final JsonSerializable value );
}
