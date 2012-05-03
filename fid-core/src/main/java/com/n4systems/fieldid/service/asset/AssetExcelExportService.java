package com.n4systems.fieldid.service.asset;

import com.n4systems.ejb.PageHolder;
import com.n4systems.fieldid.service.export.ExcelExportService;
import com.n4systems.fieldid.service.search.AssetSearchService;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.util.persistence.search.ResultTransformer;
import com.n4systems.util.views.TableView;
import org.springframework.beans.factory.annotation.Autowired;

public class AssetExcelExportService extends ExcelExportService<AssetSearchCriteria> {
    
    @Autowired
    private AssetSearchService assetSearchService;

    @Override
    protected int countTotalResults(AssetSearchCriteria criteria) {
        return assetSearchService.countPages(criteria, 1L);
    }

    @Override
    protected PageHolder<TableView> performSearch(AssetSearchCriteria criteria, ResultTransformer<TableView> resultTransformer, int pageNumber, int pageSize, boolean useSelection) {
        return assetSearchService.performSearch(criteria,  resultTransformer, pageNumber, pageSize, useSelection);
    }

}
