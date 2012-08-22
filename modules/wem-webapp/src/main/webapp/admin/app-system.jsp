<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8"/>
  <title>Enonic WEM Admin</title>
  <link rel="stylesheet" type="text/css" href="resources/lib/ext/resources/css/ext-all.css">
  <link rel="stylesheet" type="text/css" href="resources/css/main.css">

  <!-- Ext JS -->

  <script type="text/javascript" src="resources/lib/ext/ext-all-debug.js"></script>

  <!-- Configuartion -->

  <script type="text/javascript" src="config.js"></script>
  <script type="text/javascript">

    Ext.Loader.setConfig({
      paths: {
        'App': '_app/system/js',
        'Common': 'common/js',
        'Admin': 'resources/app'
      }
    });

  </script>

  <!-- Application -->

  <script type="text/javascript">
    Ext.application({
      name: 'App',
      appFolder: '_app/system/js',

      controllers: [
        'SystemController'
      ],

      requires: [
        'Admin.view.TabPanel',
        'App.view.NavigationPanel'
      ],

      launch: function () {
        Ext.create('Ext.container.Viewport', {
          layout: 'border',
          padding: 5,

          items: [
            {
              region: 'center',
              xtype: 'cmsTabPanel',
              id: 'systemTabPanelID',
              items: [
                {
                  id: 'tab-browse',
                  title: 'Browse',
                  closable: false,
                  xtype: 'panel',
                  layout: 'border',
                  items: [
                    {
                      region: 'west',
                      width: 225,
                      xtype: 'systemNavigation'
                    },
                    {
                      id: 'system-center',
                      region: 'center',
                      bodyCls: 'system-center-inner',
                      html: '<iframe id="system-iframe" src="blank.html"><!-- --></iframe>'
                    }
                  ]
                }
              ]
            }
          ]
        });
      }
    });

  </script>

</head>
<body>
</body>
</html>
