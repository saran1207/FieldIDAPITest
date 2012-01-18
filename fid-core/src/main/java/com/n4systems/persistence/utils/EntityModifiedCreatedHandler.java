package com.n4systems.persistence.utils;

import com.n4systems.model.parents.AbstractEntity;

import java.io.Serializable;

public interface EntityModifiedCreatedHandler extends Serializable {

    public void onCreate(AbstractEntity entity);
    public void onUpdate(AbstractEntity entity);

}
