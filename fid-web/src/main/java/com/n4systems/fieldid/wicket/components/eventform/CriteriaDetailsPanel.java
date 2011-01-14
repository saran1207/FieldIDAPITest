package com.n4systems.fieldid.wicket.components.eventform;

import com.n4systems.fieldid.wicket.components.eventform.details.OneClickDetailsPanel;
import com.n4systems.fieldid.wicket.components.eventform.details.SelectDetailsPanel;
import com.n4systems.fieldid.wicket.components.eventform.details.TextFieldDetailsPanel;
import com.n4systems.model.Criteria;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.SelectCriteria;
import com.n4systems.model.StateSet;
import com.n4systems.model.TextFieldCriteria;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class CriteriaDetailsPanel extends Panel {

    private StringListEditor recommendationsEditor;
    private StringListEditor deficienciesEditor;

    public CriteriaDetailsPanel(String id, IModel<Criteria> criteriaModel) {
        super(id, criteriaModel);
        setOutputMarkupPlaceholderTag(true);
        add(recommendationsEditor = new StringListEditor("recommendationsEditor", new PropertyModel<List<String>>(criteriaModel, "recommendations")));
        add(deficienciesEditor = new StringListEditor("deficienciesEditor", new PropertyModel<List<String>>(criteriaModel, "deficiencies")));
    }

    @Override
    protected void onModelChanged() {
        if (get("specificDetailsPanel") != null) {
            remove("specificDetailsPanel");
        }

        Criteria criteria = (Criteria) getDefaultModelObject();
        if (criteria instanceof OneClickCriteria) {
            add(new OneClickDetailsPanel("specificDetailsPanel", new Model<OneClickCriteria>((OneClickCriteria) criteria)) {
                @Override
                protected void onStateSetSelected(StateSet stateSet) {
                    CriteriaDetailsPanel.this.onStateSetSelected(stateSet);
                }
            });
        } else if (criteria instanceof TextFieldCriteria) {
            add(new TextFieldDetailsPanel("specificDetailsPanel", new Model<TextFieldCriteria>((TextFieldCriteria) criteria)));
        } else if (criteria instanceof SelectCriteria) {
        	add(new SelectDetailsPanel("specificDetailsPanel", new Model<SelectCriteria>((SelectCriteria) criteria)));
        }
    }

    protected void onStateSetSelected(StateSet stateSet) { }

}
