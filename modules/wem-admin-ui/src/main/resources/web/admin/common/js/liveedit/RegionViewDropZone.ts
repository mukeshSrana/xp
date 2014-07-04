module api.liveedit {

    import PageComponent = api.content.page.PageComponent;

    export class RegionViewDropZoneBuilder {

        itemType: ItemType;

        dropAllowed: boolean = true;

        text: string = "Drop %COMPONENT% here";

        regionView: RegionView;

        pageComponentView: PageComponentView<PageComponent>;

        setItemType(value: ItemType): RegionViewDropZoneBuilder {
            this.itemType = value;
            return this;
        }

        setDropAllowed(value: boolean): RegionViewDropZoneBuilder {
            this.dropAllowed = value;
            return this;
        }

        setText(value: string): RegionViewDropZoneBuilder {
            this.text = value;
            return this;
        }

        setRegionView(value: RegionView): RegionViewDropZoneBuilder {
            this.regionView = value;
            return this;
        }

        setPageComponentView(value: PageComponentView<PageComponent>): RegionViewDropZoneBuilder {
            this.pageComponentView = value;
            this.itemType = value.getType();
            return this;
        }

        build(): RegionViewDropZone {
            return new RegionViewDropZone(this);
        }
    }

    export class RegionViewDropZone extends api.dom.DivEl {

        private itemType: ItemType;

        private dropAllowed: boolean;

        private text: string;

        private regionView: RegionView;

        private pageComponentView: PageComponentView<PageComponent>;

        constructor(builder: RegionViewDropZoneBuilder) {
            super("item-view-drop-zone");
            this.itemType = builder.itemType;
            this.dropAllowed = builder.dropAllowed;
            this.text = api.util.replaceTokens(builder.text, {
                "%COMPONENT%": api.util.capitalize(this.itemType.getShortName())
            });
            this.regionView = builder.regionView;
            this.pageComponentView = builder.pageComponentView;

            if (this.regionView.countNonMovingPageComponentViews() == 0) {
                this.getEl().setPaddingTop("28px");
                this.getEl().setPaddingBottom("28px");
                this.getEl().setPaddingLeft("10px");
                this.getEl().setPaddingRight("10px");
            }

            var innerHtml = "";

            if (this.dropAllowed) {
                innerHtml += this.text + ' ';
            }
            else {
                innerHtml += '<span style="color: red">' + this.text + '</span> ';
            }

            if (this.pageComponentView) {
                var target = api.util.capitalize(this.pageComponentView.getType().getShortName()) + ': ' + this.pageComponentView.getName();
                innerHtml += '<div style = "font-size: 11px;"> dragging ' + target + ' </div > ';
            }
            if (this.regionView) {
                innerHtml += '<div style = "font-size: 11px;"> target region: ' + this.regionView.getRegionPath().toString() + ' </div > ';
            }

            this.getEl().setInnerHtml(innerHtml);
        }
    }
}