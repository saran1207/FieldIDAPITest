package com.n4systems.fieldid.service.procedure;

import com.google.common.base.Preconditions;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.user.UserGroupService;
import com.n4systems.model.Asset;
import com.n4systems.model.IsolationPointSourceType;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.procedure.*;
import com.n4systems.model.user.Assignable;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import com.n4systems.services.date.DateService;
import com.n4systems.util.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import rfid.ejb.entity.InfoOptionBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ProcedureDefinitionService extends FieldIdPersistenceService {

    @Autowired private UserGroupService userGroupService;
    @Autowired private S3Service s3Service;
    @Autowired private DateService dateService;

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
        for (ProcedureDefinitionImage image:procedureDefinition.getImages()) {
            s3Service.finalizeProcedureDefinitionImageUpload(image);
        }
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
            previousDefinition.setRetireDate(dateService.now().toDate());
            persistenceService.update(previousDefinition);
        }
        definition.setPublishedState(PublishedState.PUBLISHED);
        definition.setOriginDate(dateService.now().toDate());
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

    public void deleteProcedureDefinition(ProcedureDefinition procedureDefinition) {
        Preconditions.checkArgument(procedureDefinition.getPublishedState().isPreApproval(), "can't delete a procedure that has been published");
        s3Service.removeProcedureDefinitionImages(procedureDefinition);
        persistenceService.delete(procedureDefinition);
    }

    public ProcedureDefinition copyProcedureDefinition(ProcedureDefinition from, ProcedureDefinition to) {
        Preconditions.checkArgument(from != null && to != null, "can't use null procedure definitions when copying.");
        to.setAsset(from.getAsset());
        to.setTenant(from.getTenant());
        to.setProcedureCode(from.getProcedureCode());
        to.setElectronicIdentifier(from.getElectronicIdentifier());
        to.setWarnings(from.getWarnings());
        to.setDevelopedBy(from.getDevelopedBy());
        to.setEquipmentNumber(from.getEquipmentNumber());
        to.setEquipmentLocation(from.getEquipmentLocation());
        to.setBuilding(from.getBuilding());
        to.setEquipmentDescription(from.getEquipmentDescription());
        to.setPublishedState(PublishedState.DRAFT);
        for(IsolationPoint isolationPoint: from.getIsolationPoints()) {
            IsolationPoint copiedIsolationPoint = copyIsolationPoint(isolationPoint, new IsolationPoint(), true);
            to.getIsolationPoints().add(copiedIsolationPoint);
            if(copiedIsolationPoint.getAnnotation() != null) {
                ProcedureDefinitionImage originalImage = (ProcedureDefinitionImage) isolationPoint.getAnnotation().getImage();
                ProcedureDefinitionImage copiedImage = (ProcedureDefinitionImage) copiedIsolationPoint.getAnnotation().getImage();
                s3Service.copyProcedureDefImageToTemp(originalImage, copiedImage);
                to.addImage(copiedImage);
            }
        }
        return to;
    }

    public IsolationPoint copyIsolationPoint(IsolationPoint from, IsolationPoint to) {
        return copyIsolationPoint(from, to, false);
    }

    public IsolationPoint copyIsolationPoint(IsolationPoint from, IsolationPoint to, boolean isDeepCopy) {
        Preconditions.checkArgument(from != null && to != null, "can't use null isolation points when copying.");

        to.setAnnotation(from.getAnnotation());
        to.setIdentifier(from.getIdentifier());
        to.setLocation(from.getLocation());
        to.setMethod(from.getMethod());
        to.setCheck(from.getCheck());
        to.setSourceType(from.getSourceType());
        to.setTenant(from.getTenant());
        to.setSourceText(from.getSourceText());
        if(from.getDeviceDefinition() != null) {
            to.setDeviceDefinition(copyIsolationDeviceDescription(from.getDeviceDefinition(), new IsolationDeviceDescription()));
        }
        if(from.getLockDefinition() != null) {
            to.setLockDefinition(copyIsolationDeviceDescription(from.getLockDefinition(), new IsolationDeviceDescription()));
        }
        if(from.getAnnotation() != null) {
            to.setAnnotation(copyImageAnnotation(from.getAnnotation(), new ImageAnnotation(), isDeepCopy));
        }
        Date now = dateService.now().toDate();
        to.setCreated(now);
        to.setModified(now);

        return to;
    }

    /**
     * used to copy data backing wicket model. this result is re-attached and peristed.
     */
    public IsolationPoint copyIsolationPointForEditing(IsolationPoint from, IsolationPoint to) {
        Preconditions.checkArgument(from != null && to != null, "can't use null isolation points when copying.");

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
        Date now = dateService.now().toDate();
        to.setCreated(now);
        to.setModified(now);

        return to;
    }

    private IsolationDeviceDescription copyIsolationDeviceDescription(IsolationDeviceDescription from, IsolationDeviceDescription to) {
        Preconditions.checkArgument(from != null && to != null, "can't use null isolation deviceDescription when copying.");
        to.setFreeformDescription(from.getFreeformDescription());
        to.setAssetType(from.getAssetType());
        to.setAttributeValues(new ArrayList<InfoOptionBean>());
        //TODO: Deal with copying the list later...
        //to.setAttributeValues(Lists.newArrayList(from.getAttributeValues()));
        return to;
    }

    private ImageAnnotation copyImageAnnotation(ImageAnnotation from, ImageAnnotation to, boolean isDeepCopy) {
        Preconditions.checkArgument(from != null && to != null, "can't use null isolation annotation when copying.");
        to.setTenant(from.getTenant());
        to.setType(from.getType());
        to.setText(from.getText());
        if (from.isNew() || !isDeepCopy) {
            to.setImage(from.getImage());
        } else {
            to.setImage(copyEditableImage((ProcedureDefinitionImage) from.getImage(), new ProcedureDefinitionImage(), to));
        }
        Date now = dateService.now().toDate();
        to.setCreated(now);
        to.setModified(now);
        return to;
    }

    private ProcedureDefinitionImage copyEditableImage(ProcedureDefinitionImage from, ProcedureDefinitionImage to, ImageAnnotation imageAnnotation) {
        Preconditions.checkArgument(from != null && to != null, "can't use null isolation annotation when copying.");
        to.setTenant(from.getTenant());
        to.addImageAnnotation(imageAnnotation);
        to.setFileName(from.getFileName());
        to.setMobileGUID(from.getMobileGUID());
        Date now = dateService.now().toDate();
        to.setCreated(now);
        to.setModified(now);
        return to;
    }
}
