package com.n4systems.fieldid.wicket.pages.widgets.attributes;

import com.n4systems.fieldid.actions.helpers.InfoFieldInput;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.util.StringListingPair;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import rfid.ejb.entity.InfoFieldBean;

import java.util.List;

/**
 * A reusable select box control for display/updating attribute value
 */
public class AttributeSelectFieldWidget extends Panel {

    private static final Logger logger = Logger.getLogger(AttributeSelectFieldWidget.class);

    private boolean allowNoSelection;
    private InfoFieldBean infoFieldBean;
    private InfoOptionInput infoOptionInput;
    private IModel<List<InfoOptionInput>> infoOptionInputModel;
    private IModel<StringListingPair> selectedListingPair;

    /**
     *
     * @param id
     * @param allowNoSelection specifies if the select box allows no selection
     * @param infoFieldBean the entity we are selecting a value for
     * @param infoOptionInput the currently selected value is contained here in the uniqueIDString field
     * @param infoOptionInputModel the list of possible selections for the select box
     */
    public AttributeSelectFieldWidget(String id,
                                      boolean allowNoSelection,
                                      InfoFieldBean infoFieldBean,
                                      InfoOptionInput infoOptionInput,
                                      IModel<List<InfoOptionInput>> infoOptionInputModel) {
        super(id);
        this.allowNoSelection = allowNoSelection;
        this.infoFieldBean = infoFieldBean;
        this.infoOptionInput = infoOptionInput;
        this.infoOptionInputModel = infoOptionInputModel;
        selectedListingPair = new Model(null);
        addComponents();
    }

    private void addComponents() {

        add(new Label("required", new FIDLabelModel("indicator.required")) {
            @Override
            public boolean isVisible() {
                return infoFieldBean.isRequired();
            }
        });
        add(new Label("fieldName", infoFieldBean.getName()));

        /* Inputs are limited to combo and select boxes */
        final DropDownChoice<StringListingPair> inputValueSelection = new DropDownChoice<StringListingPair>("fieldValue",
                selectedListingPair,
                new LoadableDetachableModel<List<StringListingPair>>() {
                    @Override
                    protected List<StringListingPair> load() {
                        List<StringListingPair> listingPair = getComboBoxInfoOptions(infoFieldBean, infoOptionInputModel.getObject());
                        if (selectedListingPair.getObject() == null && infoOptionInput.getUniqueIDString() != null) {
                            for (StringListingPair pair : listingPair) {
                                if (pair.getId().equals(infoOptionInput.getUniqueIDString())) {
                                    selectedListingPair.setObject(pair);
                                    break;
                                }
                            }
                        }
                        else
                        if (!allowNoSelection && selectedListingPair.getObject() == null && !listingPair.isEmpty()) {
                            selectedListingPair.setObject(listingPair.get(0));
                            infoOptionInput.setUniqueIDString(selectedListingPair.getObject().getId());
                        }
                        return listingPair;
                    }
                },
                new IChoiceRenderer<StringListingPair>() {
                    @Override
                    public Object getDisplayValue(StringListingPair object) {
                        return object.getName();
                    }

                    @Override
                    public String getIdValue(StringListingPair object, int index) {
                        return object.getId().toString();
                    }
                }
        );
        inputValueSelection.setNullValid(allowNoSelection);
        // Add Ajax Behaviour to prevent change in unit selection from generating a
        // page refresh which will lose the state.
        inputValueSelection.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            protected void onUpdate(AjaxRequestTarget target) {
                if (inputValueSelection.getModelObject() != null)
                    infoOptionInput.setUniqueIDString(inputValueSelection.getModelObject().getId());
            }
        });

        add(inputValueSelection);
    }

    private List<StringListingPair> getComboBoxInfoOptions(InfoFieldBean field,  List<InfoOptionInput> inputOptions) {
        // WEB-3518 CAVEAT : note that the list of InfoFieldBeans and InfoOptionInputs aren't treated the same.
        // one filters out retired fields, the other doesn't so we need to account for this.
        for (InfoOptionInput inputOption : inputOptions) {
            if (field.getUniqueID().equals(inputOption.getInfoFieldId())) {
                return InfoFieldInput.getComboBoxInfoOptions(field, inputOption);
            }
        }
        throw new IllegalStateException("can't find input option for field " + field.getName());
    }

}
