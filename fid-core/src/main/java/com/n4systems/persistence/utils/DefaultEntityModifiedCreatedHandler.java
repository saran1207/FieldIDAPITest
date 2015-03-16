package com.n4systems.persistence.utils;

import com.n4systems.fieldid.context.InteractionContext;
import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.model.api.HasCreatedModifiedPlatform;
import com.n4systems.model.parents.AbstractEntity;
import com.n4systems.model.user.User;
import com.n4systems.util.time.Clock;
import com.n4systems.util.time.SystemClock;
import org.apache.log4j.Logger;

import java.util.Date;

public class DefaultEntityModifiedCreatedHandler implements EntityModifiedCreatedHandler {
    private static final Logger logger = Logger.getLogger(DefaultEntityModifiedCreatedHandler.class);

    private InteractionContext interactionContext;
    private Clock clock;

    public DefaultEntityModifiedCreatedHandler() {
        this(null, new SystemClock());
    }

    public DefaultEntityModifiedCreatedHandler(InteractionContext interactionContext, Clock clock) {
        this.interactionContext = interactionContext;
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

        storeCreatedPlatform(entity);

        onUpdate(entity, time);
    }

    public void onUpdate(AbstractEntity entity) {
        onUpdate(entity, clock.currentTime());
    }

    private void onUpdate(AbstractEntity entity, Date time) {
        User user = lookupCurrentUser(entity);
        if(user == null) {
            //don't update the modifiedby field
            logger.warn("Entity persisted without current user set in context", new Exception());
        } else if (shouldStoreUserOnEntity(entity) && checkTenant(entity, user)) {
            entity.setModifiedBy(user);
        }
        entity.setModified(time);
        storeModifiedPlatform(entity);
    }

    private void storeModifiedPlatform(AbstractEntity entity) {
        if (entity instanceof HasCreatedModifiedPlatform) {
            ((HasCreatedModifiedPlatform) entity).setModifiedPlatform(getInteractionContext().getCurrentPlatform());
            ((HasCreatedModifiedPlatform) entity).setModifiedPlatformType(getInteractionContext().getCurrentPlatformType());
        }
    }

    private void storeCreatedPlatform(AbstractEntity entity) {
        if (entity instanceof HasCreatedModifiedPlatform) {
            ((HasCreatedModifiedPlatform) entity).setCreatedPlatform(getInteractionContext().getCurrentPlatform());
            ((HasCreatedModifiedPlatform) entity).setCreatedPlatformType(getInteractionContext().getCurrentPlatformType());
        }
    }

    private boolean checkTenant(AbstractEntity entity, User user) {
        boolean flag = false;
        if(entity.getModifiedBy() != null) {
            flag = (user.getTenant().equals(entity.getModifiedBy().getTenant()));
            if(flag != true) {
                logger.warn("Entity tenant and User tenant are not the same!", new Exception());
            }
        }
        return flag;
    }

    private User lookupCurrentUser(AbstractEntity entity) {
        User user = getInteractionContext().getCurrentUser();
        if (user == null && shouldWarnOnNullUser(entity)) {
            logger.warn("Entity persisted without current user set in context", new Exception());
        }
        return user;
    }
    
    private boolean shouldWarnOnNullUser(AbstractEntity entity) {
    	return true;
    }

    private boolean shouldStoreUserOnEntity(AbstractEntity entity) {
        return !(entity instanceof User);
    }

    private InteractionContext getInteractionContext() {
        if (interactionContext == null) {
            return ThreadLocalInteractionContext.getInstance();
        }
        return interactionContext;
    }

}
