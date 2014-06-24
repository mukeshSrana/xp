module api.liveedit {

    import Event2 = api.event.Event2;
    import Descriptor = api.content.page.Descriptor;
    import ItemView = api.liveedit.ItemView;
    import DescriptorBasedPageComponent = api.content.page.DescriptorBasedPageComponent;

    export class PageComponentSetDescriptorEvent extends Event2 {

        private descriptor: Descriptor;

        private pageItemView: PageComponentView<DescriptorBasedPageComponent>;

        constructor(descriptor: Descriptor, itemView: PageComponentView<DescriptorBasedPageComponent>) {
            super();
            this.descriptor = descriptor;
            this.pageItemView = itemView;
        }

        getDescriptor(): Descriptor {
            return this.descriptor;
        }

        getPageComponentView(): PageComponentView<DescriptorBasedPageComponent> {
            return this.pageItemView;
        }

        static on(handler: (event: PageComponentSetDescriptorEvent) => void, contextWindow: Window = window) {
            Event2.bind(api.util.getFullName(this), handler, contextWindow);
        }

        static un(handler?: (event: PageComponentSetDescriptorEvent) => void, contextWindow: Window = window) {
            Event2.unbind(api.util.getFullName(this), handler, contextWindow);
        }
    }
}