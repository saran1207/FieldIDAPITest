package com.n4systems.fieldid.selenium.persistence;

import com.n4systems.model.api.Saveable;
import com.n4systems.model.builders.context.BuilderCallback;

public class ScenarioBuilderCallback implements BuilderCallback {

    private Scenario scenario;

    public ScenarioBuilderCallback(Scenario scenario) {
        this.scenario = scenario;
    }

    @Override
    public void onObjectBuilt(Object o) {
        scenario.save((Saveable) o);
        scenario.addBuiltObject(o);
    }
}
