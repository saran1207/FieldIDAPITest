package com.n4systems.fieldid.utils;

import java.io.Serializable;

public interface Predicate extends Serializable {

    public boolean evaluate();

}
