package com.n4systems.model;


import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityLevel;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="procedure_audit_event_types")
@PrimaryKeyJoinColumn(name="id")
@Cacheable
@org.hibernate.annotations.Cache(region = "SetupDataCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProcedureAuditEventType extends EventType<ProcedureAuditEventType> {

    @Override
    public boolean isProcedureAuditEventType() {
        return true;
    }

    @Override
    public ProcedureAuditEventType enhance(SecurityLevel level) {
        return EntitySecurityEnhancer.enhanceEntity(this, level);
    }

}
