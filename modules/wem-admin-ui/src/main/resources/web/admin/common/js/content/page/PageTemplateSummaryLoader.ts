module api.content.page {

    import PageTemplateSummaryListJson = api.content.page.PageTemplateSummaryListJson;

    export class PageTemplateSummaryLoader extends api.util.loader.BaseLoader<PageTemplateSummaryListJson, PageTemplateSummary> {



        constructor(request: PageTemplateResourceRequest<api.content.page.PageTemplateSummaryListJson>) {
            super(request);
        }

        filterFn(template:PageTemplateSummary) {
            return template.getDisplayName().toString().indexOf(this.getSearchString().toLowerCase()) != -1;
        }

    }
}