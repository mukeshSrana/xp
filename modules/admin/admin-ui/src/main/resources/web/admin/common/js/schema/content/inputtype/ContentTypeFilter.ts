module api.schema.content.inputtype {

    import ContentInputTypeViewContext = api.content.form.inputtype.ContentInputTypeViewContext;
    import InputValidationRecording = api.form.inputtype.InputValidationRecording;
    import InputValidityChangedEvent = api.form.inputtype.InputValidityChangedEvent;
    import ValueChangedEvent = api.form.inputtype.ValueChangedEvent;
    import Property = api.data.Property;
    import PropertyArray = api.data.PropertyArray;
    import Value = api.data.Value;
    import ValueType = api.data.ValueType;
    import ValueTypes = api.data.ValueTypes;
    import Input = api.form.Input;
    import ContentTypeComboBox = api.schema.content.ContentTypeComboBox;
    import ContentTypeSummary = api.schema.content.ContentTypeSummary;
    import OptionSelectedEvent = api.ui.selector.OptionSelectedEvent;
    import SelectedOption = api.ui.selector.combobox.SelectedOption;
    import ApplicationKey = api.application.ApplicationKey;

    export class ContentTypeFilter extends api.form.inputtype.support.BaseInputTypeManagingAdd<string> {

        private combobox: ContentTypeComboBox;

        private context: ContentInputTypeViewContext;

        constructor(context: ContentInputTypeViewContext) {
            super('content-type-filter');
            this.context = context;
        }

        getValueType(): ValueType {
            return ValueTypes.STRING;
        }

        newInitialValue(): Value {
            return null;
        }

        private createPageTemplateLoader(): PageTemplateContentTypeLoader {
            var contentId = this.context.site.getContentId(),
                loader = new api.schema.content.PageTemplateContentTypeLoader(contentId);

            loader.setComparator(new api.content.ContentSummaryByDisplayNameComparator());

            return loader;
        }

        private createComboBox(): ContentTypeComboBox {
            var loader = this.context.formContext.getContentTypeName().isPageTemplate() ? this.createPageTemplateLoader() : null,
                comboBox = new ContentTypeComboBox(this.getInput().getOccurrences().getMaximum(), loader);

            comboBox.onLoaded((contentTypeArray: ContentTypeSummary[]) => this.onContentTypesLoaded(contentTypeArray));
            comboBox.onOptionSelected((event: OptionSelectedEvent<ContentTypeSummary>) => this.onContentTypeSelected(event));
            comboBox.onOptionDeselected((option: SelectedOption<ContentTypeSummary>) => this.onContentTypeDeselected(option));

            return comboBox;
        }

        private onContentTypesLoaded(contentTypeArray: ContentTypeSummary[]): void {
            var contentTypes = [];
            this.getPropertyArray().forEach((property: Property) => {
                contentTypes.push(property.getString());
            });

            this.combobox.getComboBox().setValues(contentTypes);

            this.setLayoutInProgress(false);
            this.combobox.unLoaded(this.onContentTypesLoaded);

            this.validate(false);
        }

        private onContentTypeSelected(event: OptionSelectedEvent<ContentTypeSummary>): void {
            if (this.isLayoutInProgress()) {
                return;
            }

            var value = new Value(event.getOption().displayValue.getContentTypeName().toString(), ValueTypes.STRING);
            if (this.combobox.countSelected() == 1) { // overwrite initial value
                this.getPropertyArray().set(0, value);
            }
            else {
                this.getPropertyArray().add(value);
            }

            this.validate(false);
        }

        private onContentTypeDeselected(option: SelectedOption<ContentTypeSummary>): void {
            this.getPropertyArray().remove(option.getIndex());
            this.validate(false);
        }

        layout(input: Input, propertyArray: PropertyArray): wemQ.Promise<void> {
            super.layout(input, propertyArray);

            this.appendChild(this.combobox = this.createComboBox());

            return wemQ<void>(null);
        }

        private getValues(): Value[] {
            return this.combobox.getSelectedDisplayValues().map((contentType: ContentTypeSummary) => {
                return new Value(contentType.getContentTypeName().toString(), ValueTypes.STRING);
            });
        }

        protected getNumberOfValids(): number {
            return this.getValues().length;
        }

        giveFocus(): boolean {
            return this.combobox.maximumOccurrencesReached() ? false : this.combobox.giveFocus();
        }

        onFocus(listener: (event: FocusEvent) => void) {
            this.combobox.onFocus(listener);
        }

        unFocus(listener: (event: FocusEvent) => void) {
            this.combobox.unFocus(listener);
        }

        onBlur(listener: (event: FocusEvent) => void) {
            this.combobox.onBlur(listener);
        }

        unBlur(listener: (event: FocusEvent) => void) {
            this.combobox.unBlur(listener);
        }
    }

    api.form.inputtype.InputTypeManager.register(new api.Class("ContentTypeFilter", ContentTypeFilter));

}