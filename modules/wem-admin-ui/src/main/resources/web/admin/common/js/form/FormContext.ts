module api.form {

    export class FormContext {

        private parentContent: api.content.Content;

        private persistedContent: api.content.Content;

        private attachments: api.content.attachment.Attachments;

        private showEmptyFormItemSetOccurrences: boolean;

        constructor(builder: FormContextBuilder) {
            this.parentContent = builder.parentContent;
            this.persistedContent = builder.persistedContent;
            this.showEmptyFormItemSetOccurrences = builder.showEmptyFormItemSetOccurrences;
            this.attachments = builder.attachments;
        }

        getContentId(): api.content.ContentId {
            return this.persistedContent != null ? this.persistedContent.getContentId() : null;
        }

        getContentPath(): api.content.ContentPath {
            return this.persistedContent != null ? this.persistedContent.getPath(): null;
        }

        getParentContentPath(): api.content.ContentPath {

            if (this.parentContent == null) {
                return api.content.ContentPath.ROOT;
            }

            return this.parentContent.getPath();
        }

        getAttachments() : api.content.attachment.Attachments{
            return this.attachments;
        }

        getShowEmptyFormItemSetOccurrences(): boolean {
            return this.showEmptyFormItemSetOccurrences;
        }
    }

    export class FormContextBuilder {

        parentContent: api.content.Content;

        persistedContent: api.content.Content;

        attachments: api.content.attachment.Attachments;

        showEmptyFormItemSetOccurrences: boolean;

        public setParentContent(value: api.content.Content): FormContextBuilder {
            this.parentContent = value;
            return this;
        }

        public setPersistedContent(value: api.content.Content): FormContextBuilder {
            this.persistedContent = value;
            return this;
        }

        public setAttachments(value: api.content.attachment.Attachments): FormContextBuilder {
            this.attachments = value;
            return this;
        }

        public setShowEmptyFormItemSetOccurrences(value: boolean): FormContextBuilder {
            this.showEmptyFormItemSetOccurrences = value;
            return this;
        }

        public build(): FormContext {
            return new FormContext(this);
        }
    }
}