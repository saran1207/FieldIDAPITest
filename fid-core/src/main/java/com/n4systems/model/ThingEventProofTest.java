package com.n4systems.model;

import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.model.api.NetworkEntity;
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityDefiner;
import com.n4systems.model.security.SecurityLevel;
import com.n4systems.model.utils.AssetEvent;
import com.n4systems.tools.FileDataContainer;
import org.apache.commons.lang.ArrayUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="thing_event_prooftests")
public class ThingEventProofTest extends BaseEntity implements AssetEvent, Serializable {
    private static final long serialVersionUID = 2L;

    /*
     *   XXX - Peak Load and Test Duration were strings on the old InspectionDoc.
     *   My feeling is they should be moved to a Number (float or double) and the unit stored separately
     *   - mf
     */
    @Enumerated(EnumType.STRING)
    private ProofTestType proofTestType;
    private String peakLoad;
    private String duration;
    private String peakLoadDuration;
    private String proofTestFileName;

    @ManyToOne(optional=false)
    @JoinColumn(name="event_id")
    private ThingEvent thingEvent;

    @AllowSafetyNetworkAccess
    public ThingEvent getThingEvent(){
        return thingEvent;
    }

    public void setThingEvent(ThingEvent thingEvent){
        this.thingEvent = thingEvent;
    }

    public ThingEventProofTest copyDataFrom(FileDataContainer fileData) {

        setDuration(fileData.getTestDuration());
        setPeakLoad( fileData.getPeakLoad() );
        setPeakLoadDuration( fileData.getPeakLoadDuration() );
        setProofTestType(fileData.getFileType());
        setProofTestFileName( fileData.getFileName() );

        return this;
    }

    public ThingEventProofTest copyDataFrom(ThingEventProofTest oldProofTestInfo) {

        setThingEvent(oldProofTestInfo.getThingEvent());
        setDuration( oldProofTestInfo.getDuration() );
        setPeakLoad( oldProofTestInfo.getPeakLoad() );
        setPeakLoadDuration( oldProofTestInfo.getPeakLoadDuration() );
        setProofTestType( oldProofTestInfo.getProofTestType() );
        setProofTestFileName( oldProofTestInfo.getProofTestFileName() );

        return this;
    }

    public Asset getAsset() {
        return thingEvent.getAsset();
    }

    public void setAsset(Asset asset) {
        thingEvent.setAsset(asset);
    }

    public AssetStatus getAssetStatus() {
        return thingEvent.getAssetStatus();
    }

    public void setAssetStatus(AssetStatus assetStatus) {
        thingEvent.setAssetStatus(assetStatus);
    }

    public BaseOrg getOwner() {
        return thingEvent.getOwner();
    }

    public void setOwner(BaseOrg owner) {
        thingEvent.setOwner(owner);
    }

    public String getPeakLoad() {
        return peakLoad;
    }

    public void setPeakLoad(String peakLoad) {
        this.peakLoad = peakLoad;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public ProofTestType getProofTestType() {
        return proofTestType;
    }

    public void setProofTestType(ProofTestType proofTestType) {
        this.proofTestType = proofTestType;
    }

    public boolean hasProofTestFile() {
        return (proofTestType != null);
    }

    public String getPeakLoadDuration() {
        return peakLoadDuration;
    }

    public void setPeakLoadDuration(String peakLoadDuration) {
        this.peakLoadDuration = peakLoadDuration;
    }

    public String getProofTestFileName() {
        return proofTestFileName;
    }

    public void setProofTestFileName(String fileName) {
        this.proofTestFileName = fileName;
    }

    public ThingEventType getThingType() {
        return (ThingEventType) thingEvent.getType();
    }

}
