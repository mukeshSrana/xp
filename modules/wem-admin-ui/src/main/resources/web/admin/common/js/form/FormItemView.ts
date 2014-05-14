module api.form {

    export interface FormItemViewConfig {

        className: string;

        context: FormContext;

        formItem: FormItem;

        parent: FormItemSetOccurrenceView;

    }

    export class FormItemView extends api.dom.DivEl {

        private context: FormContext;

        private formItem: FormItem;

        private parent: FormItemSetOccurrenceView;

        private editContentRequestListeners: {(content: api.content.ContentSummary): void}[] = [];

        constructor(config: FormItemViewConfig) {
            super(config.className);
            this.context = config.context;
            this.formItem = config.formItem;
            this.parent = config.parent;
        }

        broadcastFormSizeChanged() {
            throw new Error("Must be implemented by inheritors");
        }

        getContext(): FormContext {
            return this.context;
        }

        getFormItem(): FormItem {
            return this.formItem;
        }

        getParent(): FormItemSetOccurrenceView {
            return this.parent;
        }

        getParentDataPath(): api.data.DataPath {
            if (this.parent) {
                return this.parent.getDataPath();
            }
            return null;
        }

        /*
         *  Override if inheriting FormItemView can provide attachments.
         */
        getAttachments(): api.content.attachment.Attachment[] {
            return [];
        }

        validate(silent: boolean = true): ValidationRecording {

            // Default method to avoid having to implement method in Layout-s.
            return new ValidationRecording();
        }

        giveFocus(): boolean {
            return false;
        }

        onEditContentRequest(listener: (content: api.content.ContentSummary) => void) {
            this.editContentRequestListeners.push(listener);
        }

        unEditContentRequest(listener: (content: api.content.ContentSummary) => void) {
            this.editContentRequestListeners = this.editContentRequestListeners.filter(function (curr) {
                return curr != listener;
            });
        }

        notifyEditContentRequested(content: api.content.ContentSummary) {
            this.editContentRequestListeners.forEach((listener) => {
                listener(content);
            })
        }

        onValidityChanged(listener: (event: ValidityChangedEvent)=>void) {
            //Should be implemented in child classes
        }

        unValidityChanged(listener: (event: ValidityChangedEvent)=>void) {
            //Should be implemented in child classes
        }
    }
}