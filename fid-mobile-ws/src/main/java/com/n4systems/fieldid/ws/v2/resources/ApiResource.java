package com.n4systems.fieldid.ws.v2.resources;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.ws.v2.filters.RequestContext;
import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.util.persistence.NewObjectSelect;
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    private long formatVersion(int major, int minor, int patch) {
        return (major * 10000) + (minor * 100) + patch;
    }

    protected String getVersionString() {
        return requestContext.getContext().getHeaderString("X-APPINFO-APPVERSION");
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

	@SuppressWarnings("unused")
    protected boolean versionEqualOrGreaterThan(int major, int minor, int patch) {
        long mobileVersion = getVersionNumber();
        long checkVersion = formatVersion(major, minor, patch);
        return (mobileVersion >= checkVersion);
    }

	@SuppressWarnings("unused")
    protected boolean versionLessThan(int major, int minor, int patch) {
        long mobileVersion = getVersionNumber();
        long checkVersion = formatVersion(major, minor, patch);
        return (mobileVersion < checkVersion);
    }

    protected QueryBuilder<ApiModelHeader> createModelHeaderQueryBuilder(Class<?> tableClass, String sidField, String modifiedByField) {
        QueryBuilder<ApiModelHeader> queryBuilder = new QueryBuilder<>(tableClass, securityContext.getUserSecurityFilter());
        queryBuilder.setSelectArgument(new NewObjectSelect(ApiModelHeader.class, sidField, modifiedByField));
        return queryBuilder;
    }

	protected QueryBuilder<ApiSortedModelHeader> createModelHeaderQueryBuilder(Class<?> tableClass, String sidField, String modifiedByField, String sortField, boolean ascending) {
		QueryBuilder<ApiSortedModelHeader> queryBuilder = new QueryBuilder<>(tableClass, securityContext.getUserSecurityFilter());
		queryBuilder.setSelectArgument(new NewObjectSelect(ApiSortedModelHeader.class, sidField, modifiedByField, sortField));
		queryBuilder.setOrder(sortField, ascending);
		return queryBuilder;
	}

}
