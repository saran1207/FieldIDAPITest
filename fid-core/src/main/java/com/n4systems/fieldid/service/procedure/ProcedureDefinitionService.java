package com.n4systems.fieldid.service.procedure;

import com.google.common.base.Preconditions;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.user.UserGroupService;
import com.n4systems.model.Asset;
import com.n4systems.model.IsolationPointSourceType;
import com.n4systems.model.procedure.*;
import com.n4systems.model.user.Assignable;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import com.n4systems.util.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class ProcedureDefinitionService extends FieldIdPersistenceService {

    @Autowired private UserGroupService userGroupService;

    public Boolean hasPublishedProcedureDefinition(Asset asset) {
        return persistenceService.exists(getPublishedProcedureDefinitionQuery(asset));
    }

    public ProcedureDefinition getPublishedProcedureDefinition(Asset asset) {
        return persistenceService.find(getPublishedProcedureDefinitionQuery(asset));
    }

    private QueryBuilder<ProcedureDefinition> getPublishedProcedureDefinitionQuery(Asset asset) {
        QueryBuilder<ProcedureDefinition> query = createTenantSecurityBuilder(ProcedureDefinition.class);

        query.addSimpleWhere("asset", asset);
        query.addSimpleWhere("publishedState", PublishedState.PUBLISHED);

        return query;
    }


    public void saveProcedureDefinitionDraft(ProcedureDefinition procedureDefinition) {
        if (procedureDefinition.getRevisionNumber() == null) {
            procedureDefinition.setRevisionNumber(generateRevisionNumber(procedureDefinition.getAsset()));
        }
        persistenceService.saveOrUpdate(procedureDefinition);
    }

    public void saveProcedureDefinition(ProcedureDefinition procedureDefinition) {
        saveProcedureDefinitionDraft(procedureDefinition);

        if (isProcedureApprovalRequiredForCurrentUser()) {
            procedureDefinition.setPublishedState(PublishedState.WAITING_FOR_APPROVAL);
            persistenceService.update(procedureDefinition);
        } else {
            publishProcedureDefinition(procedureDefinition);
        }
    }

    private Long generateRevisionNumber(Asset asset) {
        QueryBuilder<Long> query = new QueryBuilder<Long>(ProcedureDefinition.class, securityContext.getTenantSecurityFilter());
        query.addSimpleWhere("asset", asset);
        query.setSelectArgument(new MaxSelect("revisionNumber"));
        Long biggestRevision = persistenceService.find(query);
        return biggestRevision==null ? 1 :  biggestRevision+1;
    }

    public List<ProcedureDefinition> getActiveProceduresForAsset(Asset asset) {
        QueryBuilder<ProcedureDefinition> query = createUserSecurityBuilder(ProcedureDefinition.class);
        query.addSimpleWhere("asset", asset);
        query.addWhere(WhereParameter.Comparator.IN, "publishedState", "publishedState", Arrays.asList(PublishedState.ACTIVE_STATES));

        return persistenceService.findAll(query);
    }

    public List<ProcedureDefinition> getPreviouslyPublishedProceduresForAsset(Asset asset) {
        QueryBuilder<ProcedureDefinition> query = createUserSecurityBuilder(ProcedureDefinition.class);
        query.addSimpleWhere("asset", asset);
        query.addSimpleWhere("publishedState", PublishedState.PREVIOUSLY_PUBLISHED);

        return persistenceService.findAll(query);
    }

    public List<String> getPreConfiguredDevices(IsolationPointSourceType sourceType) {
        QueryBuilder<String> query = new QueryBuilder<String>(PreconfiguredDevice.class);
        query.setSimpleSelect("device");

        if(sourceType != null) {
            WhereParameterGroup sourceGroup = new WhereParameterGroup("isolationPointSourceType");
            sourceGroup.addClause(WhereClauseFactory.createIsNull("isolationPointSourceType"));
            sourceGroup.addClause(WhereClauseFactory.create("isolationPointSourceType", sourceType, WhereClause.ChainOp.OR));
            query.addWhere(sourceGroup);
        } else {
            query.addWhere(WhereClauseFactory.createIsNull("isolationPointSourceType"));
        }
        return persistenceService.findAll(query);
    }

    public void publishProcedureDefinition(ProcedureDefinition definition) {
        ProcedureDefinition previousDefinition = getPublishedProcedureDefinition(definition.getAsset());
        if (previousDefinition != null) {
            previousDefinition.setPublishedState(PublishedState.PREVIOUSLY_PUBLISHED);
            persistenceService.update(previousDefinition);
        }
        definition.setPublishedState(PublishedState.PUBLISHED);
        persistenceService.update(definition);
    }

    public boolean isProcedureApprovalRequiredForCurrentUser() {
        Assignable approvalUserOrGroup = getCurrentTenant().getSettings().getApprovalUserOrGroup();
        if (approvalUserOrGroup == null) {
            // There is no approval user/group, no approval required
            return false;
        }
        if (approvalUserOrGroup instanceof User && getCurrentUser().equals(approvalUserOrGroup)) {
            // The current user is the approval user, no approval required
            return false;
        }
        if (approvalUserOrGroup instanceof UserGroup && userGroupService.getUsersInGroup((UserGroup) approvalUserOrGroup).contains(getCurrentUser())) {
            // The current user is in the approval user group, no approval required
            return false;
        }
        return true;
    }

    public IsolationPoint copyIsolationPoint(IsolationPoint from, IsolationPoint to) {
        Preconditions.checkArgument(from != null && to != null, "can't use null isolation points when copying.");

        to.setId(from.getId());
        to.setAnnotation(from.getAnnotation());
        to.setIdentifier(from.getIdentifier());
        to.setLocation(from.getLocation());
        to.setMethod(from.getMethod());
        to.setCheck(from.getCheck());
        to.setSourceType(from.getSourceType());
        to.setTenant(from.getTenant());
        to.setSourceText(from.getSourceText());
        to.setDeviceDefinition(from.getDeviceDefinition());
        to.setLockDefinition(from.getLockDefinition());

        return to;
    }

    public void deleteProcedureDefinition(ProcedureDefinition procedureDefinition) {
        persistenceService.delete(procedureDefinition);
    }

}
