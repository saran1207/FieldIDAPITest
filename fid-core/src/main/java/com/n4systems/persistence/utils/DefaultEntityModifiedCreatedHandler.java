package com.n4systems.persistence.utils;

import java.util.Date;

import org.apache.log4j.Logger;

import com.n4systems.fieldid.context.ThreadLocalUserContext;
import com.n4systems.fieldid.context.UserContext;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.model.user.User;
import com.n4systems.util.time.Clock;
import com.n4systems.util.time.SystemClock;

public class DefaultEntityModifiedCreatedHandler implements EntityModifiedCreatedHandler {
    private static final Logger logger = Logger.getLogger(DefaultEntityModifiedCreatedHandler.class);
    private static final Class<?>[] ignoreNullUserClasses = new Class<?>[] { EventSchedule.class };
    
    private UserContext userContext;
    private Clock clock;

    public DefaultEntityModifiedCreatedHandler() {
        this(null, new SystemClock());
    }

    public DefaultEntityModifiedCreatedHandler(UserContext userContext, Clock clock) {
        this.userContext = userContext;
        this.clock = clock;
    }

    public void onCreate(AbstractEntity entity) {
        onCreate(entity, clock.currentTime());
    }

    private void onCreate(AbstractEntity entity, Date time) {
        User user = lookupCurrentUser(entity);
        if (entity.getCreated() == null) {
            entity.setCreated(time);
        }
        if (user != null && shouldStoreUserOnEntity(entity)) {
            entity.setCreatedBy(user);
        }

        onUpdate(entity, time);
    }

    public void onUpdate(AbstractEntity entity) {
        onUpdate(entity, clock.currentTime());
    }

    private void onUpdate(AbstractEntity entity, Date time) {
        User user = lookupCurrentUser(entity);
        if (shouldStoreUserOnEntity(entity)) {
            entity.setModifiedBy(user);
        }
        entity.setModified(time);
    }

    private User lookupCurrentUser(AbstractEntity entity) {
        User user = getUserContext().getCurrentUser();
        if (user == null && shouldWarnOnNullUser(entity)) {
            logger.warn("Entity persisted without current user set in context", new Exception());
        }
        return user;
    }
    
    private boolean shouldWarnOnNullUser(AbstractEntity entity) {
    	for (Class<?> ignoreClass: ignoreNullUserClasses) {
    		if (ignoreClass.isInstance(entity)){
    			return false;
    		}
    	}
    	return true;
    }

    private boolean shouldStoreUserOnEntity(AbstractEntity entity) {
        return !(entity instanceof User);
    }

    private UserContext getUserContext() {
        if (userContext == null) {
            return ThreadLocalUserContext.getInstance();
        }
        return userContext;
    }

}
