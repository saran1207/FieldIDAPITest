package com.n4systems.fieldid.service.uuid;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicLongService {

    private AtomicLong atom = new AtomicLong(1000);

    public long getNext() {
        return atom.getAndIncrement();
    }

}
