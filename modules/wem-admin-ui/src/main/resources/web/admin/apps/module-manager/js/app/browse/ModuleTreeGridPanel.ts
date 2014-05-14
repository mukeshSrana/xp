module app.browse {

    export interface ModuleTreeGridPanelParams {

        contextMenu:api.ui.menu.ContextMenu;
    }

    export class ModuleTreeGridPanel extends api.app.browse.grid.TreeGridPanel {

        constructor(params:ModuleTreeGridPanelParams) {

            super({
                columns: this.createColumns(),
                gridStore: new app.browse.grid.ModuleGridStore().getExtDataStore(),
                treeStore: new app.browse.grid.ModuleTreeStore().getExtDataStore(),
                gridConfig: this.createGridConfig(),
                treeConfig: this.createTreeConfig(),
                contextMenu: params.contextMenu});

            this.setItemId("ModuleTreeGridPanel");

            this.setActiveList(api.app.browse.grid.TreeGridPanel.GRID);
            this.setKeyField("key");

        }

        private createColumns() {
            return <any[]> [
                {
                    header: 'Name',
                    dataIndex: 'displayName',
                    flex: 0.5,
                    renderer: this.nameRenderer,
                    scope: this
                },
                {
                    header: 'Version',
                    dataIndex: 'version'
                },
                {
                    header: 'Vendor Name',
                    dataIndex: 'vendorName'
                },
                {
                    header: 'Vendor URL',
                    dataIndex: 'vendorUrl'
                }
            ];
        }

        private createGridConfig() {
            return {
               sortableColumns: false,
               selModel: Ext.create('Ext.selection.CheckboxModel', {headerWidth: 36})
            }
        }

        private createTreeConfig() {
            return {
                selModel: {
                    allowDeselect: false,
                    ignoreRightMouseSelection: true
                }
            }
        }

        private nameRenderer(value, metaData, record) {
            // typescript swears when extracting this as the class field
            var nameTemplate = '<div class="admin-{0}-thumbnail">' +
                               '<img src="{1}"/>' +
                               '<span class="overlay"/>' +
                               '</div>' +
                               '<div class="admin-{0}-description">' +
                               '<h6>{2}</h6>' +
                               '<p>{3}</p>' +
                               '</div>';

            var moduleData = record.data;
            var activeListType = this.getActiveList().getItemId();
            return Ext.String.format( nameTemplate, activeListType, api.util.getAdminUri('common/images/icons/icoMoon/32x32/puzzle.png'), value, moduleData.key);
        }

    }
}