package com.n4systems.fieldid.service.asset;

import com.n4systems.ejb.PageHolder;
import com.n4systems.fieldid.service.export.ExcelExportService;
import com.n4systems.fieldid.service.search.columns.AssetTextOrFilterSearchService;
import com.n4systems.model.search.AssetSearchCriteria;
import com.n4systems.util.persistence.search.ResultTransformer;
import com.n4systems.util.views.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class AssetExcelExportService extends ExcelExportService<AssetSearchCriteria> {

    @Autowired private AssetTextOrFilterSearchService assetTextOrFilterSearchService;

    @Override
    protected int countTotalResults(AssetSearchCriteria criteria) {
        return assetTextOrFilterSearchService.countPages(criteria, 1L);
    }

    @Override
    protected PageHolder<TableView> performSearch(AssetSearchCriteria criteria, ResultTransformer<TableView> resultTransformer, int pageNumber, int pageSize, boolean useSelection) {
        return assetTextOrFilterSearchService.performSearch(criteria,  resultTransformer, pageNumber, pageSize, useSelection);
    }

}
