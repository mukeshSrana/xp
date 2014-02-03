package com.enonic.wem.admin.rest.resource.content.json;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import com.enonic.wem.api.query.aggregation.AggregationQuery;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({@JsonSubTypes.Type(value = TermsAggregationQueryJson.class, name = "TermsAggregationQuery")})
public abstract class AggregationQueryJson
{

    public abstract AggregationQuery getAggregationQuery();


}
