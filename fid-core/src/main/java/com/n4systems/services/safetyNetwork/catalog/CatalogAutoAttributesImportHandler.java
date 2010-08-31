package com.n4systems.services.safetyNetwork.catalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.ProductType;
import com.n4systems.model.Tenant;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.services.safetyNetwork.CatalogService;
import com.n4systems.services.safetyNetwork.exception.ImportFailureException;
import com.n4systems.tools.Pager;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.persistence.QueryBuilder;

public class CatalogAutoAttributesImportHandler extends CatalogImportHandler {
	
	private AutoAttributeCriteria criteria;
	private AutoAttributeCriteria importCriteria;
	private Map<InfoFieldBean,InfoFieldBean> infoFieldToInfoFieldMapping;
	private ProductType originalType;
	private ProductType importedProductType;
	
	public CatalogAutoAttributesImportHandler(PersistenceManager persistenceManager, Tenant tenant, CatalogService importCatalog) {
		super(persistenceManager, tenant, importCatalog);
	}
	
	
	public void importCatalog() throws ImportFailureException {
		if (this.originalType.getAutoAttributeCriteria() != null) {
			mapOriginalInfoFieldsToNew();
			try {
				importAutoAttributes();
			} catch (Exception e) {
				throw new ImportFailureException();
			}
		}
	}


	private void importAutoAttributes() {
		criteria = importCatalog.getCriteriaFor(originalType.getId()); 
		importCriteria = new AutoAttributeCriteria();
		
		copyInfoFieldList(criteria.getInputs(), importCriteria.getInputs());
		copyInfoFieldList(criteria.getOutputs(), importCriteria.getOutputs());
		
		importCriteria.setTenant(tenant);
		importCriteria.setProductType(importedProductType);
		persistenceManager.save(importCriteria);
		
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
			for (InfoFieldBean importedInfoField : importedProductType.getInfoFields()) {
				if(originalInfoField.getName().equals(importedInfoField.getName())) {
					infoFieldToInfoFieldMapping.put(originalInfoField, importedInfoField);
					break;
				}
			}
		}
	}


	public CatalogAutoAttributesImportHandler setOriginalType(ProductType originalType) {
		this.originalType = originalType;
		return this;
	}


	public CatalogAutoAttributesImportHandler setImportedProductType(ProductType importedProductType) {
		this.importedProductType = importedProductType;
		return this;
	}
	
	public void rollback() {
		rollbackDefinitions();
		persistenceManager.delete(importCriteria);
	}

	private void rollbackDefinitions() {
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
