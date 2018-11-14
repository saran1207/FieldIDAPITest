package com.n4systems.fieldid.wicket.pages.autoattributes;

import com.n4systems.fieldid.actions.helpers.InfoFieldInput;
import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.util.StringListingPair;
import org.apache.log4j.Logger;
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
 * Created by agrabovskis on 2018-11-14.
 */
public class AutoAttributeDisplaySelectFieldPanel extends Panel {

    private static final Logger logger = Logger.getLogger(AutoAttributeDisplaySelectFieldPanel.class);

    private boolean allowNoSelection;
    private InfoFieldBean infoFieldBean;
    private IModel<List<InfoOptionInput>> infoOptionInputModel;

    public AutoAttributeDisplaySelectFieldPanel(String id,
                                                boolean allowNoSelection,
                                                InfoFieldBean infoFieldBean,
                                                IModel<List<InfoOptionInput>> infoOptionInputModel) {
        super(id);
        this.allowNoSelection = allowNoSelection;
        this.infoFieldBean = infoFieldBean;
        this.infoOptionInputModel = infoOptionInputModel;
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

        IModel<StringListingPair> selectedListingPair = new Model(null);
        final DropDownChoice<StringListingPair> inputValueSelection = new DropDownChoice<StringListingPair>("fieldValue",
                selectedListingPair,
                new LoadableDetachableModel<List<StringListingPair>>() {
                    @Override
                    protected List<StringListingPair> load() {
                        List<StringListingPair> listingPair = getComboBoxInfoOptions(infoFieldBean, infoOptionInputModel.getObject());
                        if (!allowNoSelection && selectedListingPair.getObject() == null && !listingPair.isEmpty()) {
                            selectedListingPair.setObject(listingPair.get(0));
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
        ) {
            @Override
            protected boolean wantOnSelectionChangedNotifications() {
                return true;
            }

        };

        inputValueSelection.setNullValid(allowNoSelection);
        add(inputValueSelection);
    }

    public List<StringListingPair> getComboBoxInfoOptions(InfoFieldBean field,  List<InfoOptionInput> inputOptions) {
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
