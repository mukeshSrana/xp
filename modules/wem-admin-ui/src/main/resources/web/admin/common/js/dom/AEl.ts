module api.dom {

    export class AEl extends Element {

        constructor(className?: string) {
            super(new ElementProperties().setTagName("a").setClassName(className));
        }

        public setText(value: string) {
            this.getEl().setInnerHtml(value);
        }

        public setUrl(value: string) {
            this.getEl().setAttribute('href', value);
        }
    }
}