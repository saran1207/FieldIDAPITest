package com.n4systems.model.procedure;

import com.google.common.collect.Lists;
import com.n4systems.model.Asset;
import com.n4systems.model.parents.ArchivableEntityWithTenant;
import com.n4systems.model.user.User;
import org.hibernate.annotations.IndexColumn;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "procedure_definitions")
public class ProcedureDefinition extends ArchivableEntityWithTenant {

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

    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @IndexColumn(name="orderIdx")
    @JoinTable(name="procedure_definitions_isolation_points", joinColumns = @JoinColumn(name = "procedure_definition_id"), inverseJoinColumns = @JoinColumn(name = "isolation_point_id"))
    private List<IsolationPoint> isolationPoints = Lists.newArrayList();

    @OneToMany(mappedBy = "procedureDefinition")
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

    public List<IsolationPoint> getIsolationPoints() {
        return isolationPoints;
    }

    public void setIsolationPoints(List<IsolationPoint> isolationPoints) {
        this.isolationPoints = isolationPoints;
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
}
