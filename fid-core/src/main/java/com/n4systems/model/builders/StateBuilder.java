package com.n4systems.model.builders;

import com.n4systems.model.State;
import com.n4systems.model.Status;

public class StateBuilder extends EntityWithTenantBuilder<State> {

    private String displayText;
    private String buttonName;
    private Status status;

    public static StateBuilder aState() {
        return new StateBuilder(null, null, null);
    }

    public static StateBuilder passState() {
        return aState().status(Status.PASS).displayText("Pass").buttonName("btn0");
    }

    public static StateBuilder failState() {
        return aState().status(Status.FAIL).displayText("Fail").buttonName("btn1");
    }

    public static StateBuilder naState() {
        return aState().status(Status.NA).displayText("N/A").buttonName("btn2");
    }

    public StateBuilder(String displayText, String buttonName, Status status) {
        this.displayText = displayText;
        this.buttonName = buttonName;
        this.status = status;
    }

    public StateBuilder status(Status status) {
        return makeBuilder(new StateBuilder(displayText, buttonName, status));
    }

    public StateBuilder displayText(String displayText) {
        return makeBuilder(new StateBuilder(displayText, buttonName, status));
    }

    public StateBuilder buttonName(String buttonName) {
        return makeBuilder(new StateBuilder(displayText, buttonName, status));
    }

    @Override
    public State createObject() {
        State s = super.assignAbstractFields(new State());
        s.setDisplayText(displayText);
        s.setButtonName(buttonName);
        s.setStatus(status);
        return s;
    }

}
