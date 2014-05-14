module api.form {

    export class Input extends FormItem implements api.Equitable {

        private inputType: InputTypeName;

        private label: string;

        private immutable: boolean;

        private occurrences: Occurrences;

        private indexed: boolean;

        private customText: string;

        private validationRegex: string;

        private helpText: string;

        private inputTypeConfig: any;

        constructor(name: string) {
            super(name);
        }

        static fromJson(json: api.form.json.InputJson): Input {
            var input = new Input(json.name);
            input.setFromJson(json);
            return input;
        }

        private setFromJson(json: api.form.json.InputJson) {
            this.inputType = InputTypeName.parseInputTypeName(json.inputType.name);
            this.label = json.label;
            this.immutable = json.immutable;
            this.occurrences = new Occurrences(json.occurrences);
            this.indexed = json.indexed;
            this.customText = json.customText;
            this.validationRegex = json.validationRegexp;
            this.helpText = json.helpText;
            this.inputTypeConfig = json.config;
        }

        getInputType(): InputTypeName {
            return this.inputType;
        }

        getLabel(): string {
            return this.label;
        }

        isImmutable(): boolean {
            return this.immutable;
        }

        getOccurrences(): Occurrences {
            return this.occurrences;
        }

        isIndexed(): boolean {
            return this.indexed;
        }

        getCustomText(): string {
            return this.customText;
        }

        getValidationRegex(): string {
            return this.validationRegex;
        }

        getHelpText(): string {
            return this.helpText;
        }

        getInputTypeConfig(): any {
            return this.inputTypeConfig;
        }

        setInputType(inputTypeName: InputTypeName) {
            this.inputType = inputTypeName;
        }

        setOccurences(minimum: number, maximum: number) {
            this.occurrences = new Occurrences({"maximum": maximum, "minimum": minimum})
        }

        equals(o: api.Equitable): boolean {

            if (!api.ObjectHelper.iFrameSafeInstanceOf(o, Input)) {
                return false;
            }

            if (!super.equals(o)) {
                return false;
            }

            var other = <Input>o;

            if (!api.ObjectHelper.equals(this.inputType, other.inputType)) {
                return false;
            }

            if (!api.ObjectHelper.stringEquals(this.label, other.label)) {
                return false;
            }

            if (!api.ObjectHelper.booleanEquals(this.immutable, other.immutable)) {
                return false;
            }

            if (!api.ObjectHelper.equals(this.occurrences, other.occurrences)) {
                return false;
            }

            if (!api.ObjectHelper.booleanEquals(this.indexed, other.indexed)) {
                return false;
            }

            if (!api.ObjectHelper.stringEquals(this.customText, other.customText)) {
                return false;
            }

            if (!api.ObjectHelper.stringEquals(this.validationRegex, other.validationRegex)) {
                return false;
            }

            if (!api.ObjectHelper.stringEquals(this.helpText, other.helpText)) {
                return false;
            }

            if (!api.ObjectHelper.anyEquals(this.inputTypeConfig, other.inputTypeConfig)) {
                return false;
            }

            return true;
        }

        public toInputJson(): api.form.json.FormItemTypeWrapperJson {

            return <api.form.json.FormItemTypeWrapperJson>{Input: <api.form.json.InputJson>{
                name: this.getName(),
                customText: this.getCustomText(),
                helpText: this.getHelpText(),
                immutable: this.isImmutable(),
                indexed: this.isIndexed(),
                label: this.getLabel(),
                occurrences: this.getOccurrences().toJson(),
                validationRegexp: this.getValidationRegex(),
                inputType: this.getInputType().toJson(),
                config: this.getInputTypeConfig()
            }};
        }
    }
}