package com.n4systems.fieldid.actions.select;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.SearchPerformerWithReadOnlyTransactionManagement;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.fieldid.viewhelpers.SearchContainer;
import com.n4systems.util.persistence.search.ImmutableBaseSearchDefiner;
import com.n4systems.util.selection.MultiIdSelection;

import java.util.List;

public class SelectAction extends AbstractAction {

    private String searchContainerKey;
    private String searchId;
    private List<Long> ids;

    private static final String SUCCESS_MULTIPLE = "successMultiple";

    public SelectAction(PersistenceManager persistenceManager) {
        super(persistenceManager);
    }

    public String doAdd() {
        MultiIdSelection multiSelection = getSelection();
        if (multiSelection != null) {
            for (Long id : ids) {
                multiSelection.addId(id);
            }
        }

        return successStatus();
    }

    public String doDelete() {
        MultiIdSelection multiSelection = getSelection();
        if (multiSelection != null) {
            for (Long id : ids) {
                multiSelection.removeId(id);
            }
        }

        return SUCCESS;
    }

    public String doAddEntireResult() {
        MultiIdSelection multiIdSelection = getSelection();
        if (multiIdSelection != null) {
            List<Long> allIdsMatchingSearch = getAllIdsMatchingSearch();
            multiIdSelection.addAllIds(allIdsMatchingSearch);
        }

        return SUCCESS;
    }

    public String doClear() {
        MultiIdSelection multiSelection = getSelection();
        if (multiSelection != null) {
            multiSelection.clear();
        }

        return SUCCESS;
    }

    private MultiIdSelection getSelection() {
        SearchContainer container = getSearchContainer();
        if (container == null || !container.getSearchId().equals(searchId)) {
            return null;
        }
        return container.getMultiIdSelection();
    }

    private SearchContainer getSearchContainer() {
        return (SearchContainer) getSession().get(searchContainerKey);
    }

    private String successStatus() {
        if (ids.size() > 1) {
            return SUCCESS_MULTIPLE;
        }
        return SUCCESS;
    }

    public String getSearchContainerKey() {
        return searchContainerKey;
    }

    public void setSearchContainerKey(String searchContainerKey) {
        this.searchContainerKey = searchContainerKey;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public int getNumSelectedItems() {
        MultiIdSelection selection = getSelection();
        if (selection == null) {
            return 0;
        }
        return selection.getNumSelectedIds();
    }

    private List<Long> getAllIdsMatchingSearch() {
        List<Long> idsMatchingSearch = new SearchPerformerWithReadOnlyTransactionManagement().idSearch(new ImmutableBaseSearchDefiner(getSearchContainer()), getSearchContainer().getSecurityFilter());
        return idsMatchingSearch;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public int getNumItemsJustSelected() {
        return this.ids.size();
    }

    public int getTotalResults() {
        return getAllIdsMatchingSearch().size();
    }
}
