module app.wizard.page {

    import ComponentPath = api.content.page.ComponentPath;

    export class PageComponentResetEvent {

        private path: ComponentPath;

        constructor(path: ComponentPath) {
            this.path = path;
        }

        getPath(): ComponentPath {
            return this.path;
        }
    }
}