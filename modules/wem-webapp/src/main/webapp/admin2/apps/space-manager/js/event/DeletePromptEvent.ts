module app_event {

    export class DeletePromptEvent extends BaseSpaceModelEvent {
        constructor(model:api_model.SpaceModel[]) {
            super('deletePrompt', model);
        }

        static on(handler:(event:DeletePromptEvent) => void) {
            api_event.onEvent('deletePrompt', handler);
        }
    }
}

