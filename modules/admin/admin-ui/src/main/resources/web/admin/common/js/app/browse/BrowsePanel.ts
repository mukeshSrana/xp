module api.app.browse {

    import ResponsiveManager = api.ui.responsive.ResponsiveManager;
    import ResponsiveRanges = api.ui.responsive.ResponsiveRanges;
    import ResponsiveItem = api.ui.responsive.ResponsiveItem;
    import TreeNode = api.ui.treegrid.TreeNode;

    export interface BrowsePanelParams<M extends api.Equitable> {

        browseToolbar:api.ui.toolbar.Toolbar;

        treeGrid?:api.ui.treegrid.TreeGrid<Object>;

        browseItemPanel:BrowseItemPanel<M>;

        filterPanel?:api.app.browse.filter.BrowseFilterPanel;
    }

    export class BrowsePanel<M extends api.Equitable> extends api.ui.panel.Panel implements api.ui.ActionContainer {

        private static SPLIT_PANEL_ALIGNMENT_TRESHOLD: number = 1180;

        private browseToolbar: api.ui.toolbar.Toolbar;

        private treeGrid: api.ui.treegrid.TreeGrid<Object>;

        private browseItemPanel: BrowseItemPanel<M>;

        private gridAndDetailSplitPanel: api.ui.panel.SplitPanel;

        private filterPanel: api.app.browse.filter.BrowseFilterPanel;

        private filterAndGridAndDetailSplitPanel: api.ui.panel.SplitPanel;

        private gridAndToolbarContainer: api.ui.panel.Panel;

        private filterPanelRefreshNeeded: boolean = false;

        private filterPanelForcedShown: boolean = false;

        private filterPanelForcedHidden: boolean = false;

        private filterPanelToBeShownFullScreen: boolean = false;

        private filterPanelIsHiddenByDefault: boolean = true;

        private toggleFilterPanelAction: api.ui.Action;

        constructor(params: BrowsePanelParams<M>) {
            super();

            this.browseToolbar = params.browseToolbar;
            this.treeGrid = params.treeGrid;
            this.browseItemPanel = params.browseItemPanel;
            this.filterPanel = params.filterPanel;

            this.gridAndToolbarContainer = new api.ui.panel.Panel();
            this.gridAndToolbarContainer.appendChild(this.browseToolbar);

            var gridPanel = new api.ui.panel.Panel();
            gridPanel.appendChild(this.treeGrid);

            this.gridAndToolbarContainer.appendChild(gridPanel);

            this.gridAndDetailSplitPanel = new api.ui.panel.SplitPanelBuilder(this.gridAndToolbarContainer, this.browseItemPanel)
                .setAlignmentTreshold(BrowsePanel.SPLIT_PANEL_ALIGNMENT_TRESHOLD).build();

            if (this.filterPanel) {
                this.setupFilterPanel();
                if(this.filterPanelIsHiddenByDefault) {
                    this.hideFilterPanel();
                }
            } else {
                this.filterAndGridAndDetailSplitPanel = this.gridAndDetailSplitPanel;
            }

            this.treeGrid.onSelectionChanged((currentSelection: TreeNode<Object>[], fullSelection: TreeNode<Object>[]) => {
                var browseItems: api.app.browse.BrowseItem<M>[] = this.treeNodesToBrowseItems(fullSelection);
                this.browseItemPanel.setItems(browseItems);

                this.treeGrid.getContextMenu().getActions().updateActionsEnabledState(this.browseItemPanel.getItems()).
                    then(() => {
                        this.browseItemPanel.updateDisplayedPanel();
                    });

                this.treeGrid.getToolbar().refresh(browseItems.length);
            });

            this.onRendered((event) => {
                this.appendChild(this.filterAndGridAndDetailSplitPanel);
            });

            ResponsiveManager.onAvailableSizeChanged(this, (item: ResponsiveItem) => {
                this.checkFilterPanelToBeShownFullScreen(item);

                if(!this.filterPanelIsHiddenByDefault) { //not relevant if filter panel is hidden by default
                    this.toggleFilterPanelDependingOnScreenSize(item);
                }

                this.togglePreviewPanelDependingOnScreenSize(item);
            });
        }

        getTreeGrid(): api.ui.treegrid.TreeGrid<Object> {
            return this.treeGrid;
        }

        getBrowseItemPanel(): BrowseItemPanel<M> {
            return this.browseItemPanel;
        }

        getActions(): api.ui.Action[] {
            return this.browseToolbar.getActions();
        }

        // TODO: ContentSummary must be replaced with an ContentSummaryAndCompareStatus after old grid is removed
        treeNodesToBrowseItems(nodes: TreeNode<Object>[]): BrowseItem<M>[] {
            return [];
        }

        refreshFilter() {
            if (this.isFilterPanelRefreshNeeded()) {
                if (this.filterPanel) {
                    this.filterPanel.refresh();
                }
                this.filterPanelRefreshNeeded = false;
            }
        }

        isFilterPanelRefreshNeeded(): boolean {
            return this.filterPanelRefreshNeeded;
        }

        setFilterPanelRefreshNeeded(refreshNeeded: boolean) {
            this.filterPanelRefreshNeeded = refreshNeeded;
        }

        toggleFilterPanel() {
            this.filterAndGridAndDetailSplitPanel.setFirstPanelIsFullScreen(this.filterPanelToBeShownFullScreen);

            if(this.filterPanelIsHidden()) {
                this.showFilterPanel();
            }
            else {
                this.hideFilterPanel();
            }
        }

        private filterPanelIsHidden(): boolean {
            return this.filterAndGridAndDetailSplitPanel.isFirstPanelHidden();
        }

        private showFilterPanel() {
            this.filterPanelForcedShown = true;
            this.filterPanelForcedHidden = false;

            if(this.filterPanelToBeShownFullScreen) {
                this.filterAndGridAndDetailSplitPanel.hideSecondPanel();
            }

            this.filterAndGridAndDetailSplitPanel.showFirstPanel();
            this.filterPanel.giveFocusToSearch();
            this.toggleFilterPanelAction.setVisible(false);
        }

        private hideFilterPanel() {
            this.filterPanelForcedShown = false;
            this.filterPanelForcedHidden = true;
            this.filterAndGridAndDetailSplitPanel.showSecondPanel();
            this.filterAndGridAndDetailSplitPanel.hideFirstPanel();

            this.toggleFilterPanelAction.setVisible(true);
        }

        private setupFilterPanel() {
            this.filterAndGridAndDetailSplitPanel = new api.ui.panel.SplitPanelBuilder(this.filterPanel, this.gridAndDetailSplitPanel)
                .setFirstPanelSize(200,
                api.ui.panel.SplitPanelUnit.PIXEL).setAlignment(api.ui.panel.SplitPanelAlignment.VERTICAL).build();

            this.filterPanel.onHideFilterPanelButtonClicked(() => {
                this.toggleFilterPanel();
            });

            this.filterPanel.onShowResultsButtonClicked(() => {
                this.toggleFilterPanel();
            });

            this.addToggleFilterPanelButtonInToolbar();
        }

        private addToggleFilterPanelButtonInToolbar() {
            this.toggleFilterPanelAction = new api.app.browse.action.ToggleFilterPanelAction(this);
            var existingActions: api.ui.Action[] = this.browseToolbar.getActions();
            this.browseToolbar.removeActions();
            this.browseToolbar.addAction(this.toggleFilterPanelAction);
            this.browseToolbar.addActions(existingActions);
            this.toggleFilterPanelAction.setVisible(false);
        }

        private checkFilterPanelToBeShownFullScreen(item: ResponsiveItem) {
            if (item.isInRangeOrSmaller(ResponsiveRanges._360_540)) {
                this.filterPanelToBeShownFullScreen = true;
            }
            else {
                this.filterPanelToBeShownFullScreen = false;
            }
        }

        private toggleFilterPanelDependingOnScreenSize(item: ResponsiveItem) {
            if (item.isInRangeOrSmaller(ResponsiveRanges._1380_1620)) {
                if (this.filterPanel && !this.filterAndGridAndDetailSplitPanel.isFirstPanelHidden() && !this.filterPanelForcedShown) {
                    this.filterAndGridAndDetailSplitPanel.hideFirstPanel();
                    this.toggleFilterPanelAction.setVisible(true);
                }
            } else if (item.isInRangeOrBigger(ResponsiveRanges._1620_1920)) {
                if (this.filterPanel && this.filterAndGridAndDetailSplitPanel.isFirstPanelHidden() && !this.filterPanelForcedHidden) {
                    this.filterAndGridAndDetailSplitPanel.showFirstPanel();
                    this.toggleFilterPanelAction.setVisible(false);
                }
            }
        }

        private togglePreviewPanelDependingOnScreenSize(item: ResponsiveItem) {
            if (item.isInRangeOrSmaller(ResponsiveRanges._360_540)) {
                if (!this.gridAndDetailSplitPanel.isSecondPanelHidden()) {
                    this.gridAndDetailSplitPanel.hideSecondPanel();
                }
            } else if (item.isInRangeOrBigger(ResponsiveRanges._540_720)) {
                if (this.gridAndDetailSplitPanel.isSecondPanelHidden()) {
                    this.gridAndDetailSplitPanel.showSecondPanel();
                }
            }
        }

    }
}
