module app.publish {

    import ContentIconUrlResolver = api.content.ContentIconUrlResolver;
    import BrowseItem = api.app.browse.BrowseItem;
    import ContentPath = api.content.ContentPath;
    import SelectionItem = api.app.browse.SelectionItem;
    import ContentSummary = api.content.ContentSummary;
    import DialogButton = api.ui.dialog.DialogButton;
    import PublishContentRequest = api.content.PublishContentRequest;

    export class ContentPublishDialog extends api.ui.dialog.ModalDialog {

        private modelName: string;

        private selectedItems: SelectionItem<ContentSummary>[];

        private publishButton: DialogButton;

        private publishAction: api.ui.Action;

        private itemList: PublishDialogItemList = new PublishDialogItemList();

        private includeChildItemsCheck: api.ui.Checkbox;

        constructor() {
            super({
                title: new api.ui.dialog.ModalDialogHeader("Publishing Wizard")
            });

            this.modelName = "item";

            this.getEl().addClass("publish-dialog");
            this.appendChildToContentPanel(this.itemList);

            var descMessage = new api.dom.H6El().addClass("publish-dialog-subheader").
                setHtml("Based on your <b>selection</b> - we found <b>(x) dependent</b> changes");
            this.appendChildToTitle(descMessage);

            this.publishButton = this.setPublishAction(new ContentPublishDialogAction());

            this.getPublishAction().onExecuted(() => {
                this.doPublish();
            });

            this.addCancelButtonToBottom();

            this.includeChildItemsCheck = new api.ui.Checkbox().setLabel('Include child items');
            this.includeChildItemsCheck.addClass('include-child-check');
            this.includeChildItemsCheck.onValueChanged(() => {
                this.countItemsToPublishAndUpdateButtonCounter();
            });
            this.appendChildToContentPanel(this.includeChildItemsCheck);

        }

        show() {
            api.dom.Body.get().appendChild(this);
            super.show();
        }

        close() {
            super.close();
            this.remove();
        }

        setPublishAction(action: api.ui.Action): DialogButton {
            this.publishAction = action;
            return this.addAction(action, true, true);
        }

        getPublishAction(): api.ui.Action {
            return this.publishAction;
        }

        renderSelectedItems(selectedItems: SelectionItem<ContentSummary>[]) {
            this.itemList.clear();

            for (var i in selectedItems) {
                var selectionItem: SelectionItem<ContentSummary> = selectedItems[i];
                this.itemList.appendChild(selectionItem);
            }
        }


        setContentToPublish(contents: ContentSummary[]) {
            this.selectedItems = [];

            contents.forEach((content: ContentSummary) => {
                this.selectedItems.push(this.createSelectionItemForPublish(content));
            });

            this.renderSelectedItems(this.selectedItems);

            this.countItemsToPublishAndUpdateButtonCounter();
        }

        private indexOf(item: SelectionItem<ContentSummary>): number {
            for (var i = 0; i < this.selectedItems.length; i++) {
                if (item.getBrowseItem().getPath() == this.selectedItems[i].getBrowseItem().getPath()) {
                    return i;
                }
            }
            return -1;
        }

        private createSelectionItemForPublish(content: ContentSummary): SelectionItem<ContentSummary> {

            var publishItemViewer = new api.content.ContentSummaryViewer();
            publishItemViewer.setObject(content);

            var browseItem = new BrowseItem<ContentSummary>(content).
                setId(content.getId()).
                setDisplayName(content.getDisplayName()).
                setPath(content.getPath().toString()).
                setIconUrl(new ContentIconUrlResolver().setContent(content).resolve());

            var selectionItem = new SelectionItem(publishItemViewer, browseItem, () => {
                var index = this.indexOf(selectionItem);
                if (index < 0) {
                    return;
                }

                this.selectedItems[index].remove();
                this.selectedItems.splice(index, 1);

                if (this.selectedItems.length == 0) {
                    this.close();
                }
                else {
                    this.countItemsToPublishAndUpdateButtonCounter();
                }
            });

            return selectionItem;
        }

        private countItemsToPublishAndUpdateButtonCounter() {
            this.cleanPublishButtonText();
            this.showLoadingSpinner();

            this.createRequestForCountingItemsToPublish().sendAndParse().then((itemsToPublishCounter: number) => {
                this.hideLoadingSpinner();
                this.updatePublishButtonCounter(itemsToPublishCounter);
            }).finally(() => {
                this.hideLoadingSpinner();
            }).done();
        }


        private createRequestForCountingItemsToPublish(): api.content.CountContentsWithDescendantsRequest {
            var countContentChildrenRequest = new api.content.CountContentsWithDescendantsRequest();
            for (var j = 0; j < this.selectedItems.length; j++) {
                countContentChildrenRequest.addContentPath(ContentPath.fromString(this.selectedItems[j].getBrowseItem().getPath()));
            }

            return countContentChildrenRequest;
        }

        private doPublish() {
            new PublishContentRequest().setIds(this.selectedItems.map((el) => {
                return new api.content.ContentId(el.getBrowseItem().getId());
            })).send().done((jsonResponse: api.rest.JsonResponse<api.content.PublishContentResult>) => {
                this.close();
                PublishContentRequest.feedback(jsonResponse);
            }).catch((reason: any) => {
                this.close();
                if (reason && reason.message) {
                    api.notify.showError(reason.message);
                } else {
                    api.notify.showError('Content could not be published.');
                }
            }).done();
        }

        private updatePublishButtonCounter(count: number) {
            this.publishButton.setLabel("Publish Now (" + count + ")");
        }

        private showLoadingSpinner() {
            this.publishButton.addClass("spinner");
        }

        private hideLoadingSpinner() {
            this.publishButton.removeClass("spinner");
        }

        private cleanPublishButtonText() {
            this.publishButton.setLabel("Publish Now");
        }
    }

    export class PublishDialogItemList extends api.dom.DivEl {
        constructor() {
            super();
            this.getEl().addClass("item-list");
        }

        clear() {
            this.removeChildren();
        }
    }

    export class ContentPublishDialogAction extends api.ui.Action {
        constructor() {
            super("Publish", "enter");
            this.setIconClass("publish-action");
        }
    }
}