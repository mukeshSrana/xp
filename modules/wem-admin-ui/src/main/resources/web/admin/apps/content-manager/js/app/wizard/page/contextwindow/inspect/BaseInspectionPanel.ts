module app.wizard.page.contextwindow.inspect {

    export class BaseInspectionPanel extends api.ui.Panel {

        constructor() {
            super("inspection-panel");

            this.onRendered((event) => {
                $(this.getHTMLElement()).slimScroll({
                    height: '100%'
                });
            })
        }
    }
}