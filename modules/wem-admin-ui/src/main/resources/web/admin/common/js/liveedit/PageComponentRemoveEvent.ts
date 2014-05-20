module api.liveedit {

    import Event2 = api.event.Event2;
    import ComponentPath = api.content.page.ComponentPath;

    export class PageComponentRemoveEvent extends Event2 {

        private path: ComponentPath;

        constructor(path: ComponentPath) {
            super();
            this.path = path;
        }

        getPath(): ComponentPath {
            return this.path;
        }

        static on(handler: (event: PageComponentRemoveEvent) => void, contextWindow: Window = window) {
            Event2.bind(api.util.getFullName(this), handler, contextWindow);
        }

        static un(handler?: (event: PageComponentRemoveEvent) => void, contextWindow: Window = window) {
            Event2.unbind(api.util.getFullName(this), handler, contextWindow);
        }
    }
}