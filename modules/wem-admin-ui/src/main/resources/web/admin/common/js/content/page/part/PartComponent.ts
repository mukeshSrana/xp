module api.content.page.part {

    export class PartComponent extends api.content.page.DescriptorBasedPageComponent implements api.Equitable, api.Cloneable {

        constructor(builder: PartComponentBuilder) {
            super(builder);
        }

        toJson(): api.content.page.PageComponentTypeWrapperJson {
            var json: PartComponentJson = <PartComponentJson>super.toPageComponentJson();

            return <api.content.page.PageComponentTypeWrapperJson> {
                PartComponent: json
            };
        }

        equals(o: api.Equitable): boolean {

            if (!(o instanceof PartComponent)) {
                return false;
            }

            return super.equals(o);
        }

        clone(): PartComponent {
            return new PartComponentBuilder(this).build();
        }
    }

    export class PartComponentBuilder extends api.content.page.DescriptorBasedPageComponentBuilder<PartComponent> {

        constructor(source?: PartComponent) {

            super();

            if (source) {
                this.name = source.getName();
                this.descriptor = source.getDescriptor();
                this.parent = source.getParent();
                this.config = source.getConfig() ? source.getConfig().clone() : null;
            }
        }

        public fromJson(json: PartComponentJson, regionPath: RegionPath): PartComponentBuilder {

            if (json.descriptor) {
                this.setDescriptor(api.content.page.DescriptorKey.fromString(json.descriptor));
            }
            this.setName(new api.content.page.ComponentName(json.name));
            this.setConfig(api.data.DataFactory.createRootDataSet(json.config));
            this.setParent(regionPath);
            return this;
        }

        public build(): PartComponent {
            return new PartComponent(this);
        }
    }
}