module app.contextwindow {

    import SiteTemplate = api.content.site.template.SiteTemplate;

    export interface DetailPanelConfig {
        siteTemplate:SiteTemplate;
    }

    export class DetailPanel extends api.ui.Panel {
        private siteTemplate: SiteTemplate;
        private nameAndIcon:api.app.NamesAndIconView;

        constructor(config: DetailPanelConfig) {
            super("detail-panel");

            this.siteTemplate = config.siteTemplate;

            this.initElements();
            this.setEmpty();

            SelectComponentEvent.on((event) => {
                this.setName(event.getComponent().name);
                this.setType(event.getComponent().componentType.typeName);
                this.setIcon(event.getComponent().componentType.iconCls);
            });

            ComponentDeselectEvent.on((event) => {
                this.setEmpty();
            });

        }

        private initElements() {
            this.nameAndIcon = new api.app.NamesAndIconView(new api.app.NamesAndIconViewBuilder().setSize(api.app.NamesAndIconViewSize.medium));

            var templateBox = new api.content.page.TemplateComboBox();
            templateBox.setLoader(new api.content.page.image.ImageTemplateSummaryLoader(this.siteTemplate.getKey()));
            templateBox.addLoadedListener((modules) => {
                console.log("modules", modules);
                templateBox.setValue(this.siteTemplate.getDefaultImageTemplate().toString());
            });



            var templateHeader = new api.dom.H6El();
            templateHeader.setText("Template");
            templateHeader.addClass("template-header");

            this.appendChild(this.nameAndIcon);

            this.appendChild(templateHeader);
            this.appendChild(templateBox);
        }

        private setEmpty() {
            this.nameAndIcon.setMainName("Empty");
            this.nameAndIcon.setMainName("No component selected");
            this.nameAndIcon.setIconUrl("");
        }

        setIcon(iconCls: string) {
            this.nameAndIcon.setIconClass(iconCls);
        }

        setName(name: string) {
            this.nameAndIcon.setMainName(name);
        }

        setType(type: string) {
            this.nameAndIcon.setSubName(type);
        }
    }
}