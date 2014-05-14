module api.liveedit {

    import Event2 = api.event.Event2;
    import RegionPath = api.content.page.RegionPath;

    export class RegionSelectEvent extends Event2 {

        private regionPath: RegionPath;

        constructor(regionPath: api.content.page.RegionPath) {
            super();
            this.regionPath = regionPath;
        }

        getPath(): RegionPath {
            return this.regionPath;
        }

        static on(handler: (event: RegionSelectEvent) => void, contextWindow: Window = window) {
            Event2.bind(api.util.getFullName(this), handler, contextWindow);
        }

        static un(handler: (event: RegionSelectEvent) => void, contextWindow: Window = window) {
            Event2.unbind(api.util.getFullName(this), handler, contextWindow);
        }
    }
}