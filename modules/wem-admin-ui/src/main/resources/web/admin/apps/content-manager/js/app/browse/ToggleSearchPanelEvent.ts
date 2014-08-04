module app.browse {

    export class ToggleSearchPanelEvent extends api.event.Event {

        static on(handler: (event: ToggleSearchPanelEvent) => void, contextWindow: Window = window) {
            api.event.Event.bind(api.util.getFullName(this), handler, contextWindow);
        }

        static un(handler?: (event: ToggleSearchPanelEvent) => void, contextWindow: Window = window) {
            api.event.Event.unbind(api.util.getFullName(this), handler, contextWindow);
        }
    }

}