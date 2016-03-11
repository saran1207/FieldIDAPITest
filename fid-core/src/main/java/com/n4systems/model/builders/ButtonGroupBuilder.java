package com.n4systems.model.builders;

import com.google.common.collect.Lists;
import com.n4systems.model.Button;
import com.n4systems.model.ButtonGroup;
import com.n4systems.model.EventResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ButtonGroupBuilder extends EntityWithTenantBuilder<ButtonGroup> {

    private List<Button> buttons;
    private String name;

    public static ButtonGroupBuilder aButtonGroup() {
        return new ButtonGroupBuilder(null, Lists.newArrayList(new Button("Pass", EventResult.PASS, "pass")));
    }

    public ButtonGroupBuilder(String name, List<Button> buttons) {
        this.name = name;
        this.buttons = buttons;
    }
    
    public ButtonGroupBuilder buttons(Button... buttons) {
        List<Button> newButtons =  new ArrayList<Button>();
        newButtons.addAll(Arrays.asList(buttons));
        return makeBuilder(new ButtonGroupBuilder(name, newButtons));
    }

    public ButtonGroupBuilder named(String name) {
        return makeBuilder(new ButtonGroupBuilder(name, buttons));
    }

    @Override
    public ButtonGroup createObject() {
        ButtonGroup buttonGroup = super.assignAbstractFields(new ButtonGroup());
        buttonGroup.setButtons(buttons);
        buttonGroup.setName(name);
        return buttonGroup;
    }

}
