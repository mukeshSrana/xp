package com.enonic.wem.api.content.page;


import com.enonic.wem.api.content.ContentName;
import com.enonic.wem.api.content.ContentPath;
import com.enonic.wem.api.data.RootDataSet;
import com.enonic.wem.api.schema.content.ContentTypeNames;

public class CreatePageTemplateParams
{
    private ContentPath site;

    private ContentName name;

    private String displayName;

    private PageDescriptorKey controller;

    private ContentTypeNames supports;

    private PageRegions pageRegions;

    private RootDataSet pageConfig;

    public CreatePageTemplateParams site( final ContentPath site )
    {
        this.site = site;
        return this;
    }

    public CreatePageTemplateParams name( final ContentName name )
    {
        this.name = name;
        return this;
    }

    public CreatePageTemplateParams name( final String name )
    {
        this.name = ContentName.from( name );
        return this;
    }

    public CreatePageTemplateParams displayName( final String displayName )
    {
        this.displayName = displayName;
        return this;
    }

    public CreatePageTemplateParams controller( final PageDescriptorKey controller )
    {
        this.controller = controller;
        return this;
    }

    public CreatePageTemplateParams supports( final ContentTypeNames supports )
    {
        this.supports = supports;
        return this;
    }

    public CreatePageTemplateParams pageRegions( final PageRegions pageRegions )
    {
        this.pageRegions = pageRegions;
        return this;
    }

    public CreatePageTemplateParams pageConfig( final RootDataSet pageConfig )
    {
        this.pageConfig = pageConfig;
        return this;
    }

    public ContentPath getSite()
    {
        return site;
    }

    public ContentName getName()
    {
        return name;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public PageDescriptorKey getController()
    {
        return controller;
    }

    public ContentTypeNames getSupports()
    {
        return supports;
    }

    public PageRegions getPageRegions()
    {
        return pageRegions;
    }

    public RootDataSet getPageConfig()
    {
        return pageConfig;
    }

    public void validate()
    {
        // TODO
    }
}