package com.n4systems.persistence.utils;

import com.n4systems.model.parents.AbstractEntity;

public interface EntityModifiedCreatedHandler {

    public void onCreate(AbstractEntity entity);
    public void onUpdate(AbstractEntity entity);

}
