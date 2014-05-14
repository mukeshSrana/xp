module api.schema.content {

    export class UpdateContentTypeRequest extends ContentTypeResourceRequest<api.schema.content.json.ContentTypeJson> {

        private contentTypeToUpdate:ContentTypeName;

        private name:ContentTypeName;

        private config:string;

        private icon: api.icon.Icon;

        constructor(contentTypeToUpdate:ContentTypeName, name:ContentTypeName, config:string, icon: api.icon.Icon) {
            super();
            super.setMethod('POST');
            this.contentTypeToUpdate = contentTypeToUpdate;
            this.name = name;
            this.config = config;
            this.icon = icon;
        }

        getParams():Object {
            return {
                contentTypeToUpdate: this.contentTypeToUpdate.toString(),
                name: this.name.toString(),
                config: this.config,
                icon: this.icon != null ? this.icon.toJson() : null
            }
        }

        getRequestPath():api.rest.Path {
            return api.rest.Path.fromParent(super.getResourcePath(), "update");
        }

        sendAndParse(): Q.Promise<ContentType> {

            return this.send().then((response: api.rest.JsonResponse<api.schema.content.json.ContentTypeJson>) => {
                return this.fromJsonToContentType(response.getResult());
            });
        }
    }
}