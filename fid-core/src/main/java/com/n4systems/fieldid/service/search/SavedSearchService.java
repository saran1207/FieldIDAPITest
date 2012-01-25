package com.n4systems.fieldid.service.search;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.columns.ColumnMapping;
import com.n4systems.model.columns.SystemColumnMapping;
import com.n4systems.model.saveditem.SavedItem;
import com.n4systems.model.search.ColumnMappingConverter;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.search.SearchCriteriaModel;
import com.n4systems.model.user.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public abstract class SavedSearchService<I extends SavedItem<T>, T extends SearchCriteriaModel>  extends FieldIdPersistenceService {

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

    protected abstract void storeTransientColumns(T searchCriteria);

    @Transactional
    public void saveReport(I savedItem, boolean overwrite, String name) {
        boolean updating = overwrite && savedItem.getSearchCriteria().getId() != null;
        final User user = getCurrentUser();

        savedItem.setTenant(getCurrentTenant());
        savedItem.setName(name);

        storeSelectedColumns(savedItem.getSearchCriteria());

        if (updating) {
            persistenceService.update(savedItem);
        } else {
            savedItem.reset();
            savedItem.getSearchCriteria().reset();
            T copiedSearchCriteria;
            try {
                copiedSearchCriteria = (T) savedItem.getSearchCriteria().clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }

            persistenceService.save(copiedSearchCriteria);
            user.getSavedItems().add(savedItem);

            persistenceService.save(user);
        }

    }

    private void enableSelectedColumns(T criteriaModel, List<String> columns) {
        for (ColumnMappingView columnMappingView : criteriaModel.getSortedStaticAndDynamicColumns(false)) {
            columnMappingView.setEnabled(columns.contains(columnMappingView.getId()));
        }
    }

    private void storeSelectedColumns(T criteriaModel) {
        List<String> selectedColumns = new ArrayList<String>();
        for (ColumnMappingView columnMappingView : criteriaModel.getSortedStaticAndDynamicColumns(true)) {
            selectedColumns.add(columnMappingView.getId());
        }
        criteriaModel.setColumns(selectedColumns);
    }

}
