package com.enonic.wem.admin.rest.resource.content.site;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.enonic.wem.admin.json.content.site.ModuleConfigJson;
import com.enonic.wem.api.command.Commands;
import com.enonic.wem.api.command.content.site.UpdateSite;
import com.enonic.wem.api.content.ContentId;
import com.enonic.wem.api.content.site.Site;
import com.enonic.wem.api.content.site.SiteEditor;
import com.enonic.wem.api.content.site.SiteTemplateKey;

public class UpdateSiteJson
{
    private final UpdateSite updateSite;

    @SuppressWarnings("UnusedDeclaration")
    @JsonCreator
    UpdateSiteJson( @JsonProperty("contentId") String content, @JsonProperty("siteTemplateKey") final String siteTemplate,
                    @JsonProperty("moduleConfigs") final List<ModuleConfigJson> moduleConfigs )
    {
        this.updateSite = Commands.site().update().
            content( ContentId.from( content ) ).
            editor( new SiteEditor()
            {
                @Override
                public Site.SiteEditBuilder edit( final Site toBeEdited )
                {
                    return Site.editSite( toBeEdited ).
                        template( SiteTemplateKey.from( siteTemplate ) ).
                        moduleConfigs( ModuleConfigJson.toModuleConfigs( moduleConfigs ) );
                }
            } );
    }

    @JsonIgnore
    UpdateSite getUpdateSite()
    {
        return this.updateSite;
    }

}
