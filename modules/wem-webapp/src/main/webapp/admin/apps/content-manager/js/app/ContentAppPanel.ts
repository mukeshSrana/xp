module app {

    export class ContentAppPanel extends api_app.BrowseAndWizardBasedAppPanel<api_content.ContentSummary> {

        constructor(appBar:api_app.AppBar) {

            var browsePanel = new app_browse.ContentBrowsePanel();

            super({
                appBar: appBar,
                browsePanel: browsePanel
            });

            app_new.NewContentEvent.on((event) => {
                this.handleNew(event.getContentType(), event.getParentContent());
            });

            app_browse.OpenContentEvent.on((event) => {
                this.handleOpen(event.getModels());
            });

            app_browse.EditContentEvent.on((event) => {
                this.handleEdit(event.getModels());
            });

            api_app.ShowAppBrowsePanelEvent.on((event) => {
                this.showHomePanel();
                this.getAppBarTabMenu().deselectNavigationItem();
            });

            app_browse.CloseContentEvent.on((event) => {
                this.removePanel(event.getPanel(), event.isCheckCanRemovePanel());
            });
        }

        addWizardPanel(tabMenuItem:api_app.AppBarTabMenuItem, wizardPanel:api_app_wizard.WizardPanel<api_content.Content>) {
            super.addWizardPanel(tabMenuItem, wizardPanel);

            wizardPanel.getHeader().addListener(
                {
                    onPropertyChanged: (event:api_app_wizard.WizardHeaderPropertyChangedEvent) => {
                        if (event.property == "displayName") {
                            tabMenuItem.setLabel(event.newValue);
                        }
                    }
                });
        }

        private handleNew(contentType:api_schema_content.ContentTypeSummary, parentContent:api_content.Content) {

            var tabId = api_app.AppBarTabId.forNew(contentType.getName());
            var tabMenuItem = this.getAppBarTabMenu().getNavigationItemById(tabId);

            if (tabMenuItem != null) {
                this.selectPanel(tabMenuItem);

            } else {

                var contentTypeRequest = new api_schema_content.GetContentTypeByQualifiedNameRequest(new api_schema_content.ContentTypeName(contentType.getName())).send();
                jQuery.
                    when(contentTypeRequest).
                    then((contentTypeResponse:api_rest.JsonResponse) => {

                        var newContentType = new api_schema_content.ContentType(contentTypeResponse.getJson());

                        tabMenuItem = new api_app.AppBarTabMenuItem("New " + contentType.getDisplayName(), tabId);
                        var wizardPanel = new app_wizard.ContentWizardPanel(tabId, newContentType, parentContent);
                        wizardPanel.renderNew();
                        this.addWizardPanel(tabMenuItem, wizardPanel);
                        wizardPanel.reRender();

                    });
            }
        }

        private handleOpen(contents:api_content.ContentSummary[]) {

            contents.forEach((contentModel:api_content.ContentSummary) => {

                var tabId = api_app.AppBarTabId.forView(contentModel.getId());
                var tabMenuItem = this.getAppBarTabMenu().getNavigationItemById(tabId);

                if (tabMenuItem != null) {
                    this.selectPanel(tabMenuItem);

                } else {
                    tabMenuItem = new api_app.AppBarTabMenuItem(contentModel.getDisplayName(), tabId);
                    var contentItemViewPanel = new app_view.ContentItemViewPanel({
                        showPreviewAction: app_browse.ContentBrowseActions.get().SHOW_PREVIEW,
                        showDetailsAction: app_browse.ContentBrowseActions.get().SHOW_DETAILS
                    });

                    var contentItem = new api_app_view.ViewItem(contentModel)
                        .setDisplayName(contentModel.getDisplayName())
                        .setPath(contentModel.getPath().toString())
                        .setIconUrl(contentModel.getIconUrl());

                    contentItemViewPanel.setItem(contentItem);

                    this.addViewPanel(tabMenuItem, contentItemViewPanel);
                }
            });
        }

        private handleEdit(contents:api_content.ContentSummary[]) {

            contents.forEach((content:api_content.ContentSummary) => {

                var tabId = api_app.AppBarTabId.forEdit(content.getId());
                var tabMenuItem = this.getAppBarTabMenu().getNavigationItemById(tabId);

                if (tabMenuItem != null) {
                    this.selectPanel(tabMenuItem);

                } else {


                    var getContentByIdPromise = new api_content.GetContentByIdRequest(content.getId()).send();
                    var getContentTypeByQualifiedNamePromise = new api_schema_content.GetContentTypeByQualifiedNameRequest(new api_schema_content.ContentTypeName(content.getType())).send();
                    jQuery.
                        when(getContentByIdPromise, getContentTypeByQualifiedNamePromise).
                        then((contentResponse:api_rest.JsonResponse<api_content_json.ContentJson>, contentTypeResponse:api_rest.JsonResponse) => {

                            var contentToEdit:api_content.Content = new api_content.Content(contentResponse.getResult());
                            var contentType:api_schema_content.ContentType = new api_schema_content.ContentType(<api_schema_content_json.ContentTypeJson>contentTypeResponse.getJson());
                            tabMenuItem = new api_app.AppBarTabMenuItem(contentToEdit.getDisplayName(), tabId, true);

                            if (contentToEdit.getPath().hasParent()) {
                                new api_content.GetContentByPathRequest(contentToEdit.getPath().getParentPath()).send().
                                    done((parentContentResponse:api_rest.JsonResponse<api_content_json.ContentJson>) => {
                                        var parentContent:api_content.Content = new api_content.Content(parentContentResponse.getResult());

                                        var contentWizardPanel = new app_wizard.ContentWizardPanel(tabId, contentType, parentContent);

                                        contentWizardPanel.setPersistedItem(contentToEdit);
                                        this.addWizardPanel(tabMenuItem, contentWizardPanel);
                                    });
                            }
                            else {
                                var contentWizardPanel = new app_wizard.ContentWizardPanel(tabId, contentType, null);
                                contentWizardPanel.setPersistedItem(contentToEdit);
                                this.addWizardPanel(tabMenuItem, contentWizardPanel);
                            }
                        });

                }
            });
        }
    }

}
