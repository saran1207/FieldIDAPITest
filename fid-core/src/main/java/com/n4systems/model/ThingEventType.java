package com.n4systems.model;

import com.n4systems.fileprocessing.ProofTestType;
import com.n4systems.model.api.SecurityEnhanced;
import com.n4systems.model.security.AllowSafetyNetworkAccess;
import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityLevel;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "thing_event_types")
@PrimaryKeyJoinColumn(name="id")
public class ThingEventType extends EventType implements SecurityEnhanced<ThingEventType> {

    public ThingEventType() {}

    public ThingEventType(String name) {
        setName(name);
    }

    @ElementCollection(fetch= FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @JoinTable(name="eventtypes_supportedprooftests", joinColumns = {@JoinColumn(name="eventtypes_id")})
    @Column(name="element")
    private Set<ProofTestType> supportedProofTests = new HashSet<ProofTestType>();

    @Column(nullable=false)
    private boolean master = false;

    @AllowSafetyNetworkAccess
    public Set<ProofTestType> getSupportedProofTests() {
        return supportedProofTests;
    }

    public void setSupportedProofTests(Set<ProofTestType> supportedProofTests) {
        this.supportedProofTests = supportedProofTests;
    }

    @AllowSafetyNetworkAccess
    public boolean supports(ProofTestType proofTestType) {
        return supportedProofTests.contains(proofTestType);
    }

    @AllowSafetyNetworkAccess
    public boolean isMaster() {
        return master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }

    public ThingEventType enhance(SecurityLevel level) {
        return EntitySecurityEnhancer.enhanceEntity(this, level);
    }

}
