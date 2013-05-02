package com.n4systems.fieldid.service.uuid;

import java.util.UUID;

public class UUIDService {

    public String createUuid() {
        return  UUID.randomUUID().toString();
    };
}
