package com.n4systems.fieldid.wicket.components;

import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

import java.util.ArrayList;
import java.util.List;

public class ComboBox extends DropDownChoice<String> {
    
    private static final String COMBO_BOX_JS_ID = ComboBox.class.getName()+".COMBO_BOX_JS";

    public ComboBox(String id, IModel<String> stringIModel, IModel<? extends List<? extends String>> choices) {
        super(id, stringIModel, choices);
    }
    
    @Override
    protected String convertChoiceIdToChoice(String id) {
        if (id != null && id.startsWith("!")) {
            return id.substring(1);
        }
        return super.convertChoiceIdToChoice(id);
    }


    @Override
    public boolean isNullValid() {
        // We only want the blank choice to appear if it so happens that we haven't entered
        // anything OR we've selected one of the default options.
        String modelObject = getModelObject();
        if (modelObject == null) {
            return true;
        }
        return getChoicesWithoutAddedOption().contains(modelObject);
    }

    public List<? extends String> getChoicesWithoutAddedOption() {
        return super.getChoices();
    }

    @Override
    public List<? extends String> getChoices() {
        List<? extends String> defaultChoices = super.getChoices();
        String modelObject = getModelObject();
        if (modelObject != null && !defaultChoices.contains(modelObject)) {
            ArrayList<String> choicesWithAddedOption = new ArrayList<String>(defaultChoices);
            choicesWithAddedOption.add(0, modelObject);
            return choicesWithAddedOption;
        }
        return defaultChoices;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderJavaScriptReference("javascript/combobox.js", COMBO_BOX_JS_ID);
    }

}
