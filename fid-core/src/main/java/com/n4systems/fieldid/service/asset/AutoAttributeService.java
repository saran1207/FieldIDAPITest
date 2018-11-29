package com.n4systems.fieldid.service.asset;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.AssetType;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.transaction.annotation.Transactional;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AutoAttributeService extends FieldIdPersistenceService {

    @Transactional
    public AutoAttributeDefinition findTemplateToApply(AssetType assetType, Collection<InfoOptionBean> selectedInfoOptions) {
        // make sure the asset type is attached.
        AssetType pt = persistenceService.find(AssetType.class, assetType.getId());
        return findTemplateToApply(pt.getAutoAttributeCriteria(), selectedInfoOptions);
    }

    public AutoAttributeDefinition findTemplateToApply(AutoAttributeCriteria criteria, Collection<InfoOptionBean> selectedInfoOptions) {
        selectedInfoOptions = filterOutNonCombosOrSelects(selectedInfoOptions);

        if (criteria == null || criteria.getInputs().size() > selectedInfoOptions.size()) {
            return null; // no way to find a template. if the number of
            // inputs is less than the inputs in the
        }

        String queryString = "SELECT def.id from AutoAttributeDefinition def, IN (def.inputs) io WHERE def.criteria = :criteria ";

        if (selectedInfoOptions.size() > 0) {
            queryString += "AND ( ";
            for (int i = 0; i < selectedInfoOptions.size(); i++) {
                if (i != 0) {
                    queryString += "OR ";
                }
                queryString += "io.uniqueID = :infoOptionId" + i + " ";
            }
            queryString += " ) ";
        }

        queryString += "group by def.id having count(io) = :inputSize ";

        Query query = persistenceService.createQuery(queryString);

        query.setParameter("criteria", criteria);
        query.setParameter("inputSize", Long.valueOf(criteria.getInputs().size()));

        int parameterIndex = 0;
        for (InfoOptionBean infoOption : selectedInfoOptions) {
            query.setParameter("infoOptionId" + parameterIndex, infoOption.getUniqueID());
            parameterIndex++;
        }

        try {
            Long definitionId = (Long) query.getSingleResult();

            AutoAttributeDefinition definition = null;
            if (definitionId != null) {
                String[] fetches = { "outputs", "criteria.outputs" };
                definition = persistenceService.find(AutoAttributeDefinition.class, definitionId);
                return definition;
            }

        } catch (NoResultException nr) {
        } catch (NonUniqueResultException nur) {
        }
        return null;
    }

    private Collection<InfoOptionBean> filterOutNonCombosOrSelects(Collection<InfoOptionBean> selectedInfoOptions) {
        List<InfoOptionBean> filteredOptions = new ArrayList<InfoOptionBean>(selectedInfoOptions.size());
        for (InfoOptionBean selectedInfoOption : selectedInfoOptions) {
            if (selectedInfoOption == null) {
                continue;
            }
            InfoFieldBean infoField = selectedInfoOption.getInfoField();
            if (infoField.getType() == InfoFieldBean.InfoFieldType.SelectBox || infoField.getType() == InfoFieldBean.InfoFieldType.ComboBox) {
                filteredOptions.add(selectedInfoOption);
            }
        }
        return filteredOptions;
    }

    public void clearRetiredInfoFields(AssetType assetType) {
        List<InfoFieldBean> retiredFields = new ArrayList<InfoFieldBean>();

        for (InfoFieldBean infoField : assetType.getInfoFields()) {
            if (infoField.isRetired()) {
                retiredFields.add(infoField);
            }
        }

        for (InfoFieldBean field : retiredFields) {
            AutoAttributeCriteria criteria = criteriaUses(assetType, field);
            if (criteria != null) {
                persistenceService.remove(criteria);
                return;
            }
        }
    }

    @Transactional(readOnly = true)
    public AutoAttributeCriteria getAutoAttributeCriteriaWithPostFetches(Long criteriaId) {
        QueryBuilder<AutoAttributeCriteria> query = new QueryBuilder<AutoAttributeCriteria>(AutoAttributeCriteria.class, new OpenSecurityFilter());
        query.addSimpleWhere("id", criteriaId);
        query.addPostFetchPaths("inputs", "outputs", "assetType.name");
        return persistenceService.find(query);
    }

    @Transactional(readOnly = true)
    public AutoAttributeDefinition getAutoAttributeDefinitionWithPostFetches(Long definitionId) {
        QueryBuilder<AutoAttributeDefinition> query = new QueryBuilder<AutoAttributeDefinition>(AutoAttributeDefinition.class, new OpenSecurityFilter());
        query.addSimpleWhere("id", definitionId);
        query.addPostFetchPaths("inputs");
        return persistenceService.find(query);
    }

    @Transactional(readOnly = true)
    public List<AutoAttributeDefinition> getAutoAttributeDefinitionsWithPostFetches(Long criteriaId) {
        QueryBuilder<AutoAttributeDefinition> query = new QueryBuilder<AutoAttributeDefinition>(AutoAttributeDefinition.class, new OpenSecurityFilter());
        query.addSimpleWhere("criteria", getAutoAttributeCriteriaWithPostFetches(criteriaId));
        query.addPostFetchPaths("inputs");
        return persistenceService.findAll(query);
    }

    private AutoAttributeCriteria criteriaUses(AssetType assetType, InfoFieldBean field) {
        if (assetType.getAutoAttributeCriteria() != null) {
            AutoAttributeCriteria criteria = persistenceService.find(AutoAttributeCriteria.class, assetType.getAutoAttributeCriteria().getId());
            if (criteria.getInputs().contains(field) || criteria.getOutputs().contains(field)) {
                return criteria;
            }
        }
        return null;
    }

    @Transactional
    public AutoAttributeCriteria save(AutoAttributeCriteria criteria, User user) {
        criteria.setCreatedBy(user);
        criteria.setModifiedBy(user);
        persistenceService.save(criteria);
        return criteria;
    }

    @Transactional
    public AutoAttributeCriteria update(AutoAttributeCriteria criteria) {
        deleteExistingDefinitions(criteria);
        criteria.touch();
        return persistenceService.update(criteria);
    }

    @Transactional
    public void delete(AutoAttributeCriteria criteria) {
        /* Attach entity first */
        persistenceService.delete(persistenceService.find(AutoAttributeCriteria.class, criteria.getId()));
    }

    @Transactional
    private void deleteExistingDefinitions(AutoAttributeCriteria criteria) {
        QueryBuilder<AutoAttributeDefinition> builder = new QueryBuilder<AutoAttributeDefinition>(AutoAttributeDefinition.class, new OpenSecurityFilter());
        builder.addSimpleWhere("criteria", criteria);
        for (AutoAttributeDefinition definition : persistenceService.findAll(builder)) {
            persistenceService.delete(definition);
        }
    }

    @Transactional
    public AutoAttributeDefinition saveDefinition(AutoAttributeDefinition definition) {
        definition = getEntityManager().merge(definition);
        modifyCriteria(definition);
        return definition;
    }

    @Transactional
    public void removeDefinition(AutoAttributeDefinition definition) {
        definition = persistenceService.find(AutoAttributeDefinition.class, definition.getId());
        persistenceService.remove(definition);
        modifyCriteria(definition);
    }

    @Transactional
    private void modifyCriteria(AutoAttributeDefinition definition) {
        AutoAttributeCriteria criteria = persistenceService.find(AutoAttributeCriteria.class, definition.getCriteria().getId());
        criteria.touch();
        persistenceService.merge(criteria);
    }
}
