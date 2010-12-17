package com.n4systems.services.safetyNetwork.catalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.n4systems.model.AssetType;
import org.apache.log4j.Logger;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.Tenant;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.services.safetyNetwork.CatalogService;
import com.n4systems.services.safetyNetwork.exception.ImportFailureException;
import com.n4systems.tools.Pager;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.persistence.QueryBuilder;

public class CatalogAutoAttributesImportHandler extends CatalogImportHandler {

    private static final Logger logger = Logger.getLogger(CatalogAutoAttributesImportHandler.class);
	
	private AutoAttributeCriteria criteria;
	private AutoAttributeCriteria importCriteria;
	private Map<InfoFieldBean,InfoFieldBean> infoFieldToInfoFieldMapping;
	private AssetType originalType;
	private AssetType importedAssetType;
	
	public CatalogAutoAttributesImportHandler(PersistenceManager persistenceManager, Tenant tenant, CatalogService importCatalog) {
		super(persistenceManager, tenant, importCatalog);
	}
	
	
	public void importCatalog() throws ImportFailureException {
		if (this.originalType.getAutoAttributeCriteria() != null) {
			mapOriginalInfoFieldsToNew();
			try {
				importAutoAttributes();
			} catch (Exception e) {
                logger.error("Error importing auto attributes", e);
				throw new ImportFailureException(e);
			}
		}
	}


	private void importAutoAttributes() {
		criteria = importCatalog.getCriteriaFor(originalType.getId()); 
		importCriteria = new AutoAttributeCriteria();
		
		copyInfoFieldList(criteria.getInputs(), importCriteria.getInputs());
		copyInfoFieldList(criteria.getOutputs(), importCriteria.getOutputs());
		
		importCriteria.setTenant(tenant);
		importCriteria.setAssetType(importedAssetType);
		persistenceManager.save(importCriteria);
        importedAssetType.setAutoAttributeCriteria(importCriteria);
		
		importDefinitions();
	}

	private void importDefinitions() {
		Pager<AutoAttributeDefinition> pager = null;
		int pageNumber = 1;
		while ((pager = importCatalog.getDefinitionPageFor(originalType.getId(), pageNumber, ConfigContext.getCurrentContext().getInteger(ConfigEntry.CATALOG_IMPORTER_PAGE_SIZE))).validPage() ) {
			for (AutoAttributeDefinition definition : pager.getList()) {
				importDefinition(definition);
			}
			pageNumber++;
		}
	}
	
	
	private void importDefinition(AutoAttributeDefinition originalDefinition) {
		AutoAttributeDefinition importedDefinition = new AutoAttributeDefinition();
		importedDefinition.setTenant(tenant);
		importedDefinition.setCriteria(importCriteria);
		importedDefinition.setInputs(copyDefinitionOptions(originalDefinition.getInputs()));
		importedDefinition.setOutputs(copyDefinitionOptions(originalDefinition.getOutputs()));
		persistenceManager.update(importedDefinition);
	}
	
	private List<InfoOptionBean> copyDefinitionOptions(List<InfoOptionBean> sourceOptions) {
		List<InfoOptionBean> copiedInfoOptions = new ArrayList<InfoOptionBean>();
		for (InfoOptionBean input : sourceOptions) {
			InfoOptionBean copiedInput = null;
			InfoFieldBean importedInfoField = infoFieldToInfoFieldMapping.get(input.getInfoField());
			if (input.isStaticData()) {
				for (InfoOptionBean importedInfoOption : importedInfoField.getInfoOptions()) {
					if (importedInfoOption.getName().equals(input.getName())) {
						copiedInput = importedInfoOption;
					}
				}
			} else {
				copiedInput = new InfoOptionBean();
				copiedInput.setInfoField(importedInfoField);
				copiedInput.setStaticData(false);
				copiedInput.setName(input.getName());
			}
				
			copiedInfoOptions.add(copiedInput);
		}
		return(copiedInfoOptions);
	}
	
	private void copyInfoFieldList(List<InfoFieldBean> original, List<InfoFieldBean> copyTo) {
		for (InfoFieldBean infoField : original) {
			if (infoFieldToInfoFieldMapping.get(infoField) != null) {
				copyTo.add(infoFieldToInfoFieldMapping.get(infoField));
			}
		}
	}
	
	private void mapOriginalInfoFieldsToNew() {
		infoFieldToInfoFieldMapping = new HashMap<InfoFieldBean, InfoFieldBean>();
		for (InfoFieldBean originalInfoField : originalType.getInfoFields()) {
			for (InfoFieldBean importedInfoField : importedAssetType.getInfoFields()) {
				if(originalInfoField.getName().equals(importedInfoField.getName())) {
					infoFieldToInfoFieldMapping.put(originalInfoField, importedInfoField);
					break;
				}
			}
		}
	}


	public CatalogAutoAttributesImportHandler setOriginalType(AssetType originalType) {
		this.originalType = originalType;
		return this;
	}


	public CatalogAutoAttributesImportHandler setImportedAssetType(AssetType importedAssetType) {
		this.importedAssetType = importedAssetType;
		return this;
	}
	
	public void rollback() {
        if (importCriteria != null) {
            rollbackDefinitions();
		    persistenceManager.delete(importCriteria);
        }
	}

    public CatalogAutoAttributesImportHandler setImportedCriteria(AutoAttributeCriteria criteria) {
        this.importCriteria = criteria;
        return this;
    }

	private void rollbackDefinitions() {
        if (importCriteria.getId() != null) {
            Pager<AutoAttributeDefinition> pager = null;
            QueryBuilder<AutoAttributeDefinition> existingDefinitionQuery = new QueryBuilder<AutoAttributeDefinition>(AutoAttributeDefinition.class, new OpenSecurityFilter());
            existingDefinitionQuery.addSimpleWhere("criteria", importCriteria);
            while ((pager = persistenceManager.findAllPaged(existingDefinitionQuery, 1, ConfigContext.getCurrentContext().getInteger(ConfigEntry.CATALOG_IMPORTER_PAGE_SIZE))).validPage() ) {
                for (AutoAttributeDefinition definition : pager.getList()) {
                    persistenceManager.delete(definition);
                }
            }
        }
	}
	
}
