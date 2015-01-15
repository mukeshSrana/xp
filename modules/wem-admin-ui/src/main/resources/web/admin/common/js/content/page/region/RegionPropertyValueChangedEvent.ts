module api.content.page.region {

    export class RegionPropertyValueChangedEvent extends RegionChangedEvent {

        private propertyName: string;

        constructor(path: RegionPath, propertyName: string) {
            super(path);
            this.propertyName = propertyName;
        }

        public getPropertyName(): string {
            return this.propertyName;
        }

    }
}