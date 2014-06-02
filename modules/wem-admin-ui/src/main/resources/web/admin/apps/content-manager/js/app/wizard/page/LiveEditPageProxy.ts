module app.wizard.page {

    import SiteTemplate = api.content.site.template.SiteTemplate;
    import Content = api.content.Content;
    import ContentId = api.content.ContentId;
    import Descriptor = api.content.page.Descriptor;
    import RegionPath = api.content.page.RegionPath;
    import PageComponentType = api.content.page.PageComponentType;
    import ComponentPath = api.content.page.ComponentPath;
    import UploadDialog = api.content.inputtype.image.UploadDialog;
    import RenderingMode = api.rendering.RenderingMode;

    import ItemView = api.liveedit.ItemView;
    import PageViewItemsParsedEvent = api.liveedit.PageViewItemsParsedEvent;
    import NewPageComponentIdMapEvent = api.liveedit.NewPageComponentIdMapEvent;
    import ImageOpenUploadDialogEvent = api.liveedit.ImageOpenUploadDialogEvent;
    import ImageUploadedEvent = api.liveedit.ImageUploadedEvent;
    import ImageComponentSetImageEvent = api.liveedit.image.ImageComponentSetImageEvent;
    import DraggableStartEvent = api.liveedit.DraggableStartEvent;
    import DraggableStopEvent = api.liveedit.DraggableStopEvent;
    import SortableStartEvent = api.liveedit.SortableStartEvent;
    import SortableStopEvent = api.liveedit.SortableStopEvent;
    import SortableUpdateEvent = api.liveedit.SortableUpdateEvent;
    import PageSelectEvent = api.liveedit.PageSelectEvent;
    import RegionSelectEvent = api.liveedit.RegionSelectEvent;
    import PageComponentSelectEvent = api.liveedit.PageComponentSelectEvent;
    import PageComponentDeselectEvent = api.liveedit.PageComponentDeselectEvent;
    import PageComponentAddedEvent = api.liveedit.PageComponentAddedEvent;
    import PageComponentRemoveEvent = api.liveedit.PageComponentRemoveEvent;
    import PageComponentResetEvent = api.liveedit.PageComponentResetEvent;
    import PageComponentDuplicateEvent = api.liveedit.PageComponentDuplicateEvent;
    import PageComponentSetDescriptorEvent = api.liveedit.PageComponentSetDescriptorEvent;
    import PageComponentLoadedEvent = api.liveedit.PageComponentLoadedEvent;
    import PageComponentSelectComponentEvent = api.liveedit.PageComponentSelectComponentEvent;
    import RegionEmptyEvent = api.liveedit.RegionEmptyEvent;

    export interface LiveEditPageProxyConfig {

        liveFormPanel: LiveFormPanel;

        siteTemplate: SiteTemplate;
    }

    export class LiveEditPageProxy {

        private baseUrl: string;

        private liveFormPanel: LiveFormPanel;

        private siteTemplate: SiteTemplate;

        private liveEditIFrame: api.dom.IFrameEl;

        private loadMask: api.ui.LoadMask;

        private liveEditWindow: any;

        private liveEditJQuery: JQueryStatic;

        private dragMask: api.ui.DragMask;

        private iFrameLoadDeffered: Q.Deferred<void>;

        private contentLoadedOnPage: Content;

        private loadedListeners: {(): void;}[] = [];

        private pageViewItemsParsedListeners: {(event: PageViewItemsParsedEvent): void;}[] = [];

        private draggableStartListeners: {(event: DraggableStartEvent): void;}[] = [];

        private draggableStopListeners: {(event: DraggableStopEvent): void;}[] = [];

        private sortableStartListeners: {(event: SortableStartEvent): void;}[] = [];

        private sortableUpdateListeners: {(event: SortableUpdateEvent): void;}[] = [];

        private sortableStopListeners: {(event: SortableStopEvent): void;}[] = [];

        private pageSelectedListeners: {(event: PageSelectEvent): void;}[] = [];

        private regionSelectedListeners: {(event: RegionSelectEvent): void;}[] = [];

        private pageComponentSelectedListeners: {(event: PageComponentSelectEvent): void;}[] = [];

        private deselectListeners: {(event: PageComponentDeselectEvent): void;}[] = [];

        private pageComponentAddedListeners: {(event: PageComponentAddedEvent): void;}[] = [];

        private imageComponentSetImageListeners: {(event: ImageComponentSetImageEvent): void;}[] = [];

        private pageComponentSetDescriptorListeners: {(event: PageComponentSetDescriptorEvent): void;}[] = [];

        private pageComponentRemovedListeners: {(event: PageComponentRemoveEvent): void;}[] = [];

        private pageComponentResetListeners: {(event: PageComponentResetEvent): void;}[] = [];

        private pageComponentDuplicatedListeners: {(event: PageComponentDuplicateEvent): void;}[] = [];

        private regionEmptyListeners: {(event: RegionEmptyEvent): void;}[] = [];

        constructor(config: LiveEditPageProxyConfig) {

            this.baseUrl = api.util.getUri("portal/edit/");
            this.liveFormPanel = config.liveFormPanel;
            this.siteTemplate = config.siteTemplate;

            this.liveEditIFrame = new api.dom.IFrameEl("live-edit-frame");
            this.iFrameLoadDeffered = Q.defer<void>();
            this.liveEditIFrame.onLoaded(() => this.handleIFrameLoadedEvent());
            this.loadMask = new api.ui.LoadMask(this.liveEditIFrame);
            this.dragMask = new api.ui.DragMask(this.liveEditIFrame);

            ShowContentFormEvent.on(() => {
                if (this.loadMask.isVisible()) {
                    this.loadMask.hide();
                }
            });
        }

        public setWidth(value: string) {
            this.liveEditIFrame.getEl().setWidth(value);
        }

        public setWidthPx(value: number) {
            this.liveEditIFrame.getEl().setWidthPx(value);
        }

        public setHeight(value: string) {
            this.liveEditIFrame.getEl().setHeight(value);
        }

        public setHeightPx(value: number) {
            this.liveEditIFrame.getEl().setHeightPx(value);
        }

        public getWidth(): number {
            return this.liveEditIFrame.getEl().getWidth();
        }

        public getHeight(): number {
            return this.liveEditIFrame.getEl().getHeight();
        }

        public getIFrame(): api.dom.IFrameEl {
            return this.liveEditIFrame;
        }

        public getLoadMask(): api.ui.LoadMask {
            return this.loadMask;
        }

        public getLiveEditJQuery(): JQueryStatic {
            return this.liveEditJQuery;
        }

        public createDraggable(item: JQuery) {

            this.liveEditWindow.LiveEdit.component.dragdropsort.DragDropSort.createJQueryUiDraggable(item);
        }

        public showDragMask() {
            this.dragMask.show();
        }

        public hideDragMask() {
            this.dragMask.hide();
        }

        public remove() {
            this.dragMask.remove();
            this.loadMask.remove();
        }

        public load(content: Content): Q.Promise<void> {

            this.iFrameLoadDeffered = Q.defer<void>();
            this.contentLoadedOnPage = content;

            this.loadMask.show();
            this.liveEditIFrame.setSrc(this.baseUrl + content.getContentId().toString());

            return this.iFrameLoadDeffered.promise;
        }

        private handleIFrameLoadedEvent() {

            var liveEditWindow = this.liveEditIFrame.getHTMLElement()["contentWindow"];
            if (liveEditWindow && liveEditWindow.wemjq) {
                // Give loaded page same CONFIG.baseUri as in admin
                liveEditWindow.CONFIG = { baseUri: CONFIG.baseUri };

                this.liveEditJQuery = <JQueryStatic>liveEditWindow.wemjq;
                if (this.liveEditIFrame != liveEditWindow) {
                    this.liveEditWindow = liveEditWindow;
                    this.listenToPage();
                }

                this.loadMask.hide();

                new api.liveedit.InitializeLiveEditEvent(this.contentLoadedOnPage, this.siteTemplate).fire(this.liveEditWindow);

                this.notifyLoaded();
            }

            this.iFrameLoadDeffered.resolve(null);
        }

        public loadComponent(componentPath: ComponentPath, itemView: ItemView, content: api.content.Content) {

            api.util.assertNotNull(componentPath, "componentPath cannot be null");
            api.util.assertNotNull(itemView, "itemView cannot be null");
            api.util.assertNotNull(content, "content cannot be null");

            this.liveEditWindow.pageItemViews.removeItemViewById(itemView.getItemId());

            wemjq.ajax({
                url: api.rendering.UriHelper.getComponentUri(content.getContentId().toString(), componentPath.toString(),
                    RenderingMode.EDIT),
                method: 'GET',
                success: (data) => {

                    var newElement = wemjq(data);
                    wemjq(itemView.getHTMLElement()).replaceWith(newElement);
                    itemView.remove();
                    var newItemView: ItemView = itemView.getType().createView(newElement.get(0));

                    new PageComponentLoadedEvent(newItemView).fire(this.liveEditWindow);

                    this.liveEditWindow.LiveEdit.PlaceholderCreator.renderEmptyRegionPlaceholders();

                    this.liveEditWindow.LiveEdit.component.Selection.handleSelect(newItemView, null, true);

                }
            });
        }

        public getComponentByPath(path: api.content.page.ComponentPath): ItemView {

            return this.liveEditWindow.getComponentByPath(path);
        }

        public selectComponent(path: api.content.page.ComponentPath): void {
            var comp = this.getComponentByPath(path);
            var element: HTMLElement = comp.getHTMLElement();
            new PageComponentSelectComponentEvent(comp, null).fire(this.liveEditWindow);
        }

        public listenToPage() {

            PageViewItemsParsedEvent.on(this.notifyPageViewItemsParsed.bind(this), this.liveEditWindow);

            NewPageComponentIdMapEvent.on((event: NewPageComponentIdMapEvent) => {
                console.log('PageComponentIdMap: %o', event.getMap());
            }, this.liveEditWindow);

            ImageOpenUploadDialogEvent.on(() => {
                var uploadDialog = new UploadDialog();
                uploadDialog.onImageUploaded((event: api.ui.ImageUploadedEvent) => {
                    new ImageUploadedEvent(event.getUploadedItem()).fire(this.liveEditWindow);
                    uploadDialog.close();
                    uploadDialog.remove();
                });
                uploadDialog.open();
            }, this.liveEditWindow);

            DraggableStartEvent.on(this.notifyDraggableStart.bind(this), this.liveEditWindow);

            DraggableStopEvent.on(this.notifyDraggableStop.bind(this), this.liveEditWindow);

            SortableStartEvent.on(this.notifySortableStart.bind(this), this.liveEditWindow);

            SortableStopEvent.on(this.notifySortableStop.bind(this), this.liveEditWindow);

            SortableUpdateEvent.on((event: SortableUpdateEvent) => {
                if (event.getComponentView()) {
                    this.notifySortableUpdate(event);
                }
            }, this.liveEditWindow);

            PageSelectEvent.on(this.notifyPageSelected.bind(this), this.liveEditWindow);

            RegionSelectEvent.on(this.notifyRegionSelected.bind(this), this.liveEditWindow);

            PageComponentSelectEvent.on((event: PageComponentSelectEvent) => {
                if (event.getPath()) {
                    this.notifyPageComponentSelected(event);
                }
            }, this.liveEditWindow);

            PageComponentDeselectEvent.on(this.notifyDeselect.bind(this), this.liveEditWindow);

            PageComponentAddedEvent.on(this.notifyPageComponentAdded.bind(this), this.liveEditWindow);

            PageComponentRemoveEvent.on(this.notifyPageComponentRemoved.bind(this), this.liveEditWindow);

            PageComponentResetEvent.on(this.notifyPageComponentReset.bind(this), this.liveEditWindow);

            PageComponentDuplicateEvent.on(this.notifyPageComponentDuplicated.bind(this), this.liveEditWindow);

            ImageComponentSetImageEvent.on((event: ImageComponentSetImageEvent) => {
                if (!event.getErrorMessage()) {
                    this.notifyImageComponentSetImage(event);
                } else {
                    api.notify.showError(event.getErrorMessage());
                }
            }, this.liveEditWindow);

            PageComponentSetDescriptorEvent.on(this.notifyPageComponentSetDescriptor.bind(this), this.liveEditWindow);

            RegionEmptyEvent.on(this.notifyRegionEmpty.bind(this), this.liveEditWindow);
        }

        onLoaded(listener: {(): void;}) {
            this.loadedListeners.push(listener);
        }

        unLoaded(listener: {(): void;}) {
            this.loadedListeners = this.loadedListeners.filter(function (curr) {
                return curr != listener;
            });
        }

        private notifyLoaded() {
            this.loadedListeners.forEach((listener) => {
                listener();
            });
        }

        onPageViewItemsParsed(listener: {(event: PageViewItemsParsedEvent): void;}) {
            this.pageViewItemsParsedListeners.push(listener);
        }

        unPageViewItemsParsed(listener: {(event: PageViewItemsParsedEvent): void;}) {
            this.pageViewItemsParsedListeners = this.pageViewItemsParsedListeners.filter((curr) => (curr != listener));
        }

        private notifyPageViewItemsParsed(event: PageViewItemsParsedEvent) {
            this.pageViewItemsParsedListeners.forEach((listener) => listener(event));
        }

        onDraggableStart(listener: {(event: DraggableStartEvent): void;}) {
            this.draggableStartListeners.push(listener);
        }

        unDraggableStart(listener: {(event: DraggableStartEvent): void;}) {
            this.draggableStartListeners = this.draggableStartListeners.filter((curr) => (curr != listener));
        }

        private notifyDraggableStart(event: DraggableStartEvent) {
            this.draggableStartListeners.forEach((listener) => listener(event));
        }

        onDraggableStop(listener: {(event: DraggableStopEvent): void;}) {
            this.draggableStopListeners.push(listener);
        }

        unDraggableStop(listener: {(event: DraggableStopEvent): void;}) {
            this.draggableStopListeners = this.draggableStopListeners.filter((curr) => (curr != listener));
        }

        private notifyDraggableStop(event: DraggableStopEvent) {
            this.draggableStopListeners.forEach((listener) => listener(event));
        }

        onSortableStart(listener: (event: SortableStartEvent) => void) {
            this.sortableStartListeners.push(listener);
        }

        unSortableStart(listener: (event: SortableStartEvent) => void) {
            this.sortableStartListeners = this.sortableStartListeners.filter((curr) => (curr != listener));
        }

        private notifySortableStart(event: SortableStartEvent) {
            this.sortableStartListeners.forEach((listener) => listener(event));
        }

        onSortableUpdate(listener: {(event: SortableUpdateEvent): void;}) {
            this.sortableUpdateListeners.push(listener);
        }

        unSortableUpdate(listener: {(event: SortableUpdateEvent): void;}) {
            this.sortableUpdateListeners = this.sortableUpdateListeners.filter((curr) => (curr != listener));
        }

        private notifySortableUpdate(event: SortableUpdateEvent) {
            this.sortableUpdateListeners.forEach((listener) => listener(event));
        }

        onSortableStop(listener: {(event: SortableStopEvent): void;}) {
            this.sortableStopListeners.push(listener);
        }

        unSortableStop(listener: {(event: SortableStopEvent): void;}) {
            this.sortableStopListeners = this.sortableStopListeners.filter((curr) => (curr != listener));
        }

        private notifySortableStop(event: SortableStopEvent) {
            this.sortableStopListeners.forEach((listener) => listener(event));
        }

        onPageSelected(listener: (event: PageSelectEvent) => void) {
            this.pageSelectedListeners.push(listener);
        }

        unPageSelected(listener: (event: PageSelectEvent) => void) {
            this.pageSelectedListeners = this.pageSelectedListeners.filter((curr) => (curr != listener));
        }

        private notifyPageSelected(event: PageSelectEvent) {
            this.pageSelectedListeners.forEach((listener) => listener(event));
        }

        onRegionSelected(listener: {(event: RegionSelectEvent): void;}) {
            this.regionSelectedListeners.push(listener);
        }

        unRegionSelected(listener: {(event: RegionSelectEvent): void;}) {
            this.regionSelectedListeners = this.regionSelectedListeners.filter((curr) => (curr != listener));
        }

        private notifyRegionSelected(event: RegionSelectEvent) {
            this.regionSelectedListeners.forEach((listener) => listener(event));
        }

        onPageComponentSelected(listener: {(event: PageComponentSelectEvent): void;}) {
            this.pageComponentSelectedListeners.push(listener);
        }

        unPageComponentSelected(listener: {(event: PageComponentSelectEvent): void;}) {
            this.pageComponentSelectedListeners = this.pageComponentSelectedListeners.filter((curr) => (curr != listener));
        }

        private notifyPageComponentSelected(event: PageComponentSelectEvent) {
            this.pageComponentSelectedListeners.forEach((listener) => listener(event));
        }

        onDeselect(listener: {(event: PageComponentDeselectEvent): void;}) {
            this.deselectListeners.push(listener);
        }

        unDeselect(listener: {(event: PageComponentDeselectEvent): void;}) {
            this.deselectListeners = this.deselectListeners.filter((curr) => (curr != listener));
        }

        private notifyDeselect(event: PageComponentDeselectEvent) {
            this.deselectListeners.forEach((listener) => listener(event));
        }

        onPageComponentAdded(listener: {(event: PageComponentAddedEvent): void;}) {
            this.pageComponentAddedListeners.push(listener);
        }

        unPageComponentAdded(listener: {(event: PageComponentAddedEvent): void;}) {
            this.pageComponentAddedListeners = this.pageComponentAddedListeners.filter((curr) => (curr != listener));
        }

        private notifyPageComponentAdded(event: PageComponentAddedEvent) {
            this.pageComponentAddedListeners.forEach((listener) => listener(event));
        }

        onImageComponentSetImage(listener: {(event: ImageComponentSetImageEvent): void;}) {
            this.imageComponentSetImageListeners.push(listener);
        }

        unImageComponentSetImage(listener: {(event: ImageComponentSetImageEvent): void;}) {
            this.imageComponentSetImageListeners = this.imageComponentSetImageListeners.filter((curr) => (curr != listener));
        }

        private notifyImageComponentSetImage(event: ImageComponentSetImageEvent) {
            this.imageComponentSetImageListeners.forEach((listener) => listener(event));
        }

        onPageComponentSetDescriptor(listener: {(event: PageComponentSetDescriptorEvent): void;}) {
            this.pageComponentSetDescriptorListeners.push(listener);
        }

        unPageComponentSetDescriptor(listener: {(event: PageComponentSetDescriptorEvent): void;}) {
            this.pageComponentSetDescriptorListeners = this.pageComponentSetDescriptorListeners.filter((curr) => (curr != listener));
        }

        private notifyPageComponentSetDescriptor(event: PageComponentSetDescriptorEvent) {
            this.pageComponentSetDescriptorListeners.forEach((listener) => listener(event));
        }

        onPageComponentReset(listener: {(event: PageComponentResetEvent): void;}) {
            this.pageComponentResetListeners.push(listener);
        }

        unPageComponentReset(listener: {(event: PageComponentResetEvent): void;}) {
            this.pageComponentResetListeners = this.pageComponentResetListeners.filter((curr) => (curr != listener));
        }

        private notifyPageComponentReset(event: PageComponentResetEvent) {
            this.pageComponentResetListeners.forEach((listener) => listener(event));
        }

        onPageComponentRemoved(listener: {(event: PageComponentRemoveEvent): void;}) {
            this.pageComponentRemovedListeners.push(listener);
        }

        unPageComponentRemoved(listener: {(event: PageComponentRemoveEvent): void;}) {
            this.pageComponentRemovedListeners = this.pageComponentRemovedListeners.filter((curr) => (curr != listener));
        }

        private notifyPageComponentRemoved(event: PageComponentRemoveEvent) {
            this.pageComponentRemovedListeners.forEach((listener) => listener(event));
        }

        onPageComponentDuplicated(listener: {(event: PageComponentDuplicateEvent): void;}) {
            this.pageComponentDuplicatedListeners.push(listener);
        }

        unPageComponentDuplicated(listener: {(event: PageComponentDuplicateEvent): void;}) {
            this.pageComponentDuplicatedListeners = this.pageComponentDuplicatedListeners.filter((curr) => (curr != listener));
        }

        private notifyPageComponentDuplicated(event: PageComponentDuplicateEvent) {
            this.pageComponentDuplicatedListeners.forEach((listener) => listener(event));
        }

        onRegionEmpty(listener: {(event: RegionEmptyEvent): void;}) {
            this.regionEmptyListeners.push(listener);
        }

        unRegionEmpty(listener: {(event: RegionEmptyEvent): void;}) {
            this.regionEmptyListeners = this.regionEmptyListeners.filter((curr) => (curr != listener));
        }

        private notifyRegionEmpty(event: RegionEmptyEvent) {
            this.regionEmptyListeners.forEach((listener) => listener(event));
        }
    }
}