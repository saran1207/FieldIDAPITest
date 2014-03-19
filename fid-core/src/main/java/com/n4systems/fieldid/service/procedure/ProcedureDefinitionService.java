package com.n4systems.fieldid.service.procedure;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.user.UserGroupService;
import com.n4systems.fieldid.service.uuid.AtomicLongService;
import com.n4systems.model.Asset;
import com.n4systems.model.IsolationPointSourceType;
import com.n4systems.model.common.EditableImage;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.procedure.*;
import com.n4systems.model.user.Assignable;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import com.n4systems.services.date.DateService;
import com.n4systems.util.persistence.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import rfid.ejb.entity.InfoOptionBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;



public class ProcedureDefinitionService extends FieldIdPersistenceService {

    private static final Logger logger=Logger.getLogger(ProcedureDefinitionService.class);

    @Autowired private UserGroupService userGroupService;
    @Autowired private S3Service s3Service;
    @Autowired private DateService dateService;
    @Autowired private AtomicLongService atomicLongService;


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
            procedureDefinition.setAuthorizationNotificationSent(false);
            persistenceService.update(procedureDefinition);
        } else {
            publishProcedureDefinition(procedureDefinition);
        }
    }

    /*package protected for testing purposes*/
    Long generateRevisionNumber(Asset asset) {
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
            previousDefinition.setRetireDate(dateService.nowUTC().toDate());
            persistenceService.update(previousDefinition);
        }
        definition.setPublishedState(PublishedState.PUBLISHED);
        definition.setOriginDate(dateService.nowUTC().toDate());
        persistenceService.update(definition);
    }

    public boolean isProcedureApprovalRequiredForCurrentUser() {
        Assignable approvalUserOrGroup = getCurrentTenant().getSettings().getApprovalUserOrGroup();
        if (approvalUserOrGroup == null) {
            // There is no approval user/group, no approval required
            return false;
        }
        return !canCurrentUserApprove();
    }

    public boolean canCurrentUserApprove() {
        Assignable approvalUserOrGroup = getCurrentTenant().getSettings().getApprovalUserOrGroup();

        if (approvalUserOrGroup instanceof User && getCurrentUser().equals(approvalUserOrGroup)) {
            // The current user is the approval user, no approval required
            return true;
        }
        if (approvalUserOrGroup instanceof UserGroup && userGroupService.getUsersInGroup((UserGroup) approvalUserOrGroup).contains(getCurrentUser())) {
            // The current user is in the approval user group, no approval required
            return true;
        }
        return false;
    }

    public void deleteProcedureDefinition(ProcedureDefinition procedureDefinition) {
        Preconditions.checkArgument(procedureDefinition.getPublishedState().isPreApproval(), "can't delete a procedure that has been published");
        s3Service.removeProcedureDefinitionImages(procedureDefinition);
        for (IsolationPoint isolationPoint: procedureDefinition.getLockIsolationPoints()) {
            ImageAnnotation imageAnnotation = isolationPoint.getAnnotation();
            isolationPoint.setAnnotation(null);
            persistenceService.update(isolationPoint);

            if (null != imageAnnotation) {
                persistenceService.delete(imageAnnotation);
            }

        }
        persistenceService.delete(procedureDefinition);
    }

    public ProcedureDefinition cloneProcedureDefinition(ProcedureDefinition source) {
        Preconditions.checkArgument(source != null, "can't use null procedure definitions when cloning.");
        ProcedureDefinition to = new ProcedureDefinition();
        to.setAsset(source.getAsset());
        to.setTenant(source.getTenant());
        to.setProcedureCode(source.getProcedureCode());
        to.setElectronicIdentifier(source.getElectronicIdentifier());
        to.setWarnings(source.getWarnings());
        to.setDevelopedBy(getCurrentUser());
        to.setEquipmentNumber(source.getEquipmentNumber());
        to.setEquipmentLocation(source.getEquipmentLocation());
        to.setBuilding(source.getBuilding());
        to.setEquipmentDescription(source.getEquipmentDescription());
        to.setPublishedState(PublishedState.DRAFT);

        Map<String, ProcedureDefinitionImage> clonedImages = cloneImages(source,to);
        to.setImages(Lists.newArrayList(clonedImages.values()));

        for(IsolationPoint isolationPoint: source.getLockIsolationPoints()) {
            IsolationPoint copiedIsolationPoint = cloneIsolationPoint(isolationPoint, clonedImages);
            to.addIsolationPoint(copiedIsolationPoint);
        }

        return to;
    }

    private IsolationPoint cloneIsolationPoint(IsolationPoint source, Map<String, ProcedureDefinitionImage> clonedImages) {
        Preconditions.checkArgument(source != null , "can't use null isolation points when cloning.");

        IsolationPoint isolationPoint = new IsolationPoint();
        isolationPoint.setIdentifier(source.getIdentifier());
        isolationPoint.setLocation(source.getLocation());
        isolationPoint.setMethod(source.getMethod());
        isolationPoint.setCheck(source.getCheck());
        isolationPoint.setSourceType(source.getSourceType());
        isolationPoint.setTenant(source.getTenant());
        isolationPoint.setSourceText(source.getSourceText());
        isolationPoint.setElectronicIdentifier(source.getElectronicIdentifier());
        if(source.getDeviceDefinition() != null) {
            isolationPoint.setDeviceDefinition(cloneIsolationDeviceDescription(source.getDeviceDefinition(), new IsolationDeviceDescription()));
        }
        if(source.getLockDefinition() != null) {
            isolationPoint.setLockDefinition(cloneIsolationDeviceDescription(source.getLockDefinition(), new IsolationDeviceDescription()));
        }
        if(source.getAnnotation() != null) {
            isolationPoint.setAnnotation(cloneImageAnnotation(source.getAnnotation(), clonedImages));
        }
        isolationPoint.setFwdIdx(source.getFwdIdx());
        isolationPoint.setRevIdx(source.getRevIdx());
        return isolationPoint;
    }

    private IsolationDeviceDescription cloneIsolationDeviceDescription(IsolationDeviceDescription from, IsolationDeviceDescription to) {
        Preconditions.checkArgument(from != null && to != null, "can't use null isolation deviceDescription when cloning.");
        to.setFreeformDescription(from.getFreeformDescription());
        to.setAssetType(from.getAssetType());
        to.setAttributeValues(new ArrayList<InfoOptionBean>());
        //TODO: Deal with copying the list later...currently not used.
        //to.setAttributeValues(Lists.newArrayList(from.getAttributeValues()));
        return to;
    }

    private ImageAnnotation cloneImageAnnotation(ImageAnnotation from, Map<String, ProcedureDefinitionImage> clonedImages) {
        Preconditions.checkArgument(from != null, "can't use null isolation annotation when cloning.");

        ImageAnnotation to = cloneImageAnnotation(from, clonedImages.get(from.getImage().getFileName()) );
        to.setTempId(atomicLongService.getNext());

        return to;
    }

    public ImageAnnotation cloneImageAnnotation(ImageAnnotation from, EditableImage image) {
        ImageAnnotation to = new ImageAnnotation();
        to.setTenant(from.getTenant());
        to.setType(from.getType());
        to.setText(from.getText());
        to.setX(from.getX());
        to.setY(from.getY());
        to.setImage(image);
        image.addAnnotation(to);
        return to;
    }

    private Map<String,ProcedureDefinitionImage> cloneImages(ProcedureDefinition from, ProcedureDefinition to) {
        Map<String, ProcedureDefinitionImage> result = Maps.newHashMap();
        for (ProcedureDefinitionImage image:from.getImages()) {
            ProcedureDefinitionImage imageCopy = cloneEditableImage(image);
            imageCopy.setProcedureDefinition(to);
            result.put(image.getFileName(), imageCopy);
            s3Service.copyProcedureDefImageToTemp(image, imageCopy);
        }
        return result;
    }

    private ProcedureDefinitionImage cloneEditableImage(ProcedureDefinitionImage from) {
        Preconditions.checkArgument(from != null, "can't use null isolation annotation when cloning.");
        ProcedureDefinitionImage to = new ProcedureDefinitionImage();
        to.setTenant(from.getTenant());
        to.setFileName(from.getFileName());
        return to;
    }

    /**
     * used to copy data backing wicket model. this result is re-attached and peristed.
     * if you want to actually make a copy, use the cloning methods.
     */
    public IsolationPoint copyIsolationPointForEditing(IsolationPoint from, IsolationPoint to) {
        Preconditions.checkArgument(from != null && to != null, "can't use null isolation points when copying.");

        to.setId(from.getId());
        // normalize isolation point id === annotation text.
        if (from.getAnnotation()!=null) {
            from.getAnnotation().setText(from.getIdentifier());
        }
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
        to.setElectronicIdentifier(from.getElectronicIdentifier());
        to.setFwdIdx(from.getFwdIdx());
        to.setRevIdx(from.getRevIdx());

        return to;
    }


    public ImageAnnotation addImageAnnotationToImage(EditableImage image, ImageAnnotation annotation) {
        if(annotation.getImage()!=null && annotation.getImage()!=image) {
            annotation.getImage().removeAnnotation(annotation);
        }
        image.addAnnotation(annotation);
        annotation.setImage(image);
        annotation.setTenant(image.getTenant());
        return annotation;
    }
}
