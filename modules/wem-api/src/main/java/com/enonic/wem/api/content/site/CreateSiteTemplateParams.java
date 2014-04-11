package com.enonic.wem.api.content.site;

import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import com.enonic.wem.api.content.page.PageTemplate;
import com.enonic.wem.api.content.page.PageTemplates;
import com.enonic.wem.api.module.ModuleKeys;
import com.enonic.wem.api.schema.content.ContentTypeFilter;
import com.enonic.wem.api.schema.content.ContentTypeName;

public final class CreateSiteTemplateParams
{
    private SiteTemplateName name;

    private SiteTemplateVersion version;

    private String displayName;

    private String description;

    private String url;

    private Vendor vendor;

    private ModuleKeys modules;

    private ContentTypeFilter contentTypeFilter;

    private ContentTypeName rootContentType;

    private List<PageTemplate> templates = Lists.newArrayList();

    public static CreateSiteTemplateParams fromSiteTemplate( final SiteTemplate siteTemplate )
    {
        final CreateSiteTemplateParams createSiteTemplate = new CreateSiteTemplateParams().
            name( siteTemplate.getName() ).
            version( siteTemplate.getVersion() ).
            displayName( siteTemplate.getDisplayName() ).
            description( siteTemplate.getDescription() ).
            url( siteTemplate.getUrl() ).
            vendor( siteTemplate.getVendor() ).
            modules( siteTemplate.getModules() ).
            contentTypeFilter( siteTemplate.getContentTypeFilter() ).
            rootContentType( siteTemplate.getRootContentType() );
        for ( PageTemplate template : siteTemplate.getPageTemplates() )
        {
            createSiteTemplate.addPageTemplate( template );
        }
        return createSiteTemplate;
    }

    public CreateSiteTemplateParams siteTemplateKey( final SiteTemplateKey key )
    {
        this.name( key.getName() );
        this.version( key.getVersion() );
        return this;
    }

    public CreateSiteTemplateParams name( final String name )
    {
        return this.name( new SiteTemplateName( name ) );
    }

    public CreateSiteTemplateParams name( final SiteTemplateName name )
    {
        this.name = name;
        return this;
    }

    public CreateSiteTemplateParams version( final SiteTemplateVersion version )
    {
        this.version = version;
        return this;
    }

    public CreateSiteTemplateParams displayName( final String displayName )
    {
        this.displayName = displayName;
        return this;
    }

    public CreateSiteTemplateParams description( final String description )
    {
        this.description = description;
        return this;
    }

    public CreateSiteTemplateParams url( final String url )
    {
        this.url = url;
        return this;
    }

    public CreateSiteTemplateParams vendor( final Vendor vendor )
    {
        this.vendor = vendor;
        return this;
    }

    public CreateSiteTemplateParams modules( final ModuleKeys modules )
    {
        this.modules = modules;
        return this;
    }

    public CreateSiteTemplateParams contentTypeFilter( final ContentTypeFilter contentTypeFilter )
    {
        this.contentTypeFilter = contentTypeFilter;
        return this;
    }

    public CreateSiteTemplateParams rootContentType( final ContentTypeName rootContentType )
    {
        this.rootContentType = rootContentType;
        return this;
    }

    public CreateSiteTemplateParams addPageTemplate( final PageTemplate template )
    {
        this.templates.add( template );
        return this;
    }

    public CreateSiteTemplateParams addPageTemplates( final PageTemplates templates )
    {
        this.templates.addAll( templates.getList() );
        return this;
    }

    public SiteTemplateName getName()
    {
        return name;
    }

    public SiteTemplateVersion getVersion()
    {
        return version;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public String getDescription()
    {
        return description;
    }

    public String getUrl()
    {
        return url;
    }

    public Vendor getVendor()
    {
        return vendor;
    }

    public ModuleKeys getModules()
    {
        return modules;
    }

    public ContentTypeFilter getContentTypeFilter()
    {
        return contentTypeFilter;
    }

    public ContentTypeName getRootContentType()
    {
        return rootContentType;
    }

    public List<PageTemplate> getTemplates()
    {
        return templates;
    }

    public void validate()
    {
        Preconditions.checkNotNull( this.name, "name cannot be null" );
        Preconditions.checkNotNull( this.version, "version cannot be null" );
        Preconditions.checkNotNull( this.displayName, "displayName cannot be null" );
    }
}