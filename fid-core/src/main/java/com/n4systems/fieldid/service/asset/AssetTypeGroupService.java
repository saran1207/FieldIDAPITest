package com.n4systems.fieldid.service.asset;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.search.SavedReportService;
import com.n4systems.fieldid.service.search.SavedSearchRemoveFilter;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.util.AssetTypeGroupRemovalSummary;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

public class AssetTypeGroupService extends FieldIdPersistenceService {

    @Autowired
    private SavedReportService savedReportService;

    private List<Long> reorderedIdList = new ArrayList<Long>();
    private List<AssetTypeGroup> groups;

    @Transactional
	public AssetTypeGroupRemovalSummary testDelete(AssetTypeGroup group) {
		AssetTypeGroupRemovalSummary summary = new AssetTypeGroupRemovalSummary(group);
		QueryBuilder<AssetType> countQuery = new QueryBuilder<AssetType>(AssetType.class, new OpenSecurityFilter());
		countQuery.addSimpleWhere("group", group);
        summary.setAssetTypesConnected(persistenceService.count(countQuery));

        final QueryBuilder<SearchCriteria> savedItemCountQuery = createTenantSecurityBuilder(SearchCriteria.class);
        savedItemCountQuery.addSimpleWhere("assetTypeGroup", group);
        final List<SearchCriteria> all = persistenceService.findAll(savedItemCountQuery);

        summary.setSavedReportsConnected((long)all.size());

		return summary;
	}

    @Transactional
	public void deleteAssetTypeGroup(final AssetTypeGroup group) {
        savedReportService.deleteAllSavedSearchesMatching(new SavedSearchRemoveFilter() {
            @Override
            public boolean removeThisSearch(SearchCriteria searchCriteria) {
                return searchCriteria.getAssetTypeGroup() != null && group.getId().equals(searchCriteria.getAssetTypeGroup().getId());
            }
        });

		AssetTypeGroup groupToDelete = persistenceService.find(AssetTypeGroup.class, group.getId());

        Map<String,Object> params = new HashMap<String,Object>();
		params.put("group", groupToDelete);
        Query clearAssetTypesQuery = persistenceService.createQuery("UPDATE " + AssetType.class.getName() + " assetType SET assetType.group = null WHERE assetType.group = :group", params);
        clearAssetTypesQuery.executeUpdate();

        persistenceService.delete(groupToDelete);
	}

    public AssetTypeGroup getAssetTypeGroupById(Long id) {
        return persistenceService.find(AssetTypeGroup.class, id);
    }

    public List<AssetTypeGroup> getAllAssetTypeGroups() {
        QueryBuilder<AssetTypeGroup> query = createTenantSecurityBuilder(AssetTypeGroup.class);
        query.addOrder("orderIdx");
        return persistenceService.findAll(query);
    }

    public boolean isNameExists(String name) {
        QueryBuilder<AssetTypeGroup> query = createTenantSecurityBuilder(AssetTypeGroup.class);
        query.addWhere(WhereClauseFactory.create("name", name));
        return persistenceService.exists(query);
    }

    public AssetTypeGroup saveAssetTypeGroup(AssetTypeGroup assetTypeGroup) {
        AssetTypeGroup oldPI = null;
        if( assetTypeGroup.getId() != null ) {
            oldPI = persistenceService.find( AssetTypeGroup.class, assetTypeGroup.getId());
        }
//        assetTypeGroup.touch();
        assetTypeGroup = (AssetTypeGroup) persistenceService.saveOrUpdate(assetTypeGroup);

        return assetTypeGroup;
    }

    public Long getNextAvailableIndex() {
        QueryBuilder<AssetTypeGroup> query = createTenantSecurityBuilder(AssetTypeGroup.class);
        return persistenceService.count(query);
    }

    public String reorderAssetTypeGroup() {
        List<AssetTypeGroup> reorderedGroupList = new ArrayList<AssetTypeGroup>();
        for (int i = 0; i < reorderedIdList.size(); i++) {
            Long id = reorderedIdList.get(i);
            for (AssetTypeGroup group : getGroups()) {
                if (group.getId().equals(id)) {
                    reorderedGroupList.add(group);
                    getGroups().remove(group);
                    break;
                }
            }
        }
        reorderedGroupList.addAll(getGroups());
        for (int i = 0; i < reorderedGroupList.size(); i++) {
            AssetTypeGroup group = reorderedGroupList.get(i);
            group.setOrderIdx(new Long(i));
        }
        persistenceService.updateAll(reorderedGroupList, getCurrentUser().getId());
        return null;
    }

    public List<AssetTypeGroup> getGroups() {
        if (groups == null) {
            groups = getAllAssetTypeGroups();
        }
        return groups;
    }


}
