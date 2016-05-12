package com.n4systems.model.procedure;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.n4systems.model.Asset;
import com.n4systems.model.PlatformType;
import com.n4systems.model.api.HasCreatedModifiedPlatform;
import com.n4systems.model.api.HasOwner;
import com.n4systems.model.api.Listable;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.parents.ArchivableEntityWithTenant;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.user.User;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "procedure_definitions")
@Cacheable
@org.hibernate.annotations.Cache(region = "ProcedureCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProcedureDefinition extends ArchivableEntityWithTenant implements Listable<Long>, HasOwner, HasCreatedModifiedPlatform {

    public static final SecurityDefiner createSecurityDefiner() {
        return new SecurityDefiner("tenant.id", "asset.owner", null, "state", false);
    }

    public static String TESTING_AND_VERIFICATION_REQUIREMENTS = "1. Test for zero energy by turning normal operational controls to the on position and verifying that no machine function or movement occurs.\n" +
            "2. If possible contact with exposed electrical conductors could occur; qualified personnel must perform electrical voltage testing on all phases of the load side of the circuit to verify zero energy condition.\n" +
            "3. Return all controls to the off position and complete all necessary adjustments or repair work.\n" +
            "4. During testing and adjustment, Lockout must be re-applied when contact with hazardous area(s) is required.";

    public static String LOCKOUT_APPLICATION_PROCESS = "1. Notify all affected personnel before the start of this LOCK OUT procedure.\n" +
            "2. Shut down machinery using normal procedures and operating controls. \n" +
            "3. Use energy control points to isolate energy sources and apply necessary lockout devices and locks. \n" +
            "4. Locks applied to energy isolation points must be personally identified and in the \"secured\" position.\n" +
            "5. Authorized personnel must maintain possession of the key(s) for each lock applied. \n" +
            "6. Do not work under the protection of a lock you have not personally applied.";

    public static String LOCKOUT_REMOVAL_PROCESS = "1. Ensure all tools and items have been removed.  \n" +
            "2. Confirm that all employees are safely located.  \n" +
            "3. Ensure all guarding has been replaced.\n" +
            "4. Verify that controls are in neutral.  \n" +
            "5. Remove lockout devices and reenergize machine.  \n" +
            "6. Notify affected employees that servicing is completed.";

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
    @org.hibernate.annotations.Cache(region = "ProcedureCache-Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<IsolationPoint> isolationPoints = Sets.newHashSet();

    @OneToMany(mappedBy = "procedureDefinition", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @org.hibernate.annotations.Cache(region = "ProcedureCache-Collections", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
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

    @Column(name="procedure_type")
    @Enumerated(EnumType.STRING)
    private ProcedureType procedureType;

    //These will soon be additional fields:
    @Column(name="application_process")
    private String applicationProcess = LOCKOUT_APPLICATION_PROCESS;

    @Column(name="removal_process")
    private String removalProcess = LOCKOUT_REMOVAL_PROCESS;

    @Column(name="testing_and_verification")
    private String testingAndVerification = TESTING_AND_VERIFICATION_REQUIREMENTS;

    @Column(name="annotation_type")
    @Enumerated(EnumType.STRING)
    private AnnotationType annotationType;

	@Column(nullable=false)
	private String mobileId;

    @Column(name="modified_platform", length = 200)
    private String modifiedPlatform;

    @Column(name="created_platform", length = 200)
    private String createdPlatform;

    @Enumerated(EnumType.STRING)
    @Column(name="modified_platform_type")
    private PlatformType modifiedPlatformType;

    @Enumerated(EnumType.STRING)
    @Column(name="created_platform_type")
    private PlatformType createdPlatformType;

	@Override
	protected void onCreate() {
		super.onCreate();
		ensureMobileId();
	}

	@Override
	protected void onUpdate() {
		super.onUpdate();
		ensureMobileId();
	}

	private void ensureMobileId() {
		if (mobileId == null) {
			mobileId = UUID.randomUUID().toString();
		}
	}

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

    public void copyIsolationPointForApi(IsolationPoint isolationPoint){
        isolationPoints.add(isolationPoint);
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

    public void removeIsolationPointOnly(IsolationPoint isolationPoint) {
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

    public ProcedureType getProcedureType() { return procedureType; }

    public void setProcedureType(ProcedureType procedureType) { this.procedureType = procedureType; }

    public String getProcedureTypeLabel() {
        if(procedureType == null) {
            return null;
        } else {
            return procedureType.getLabel();
        }
    }

    public void setProcedureTypeLabel(String label) {
        if(label.equals(ProcedureType.SUB.getLabel())){
            procedureType = ProcedureType.SUB;
        } else if (label.equals(ProcedureType.MAIN.getLabel())){
            procedureType = ProcedureType.MAIN;
        }
    }

    public String getAnnotationTypeLabel() {
        return annotationType.getLabel();
    }

    public void setAnnotationTypeLabel(String label) {
        if(label.equals(AnnotationType.ARROW_STYLE.getLabel())) {
            setAnnotationType(AnnotationType.ARROW_STYLE);
        } else {
            setAnnotationType(AnnotationType.CALL_OUT_STYLE);
        }
    }

    public String getApplicationProcess() {
        return applicationProcess;
    }

    public void setApplicationProcess(String applicationProcess) {
        this.applicationProcess = applicationProcess;
    }

    public String getRemovalProcess() {
        return removalProcess;
    }

    public void setRemovalProcess(String removalProcess) {
        this.removalProcess = removalProcess;
    }

    public String getTestingAndVerification() {
        return testingAndVerification;
    }

    public void setTestingAndVerification(String testingAndVerification) {
        this.testingAndVerification = testingAndVerification;
    }

    public AnnotationType getAnnotationType() {
        return annotationType;
    }

    public void setAnnotationType(AnnotationType annotationType) {
        this.annotationType = annotationType;
    }

    //We are forced to implement this due to the HasOwner interface on the event creation service and related classes
    @Override
    public BaseOrg getOwner() {
        return asset.getOwner();
    }

    @Override
    public void setOwner(BaseOrg owner) {
        //Nothing to set
    }

	public String getMobileId() {
		return mobileId;
	}

	public void setMobileId(String mobileId) {
		this.mobileId = mobileId;
	}

    @Override
    public String getModifiedPlatform() {
        return modifiedPlatform;
    }

    @Override
    public void setModifiedPlatform(String modifiedPlatform) {
        this.modifiedPlatform = modifiedPlatform;
    }

    @Override
    public String getCreatedPlatform() {
        return createdPlatform;
    }

    @Override
    public void setCreatedPlatform(String createdPlatform) {
        this.createdPlatform = createdPlatform;
    }

    @Override
    public PlatformType getModifiedPlatformType() {
        return modifiedPlatformType;
    }

    @Override
    public void setModifiedPlatformType(PlatformType modifiedPlatformType) {
        this.modifiedPlatformType = modifiedPlatformType;
    }

    @Override
    public PlatformType getCreatedPlatformType() {
        return createdPlatformType;
    }

    @Override
    public void setCreatedPlatformType(PlatformType createdPlatformType) {
        this.createdPlatformType = createdPlatformType;
    }
}
