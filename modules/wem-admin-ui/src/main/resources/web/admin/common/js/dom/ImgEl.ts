module api.dom {

    export class ImgEl extends Element {

        /* 1px x 1px gif with a 1bit palette */
        static PLACEHOLDER = "data:image/gif;base64,R0lGODlhAQABAIAAAP///////yH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==";

        constructor(src?: string, className?: string) {
            super(new ElementProperties().setTagName("img").setClassName(className).setHelper(ImgHelper.create()));
            this.getEl().setSrc(src ? src : ImgEl.PLACEHOLDER);
        }

        setSrc(source: string) {

            this.getEl().setSrc(source);
        }

        getEl(): ImgHelper {
            return <ImgHelper>super.getEl();
        }

        onLoaded(listener: (event: UIEvent) => void) {
            this.getEl().addEventListener("load", listener);
        }

        unLoaded(listener: (event: UIEvent) => void) {
            this.getEl().removeEventListener("load", listener);
        }
    }
}