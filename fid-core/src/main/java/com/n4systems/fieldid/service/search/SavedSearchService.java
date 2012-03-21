package com.n4systems.fieldid.service.search;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.columns.ColumnMapping;
import com.n4systems.model.columns.SystemColumnMapping;
import com.n4systems.model.saveditem.SavedItem;
import com.n4systems.model.search.ColumnMappingConverter;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.model.user.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public abstract class SavedSearchService<I extends SavedItem<T>, T extends SearchCriteria>  extends FieldIdPersistenceService {

    @Transactional
    public void saveLastSearch(T searchCriteria) {
        try {
            final User user = getCurrentUser();

            removeLastSavedSearch(user);

            searchCriteria = (T) searchCriteria.clone();
            searchCriteria.reset();
            storeSelectedColumns(searchCriteria);

            storeLastSearchInUser(user, searchCriteria);

            persistenceService.update(user);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(readOnly = true)
    public I getConvertedReport(Class<I> clazz, Long itemId) {
        final I savedItem = (I) persistenceService.find(clazz, itemId);
        final T searchCriteria = savedItem.getSearchCriteria();
        searchCriteria.setReportAlreadyRun(true);

        storeTransientColumns(searchCriteria);

        enableSelectedColumns(searchCriteria, searchCriteria.getColumns());

        if (searchCriteria.getSortColumnId() != null) {
            ColumnMapping mapping = persistenceService.findUnsecured(SystemColumnMapping.class, searchCriteria.getSortColumnId());
            ColumnMappingView mappingView = new ColumnMappingConverter().convert(mapping);
            searchCriteria.setSortColumn(mappingView);
        }

        return savedItem;
    }

    public abstract void removeLastSavedSearch(User user);
    public abstract T retrieveLastSearch();
    protected abstract void storeLastSearchInUser(User user, T searchCriteria);
    protected abstract void storeTransientColumns(T searchCriteria);

    @Transactional
    public void saveReport(I savedItem, boolean overwrite, String name, String description) {
        boolean updating = overwrite && savedItem.getId() != null && savedItem.getSearchCriteria().getId() != null;
        final User user = getCurrentUser();

        savedItem.setTenant(getCurrentTenant());
        savedItem.setName(name);
        savedItem.setDescription(description);
        
        storeSelectedColumns(savedItem.getSearchCriteria());

        if (updating) {
            persistenceService.update(savedItem);
        } else {
            savedItem.reset();

            if (savedItem.getSearchCriteria().getId() != null) {
                T copiedSearchCriteria;
                try {
                    copiedSearchCriteria = (T) savedItem.getSearchCriteria().clone();
                    copiedSearchCriteria.reset();
                    savedItem.setSearchCriteria(copiedSearchCriteria);
                } catch (CloneNotSupportedException e) {
                    throw new RuntimeException(e);
                }
            }

            user.getSavedItems().add(savedItem);
            persistenceService.save(user);
        }
    }

    protected void enableSelectedColumns(T criteriaModel, List<String> columns) {
        for (ColumnMappingView columnMappingView : criteriaModel.getSortedStaticAndDynamicColumns(false)) {
            columnMappingView.setEnabled(columns.contains(columnMappingView.getId()));
        }
    }

    protected void storeSelectedColumns(T criteriaModel) {
        List<String> selectedColumns = new ArrayList<String>();
        for (ColumnMappingView columnMappingView : criteriaModel.getSortedStaticAndDynamicColumns(true)) {
            selectedColumns.add(columnMappingView.getId());
        }
        criteriaModel.setColumns(selectedColumns);
    }

}
