package com.n4systems.model.search;

import com.n4systems.model.AssetType;
import com.n4systems.model.location.Location;
import com.n4systems.model.procedure.ProcedureType;
import com.n4systems.model.procedure.PublishedState;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.DateRange;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="saved_procedure_defs")
public class ProcedureDefinitionCriteria extends SearchCriteria {

    /*** Identifiers */
    private String rfidNumber;

    private String identifier;

    private String referenceNumber;

    /*** Location */
    private Location location = new Location();

    /*** Asset Details */
    @ManyToOne
    @JoinColumn(name="assetType")
    private AssetType assetType;

    /*** Procedure Details */
    private ProcedureType procedureType;

    private String procedureCode;

    private PublishedState publishedState;

    @ManyToOne
    @JoinColumn(name="author")
    private User author;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="rangeType", column = @Column(name="authoredDateRange")),
            @AttributeOverride(name="fromDate", column = @Column(name="authoredFromDate")),
            @AttributeOverride(name="toDate", column = @Column(name="authoredToDate"))
    })
    private DateRange authoredDateRange;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="rangeType", column = @Column(name="modifiedDateRange")),
            @AttributeOverride(name="fromDate", column = @Column(name="modifiedFromDate")),
            @AttributeOverride(name="toDate", column = @Column(name="modifiedToDate"))
    })
    private DateRange modifiedDateRange;

    public String getRfidNumber() {
        return rfidNumber;
    }

    public void setRfidNumber(String rfidNumber) {
        this.rfidNumber = rfidNumber;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public AssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(AssetType assetType) {
        this.assetType = assetType;
    }

    public ProcedureType getProcedureType() {
        return procedureType;
    }

    public void setProcedureType(ProcedureType procedureType) {
        this.procedureType = procedureType;
    }

    public String getProcedureCode() {
        return procedureCode;
    }

    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }

    public PublishedState getPublishedState() {
        return publishedState;
    }

    public void setPublishedState(PublishedState publishedState) {
        this.publishedState = publishedState;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public DateRange getAuthoredDateRange() {
        return authoredDateRange;
    }

    public void setAuthoredDateRange(DateRange authoredDateRange) {
        this.authoredDateRange = authoredDateRange;
    }

    public DateRange getModifiedDateRange() {
        return modifiedDateRange;
    }

    public void setModifiedDateRange(DateRange modifiedDateRange) {
        this.modifiedDateRange = modifiedDateRange;
    }

    @Override
    public List<String> getColumns() {
        return null;
    }

    @Override
    public void setColumns(List<String> columns) {

    }
}
