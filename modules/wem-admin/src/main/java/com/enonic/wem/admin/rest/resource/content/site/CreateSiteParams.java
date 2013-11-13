package com.enonic.wem.admin.rest.resource.content.site;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.enonic.wem.api.command.content.site.CreateSite;
import com.enonic.wem.api.content.ContentId;
import com.enonic.wem.api.content.site.SiteTemplateKey;

public class CreateSiteParams
{
    private final CreateSite createSite;

    @SuppressWarnings("UnusedDeclaration")
    @JsonCreator
    private CreateSiteParams( @JsonProperty("content") String content, @JsonProperty("siteTemplate") String siteTemplate )
    {
        this.createSite = new CreateSite().
            content( ContentId.from( content ) ).
            template( SiteTemplateKey.from( siteTemplate ) );
    }

    @JsonIgnore
    public CreateSite getCreateSite()
    {
        return this.createSite;
    }

}
