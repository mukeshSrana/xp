module api.content.attachment {

    export class Attachment {

        private blobKey: api.blob.BlobKey;

        private attachmentName: AttachmentName;

        private mimeType: string;

        private size: number;

        constructor(builder: AttachmentBuilder) {
            this.blobKey = builder.blobKey;
            this.attachmentName = builder.attachmentName;
            this.mimeType = builder.mimeType;
            this.size = builder.size;
        }

        getBlobKey(): api.blob.BlobKey {
            return this.blobKey;
        }

        getAttachmentName(): AttachmentName {
            return this.attachmentName;
        }

        getMimeType(): string {
            return this.mimeType;
        }

        getSize(): number {
            return this.size;
        }

        toJson(): api.content.attachment.AttachmentJson {

            return {
                "blobKey": this.getBlobKey().toString(),
                "attachmentName": this.getAttachmentName().toString(),
                "mimeType": this.getMimeType(),
                "size": this.getSize()
            };
        }

    }

    export class AttachmentBuilder {

        blobKey: api.blob.BlobKey;

        attachmentName: AttachmentName;

        mimeType: string;

        size: number;

        public setBlobKey(value: api.blob.BlobKey): AttachmentBuilder {
            this.blobKey = value;
            return this;
        }

        public setAttachmentName(value: AttachmentName): AttachmentBuilder {
            this.attachmentName = value;
            return this;
        }

        public setMimeType(value: string): AttachmentBuilder {
            this.mimeType = value;
            return this;
        }

        public setSize(value: number): AttachmentBuilder {
            this.size = value;
            return this;
        }

        public build(): Attachment {
            return new Attachment(this);
        }
    }
}