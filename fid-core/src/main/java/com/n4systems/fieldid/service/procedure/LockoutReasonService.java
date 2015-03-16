package com.n4systems.fieldid.service.procedure;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.procedure.LockoutReason;

import java.util.List;

public class LockoutReasonService extends FieldIdPersistenceService {

    public List<LockoutReason> getActiveLockoutReasons() {
        return persistenceService.findAll(createTenantSecurityBuilder(LockoutReason.class));
    }

    public List<LockoutReason> getArchivedLockoutReasons() {
        return persistenceService.findAll(createTenantSecurityBuilder(LockoutReason.class, true).addSimpleWhere("state", Archivable.EntityState.ARCHIVED));
    }

    public LockoutReason saveOrUpdate(LockoutReason lockoutReason) {
        return persistenceService.saveOrUpdate(lockoutReason);
    }

    public LockoutReason archive(LockoutReason lockoutReason) {
        lockoutReason.archiveEntity();
        return saveOrUpdate(lockoutReason);
    }

    public LockoutReason unarchive(LockoutReason lockoutReason) {
        lockoutReason.activateEntity();
        return saveOrUpdate(lockoutReason);
    }

    public Long getNumberOfActiveLockoutReasons() {
        return persistenceService.count(createTenantSecurityBuilder(LockoutReason.class));
    }

    public Long getNumberOfArchivedLockoutReasons() {
        return persistenceService.count(createTenantSecurityBuilder(LockoutReason.class, true).addSimpleWhere("state", Archivable.EntityState.ARCHIVED));
    }

}
