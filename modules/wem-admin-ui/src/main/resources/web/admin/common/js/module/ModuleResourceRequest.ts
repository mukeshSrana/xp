module api.module {

    export class ModuleResourceRequest<T> extends api.rest.ResourceRequest<T> {

        private resourcePath: api.rest.Path;

        constructor() {
            super();
            this.resourcePath = api.rest.Path.fromParent(super.getRestPath(), "module");
        }

        getResourcePath(): api.rest.Path {
            return this.resourcePath;
        }

        fromJsonToModule(json: api.module.json.ModuleJson): Module {
            return Module.fromJson(json);
        }
    }
}