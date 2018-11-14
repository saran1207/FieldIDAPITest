package com.n4systems.fieldid.wicket.pages.autoattributes;

import com.n4systems.fieldid.actions.helpers.InfoOptionInput;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import rfid.ejb.entity.InfoFieldBean;

import java.util.List;

/**
 * Created by agrabovskis on 2018-11-14.
 */
public class AutoAttributeDisplaySimpleTextFieldPanel extends Panel {

    private InfoFieldBean infoFieldBean;
    private IModel<List<InfoOptionInput>> infoOptionInputModel;


    public AutoAttributeDisplaySimpleTextFieldPanel(String id, InfoFieldBean infoFieldBean, IModel<List<InfoOptionInput>> infoOptionInputModel) {
        super(id);
        this.infoFieldBean = infoFieldBean;
        this.infoOptionInputModel = infoOptionInputModel;
    }

    private void addComponents() {
        String infoOptionInputName = "";
        for (InfoOptionInput infoOptionInput : infoOptionInputModel.getObject()) {
            if (infoFieldBean.getUniqueID().equals(infoOptionInput.getInfoFieldId())) {
                infoOptionInputName = infoOptionInput.getName();
            }
        }
        add(new Label("optionName", infoOptionInputName));
    }

}
