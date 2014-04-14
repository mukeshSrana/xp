module api.schema.content {

    export class ContentTypeSummaryViewer extends api.ui.Viewer<ContentTypeSummary> {

        private namesAndIconView: api.app.NamesAndIconView;

        constructor() {
            super();
            this.namesAndIconView = new api.app.NamesAndIconViewBuilder().setSize(api.app.NamesAndIconViewSize.small).build();
            this.appendChild(this.namesAndIconView);
        }

        setObject(contentType: ContentTypeSummary) {
            super.setObject(contentType);
            this.namesAndIconView.setMainName(contentType.getDisplayName()).
                setSubName(contentType.getKey()).
                setIconUrl(contentType.getIconUrl());
        }

        getPreferredHeight(): number {
            return 50;
        }
    }
}