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

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="thing_event_prooftests")
public class ThingEventProofTest implements AssetEvent, NetworkEntity<ThingEvent>, Serializable {
    private static final long serialVersionUID = 1L;

    /*public static final SecurityDefiner createSecurityDefiner() {
        return new SecurityDefiner("tenant.id", "asset.owner", null, "state", false);
    } */

    @SuppressWarnings("unused")
    @Id
    @GeneratedValue
    @Column(name="id", nullable=false)
    private Long thingEventProofTestId;

    private ProofTestInfo proofTestInfo = new ProofTestInfo();

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

        getProofTestInfo().setDuration( fileData.getTestDuration() );
        getProofTestInfo().setPeakLoad( fileData.getPeakLoad() );
        getProofTestInfo().setPeakLoadDuration( fileData.getPeakLoadDuration() );
        getProofTestInfo().setProofTestType( fileData.getFileType() );
        getProofTestInfo().setProofTestFileName( fileData.getFileName() );
        getProofTestInfo().setProofTestData(new String(fileData.getFileData()));

        return this;
    }

    public ThingEventProofTest copyDataFrom(ThingEventProofTest oldProofTestInfo) {

        setThingEvent(oldProofTestInfo.getThingEvent());
        getProofTestInfo().setDuration( oldProofTestInfo.getProofTestInfo().getDuration() );
        getProofTestInfo().setPeakLoad( oldProofTestInfo.getProofTestInfo().getPeakLoad() );
        getProofTestInfo().setPeakLoadDuration( oldProofTestInfo.getProofTestInfo().getPeakLoadDuration() );
        getProofTestInfo().setProofTestType( oldProofTestInfo.getProofTestInfo().getProofTestType() );
        getProofTestInfo().setProofTestFileName( oldProofTestInfo.getProofTestInfo().getProofTestFileName() );
        getProofTestInfo().setProofTestData(oldProofTestInfo.getProofTestInfo().getProofTestData() );

        return this;
    }

    @AllowSafetyNetworkAccess
    public ProofTestInfo getProofTestInfo() {
        if(proofTestInfo == null){
            proofTestInfo = new ProofTestInfo();
        }
        return proofTestInfo;
    }

    public void setProofTestInfo(ProofTestInfo proofTestInfo) {
        this.proofTestInfo = proofTestInfo;
    }

    @Override
    public ThingEvent enhance(SecurityLevel level) {
        ThingEvent enhanced = EntitySecurityEnhancer.enhanceEntity(thingEvent, level);
        enhanced.setBook(enhanced.enhance(thingEvent.getBook(), level));
        enhanced.setPerformedBy(enhanced.enhance(thingEvent.getPerformedBy(), level));
        enhanced.setType(enhanced.enhance(thingEvent.getType(), level));
        enhanced.setAsset(enhanced.enhance(getAsset(), level));
        enhanced.setOwner(enhanced.enhance(getOwner(), level));

        List<SubEvent> enhancedSubEvents = new ArrayList<SubEvent>();
        for (SubEvent subEvent : thingEvent.getSubEvents()) {
            enhancedSubEvents.add(subEvent.enhance(level));
        }
        enhanced.setSubEvents(enhancedSubEvents);

        return enhanced;
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

    /*@Override
    protected void copyDataIntoResultingAction(AbstractEvent<?,?> event) {
        ThingEventProofTest action = (ThingEventProofTest) event;
        action.setProofTestInfo(getProofTestInfo());
        action.setThingEvent(getThingEvent());
    } */

    public ThingEventType getThingType() {
        return (ThingEventType) thingEvent.getType();
    }

    public String getPeakLoad() {
        return proofTestInfo == null ? null : proofTestInfo.getPeakLoad();
    }

    public void setPeakLoad(String peakLoad) {
        getProofTestInfo().setPeakLoad(peakLoad);
    }

    public String getDuration() {
        return proofTestInfo == null ? null : proofTestInfo.getDuration();
    }

    public void setDuration(String duration) {
        getProofTestInfo().setDuration(duration);
    }

    public ProofTestType getProofTestType() {
        return proofTestInfo == null ? null : proofTestInfo.getProofTestType();
    }

    public void setProofTestType(ProofTestType proofTestType) {
        getProofTestInfo().setProofTestType(proofTestType);
    }

    public String getPeakLoadDuration() {
        return proofTestInfo == null ? null : proofTestInfo.getPeakLoadDuration();
    }

    public void setPeakLoadDuration(String peakLoadDuration) {
        getProofTestInfo().setPeakLoadDuration(peakLoadDuration);
    }

    public String getProofTestData() {
        return proofTestInfo == null ? null : proofTestInfo.getProofTestData();
    }

    public void setProofTestData(String fileData) {
        getProofTestInfo().setProofTestData(fileData);
    }

    public String getProofTestFileName() {
        return proofTestInfo == null ? null : proofTestInfo.getProofTestFileName();
    }

    public void setProofTestFileName(String fileName) {
        getProofTestInfo().setProofTestFileName(fileName);
    }

    @Override
    @AllowSafetyNetworkAccess
    public SecurityLevel getSecurityLevel(BaseOrg fromOrg) {
        return SecurityLevel.calculateSecurityLevel(fromOrg, getOwner());
    }
}
