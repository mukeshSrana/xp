module api.content.form.inputtype.time {

    import support = api.form.inputtype.support;
    import Property = api.data.Property;
    import Value = api.data.Value;
    import ValueType = api.data.ValueType;
    import ValueTypes = api.data.ValueTypes;
    import Timezone = api.util.Timezone;

    /**
     * Uses [[api.data.ValueType]] [[api.data.ValueTypeLocalDateTime]].
     */
    export class DateTime extends support.BaseInputTypeNotManagingAdd<Date> {

        withTimezone: boolean = false;
        valueType: ValueType = ValueTypes.LOCAL_DATE_TIME;

        constructor(config: api.form.inputtype.InputTypeViewContext) {
            super(config);
            this.readConfig(config.inputConfig);
        }

        private readConfig(inputConfig: { [element: string]: { [name: string]: string }[]; }): void {
            var timeZoneConfig = inputConfig['timezone'] && inputConfig['timezone'][0];
            var timeZone = timeZoneConfig && timeZoneConfig['value'];

            if (timeZone === "true") {
                this.withTimezone = true;
                this.valueType = ValueTypes.DATE_TIME;
            }
        }

        getValueType(): ValueType {
            return this.valueType;
        }

        newInitialValue(): Value {
            return this.valueType.newNullValue();
        }

        createInputOccurrenceElement(index: number, property: Property): api.dom.Element {
            if (this.valueType == ValueTypes.DATE_TIME) {
                return this.createInputAsDateTime(property, this.valueType);
            } else {
                return this.createInputAsLocalDateTime(property, this.valueType);
            }
        }

        hasInputElementValidUserInput(inputElement: api.dom.Element) {
            var dateTimePicker = <api.ui.time.DateTimePicker>inputElement;
            return dateTimePicker.hasValidUserInput();
        }

        availableSizeChanged() {
            // Nothing
        }

        valueBreaksRequiredContract(value: Value): boolean {
            return value.isNull() || !(value.getType().equals(ValueTypes.LOCAL_DATE_TIME) || value.getType().equals(ValueTypes.DATE_TIME));
        }

        private createInputAsLocalDateTime(property: Property, valueType: ValueType) {
            var dateTimeBuilder = new api.ui.time.DateTimePickerBuilder();
            if (property.hasNonNullValue()) {
                var date = property.getLocalDateTime();

                dateTimeBuilder.
                    setYear(date.getFullYear()).
                    setMonth(date.getMonth()).
                    setSelectedDate(date).
                    setHours(date.getHours()).
                    setMinutes(date.getMinutes());
            }

            var dateTimePicker = new api.ui.time.DateTimePicker(dateTimeBuilder);
            dateTimePicker.onSelectedDateTimeChanged((event: api.ui.time.SelectedDateChangedEvent) => {
                this.onValueChanged(property, event.getDate(), valueType);
            });
            return dateTimePicker;
        }

        private createInputAsDateTime(property: Property, valueType: ValueType) {
            var dateTimeBuilder = new api.ui.time.DateTimePickerBuilder();
            dateTimeBuilder.setUseLocalTimezoneIfNotPresent(true);
            if (property.hasNonNullValue()) {
                var date: api.util.DateTime = property.getDateTime();
                dateTimeBuilder.
                    setYear(date.getYear()).
                    setMonth(date.getMonth()).
                    setSelectedDate(date.toDate()).
                    setHours(date.getHours()).
                    setMinutes(date.getMinutes()).
                    setTimezone(date.getTimezone());
            }

            var dateTimePicker = new api.ui.time.DateTimePicker(dateTimeBuilder);
            dateTimePicker.onSelectedDateTimeChanged((event: api.ui.time.SelectedDateChangedEvent) => {
                this.onValueChanged(property, event.getDate() == null ? null : api.util.DateTime.fromDate(event.getDate()), valueType);
            });
            return dateTimePicker;
        }
    }
    api.form.inputtype.InputTypeManager.register(new api.Class("DateTime", DateTime));

}