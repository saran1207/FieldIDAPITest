package com.n4systems.model.procedure;

import com.google.common.collect.Lists;
import com.n4systems.model.Asset;
import com.n4systems.model.location.Location;
import com.n4systems.model.parents.ArchivableEntityWithTenant;
import com.n4systems.model.user.User;
import org.hibernate.annotations.IndexColumn;

import javax.persistence.*;
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
    private String revisionNumber;

    @Column(name="warnings")
    private String warnings;

    @Column(name="complete_points_in_order")
    private boolean completeIsolationPointInOrder;

    @ManyToOne
    @JoinColumn(name = "developed_by_id")
    private User developedBy;

    @Column(name="equipment_number")
    private String equipmentNumber;

    @Embedded
    private Location equipmentLocation;

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

    public String getRevisionNumber() {
        return revisionNumber;
    }

    public void setRevisionNumber(String revisionNumber) {
        this.revisionNumber = revisionNumber;
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


    // TODO : note.  i don't think this is "old school fieldid" location.  should be just a string.
    public Location getEquipmentLocation() {
        return equipmentLocation;
    }

    public void setEquipmentLocation(Location equipmentLocation) {
        this.equipmentLocation = equipmentLocation;
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
}
