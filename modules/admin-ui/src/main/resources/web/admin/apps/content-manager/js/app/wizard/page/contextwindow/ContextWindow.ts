module app.wizard.page.contextwindow {

    import LiveFormPanel = app.wizard.page.LiveFormPanel;
    import Content = api.content.Content;
    import PageTemplateKey = api.content.page.PageTemplateKey;
    import PageTemplate = api.content.page.PageTemplate;
    import PageDescriptor = api.content.page.PageDescriptor;
    import Region = api.content.page.region.Region;
    import ImageComponent = api.content.page.region.ImageComponent;
    import ImageComponentBuilder = api.content.page.region.ImageComponentBuilder;
    import ResponsiveManager = api.ui.responsive.ResponsiveManager;
    import ResponsiveItem = api.ui.responsive.ResponsiveItem;
    import BaseInspectionPanel = app.wizard.page.contextwindow.inspect.BaseInspectionPanel;
    import InspectionsPanel = app.wizard.page.contextwindow.inspect.InspectionsPanel;
    import InsertablesPanel = app.wizard.page.contextwindow.insert.InsertablesPanel;

    export interface ContextWindowConfig {

        liveFormPanel:LiveFormPanel;

        inspectionPanel:InspectionsPanel;

        emulatorPanel:EmulatorPanel;

        insertablesPanel:InsertablesPanel;
    }

    export class ContextWindow extends api.ui.panel.DockedPanel {

        private insertablesPanel: InsertablesPanel;

        private inspectionsPanel: InspectionsPanel;

        private emulatorPanel: EmulatorPanel;

        private liveFormPanel: LiveFormPanel;

        private shown: boolean;

        private splitter: api.dom.DivEl;

        private ghostDragger: api.dom.DivEl;

        private mask: api.ui.mask.DragMask;

        private actualWidth: number;

        private minWidth: number = 280;

        private parentMinWidth: number = 15;

        private displayModeChangedListeners: {() : void}[] = [];

        constructor(config: ContextWindowConfig) {
            super();

            this.liveFormPanel = config.liveFormPanel;
            this.inspectionsPanel = config.inspectionPanel;
            this.emulatorPanel = config.emulatorPanel;
            this.insertablesPanel = config.insertablesPanel;

            this.addClass("context-window");

            this.ghostDragger = new api.dom.DivEl("ghost-dragger");
            this.splitter = new api.dom.DivEl("splitter");

            ResponsiveManager.onAvailableSizeChanged(this, (item: ResponsiveItem) => {
                this.updateFrameSize();
            });
            ResponsiveManager.onAvailableSizeChanged(this.liveFormPanel, (item: ResponsiveItem) => {
                this.updateFrameSize();
            });

            this.appendChild(this.splitter);
            this.addItem("Insert", this.insertablesPanel);
            this.addItem("Settings", this.inspectionsPanel);
            this.addItem("Emulator", this.emulatorPanel);

            this.onRendered(() => this.initializeResizable());

            this.onRemoved((event) => {
                ResponsiveManager.unAvailableSizeChanged(this);
                ResponsiveManager.unAvailableSizeChanged(this.liveFormPanel);
            });
        }

        private initializeResizable() {
            var initialPos = 0;
            var splitterPosition = 0;
            var parent = this.getParentElement();
            this.actualWidth = this.getEl().getWidth();
            this.mask = new api.ui.mask.DragMask(parent);

            var dragListener = (e: MouseEvent) => {
                if (this.splitterWithinBoundaries(initialPos - e.clientX)) {
                    splitterPosition = e.clientX;
                    this.ghostDragger.getEl().setLeftPx(e.clientX - this.getEl().getOffsetLeft());
                }
            };

            this.splitter.onMouseDown((e: MouseEvent) => {
                e.preventDefault();
                initialPos = e.clientX;
                splitterPosition = e.clientX;
                this.startDrag(dragListener);
            });

            this.mask.onMouseUp((e: MouseEvent) => {
                this.actualWidth = this.getEl().getWidth() + initialPos - splitterPosition;
                this.stopDrag(dragListener);
                ResponsiveManager.fireResizeEvent();
            });

            app.wizard.ShowLiveEditEvent.on(() => {
                this.show();
            })
        }

        private splitterWithinBoundaries(offset: number) {
            var newWidth = this.actualWidth + offset;
            return (newWidth >= this.minWidth) && (newWidth <= this.getParentElement().getEl().getWidth() - this.parentMinWidth);
        }

        private startDrag(dragListener: {(e: MouseEvent):void}) {
            this.mask.show();
            this.mask.onMouseMove(dragListener);
            this.ghostDragger.insertBeforeEl(this.splitter);
            this.ghostDragger.getEl().setLeftPx(this.splitter.getEl().getOffsetLeftRelativeToParent()).setTop(null);
        }

        private stopDrag(dragListener: {(e: MouseEvent):void}) {
            this.getEl().setWidthPx(this.actualWidth);
            this.mask.unMouseMove(dragListener);
            this.mask.hide();
            this.removeChild(this.ghostDragger);
        }

        isShown() {
            return this.shown;
        }

        slideOut() {
            this.getEl().addClass("hidden").setRight(-this.getEl().getWidthWithBorder() + "px");
            this.shown = false;
            this.updateFrameSize();
        }

        slideIn() {
            this.getEl().removeClass("hidden").setRight("0px");
            this.shown = true;
            this.updateFrameSize();
        }

        public showInspectionPanel(panel: BaseInspectionPanel) {
            this.inspectionsPanel.showInspectionPanel(panel);
            this.selectPanel(this.inspectionsPanel);
        }

        public clearSelection() {
            this.inspectionsPanel.clearSelection();
            this.selectPanel(this.insertablesPanel);
        }

        private updateFrameSize() {
            var contextWindowWidth = this.actualWidth || this.getEl().getWidth(),
                displayModeChanged = this.hasClass('floating') && !this.isFloating();
            this.liveFormPanel.updateFrameContainerSize(!this.isFloating() && this.shown, contextWindowWidth);

            this.isFloating() ? this.addClass("floating") : this.removeClass("floating");
            if (displayModeChanged) {
                this.notifyDisplayModeChanged();
            }
        }

        isFloating(): boolean {
            var contextWindowWidth = this.actualWidth || this.getEl().getWidth();
            var liveFormPanelWidth = this.liveFormPanel.getEl().getWidth();
            return (liveFormPanelWidth < 1380) || ((liveFormPanelWidth - contextWindowWidth) < 960);
        }

        notifyDisplayModeChanged() {
            this.displayModeChangedListeners.forEach((listener: ()=> void) => listener());
        }

        onDisplayModeChanged(listener: () => void) {
            this.displayModeChangedListeners.push(listener);
        }

        unDisplayModeChanged(listener: () => void) {
            this.displayModeChangedListeners.filter((currentListener: () => void) => {
                return listener == currentListener;
            });
        }

    }
}