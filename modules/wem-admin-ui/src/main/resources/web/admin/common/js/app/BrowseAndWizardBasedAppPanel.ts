module api.app {

    export interface BrowseBasedAppPanelConfig<M> {

        appBar:api.app.AppBar;

        browsePanel:api.app.browse.BrowsePanel<M>;
    }

    export class BrowseAndWizardBasedAppPanel<M> extends api.app.AppPanel {

        private browsePanel: api.app.browse.BrowsePanel<M>;

        private appBarTabMenu: api.app.AppBarTabMenu;

        private currentKeyBindings: api.ui.KeyBinding[];

        constructor(config: BrowseBasedAppPanelConfig<M>) {
            super(config.appBar.getTabMenu(), config.browsePanel);

            this.browsePanel = config.browsePanel;
            this.appBarTabMenu = config.appBar.getTabMenu();

            this.currentKeyBindings = api.ui.Action.getKeyBindings(this.resolveActions(this.browsePanel));
            this.activateCurrentKeyBindings();

            this.onPanelShown((event: api.ui.PanelShownEvent) => {
                if (event.getPanel() === this.browsePanel) {
                    this.browsePanel.refreshFilterAndGrid();
                }

                var previousActions = this.resolveActions(event.getPreviousPanel());
                api.ui.KeyBindings.get().unbindKeys(api.ui.Action.getKeyBindings(previousActions));

                var nextActions = this.resolveActions(event.getPanel());
                this.currentKeyBindings = api.ui.Action.getKeyBindings(nextActions);
                api.ui.KeyBindings.get().bindKeys(this.currentKeyBindings);
            });
        }

        activateCurrentKeyBindings() {

            if (this.currentKeyBindings) {
                api.ui.KeyBindings.get().bindKeys(this.currentKeyBindings);
            }
        }

        getAppBarTabMenu(): api.app.AppBarTabMenu {
            return this.appBarTabMenu;
        }

        addViewPanel(tabMenuItem: AppBarTabMenuItem, viewPanel: api.app.view.ItemViewPanel<M>) {
            super.addNavigablePanel(tabMenuItem, viewPanel, true);

            tabMenuItem.onClosed(() => {
                viewPanel.close();
            });

            viewPanel.onClosed((event: api.app.view.ItemViewClosedEvent<M>) => {
                this.removeNavigablePanel(event.getView(), false);
            });
        }

        addWizardPanel(tabMenuItem: AppBarTabMenuItem, wizardPanel: api.app.wizard.WizardPanel<any>) {
            super.addNavigablePanel(tabMenuItem, wizardPanel, true);

            tabMenuItem.onClosed(() => {
                wizardPanel.close();
            });

            wizardPanel.onClosed((event: api.app.wizard.WizardClosedEvent) => {
                this.removeNavigablePanel(event.getWizard(), false);
            });
        }

        canRemovePanel(panel: api.ui.Panel): boolean {
            if (panel instanceof api.app.wizard.WizardPanel) {
                var wizardPanel: api.app.wizard.WizardPanel<any> = <api.app.wizard.WizardPanel<any>>panel;
                return wizardPanel.canClose();
            }
            return true;
        }

        private resolveActions(panel: api.ui.Panel): api.ui.Action[] {

            if (panel instanceof api.app.wizard.WizardPanel || panel instanceof api.app.browse.BrowsePanel) {
                var actionContainer: api.ui.ActionContainer = <any>panel;
                return actionContainer.getActions();
            }
            else {
                return [];
            }
        }
    }
}