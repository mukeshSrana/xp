module app.browse {

    import Module = api.module.Module;
    import Event = api.event.Event;

    export class StopModuleEvent extends Event {
        private modules: Module[];

        constructor(modules: Module[]) {
            this.modules = modules;
            super();
        }

        getModules(): Module[] {
            return this.modules;
        }

        static on(handler: (event: StopModuleEvent) => void) {
            Event.bind(api.util.getFullName(this), handler);
        }

        static un(handler?: (event: StopModuleEvent) => void) {
            Event.unbind(api.util.getFullName(this), handler);
        }
    }
}
