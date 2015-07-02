package com.n4systems.fieldid.ws.v2.resources;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.ws.v2.exceptions.NotFoundException;
import com.n4systems.fieldid.ws.v2.filters.RequestContext;
import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.model.parents.EntityWithTenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public abstract class ApiResource<A, E extends AbstractEntity> extends FieldIdPersistenceService {

    @Autowired
    private RequestContext requestContext;

	protected abstract A convertEntityToApiModel(E entityModel);

	protected List<A> convertAllEntitiesToApiModels(List<E> entityModels) {
        return convertAllEntitiesToApiModels(entityModels, this::convertEntityToApiModel);
	}

    protected <M, R> List<R> convertAllEntitiesToApiModels(List<M> entityModels, Function<M, R> converter) {
        return entityModels.stream().map(converter).filter(r -> r != null).collect(Collectors.toList());
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
        return requestContext.getContext().getHeaderString("X-APPINFO-APPVERSION");
    }

    public ServletRequest getObject() {
        return currentRequestAttributes().getRequest();
    }

    private static ServletRequestAttributes currentRequestAttributes() {
        RequestAttributes requestAttr = RequestContextHolder.currentRequestAttributes();
        if (!(requestAttr instanceof ServletRequestAttributes)) {
            throw new IllegalStateException("Current request is not a servlet request");
        }
        return (ServletRequestAttributes) requestAttr;
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

    protected boolean versionLessThan(int major, int minor, int patch) {
        long mobileVersion = getVersionNumber();
        long checkVersion = formatVersion(major, minor, patch);
        return (mobileVersion < checkVersion);
    }
}
