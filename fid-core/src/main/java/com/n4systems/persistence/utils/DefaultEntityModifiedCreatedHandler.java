package com.n4systems.persistence.utils;

import com.n4systems.fieldid.context.InteractionContext;
import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.model.api.HasPlatformContext;
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
        if (shouldStoreUserOnEntity(entity)) {
            entity.setModifiedBy(user);
        }
        entity.setModified(time);
        storeModifiedPlatform(entity);
    }

    private void storeModifiedPlatform(AbstractEntity entity) {
        if (entity instanceof HasPlatformContext) {
            ((HasPlatformContext) entity).setModifiedPlatform(getInteractionContext().getCurrentPlatform());
        }
    }

    private void storeCreatedPlatform(AbstractEntity entity) {
        if (entity instanceof HasPlatformContext) {
            ((HasPlatformContext) entity).setCreatedPlatform(getInteractionContext().getCurrentPlatform());
        }
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
