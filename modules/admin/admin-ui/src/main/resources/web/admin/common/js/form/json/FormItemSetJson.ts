module api.form.json{

    export class FormItemSetJson extends FormItemJson{

        customText:string;

        helpText:string;

        immutable:boolean;

        items:FormItemTypeWrapperJson[];

        label:string;

        occurrences:OccurrencesJson;
    }
}