package com.n4systems.fieldid;

import com.n4systems.fieldid.service.FieldIdPersistenceService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
public @interface CopiedToService {
    Class<? extends FieldIdPersistenceService> value();
}
