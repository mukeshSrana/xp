module app_wizard {

    export class ContentWizardPanel extends api_app_wizard.WizardPanel<api_content.Content> {

        public static NEW_WIZARD_HEADER = "New Content";

        private static DEFAULT_CONTENT_ICON_URL:string = api_util.getAdminUri("resources/images/default_content.png");

        private persistedContent:api_content.Content;

        private parentContent:api_content.Content;

        private renderingNew:boolean;

        private contentType:api_schema_content.ContentType;

        private formIcon:api_app_wizard.FormIcon;

        private contentWizardHeader:api_app_wizard.WizardHeaderWithDisplayNameAndName;

        private contentForm:ContentForm;

        private schemaPanel:api_ui.Panel;

        private modulesPanel:api_ui.Panel;

        private templatesPanel:api_ui.Panel;

        private iconUploadId:string;

        constructor(contentType:api_schema_content.ContentType, parentContent:api_content.Content) {

            this.parentContent = parentContent;
            this.contentType = contentType;
            this.contentWizardHeader = new api_app_wizard.WizardHeaderWithDisplayNameAndName();
            this.formIcon = new api_app_wizard.FormIcon(ContentWizardPanel.DEFAULT_CONTENT_ICON_URL, "Click to upload icon",
                api_util.getRestUri("upload"));

            this.formIcon.addListener({

                onUploadFinished: (uploadId:string, mimeType:string, uploadName:string) => {

                    this.iconUploadId = uploadId;

                    this.formIcon.setSrc(api_util.getRestUri('upload/' + uploadId));
                }
            });

            var actions = new ContentWizardActions(this);

            var toolbar = new ContentWizardToolbar({
                saveAction: actions.getSaveAction(),
                duplicateAction: actions.getDuplicateAction(),
                deleteAction: actions.getDeleteAction(),
                closeAction: actions.getCloseAction()
            });

            var livePanel = new LiveFormPanel();

            super({
                formIcon: this.formIcon,
                toolbar: toolbar,
                header: this.contentWizardHeader,
                actions: actions,
                livePanel: livePanel
            });

            this.contentWizardHeader.setDisplayName(ContentWizardPanel.NEW_WIZARD_HEADER);
            this.contentWizardHeader.setName(this.contentWizardHeader.generateName(ContentWizardPanel.NEW_WIZARD_HEADER));
            this.contentWizardHeader.setAutogenerateDisplayName(true);
            this.contentWizardHeader.setAutogenerateName(true);

            console.log("ContentWizardPanel this.contentType: ", this.contentType);
            this.contentForm = new ContentForm(this.contentType.getForm());

            this.schemaPanel = new api_ui.Panel("schemaPanel");
            var h1El = new api_dom.H1El();
            h1El.getEl().setInnerHtml("TODO: schema");
            this.schemaPanel.appendChild(h1El);

            this.modulesPanel = new api_ui.Panel("modulesPanel");
            h1El = new api_dom.H1El();
            h1El.getEl().setInnerHtml("TODO: modules");
            this.modulesPanel.appendChild(h1El);

            this.templatesPanel = new api_ui.Panel("templatesPanel");
            h1El = new api_dom.H1El();
            h1El.getEl().setInnerHtml("TODO: templates");
            this.templatesPanel.appendChild(h1El);

            this.addStep(new api_app_wizard.WizardStep(contentType.getDisplayName()), this.contentForm);
            this.addStep(new api_app_wizard.WizardStep("Schemas"), this.schemaPanel);
            this.addStep(new api_app_wizard.WizardStep("Modules"), this.modulesPanel);
            this.addStep(new api_app_wizard.WizardStep("Templates"), this.templatesPanel);

            ShowContentLiveEvent.on((event) => {
                this.toggleFormPanel(false);
            });

            ShowContentFormEvent.on((event) => {
                this.toggleFormPanel(true);
            });
        }

        showCallback() {
            if(this.persistedContent) {
                app.Router.setHash("edit/" + this.persistedContent.getId());
            } else {
                app.Router.setHash("new/" + this.contentType.getQualifiedName());
            }
            super.showCallback();
        }

        renderNew() {
            super.renderNew();
            this.contentForm.renderNew();
            this.renderingNew = true;
        }

        setPersistedItem(content:api_content.Content) {
            super.setPersistedItem(content);
            this.persistedContent = content;
            this.renderingNew = false;

            this.contentWizardHeader.setDisplayName(content.getDisplayName());
            this.contentWizardHeader.setName(content.getName());
            this.formIcon.setSrc(content.getIconUrl());

            // setup displayName and name to be generated automatically
            // if corresponding values are empty
            this.contentWizardHeader.setAutogenerateDisplayName(!content.getDisplayName());
            this.contentWizardHeader.setAutogenerateName(!content.getName());

            console.log("ContentWizardPanel.renderExisting contentData: ", content.getContentData());

            var contentData:api_content.ContentData = content.getContentData();

            this.contentForm.renderExisting(contentData);
        }

        persistNewItem(successCallback?:(contentId:string, contentPath:string) => void) {

            var createRequest = new api_content.CreateContentRequest().
                setContentName(this.contentWizardHeader.getName()).
                setParentContentPath(this.parentContent.getPath().toString()).
                setContentType(this.contentType.getQualifiedName()).
                setDisplayName(this.contentWizardHeader.getDisplayName()).
                setContentData(this.contentForm.getContentData());

                if(this.iconUploadId) {
                    createRequest.setAttachments([
                        {
                            uploadId: this.iconUploadId,
                            attachmentName: '_thumb.png'
                        }
                    ])
                }

                createRequest.send().done((createResponse:api_rest.JsonResponse<any>) => {

                    api_notify.showFeedback('Content was created!');
                    console.log('content create response', createResponse);

                    var json = createResponse.getJson();
                    new api_content.ContentCreatedEvent(api_content.ContentPath.fromString(json.contentPath)).fire();

                    if (successCallback) {
                        successCallback.call(this, json.contentId, json.contentPath);
                    }
                });
        }

        updatePersistedItem(successCallback?:() => void) {

            var updateRequest = new api_content.UpdateContentRequest(this.persistedContent.getId()).
                setContentName(this.contentWizardHeader.getName()).
                setContentType(this.contentType.getQualifiedName()).
                setDisplayName(this.contentWizardHeader.getDisplayName()).
                setContentData(this.contentForm.getContentData());

            if(this.iconUploadId) {
                updateRequest.setAttachments([
                    {
                        uploadId: this.iconUploadId,
                        attachmentName: '_thumb.png'
                    }
                ])
            }

            updateRequest.send().done((updateResponse:api_rest.JsonResponse<any>) => {
                api_notify.showFeedback('Content was updated!');
                console.log('content update response', updateResponse);

                new api_content.ContentUpdatedEvent(this.persistedContent).fire();

                if (successCallback) {
                    successCallback.call(this);
                }
            });
        }
    }

    class LiveFormPanel extends api_ui.Panel {

        private frame:api_dom.IFrameEl;

        constructor(url:string = "../../../dev/live-edit-page/bootstrap.jsp?edit=true") {
            super("LiveFormPanel");
            this.addClass("live-form-panel");

            this.frame = new api_dom.IFrameEl();
            this.frame.addClass("live-edit-frame");
            this.frame.setSrc(url);
            this.appendChild(this.frame);

            // Wait for iframe to be loaded before adding context window!
            var intervalId = setInterval(() => {
                if (this.frame.isLoaded()) {
                    var contextWindow = new app_contextwindow.ContextWindow({liveEditEl: this.frame});
                    this.appendChild(contextWindow);
                    clearInterval(intervalId);
                }
            }, 200);

        }

    }
}