package com.n4systems.fieldid.wicket.pages.widgets.attributes;

import com.n4systems.fieldid.actions.helpers.InfoFieldInput;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.ComboBox;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.util.StringListingPair;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import rfid.ejb.entity.InfoFieldBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by agrabovskis on 2018-11-22.
 */
public class AttributeComboBoxFieldWidget extends Panel {

    private ComboBox attributeValueComboBox;
    private InfoFieldBean infoFieldBean;
    private InfoOptionInput infoOptionInput;
    private IModel<List<InfoOptionInput>> infoOptionInputModel;
    private List<StringListingPair> existingOptions;

    /**
     *
     * @param id
     * @param infoFieldBean the entity we are selecting a value for
     * @param infoOptionInput the currently selected value is contained here in the uniqueIDString field
     * @param infoOptionInputModel the list of possible selections for the select box
     */
    public AttributeComboBoxFieldWidget(String id,
                                      InfoFieldBean infoFieldBean,
                                      InfoOptionInput infoOptionInput,
                                      IModel<List<InfoOptionInput>> infoOptionInputModel) {
        super(id);
        this.infoFieldBean = infoFieldBean;
        this.infoOptionInput = infoOptionInput;
        this.infoOptionInputModel = infoOptionInputModel;
        existingOptions = getComboBoxInfoOptions();
        addComponents();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderOnDomReadyJavaScript("new toCombo('"+attributeValueComboBox.getInputName()+"');");
    }

    private void addComponents() {
        add(new Label("required", new FIDLabelModel("indicator.required")) {
            @Override
            public boolean isVisible() {
                return infoFieldBean.isRequired();
            }
        });
        add(new Label("fieldName", infoFieldBean.getName()));

        attributeValueComboBox = new ComboBox(
                "fieldValue",
                new PropertyModel<String>(infoOptionInput, "name"),
                new PropertyModel<List<String>>(this, "infoOptionNames"));

        attributeValueComboBox.add(new UpdateComponentOnChange() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                super.onUpdate(target);
                /* Find matching entry in existing options */
                String selectedOptionName = attributeValueComboBox.getModelObject();
                for (StringListingPair pair: existingOptions) {
                    if (pair.getName().equals(selectedOptionName)) {
                        infoOptionInput.setUniqueIDString(pair.getId());
                        return;
                    }
                }
                if (attributeValueComboBox.getModelObject() != null)
                    infoOptionInput.setUniqueIDString(
                        InfoOptionInput.COMBOBOX_DYNAMIC_VALUE_MARKER + attributeValueComboBox.getModelObject());
            }
        });
        add(attributeValueComboBox);
    }

    public List<String> getInfoOptionNames() {
        List<String> names = new ArrayList<String>();
        for (StringListingPair pair: existingOptions) {
            names.add(pair.getName());
        }
        return names;
    }

    private List<StringListingPair> getComboBoxInfoOptions() {
        // WEB-3518 CAVEAT : note that the list of InfoFieldBeans and InfoOptionInputs aren't treated the same.
        // one filters out retired fields, the other doesn't so we need to account for this.
        for (InfoOptionInput inputOption : infoOptionInputModel.getObject()) {
            if (infoFieldBean.getUniqueID().equals(inputOption.getInfoFieldId())) {
                return InfoFieldInput.getComboBoxInfoOptions(infoFieldBean, inputOption);
            }
        }
        throw new IllegalStateException("can't find input option for field " + infoFieldBean.getName());
    }

}
