package com.n4systems.fieldid.service.asset;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.n4systems.model.AssetType;
import com.n4systems.model.asset.ScheduleSummaryEntry;
import com.n4systems.model.security.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.ReportServiceHelper;
import com.n4systems.model.Asset;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.services.reporting.AssetsIdentifiedReportRecord;
import com.n4systems.services.reporting.AssetsStatusReportRecord;
import com.n4systems.util.chart.ChartGranularity;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClause.ChainOp;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.n4systems.util.persistence.WhereParameterGroup;

public class AssetService extends FieldIdPersistenceService {
	
	@Autowired private ReportServiceHelper reportServiceHelper;

    @Transactional(readOnly=true)
    public Long countAssets() {
        QueryBuilder<Asset> builder = new QueryBuilder<Asset>(Asset.class, securityContext.getTenantSecurityFilter());
        return persistenceService.count(builder);
    }

	@Transactional(readOnly=true)
	public List<AssetsIdentifiedReportRecord> getAssetsIdentified(ChartGranularity granularity, Date fromDate, Date toDate, BaseOrg org) {
		QueryBuilder<AssetsIdentifiedReportRecord> builder = new QueryBuilder<AssetsIdentifiedReportRecord>(Asset.class, securityContext.getUserSecurityFilter());
		
		NewObjectSelect select = new NewObjectSelect(AssetsIdentifiedReportRecord.class);
		List<String> args = Lists.newArrayList("COUNT(*)");
		args.addAll(reportServiceHelper.getSelectConstructorArgsForGranularity("identified", granularity));
		select.setConstructorArgs(args);
		
		builder.setSelectArgument(select);
		builder.addGroupByClauses(reportServiceHelper.getGroupByClausesByGranularity(granularity,"identified"));
				
		WhereParameterGroup filterGroup = new WhereParameterGroup("filtergroup");		
		filterGroup.addClause(WhereClauseFactory.create(Comparator.GE, "from", "identified", fromDate, null, ChainOp.AND));
		filterGroup.addClause(WhereClauseFactory.create(Comparator.LT, "to", "identified", toDate, null, ChainOp.AND));
		builder.addWhere(filterGroup);
		
		if (org!=null) { 
			builder.addSimpleWhere("owner.id", org.getId());
		}
		builder.addOrder("identified");
		
		return persistenceService.findAll(builder);				
	}
	
	public List<AssetsStatusReportRecord> getAssetsStatus(Date fromDate, Date toDate, BaseOrg org) {
		QueryBuilder<AssetsStatusReportRecord> builder = new QueryBuilder<AssetsStatusReportRecord>(Asset.class, securityContext.getUserSecurityFilter());
		
		builder.setSelectArgument(new NewObjectSelect(AssetsStatusReportRecord.class, "assetStatus.name", "COUNT(*)"));		
		WhereParameterGroup filterGroup = new WhereParameterGroup("filtergroup");		
		filterGroup.addClause(WhereClauseFactory.create(Comparator.GE, "from", "identified", fromDate, null, ChainOp.AND));
		filterGroup.addClause(WhereClauseFactory.create(Comparator.LT, "to", "identified", toDate, null, ChainOp.AND));
		builder.addWhere(filterGroup);
		builder.addGroupBy("assetStatus.name");
		if (org!=null) { 
			builder.addSimpleWhere("owner.id", org.getId());
		}		
		
		return persistenceService.findAll(builder);
	}

    public List<ScheduleSummaryEntry> getAssetScheduleSummary(List<Long> assetIds) {
        List<Asset> assets = getAssets(assetIds);
        Map<AssetType, ScheduleSummaryEntry> summaryEntryMap = new HashMap<AssetType, ScheduleSummaryEntry>();
        for (Asset asset : assets) {
            AssetType assetType = asset.getType();

            if (!summaryEntryMap.containsKey(assetType)) {
                ScheduleSummaryEntry scheduleSummaryEntry = new ScheduleSummaryEntry();
                scheduleSummaryEntry.setAssetType(assetType);
                summaryEntryMap.put(assetType, scheduleSummaryEntry);
            }

            ScheduleSummaryEntry scheduleSummaryEntry = summaryEntryMap.get(assetType);
            scheduleSummaryEntry.getAssetIds().add(asset.getId());
        }

        return new ArrayList<ScheduleSummaryEntry>(summaryEntryMap.values());
    }

    public List<Asset> getAssets(List<Long> assetIds) {
        QueryBuilder<Asset> query = new QueryBuilder<Asset>(Asset.class, securityContext.getUserSecurityFilter());
        query.addWhere(WhereClauseFactory.create(Comparator.IN, "id", assetIds));
        return persistenceService.findAll(query);
    }

}
