package com.n4systems.model.builders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import com.n4systems.model.State;
import com.n4systems.model.StateSet;
import com.n4systems.model.Status;

public class StateSetBuilder extends EntityWithTenantBuilder<StateSet> {

    private List<State> states;
    private String name;

    public static StateSetBuilder aStateSet() {
        return new StateSetBuilder(null, Lists.newArrayList(new State("Pass", Status.PASS, "pass")));
    }

    public StateSetBuilder(String name, List<State> states) {
        this.name = name;
        this.states = states;
    }
    
    public StateSetBuilder states(State... states) {
        List<State> newStates =  new ArrayList<State>();
        newStates.addAll(Arrays.asList(states));
        return makeBuilder(new StateSetBuilder(name, newStates));
    }

    public StateSetBuilder named(String name) {
        return makeBuilder(new StateSetBuilder(name, states));
    }

    @Override
    public StateSet createObject() {
        StateSet stateSet = super.assignAbstractFields(new StateSet());
        stateSet.setStates(states);
        stateSet.setName(name);
        return stateSet;
    }

}
