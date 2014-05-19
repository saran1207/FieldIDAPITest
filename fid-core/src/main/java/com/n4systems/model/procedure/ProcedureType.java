package com.n4systems.model.procedure;

import com.n4systems.model.api.DisplayEnum;

import java.util.Arrays;
import java.util.List;

/**
 * Created by rrana on 2014-05-15.
 */
public enum ProcedureType implements DisplayEnum {

    MAIN("Main"),
    SUB("Sub");

    public static List<ProcedureType> PROCEDURE_TYPE_LIST = Arrays.asList(ProcedureType.values());

    private String label;

    ProcedureType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name();
    }

}
