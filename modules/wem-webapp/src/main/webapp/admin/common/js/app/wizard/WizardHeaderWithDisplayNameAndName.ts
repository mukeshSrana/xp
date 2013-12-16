module api_app_wizard {

    export class WizardHeaderWithDisplayNameAndName extends WizardHeader implements api_event.Observable {

        private forbiddenChars:RegExp = /[^a-z0-9\-]+/ig;

        private displayNameEl:api_ui.TextInput;

        private pathEl:api_dom.SpanEl;

        private nameEl:api_ui.TextInput;

        private autogenerateName:boolean = false;

        private nameTooltip:api_ui.Tooltip;

        private displayNameTooltip:api_ui.Tooltip;

        constructor() {
            super();

            this.displayNameEl = api_ui.AutosizeTextInput.large().setName('displayName');
            this.displayNameEl.addListener({
                onValueChanged: (oldValue, newValue) => {
                    this.notifyPropertyChanged("displayName", oldValue, newValue);
                }
            });
            this.appendChild(this.displayNameEl);

            this.pathEl = new api_dom.SpanEl(null, 'path');
            this.pathEl.hide();
            this.appendChild(this.pathEl);

            this.nameEl = api_ui.AutosizeTextInput.middle().setName('name').setForbiddenCharsRe(this.forbiddenChars);
            this.nameEl.addListener({
                onValueChanged: (oldValue, newValue) => {
                    this.notifyPropertyChanged("name", oldValue, newValue);
                }
            });
            this.appendChild(this.nameEl);

            this.displayNameEl.getEl().addEventListener('input', () => {
                this.setDisplayName(this.displayNameEl.getValue());
            });

            this.nameEl.getEl().addEventListener('input', () => {
                var currentName = this.nameEl.getValue();

                // stop name autogeneration if user typed some chars
                // or make it autogenerated if input became empty
                var generatedName = this.generateName(this.getDisplayName());
                this.autogenerateName = currentName == generatedName;
            });

            this.nameTooltip = new api_ui.Tooltip(this.nameEl, "Name", 1000, 1000, api_ui.Tooltip.TRIGGER_FOCUS, api_ui.Tooltip.SIDE_RIGHT);
            this.displayNameTooltip =
            new api_ui.Tooltip(this.displayNameEl, "Display name", 1000, 1000, api_ui.Tooltip.TRIGGER_FOCUS, api_ui.Tooltip.SIDE_RIGHT);
        }

        setAutogenerateName(value:boolean) {
            this.autogenerateName = value;
        }

        initNames(displayName:string,name:string) {

            if( displayName == name || name == this.generateName(displayName)) {
                this.autogenerateName = true;
            }

            this.displayNameEl.setValue(displayName);
            if( name != null ) {
                this.nameEl.setValue(name);
            }
            else {
                this.nameEl.setValue(this.generateName(displayName));
            }
        }

        setDisplayName(value:string) {

            // stop displayName autogeneration if user typed some values
            // or make it autogenerated if input became empty
            if (this.autogenerateName) {
                this.nameEl.setValue(this.generateName(value));
            }

            this.displayNameEl.setValue(value);

        }

        setName(value:string) {
            this.nameEl.setValue(value);
        }

        setPath(value:string) {
            this.pathEl.getEl().setInnerHtml(value);
            this.pathEl.getEl().setAttribute('title', value);
            if (value) {
                this.pathEl.show();
            } else {
                this.pathEl.hide();
            }
        }

        getName():string {
            return this.nameEl.getValue();
        }

        getDisplayName():string {
            return this.displayNameEl.getValue();
        }

        giveFocus(): boolean  {
            return this.displayNameEl.giveFocus();
        }

        private generateName(value:string):string {
            if( !value ) {
                return "";
            }

            var generated = value.replace(/[\s+\.\/]/ig, '-').replace(/-{2,}/g, '-').replace(/^-|-$/g, '').toLowerCase();
            return this.removeForbiddenChars(generated);
        }

        private removeForbiddenChars(rawValue:string):string {
            return this.forbiddenChars ? (rawValue || '').replace(this.forbiddenChars, '') : rawValue;
        }

    }
}