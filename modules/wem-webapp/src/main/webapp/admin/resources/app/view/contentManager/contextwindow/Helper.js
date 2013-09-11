Ext.define('Admin.view.contentManager.contextwindow.Helper', {
    constructor: function() { /**/ },

    statics: {
        /**
         * NOTE:
         * This method should at a later stage in the project be moved in order to
         * share the functionality between Live Edit and Context Window.
         *
         * @param componentType {number}
         * @returns {string}
        */
        resolveComponentTypeIconCls: function (componentType) {
            var iconCls;

            var type = parseInt(componentType, 10);

            switch (type) {
                case 0:
                    iconCls = 'live-edit-font-icon-page';
                    break;
                case 1:
                    iconCls = 'live-edit-font-icon-region';
                    break;
                case 2:
                    iconCls = 'live-edit-font-icon-layout';
                    break;
                case 3:
                    iconCls = 'live-edit-font-icon-part';
                    break;
                case 4:
                    iconCls = 'live-edit-font-icon-image';
                    break;
                case 5:
                    iconCls = 'live-edit-font-icon-paragraph';
                    break;
                case 6:
                    iconCls = 'live-edit-font-icon-content';
                    break;
                default:
                    iconCls = '';
            }
            return iconCls;
        },

        /**
         * @param deviceType {string}
         * @returns {string}
         */
        resolveDeviceTypeIconCls: function (deviceType) {
            var iconCls;
            switch (deviceType) {
                case 'monitor':
                    iconCls = 'icon-desktop';
                    break;
                case 'monitor_full':
                    iconCls = 'icon-desktop';
                    break;
                case 'mobile':
                    iconCls = 'icon-mobile-phone';
                    break;
                case 'tablet':
                    iconCls = 'icon-tablet';
                    break;
                default:
                    iconCls = '';
            }
            return iconCls;
        }
    }

});
