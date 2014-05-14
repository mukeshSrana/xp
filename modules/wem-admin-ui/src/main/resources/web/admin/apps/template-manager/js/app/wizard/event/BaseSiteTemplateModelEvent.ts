module app.wizard.event {

    export class BaseSiteTemplateModelEvent extends api.event.Event {

        private model:api.content.site.template.SiteTemplateSummary[];

        constructor(name:string, model:api.content.site.template.SiteTemplateSummary[]) {
            this.model = model;
            super(name);
        }

        getModels():api.content.site.template.SiteTemplateSummary[] {
            return this.model;
        }
    }
}