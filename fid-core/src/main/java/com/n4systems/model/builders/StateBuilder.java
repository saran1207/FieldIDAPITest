package com.n4systems.model.builders;

import com.n4systems.model.Button;
import com.n4systems.model.EventResult;

public class StateBuilder extends EntityWithTenantBuilder<Button> {

    private String displayText;
    private String buttonName;
    private EventResult eventResult;

    public static StateBuilder aState() {
        return new StateBuilder(null, null, null);
    }

    public static StateBuilder passState() {
        return aState().status(EventResult.PASS).displayText("Pass").buttonName("btn0");
    }

    public static StateBuilder failState() {
        return aState().status(EventResult.FAIL).displayText("Fail").buttonName("btn1");
    }

    public static StateBuilder naState() {
        return aState().status(EventResult.NA).displayText("N/A").buttonName("btn2");
    }

    public StateBuilder(String displayText, String buttonName, EventResult eventResult) {
        this.displayText = displayText;
        this.buttonName = buttonName;
        this.eventResult = eventResult;
    }

    public StateBuilder status(EventResult eventResult) {
        return makeBuilder(new StateBuilder(displayText, buttonName, eventResult));
    }

    public StateBuilder displayText(String displayText) {
        return makeBuilder(new StateBuilder(displayText, buttonName, eventResult));
    }

    public StateBuilder buttonName(String buttonName) {
        return makeBuilder(new StateBuilder(displayText, buttonName, eventResult));
    }

    @Override
    public Button createObject() {
        Button s = super.assignAbstractFields(new Button());
        s.setDisplayText(displayText);
        s.setButtonName(buttonName);
        s.setEventResult(eventResult);
        return s;
    }

}
