module api.app.browse.grid {

    export class TreeGridItemDoubleClickedEvent {

        private clickedModel: Ext_data_Model;

        constructor(clickedModel: Ext_data_Model) {
            this.clickedModel = clickedModel;
        }

        getClickedModel(): Ext_data_Model {
            return this.clickedModel;
        }
    }
}