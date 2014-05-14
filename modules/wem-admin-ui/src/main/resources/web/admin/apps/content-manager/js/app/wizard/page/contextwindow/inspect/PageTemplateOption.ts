module app.wizard.page.contextwindow.inspect {

    export class PageTemplateOption {

        private pageTemplate: api.content.page.PageTemplateSummary;

        constructor(pageTemplate: api.content.page.PageTemplateSummary) {
            this.pageTemplate = pageTemplate;
        }

        getPageTemplate(): api.content.page.PageTemplateSummary {
            return this.pageTemplate;
        }
    }
}