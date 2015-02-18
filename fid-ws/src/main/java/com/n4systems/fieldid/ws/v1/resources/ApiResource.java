package com.n4systems.fieldid.ws.v1.resources;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.ws.v1.exceptions.NotFoundException;
import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.model.parents.EntityWithTenant;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public abstract class ApiResource<A, E extends AbstractEntity> extends FieldIdPersistenceService {

    @Context
    protected HttpHeaders headers;

	protected abstract A convertEntityToApiModel(E entityModel);

	protected List<A> convertAllEntitiesToApiModels(List<E> entityModels) {
		return entityModels.stream().map(this::convertEntityToApiModel).collect(Collectors.toList());
	}

    protected <T extends EntityWithTenant> T findEntity(Class<T> entityClass, Long id) {
        T entity = persistenceService.findUsingTenantOnlySecurityWithArchived(entityClass, id);
        if (entity == null) {
            throw new NotFoundException(entityClass.getSimpleName(), id);
        }
        return entity;
    }

    private long formatVersion(int major, int minor, int patch) {
        return (major * 10000) + (minor * 100) + patch;
    }

    protected String getVersionString() {
        List<String> versionHeaders = headers.getRequestHeader("X-APPINFO-APPVERSION");
        return (versionHeaders != null && versionHeaders.size() > 0) ? versionHeaders.get(0) : null;
    }

    protected long getVersionNumber() {
        long version = 0;
        String verStr = getVersionString();
        if (verStr != null) {
            Pattern p = Pattern.compile("^(\\d+)\\.(\\d+)\\.(\\d+)$");
            Matcher m = p.matcher(verStr);
            if (m.matches()) {
                int major = Integer.parseInt(m.group(1));
                int minor = Integer.parseInt(m.group(2));
                int patch = Integer.parseInt(m.group(3));
                version = formatVersion(major, minor, patch);
            }
        }
        return version;
    }

    protected boolean versionEqualOrGreaterThan(int major, int minor, int patch) {
        long mobileVersion = getVersionNumber();
        long checkVersion = formatVersion(major, minor, patch);
        return (mobileVersion >= checkVersion);
    }
}
