module app.view.widget {

    import ResponsiveManager = api.ui.responsive.ResponsiveManager;
    import ViewItem = api.app.view.ViewItem;
    import ContentSummary = api.content.ContentSummary;
    import Widget = api.content.Widget;

    export class WidgetsPanel extends api.ui.panel.Panel {

        private widgetViews: WidgetView[] = [];
        private labelEl: api.dom.SpanEl;
        private widgetsContainer: api.dom.DivEl = new api.dom.DivEl("widgets-container");
        private animationTimer;

        private splitter: api.dom.DivEl;
        private ghostDragger: api.dom.DivEl;
        private mask: api.ui.mask.DragMask;

        private actualWidth: number;
        private minWidth: number = 280;
        private parentMinWidth: number = 15;

        private sizeChangedListeners: {() : void}[] = [];

        private versionsPanel: ContentItemVersionsPanel;
        private item: ViewItem<ContentSummary>;

        private useNameLabel: boolean;
        private slideRight: boolean;

        constructor(useNameLabel: boolean = true, slideRight: boolean = true, name?: string) {
            super("widgets-panel");
            this.setDoOffset(false);
            this.slideRight = slideRight;

            this.versionsPanel = new ContentItemVersionsPanel();
            this.ghostDragger = new api.dom.DivEl("ghost-dragger");
            this.splitter = new api.dom.DivEl("splitter");
            this.appendChild(this.splitter);

            this.onRendered(() => this.onRenderedHandler());

            this.initNameLabel(useNameLabel, name);

            this.appendChild(this.widgetsContainer)
        }

        private initNameLabel(useNameLabel: boolean, name: string) {
            this.useNameLabel = useNameLabel;

            if (useNameLabel) {
                this.labelEl = new api.dom.SpanEl("widgets-panel-label");
                if (name) {
                    this.labelEl.setHtml(name);
                }
                this.appendChild(this.labelEl);
            }
        }

        public setItem(item: ViewItem<ContentSummary>) {

            if (!this.item || (this.item && !this.item.equals(item))) {
                this.item = item;
                this.initWidgetsForItem();
                this.versionsPanel.setItem(item);
            }
        }

        private getWidgetsInterfaceName(): string {
            return "com.enonic.xp.content-manager.context-widget";
        }

        private initWidgetsForItem() {
            this.removeWidgets();

            this.setName(this.item.getDisplayName());

            this.initCommonWidgetsViews();
            this.getAndInitCustomWidgetsViews();

            this.onPanelSizeChanged(() => {
                this.versionsPanel.ReRenderActivePanel();
            });

        }

        private initCommonWidgetsViews() {
            var versionsWidget = new WidgetView("Version history");
            versionsWidget.setWidgetContents(this.versionsPanel);
            this.addWidgets([versionsWidget]);
        }

        private getAndInitCustomWidgetsViews() {
            var getWidgetsByInterfaceRequest = new api.content.GetWidgetsByInterfaceRequest(this.getWidgetsInterfaceName());

            return getWidgetsByInterfaceRequest.sendAndParse().then((widgets: api.content.Widget[]) => {
                widgets.forEach((widget) => {
                    this.addWidget(WidgetView.fromWidget(widget, this.item));
                })
            }).catch((reason: any) => {
                if (reason && reason.message) {
                    //api.notify.showError(reason.message);
                } else {
                    //api.notify.showError('Could not load widget descriptors.');
                }
            }).done();
        }

        private onRenderedHandler() {
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
                this.ghostDragger.insertBeforeEl(this.splitter);
                this.startDrag(dragListener);
            });

            this.mask.onMouseUp((e: MouseEvent) => {
                if (this.ghostDragger.getHTMLElement().parentNode) {
                    this.actualWidth = this.getEl().getWidth() + initialPos - splitterPosition;
                    this.stopDrag(dragListener);
                    this.removeChild(this.ghostDragger);
                    ResponsiveManager.fireResizeEvent();
                }
            });
        }

        private splitterWithinBoundaries(offset: number) {
            var newWidth = this.actualWidth + offset;
            return (newWidth >= this.minWidth) && (newWidth <= this.getParentElement().getEl().getWidth() - this.parentMinWidth);
        }

        private startDrag(dragListener: {(e: MouseEvent):void}) {
            this.mask.show();
            this.addClass("dragging");
            this.mask.onMouseMove(dragListener);
            this.ghostDragger.getEl().setLeftPx(this.splitter.getEl().getOffsetLeftRelativeToParent()).setTop(null);
        }

        private stopDrag(dragListener: {(e: MouseEvent):void}) {
            this.getEl().setWidthPx(this.actualWidth);
            this.removeClass("dragging");

            this.callWithTimeout(() => {
                this.notifyPanelSizeChanged();
            }, 800); //delay is required due to animation time

            this.mask.hide();
            this.mask.unMouseMove(dragListener);
        }

        private removeWidgets() {
            this.widgetsContainer.removeChildren();
        }

        private addWidget(widget: WidgetView) {
            this.widgetViews.push(widget);
            this.widgetsContainer.appendChild(widget);
        }

        private addWidgets(widgetViews: WidgetView[]) {
            widgetViews.forEach((widget) => {
                this.addWidget(widget);
            })
        }

        setName(name: string) {
            if (this.useNameLabel) {
                this.labelEl.setHtml(name);
            }
        }

        slideOut() {
            if (this.slideRight) {
                this.getEl().setRightPx(-this.getEl().getWidthWithBorder());
            } else {
                this.getEl().setLeftPx(-this.getEl().getWidthWithBorder());
            }
        }

        slideIn() {
            if (this.slideRight) {
                this.getEl().setRightPx(0);
            } else {
                this.getEl().setLeftPx(0);
            }
        }

        notifyPanelSizeChanged() {
            this.sizeChangedListeners.forEach((listener: ()=> void) => listener());
        }

        onPanelSizeChanged(listener: () => void) {
            this.sizeChangedListeners.push(listener);
        }

        unPanelSizeChanged(listener: () => void) {
            this.sizeChangedListeners.filter((currentListener: () => void) => {
                return listener == currentListener;
            });
        }

        callWithTimeout(callback: () => void, timer: number) {

            if (this.animationTimer) {
                clearTimeout(this.animationTimer);
            }
            this.animationTimer = setTimeout(() => {
                callback();
                this.animationTimer = null
            }, timer);
        }
    }

    export class WidgetsPanelToggleButton extends api.dom.DivEl {

        private widgetsPanel: WidgetsPanel;

        constructor(widgetsPanel: WidgetsPanel, className?: string) {
            super("widget-panel-toggle-button" + (className ? " " + className : ""));

            this.widgetsPanel = widgetsPanel;

            this.onClicked((event) => {
                this.toggleClass("expanded");
                if (this.hasClass("expanded")) {
                    this.widgetsPanel.slideIn();
                } else {
                    this.widgetsPanel.slideOut();
                }
            });
        }

    }

    export class MobileWidgetsPanelToggleButton extends WidgetsPanelToggleButton {

        constructor(widgetsPanel: WidgetsPanel) {
            super(widgetsPanel, "mobile-widget-panel-toggle-button");
        }

    }
}
