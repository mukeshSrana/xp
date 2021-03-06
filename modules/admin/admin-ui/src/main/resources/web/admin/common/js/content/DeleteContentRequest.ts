module api.content {

    export class DeleteContentRequest extends ContentResourceRequest<DeleteContentResultJson, DeleteContentResult> {

        private contentPaths:ContentPath[] = [];

        constructor(contentPath?:ContentPath) {
            super();
            super.setMethod("POST");
            if (contentPath) {
                this.addContentPath(contentPath);
            }
        }

        setContentPaths(contentPaths:ContentPath[]):DeleteContentRequest {
            this.contentPaths = contentPaths;
            return this;
        }

        addContentPath(contentPath:ContentPath):DeleteContentRequest {
            this.contentPaths.push(contentPath);
            return this;
        }

        getParams():Object {
            var fn = (contentPath:ContentPath) => {
                return contentPath.toString();
            };
            return {
                contentPaths: this.contentPaths.map(fn)
            };
        }

        getRequestPath():api.rest.Path {
            return api.rest.Path.fromParent(super.getResourcePath(), "delete");
        }

        sendAndParse(): wemQ.Promise<DeleteContentResult> {

            return this.send().
                then((response: api.rest.JsonResponse<DeleteContentResultJson>) => {

                    return DeleteContentResult.fromJson(response.getResult());

                });
        }
    }
}