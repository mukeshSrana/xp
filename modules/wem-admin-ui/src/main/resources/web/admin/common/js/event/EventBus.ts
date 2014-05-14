module api.event {

    var bus = new Ext.util.Observable({});

    export function onEvent(name: string, handler: (event: api.event.Event) => void) {
        bus.on(name, handler);
    }

    export function fireEvent(event: api.event.Event) {
        bus.fireEvent(event.getName(), event);
    }
}