package com.n4systems.fieldid.ws.v1.resources;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.ws.v1.exceptions.NotFoundException;
import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.model.parents.EntityWithTenant;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public abstract class ApiResource<A, E extends AbstractEntity> extends FieldIdPersistenceService {

    private static Logger logger = Logger.getLogger(ApiResource.class);

    @Autowired
    private HttpServletRequest request;

    protected final Class<E> entityClass;

	protected abstract A convertEntityToApiModel(E entityModel);

    protected ApiResource(Class<E> entityClass) {
        this.entityClass =entityClass;
    }

	protected List<A> convertAllEntitiesToApiModels(List<E> entityModels) {
        Instant b = Instant.now();
		List<A> list = entityModels.stream().map(this::convertEntityToApiModel).collect(Collectors.toList());
        Instant a = Instant.now();
        logger.info("Convert Entities: " + entityClass.getName() + " - " + Duration.between(b, a).toMillis());

        return list;
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
        return request.getHeader("X-APPINFO-APPVERSION");
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
            } else {
				// Some legacy mobile versions send <major>.<minor> and are missing the patch.
				p = Pattern.compile("^(\\d+)\\.(\\d+)$");
				m = p.matcher(verStr);
				if (m.matches()) {
					int major = Integer.parseInt(m.group(1));
					int minor = Integer.parseInt(m.group(2));
					version = formatVersion(major, minor, 0);
				}
            }
        }
        return version;
    }

    protected boolean versionEqualOrGreaterThan(int major, int minor, int patch) {
        long mobileVersion = getVersionNumber();
        long checkVersion = formatVersion(major, minor, patch);
        return (mobileVersion >= checkVersion);
    }

    protected boolean versionLessThan(int major, int minor, int patch) {
        long mobileVersion = getVersionNumber();
        long checkVersion = formatVersion(major, minor, patch);
        return (mobileVersion < checkVersion);
    }
}
