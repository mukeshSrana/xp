Ext.define('Admin.lib.formitem.FormHelper', {

    addFormItems: function (contentTypeItems, parentContainer) {
        var me = this;
        var formItem;
        Ext.each(contentTypeItems, function (item) {
            if (item.FormItemSet) {
                formItem = me.createFormItemSet(item.FormItemSet);
            } else { // Input
                formItem = me.createItem(item.Input);
            }

            if (parentContainer.getXType() === 'FormItemSet' || parentContainer.getXType() === 'fieldcontainer') {
                parentContainer.add(formItem);
            } else {
                parentContainer.items.push(formItem);
            }
        });
    },


    createFormItemSet: function (formItemSetConfig) {
        return Ext.create({
            xclass: 'widget.FormItemSet',
            name: formItemSetConfig.name,
            formItemSetConfig: formItemSetConfig
        });
    },


    createItem: function (inputConfig) {
        var me = this;
        var classAlias = 'widget.' + inputConfig.type.name;

        // TODO: Move to addFormItem
        if (!me.formItemIsSupported(classAlias)) {
            console.error('Unsupported input type', inputConfig);
            return;
        }

        return Ext.create({
            xclass: classAlias,
            fieldLabel: inputConfig.label,
            name: inputConfig.name,
            inputConfig: inputConfig
        });

    },


    formItemIsSupported: function (classAlias) {
        return Ext.ClassManager.getByAlias(classAlias);
    }

});