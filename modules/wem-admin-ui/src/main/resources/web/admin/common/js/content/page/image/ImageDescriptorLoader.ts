module api.content.page.image {

    import LoadedDataEvent = api.util.loader.event.LoadedDataEvent;
    import LoadingDataEvent = api.util.loader.event.LoadingDataEvent;

    export class ImageDescriptorLoader extends api.util.loader.BaseLoader<ImageDescriptorsJson, ImageDescriptor> {

        constructor(request: ImageDescriptorsResourceRequest) {
           super(request);
        }

        filterFn(descriptor: ImageDescriptor) {
            return descriptor.getDisplayName().toString().toLowerCase().indexOf(this.getSearchString().toLowerCase()) != -1;
        }


    }
}