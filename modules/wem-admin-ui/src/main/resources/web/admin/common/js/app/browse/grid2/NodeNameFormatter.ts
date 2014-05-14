module api.app.browse.grid2 {

    export class NodeNameFormatter<T> {

        static createHtml(node:api.node.Node, mainName:string, subName:string, iconUrl:string):string {
            var rowEl = new api.dom.DivEl();

            if( node.hasChildren ) {

            }

            var iconEl = new api.dom.ImgEl();
            iconEl.getEl().setSrc(iconUrl);
            iconEl.getEl().setWidth("32px");
            iconEl.getEl().setHeight("32px");


            var displayNameEl = new api.dom.H6El();
            displayNameEl.getEl().setInnerHtml(mainName);

            var subNameEl = new api.dom.PEl();
            subNameEl.getEl().setInnerHtml(subName);

            rowEl.appendChild(iconEl);
            rowEl.appendChild(displayNameEl);
            rowEl.appendChild(subNameEl);

            return rowEl.toString();
        }
    }
}