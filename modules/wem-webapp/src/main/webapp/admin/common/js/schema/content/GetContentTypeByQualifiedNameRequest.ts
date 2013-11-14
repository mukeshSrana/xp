module api_schema_content {

    export class GetContentTypeByQualifiedNameRequest extends ContentTypeResourceRequest<api_schema_content_json.ContentTypeJson> {

        private name:ContentTypeName;

        private mixinReferencesToFormItems:boolean = true;

        constructor(qualifiedName:ContentTypeName) {
            super();
            super.setMethod("GET");
            this.name = qualifiedName;
        }

        setMixinReferencesToFormItems(value:boolean):GetContentTypeByQualifiedNameRequest {
            this.mixinReferencesToFormItems = value;
            return this;
        }

        getParams():Object {
            return {
                qualifiedName: this.name.toString(),
                mixinReferencesToFormItems: this.mixinReferencesToFormItems
            };
        }

        getRequestPath():api_rest.Path {
            return super.getResourcePath();
        }
    }
}