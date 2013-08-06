module api_app_wizard {

    export class WizardPanelHeader extends api_dom.DivEl {

        private wizardPanel:WizardPanel;

        private displayNameEl:api_ui.TextInput;

        private nameEl:api_ui.TextInput;

        private autogenerateDisplayName:bool = false;

        private autogenerateName:bool = false;

        constructor(wizardPanel:WizardPanel) {
            super(null, "wizard-header");

            this.wizardPanel = wizardPanel;

            this.displayNameEl = api_ui.AutosizeTextInput.large().setName('displayName');
            this.appendChild(this.displayNameEl);

            this.nameEl = api_ui.AutosizeTextInput.middle().setName('name').setForbiddenCharsRe(/[^a-z0-9\-]+/ig);
            this.appendChild(this.nameEl);

            this.displayNameEl.getEl().addEventListener('input', () => {
                var displayNameValue = this.getDisplayName();

                // stop displayName autogeneration if user typed some values
                // or make it autogenerated if input became empty
                this.setAutogenerateDisplayName(!displayNameValue);

                if (this.isAutogenerateName()) {
                    this.setName(this.generateName(displayNameValue));
                }

                new DisplayNameChangedEvent(this.wizardPanel).fire();
            });

            this.nameEl.getEl().addEventListener('input', () => {
                var nameValue = this.getName();

                // stop name autogeneration if user typed some chars
                // or make it autogenerated if input became empty
                this.setAutogenerateName(!nameValue);

                new NameChangedEvent(this.wizardPanel).fire();
            });
        }

        getDisplayName():string {
            return this.displayNameEl.getValue();
        }

        setDisplayName(value:string) {
            this.displayNameEl.setValue(value);
            new DisplayNameChangedEvent(this.wizardPanel).fire();
        }

        getName():string {
            return this.nameEl.getValue();
        }

        setName(value:string) {
            this.nameEl.setValue(value);
            new NameChangedEvent(this.wizardPanel).fire();
        }

        isAutogenerateDisplayName():bool {
            return this.autogenerateDisplayName;
        }

        setAutogenerateDisplayName(value:bool) {
            this.autogenerateDisplayName = value;
        }

        isAutogenerateName():bool {
            return this.autogenerateName;
        }

        setAutogenerateName(value:bool) {
            this.autogenerateName = value;
        }

        generateName(value:string):string {
            return value ? value.replace(/[\s+\.\/]/ig, '-').replace(/-{2,}/g, '-').replace(/^-|-$/g, '').toLowerCase() : '';
        }
    }
}