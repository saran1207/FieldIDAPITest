package com.n4systems.fieldid.wicket.pages.identify.components.attributes;

import com.n4systems.fieldid.wicket.components.ComboBox;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import java.util.ArrayList;
import java.util.List;

public class ComboBoxAttributeEditor extends FormComponentPanel<InfoOptionBean> {

    private String selectedItemText;
    private IModel<InfoFieldBean> fieldModel;

    public ComboBoxAttributeEditor(String id, IModel<InfoOptionBean> optionModel, IModel<InfoFieldBean> fieldModel) {
        super(id, optionModel);
        this.fieldModel = fieldModel;
        selectedItemText = optionModel.getObject() == null ? null : optionModel.getObject().getName();

        IModel<ArrayList<String>> choices = new Model<ArrayList<String>>(prepareChoices(fieldModel.getObject()));
        ComboBox comboBox = new ComboBox("combo", new PropertyModel<String>(this, "selectedItemText"), choices);
        comboBox.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                // We have to do a little processing here to make sure the model's updated for auto attribute checking
                convertInput();
                updateModel();
                onChange(target);
            }
        });

        add(comboBox);
    }

    private ArrayList<String> prepareChoices(InfoFieldBean infoField) {
        ArrayList<String> choices = new ArrayList<String>();
        List<InfoOptionBean> infoOptions = infoField.getInfoOptions();
        for (InfoOptionBean infoOption : infoOptions) {
            choices.add(infoOption.getName());
        }
        return choices;
    }

    @Override
    protected void convertInput() {
        if (selectedItemText == null) {
            setConvertedInput(null);
        } else {
            List<InfoOptionBean> infoOptions = fieldModel.getObject().getInfoOptions();

            for (InfoOptionBean infoOption : infoOptions) {
                if (selectedItemText.equals(infoOption.getName())) {
                    setConvertedInput(infoOption);
                    return;
                }
            }

            InfoOptionBean newOption = new InfoOptionBean();
            newOption.setName(selectedItemText);
            newOption.setInfoField(fieldModel.getObject());
            newOption.setStaticData(false);
            setConvertedInput(newOption);
        }
    }

    protected void onChange(AjaxRequestTarget target) { }

}
