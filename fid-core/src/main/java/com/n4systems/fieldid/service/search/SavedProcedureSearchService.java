package com.n4systems.fieldid.service.search;

import com.n4systems.fieldid.service.search.columns.ProcedureColumnsService;
import com.n4systems.model.saveditem.SavedProcedureItem;
import com.n4systems.model.search.ProcedureCriteria;
import com.n4systems.model.search.ReportConfiguration;
import com.n4systems.model.user.User;

public class SavedProcedureSearchService extends SavedSearchService<SavedProcedureItem, ProcedureCriteria> {

    @Override
    public void removeLastSavedSearch(User user) {
        if (user.getLastRunProcedures() != null) {
            final ProcedureCriteria lastRunReport = user.getLastRunProcedures();
            user.setLastRunProcedures(null);
            persistenceService.update(user);
            persistenceService.delete(lastRunReport);
        }
    }

    @Override
    public ProcedureCriteria retrieveLastSearch() {
        ProcedureCriteria lastRunProcedures = getCurrentUser().getLastRunProcedures();
        if(lastRunProcedures != null) {
            lastRunProcedures.setReportAlreadyRun(true);
            storeTransientColumns(lastRunProcedures);
            enableSelectedColumns(lastRunProcedures, lastRunProcedures.getColumns());
        }
        return lastRunProcedures;
    }

    @Override
    protected void storeLastSearchInUser(User user, ProcedureCriteria searchCriteria) {
        user.setLastRunProcedures(searchCriteria);
    }

    @Override
    protected void storeTransientColumns(ProcedureCriteria searchCriteria) {
        ReportConfiguration reportConfiguration = new ProcedureColumnsService().getReportConfiguration(securityContext.getUserSecurityFilter());
        searchCriteria.setColumnGroups(reportConfiguration.getColumnGroups());
    }

    @Override
    protected SavedProcedureItem createSavedItem(SavedProcedureItem savedItem) {
        return savedItem.copy(new SavedProcedureItem());
    }
}
