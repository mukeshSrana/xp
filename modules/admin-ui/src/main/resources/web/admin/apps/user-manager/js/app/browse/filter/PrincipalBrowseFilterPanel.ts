module app.browse.filter {

    import AggregationGroupView = api.aggregation.AggregationGroupView;
    import SearchInputValues = api.query.SearchInputValues;
    import Principal = api.security.Principal;
    import FindPrincipalsRequest = api.security.FindPrincipalsRequest;
    import PrincipalType = api.security.PrincipalType;
    import RefreshEvent = api.app.browse.filter.RefreshEvent;

    export class PrincipalBrowseFilterPanel extends api.app.browse.filter.BrowseFilterPanel {


        constructor() {

            super(null);

            this.onReset(()=> {
                this.resetFacets();
            });

            this.onRefresh(this.searchFacets);

            this.onSearch(this.searchFacets);
        }

        private resetFacets(supressEvent?: boolean) {

            if (!supressEvent) { // then fire usual reset event with content grid reloading
                new PrincipalBrowseResetEvent().fire();
            }
        }

        private searchFacets(event: api.app.browse.filter.SearchEvent) {
            var searchText = event.getSearchInputValues().getTextSearchFieldValue();
            if (!searchText) {
                this.handleEmptyFilterInput(event);
                return;
            }

            this.searchDataAndHandleResponse(searchText);
        }

        private  handleEmptyFilterInput(event: api.app.browse.filter.SearchEvent) {
            if (event instanceof RefreshEvent) {
                this.resetFacets(true);
            } else { // it's SearchEvent, usual reset with grid reload
                this.reset();
            }
        }

        private searchDataAndHandleResponse(searchString: string) {
            new FindPrincipalsRequest().
                setAllowedTypes([PrincipalType.GROUP, PrincipalType.USER, PrincipalType.ROLE]).
                setSearchQuery(searchString).
                sendAndParse().then((principals: Principal[]) => {
                    new PrincipalBrowseSearchEvent(principals).fire();
                }).catch((reason: any) => {
                    api.DefaultErrorHandler.handle(reason);
                }).done();
        }
    }
}
