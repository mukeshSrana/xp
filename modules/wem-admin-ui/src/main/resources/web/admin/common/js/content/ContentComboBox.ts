module api.content {

    export class ContentComboBox extends api.ui.selector.combobox.RichComboBox<api.content.ContentSummary> {

        constructor(contentComboBoxBuilder: ContentComboBoxBuilder) {
            var builder: api.ui.selector.combobox.RichComboBoxBuilder<api.content.ContentSummary> = new api.ui.selector.combobox.RichComboBoxBuilder<api.content.ContentSummary>();
            builder
                .setComboBoxName(contentComboBoxBuilder.name ? contentComboBoxBuilder.name : 'contentSelector')
                .setLoader(contentComboBoxBuilder.loader ? contentComboBoxBuilder.loader : new ContentSummaryLoader())
                .setSelectedOptionsView(new ContentSelectedOptionsView())
                .setMaximumOccurrences(contentComboBoxBuilder.maximumOccurrences)
                .setOptionDisplayValueViewer(new api.content.ContentSummaryViewer())
                .setDelayedInputValueChangedHandling(500);

            super(builder);

            if (contentComboBoxBuilder.allowedContentTypes) {
                var loader = <ContentSummaryLoader>this.getLoader();
                loader.setAllowedContentTypes(contentComboBoxBuilder.allowedContentTypes);
            }
        }
    }

    export class ContentSelectedOptionsView extends api.ui.selector.combobox.SelectedOptionsView<api.content.ContentSummary> {

        createSelectedOption(option: api.ui.selector.Option<api.content.ContentSummary>,
                             index: number): api.ui.selector.combobox.SelectedOption<api.content.ContentSummary> {
            var optionView = new ContentSelectedOptionView(option);
            return new api.ui.selector.combobox.SelectedOption<api.content.ContentSummary>(optionView, option, index);
        }
    }

    export class ContentSelectedOptionView extends api.ui.selector.combobox.RichSelectedOptionView<api.content.ContentSummary> {


        constructor(option: api.ui.selector.Option<api.content.ContentSummary>) {
            super(option);
        }

        resolveIconUrl(content: api.content.ContentSummary): string {
            return content.getIconUrl();
        }

        resolveTitle(content: api.content.ContentSummary): string {
            return content.getDisplayName().toString();
        }

        resolveSubTitle(content: api.content.ContentSummary): string {
            return content.getPath().toString();
        }

    }

    export class ContentComboBoxBuilder {

        name: string;

        maximumOccurrences: number = 0;

        loader: api.util.loader.BaseLoader<api.schema.SchemaJson, api.content.ContentSummary>;

        allowedContentTypes: string[];

        setName(value: string): ContentComboBoxBuilder {
            this.name = value;
            return this;
        }

        setMaximumOccurrences(maximumOccurrences: number): ContentComboBoxBuilder {
            this.maximumOccurrences = maximumOccurrences;
            return this;
        }

        setLoader(loader: api.util.loader.BaseLoader<api.schema.SchemaJson, api.content.ContentSummary>): ContentComboBoxBuilder {
            this.loader = loader;
            return this;
        }

        setAllowedContentTypes(allowedTypes: string[]): ContentComboBoxBuilder {
            this.allowedContentTypes = allowedTypes;
            return this;
        }

        build(): ContentComboBox {
            return new ContentComboBox(this);
        }

    }
}