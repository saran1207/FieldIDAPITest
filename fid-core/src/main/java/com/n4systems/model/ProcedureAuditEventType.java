package com.n4systems.model;


import com.n4systems.model.security.EntitySecurityEnhancer;
import com.n4systems.model.security.SecurityLevel;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="procedure_audit_event_types")
@PrimaryKeyJoinColumn(name="id")
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
