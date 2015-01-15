module api.content.page {

    import PropertyIdProvider = api.data.PropertyIdProvider;

    export class PageRegions extends AbstractRegions implements api.Equitable, api.Cloneable {

        constructor(builder: PageRegionsBuilder) {
            super(builder.regions);
        }

        equals(o: api.Equitable): boolean {

            if (!api.ObjectHelper.iFrameSafeInstanceOf(o, PageRegions)) {
                return false;
            }

            return super.equals(o);
        }

        clone(): PageRegions {
            return new PageRegionsBuilder(this).build();
        }

        /**
         * Keeps existing regions (including components) if they are listed in given regionDescriptors.
         * Removes others and adds those missing.
         * @param regionDescriptors
         */
        changeRegionsTo(regionDescriptors: region.RegionDescriptor[]) {

            // Remove regions not existing in regionDescriptors
            var regionsToRemove: region.Region[] = this.getRegions().
                filter((region: region.Region, index: number) => {
                    return !regionDescriptors.
                        some((regionDescriptor: region.RegionDescriptor) => {
                            return regionDescriptor.getName() == region.getName();
                        });
                });
            this.removeRegions(regionsToRemove);

            // Add missing regions
            regionDescriptors.forEach((regionDescriptor: region.RegionDescriptor) => {
                var region = this.getRegionByName(regionDescriptor.getName());
                if (!region) {
                    region = new api.content.page.region.RegionBuilder().
                        setName(regionDescriptor.getName()).
                        build();
                    this.addRegion(region);
                }
            });
        }
    }

    export class PageRegionsBuilder {

        regions: region.Region[] = [];

        constructor(source?: PageRegions) {
            if (source) {
                source.getRegions().forEach((region: region.Region) => {
                    this.regions.push(region.clone());
                });
            }
        }

        fromJson(regionsJson: region.RegionJson[], propertyIdProvider: PropertyIdProvider): PageRegionsBuilder {

            regionsJson.forEach((regionJson: region.RegionJson, index: number) => {

                var region = new api.content.page.region.RegionBuilder().
                    setName(regionJson.name).
                    setParent(null).
                    build();

                regionJson.components.forEach((componentJson: region.ComponentTypeWrapperJson) => {
                    var component: api.content.page.region.Component = api.content.page.region.ComponentFactory.createFromJson(componentJson,
                        index, region, propertyIdProvider);
                    region.addComponent(component);
                });

                this.addRegion(region);
            });
            return this;
        }

        addRegion(value: region.Region): PageRegionsBuilder {
            this.regions.push(value);
            return this;
        }

        public build(): PageRegions {
            return new PageRegions(this);
        }
    }
}