package com.n4systems.model.procedure;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.n4systems.model.Asset;
import com.n4systems.model.api.Listable;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.parents.ArchivableEntityWithTenant;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.user.User;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "procedure_definitions")
public class ProcedureDefinition extends ArchivableEntityWithTenant implements Listable<Long> {

    public static final SecurityDefiner createSecurityDefiner() {
        return new SecurityDefiner("tenant.id", "asset.owner", null, "state", true);
    }

    @ManyToOne
    @JoinColumn(name = "asset_id")
    private Asset asset;

    @Column(name="procedure_code")
    private String procedureCode;

    @Column(name="electronic_identifier")
    private String electronicIdentifier;

    @Column(name="revision_number")
    private Long revisionNumber;

    @Column(name="warnings")
    private String warnings;

    @Column(name="complete_points_in_order")
    private boolean completeIsolationPointInOrder;

    @ManyToOne
    @JoinColumn(name = "developed_by_id")
    private User developedBy;

    @Column(name="equipment_number")
    private String equipmentNumber;

    @Column(name="equipment_location")
    private String equipmentLocation;

    private String building;

    @Column(name="equipment_description")
    private String equipmentDescription;

    @Column(name="published_state")
    @Enumerated(EnumType.STRING)
    private PublishedState publishedState;

    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name="procedure_definitions_isolation_points", joinColumns = @JoinColumn(name = "procedure_definition_id"), inverseJoinColumns = @JoinColumn(name = "isolation_point_id"))
    private Set<IsolationPoint> isolationPoints = Sets.newHashSet();

    @OneToMany(mappedBy = "procedureDefinition", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ProcedureDefinitionImage> images = Lists.newArrayList();

    @Column(name="origin_date")
    private Date originDate;

    @Column(name="retire_date")
    private Date retireDate;

    @ManyToOne
    @JoinColumn(name = "approved_by_id")
    private User approvedBy;

    @Column(name="auth_notification_sent")
    private boolean authorizationNotificationSent = false;

    @Column(name="rejected_date")
    private Date rejectedDate;

    @ManyToOne
    @JoinColumn(name = "rejected_by_id")
    private User rejectedBy;

    @Column(name="rejected_reason")
    private String rejectedReason;

    @Column(name="family_id", nullable = false)
    private Long familyId;

    @Column(name="unpublished_date")
    private Date unpublishedDate;
    @ManyToOne
    @JoinColumn(name = "unpublished_by_id")
    private User unpublishedBy;

    public String getProcedureCode() {
        return procedureCode;
    }

    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }

    public String getElectronicIdentifier() {
        return electronicIdentifier;
    }

    public void setElectronicIdentifier(String electronicIdentifier) {
        this.electronicIdentifier = electronicIdentifier;
    }

    public String getWarnings() {
        return warnings;
    }

    public void setWarnings(String warnings) {
        this.warnings = warnings;
    }

    public boolean isCompleteIsolationPointInOrder() {
        return completeIsolationPointInOrder;
    }

    public void setCompleteIsolationPointInOrder(boolean completeIsolationPointInOrder) {
        this.completeIsolationPointInOrder = completeIsolationPointInOrder;
    }

    public User getDevelopedBy() {
        return developedBy;
    }

    public void setDevelopedBy(User developedBy) {
        this.developedBy = developedBy;
    }

    public String getEquipmentNumber() {
        return equipmentNumber;
    }

    public void setEquipmentNumber(String equipmentNumber) {
        this.equipmentNumber = equipmentNumber;
    }

    public String getEquipmentLocation() {
        return equipmentLocation;
    }

    public void setEquipmentLocation(String equipmentLocation) {
        this.equipmentLocation = equipmentLocation;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getEquipmentDescription() {
        return equipmentDescription;
    }

    public void setEquipmentDescription(String equipmentDescription) {
        this.equipmentDescription = equipmentDescription;
    }

    @Deprecated
    public List<IsolationPoint> getIsolationPoints() {
        return getLockIsolationPoints();
    }

    @Deprecated
    public void setIsolationPoints(List<IsolationPoint> isolationPoints) {
        this.isolationPoints = Sets.newHashSet(isolationPoints);
    }

    public List<IsolationPoint> getLockIsolationPoints() {
        List<IsolationPoint> lockIsolationPoints = Lists.newArrayList(isolationPoints);
        Collections.sort(lockIsolationPoints, IsolationPoint.LOCK_ORDER);
        return lockIsolationPoints;
    }

    public List<IsolationPoint> getUnlockIsolationPoints() {
        List<IsolationPoint> unlockIsolationPoints = Lists.newArrayList(isolationPoints);
        Collections.sort(unlockIsolationPoints, IsolationPoint.UNLOCK_ORDER);
        return unlockIsolationPoints;
    }

    public void addIsolationPoint(IsolationPoint isolationPoint) {
        int index = 1;
        for (IsolationPoint point: getUnlockIsolationPoints()) {
            point.setRevIdx(index++);
        }
        isolationPoint.setFwdIdx(isolationPoints.size());
        isolationPoints.add(isolationPoint);
    }

    public void reindexUnlockIsolationPoints(List<IsolationPoint> isolationPointList) {
        int i = 0;
        for(IsolationPoint point: isolationPointList) {
            point.setRevIdx(i++);
        }
    }

    public void reindexLockIsolationPoints(List<IsolationPoint> isolationPointList) {
        int i = 0;
        for(IsolationPoint point: isolationPointList) {
            point.setFwdIdx(i++);
        }
    }

    public List<ProcedureDefinitionImage> getImages() {
        return images;
    }

    public void setImages(List<ProcedureDefinitionImage> images) {
        this.images = images;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public String getDisplayName() {
        return procedureCode;
    }

    public PublishedState getPublishedState() {
        return publishedState;
    }

    public void setPublishedState(PublishedState publishedState) {
        this.publishedState = publishedState;
    }

    public Long getRevisionNumber() {
        return revisionNumber;
    }

    public void setRevisionNumber(Long revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    public Date getOriginDate() {
        return originDate;
    }

    public void setOriginDate(Date originDate) {
        this.originDate = originDate;
    }

    public Date getRetireDate() {
        return retireDate;
    }

    public void setRetireDate(Date retireDate) {
        this.retireDate = retireDate;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
    }

    public boolean isAuthorizationNotificationSent() {
        return authorizationNotificationSent;
    }

    public void setAuthorizationNotificationSent(boolean authorizationNotificationSent) {
        this.authorizationNotificationSent = authorizationNotificationSent;
    }

    public void addImage(ProcedureDefinitionImage image) {
        if (!getImages().contains(image)) {
            getImages().add(image);
        }
        if(image.getProcedureDefinition() == null) {
            image.setProcedureDefinition(this);
        }
    }

    public void removeIsolationPoint(IsolationPoint isolationPoint) {
        softDeleteImage(isolationPoint.getAnnotation());

        List<IsolationPoint> isolationPointList = getLockIsolationPoints();
        isolationPointList.remove(isolationPoint);

        isolationPoints.remove(isolationPoint);

        reindexLockIsolationPoints(isolationPointList);
        reindexUnlockIsolationPoints(getUnlockIsolationPoints());
    }

    public void softDeleteImage(ImageAnnotation annotation) {
        if (annotation==null) {
            return;
        }
        boolean usedInOtherIsolationPoint = false;

        ProcedureDefinitionImage image = (ProcedureDefinitionImage) annotation.getImage();

        for (IsolationPoint isolationPoint:getLockIsolationPoints()) {
            if (null != isolationPoint.getAnnotation() && !annotation.equals(isolationPoint.getAnnotation()) && annotation.getImage().equals(isolationPoint.getAnnotation().getImage())) {
                usedInOtherIsolationPoint = true;
            }
        }
        image.removeAnnotation(annotation);

        if (!usedInOtherIsolationPoint) {
            getImages().remove(image);
        }
    }

    public User getRejectedBy() {
        return rejectedBy;
    }

    public void setRejectedBy(User rejectedBy) {
        this.rejectedBy = rejectedBy;
    }

    public Date getRejectedDate() {
        return rejectedDate;
    }

    public void setRejectedDate(Date rejectedDate) {
        this.rejectedDate = rejectedDate;
    }

    public String getRejectedReason() {
        return rejectedReason;
    }

    public void setRejectedReason(String rejectedReason) {
        this.rejectedReason = rejectedReason;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public Date getUnpublishedDate() {
        return unpublishedDate;
    }

    public void setUnpublishedDate(Date unpublishedDate) {
        this.unpublishedDate = unpublishedDate;
    }

    public User getUnpublishedBy() {
        return unpublishedBy;
    }

    public void setUnpublishedBy(User unpublishedBy) {
        this.unpublishedBy = unpublishedBy;
    }
}
