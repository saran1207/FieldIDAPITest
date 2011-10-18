package com.n4systems.fieldid.wicket.components.eventform;

import java.util.List;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.n4systems.fieldid.wicket.components.eventform.details.ComboBoxDetailsPanel;
import com.n4systems.fieldid.wicket.components.eventform.details.DateFieldDetailsPanel;
import com.n4systems.fieldid.wicket.components.eventform.details.NumberFieldDetailsPanel;
import com.n4systems.fieldid.wicket.components.eventform.details.OneClickDetailsPanel;
import com.n4systems.fieldid.wicket.components.eventform.details.ScoreDetailsPanel;
import com.n4systems.fieldid.wicket.components.eventform.details.SelectDetailsPanel;
import com.n4systems.fieldid.wicket.components.eventform.details.SignatureDetailsPanel;
import com.n4systems.fieldid.wicket.components.eventform.details.TextFieldDetailsPanel;
import com.n4systems.fieldid.wicket.components.eventform.details.UnitOfMeasureDetailsPanel;
import com.n4systems.model.ComboBoxCriteria;
import com.n4systems.model.Criteria;
import com.n4systems.model.DateFieldCriteria;
import com.n4systems.model.NumberFieldCriteria;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.ScoreCriteria;
import com.n4systems.model.ScoreGroup;
import com.n4systems.model.SelectCriteria;
import com.n4systems.model.SignatureCriteria;
import com.n4systems.model.StateSet;
import com.n4systems.model.TextFieldCriteria;
import com.n4systems.model.UnitOfMeasureCriteria;

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
                    // we need to notify when buttons are selected since new one click criteria need to have the previous choice
                    // pre selected as they're created. We could generalize this if it became necessary for other criteria types
                    CriteriaDetailsPanel.this.onStateSetSelected(stateSet);
                }

                @Override
                protected void onSetsResultSelected(boolean setsResult) {
                    CriteriaDetailsPanel.this.onSetsResultSelected(setsResult);
                }
            });
        } else if (criteria instanceof TextFieldCriteria) {
            add(new TextFieldDetailsPanel("specificDetailsPanel", new Model<TextFieldCriteria>((TextFieldCriteria) criteria)));
        } else if (criteria instanceof SelectCriteria) {
        	add(new SelectDetailsPanel("specificDetailsPanel", new Model<SelectCriteria>((SelectCriteria) criteria)));
        } else if (criteria instanceof ComboBoxCriteria) {
        	add(new ComboBoxDetailsPanel("specificDetailsPanel", new Model<ComboBoxCriteria>((ComboBoxCriteria) criteria)));
        } else if (criteria instanceof UnitOfMeasureCriteria) {
            add(new UnitOfMeasureDetailsPanel("specificDetailsPanel", new Model<UnitOfMeasureCriteria>((UnitOfMeasureCriteria) criteria)));
        } else if (criteria instanceof SignatureCriteria) {
            add(new SignatureDetailsPanel("specificDetailsPanel", new Model<SignatureCriteria>((SignatureCriteria) criteria)));
        } else if (criteria instanceof DateFieldCriteria) {
            add(new DateFieldDetailsPanel("specificDetailsPanel", new Model<DateFieldCriteria>((DateFieldCriteria) criteria)));
        } else if (criteria instanceof ScoreCriteria) {
            add(new ScoreDetailsPanel("specificDetailsPanel", new Model<ScoreCriteria>((ScoreCriteria) criteria)){
            	@Override
            	protected void onScoreGroupSelected(ScoreGroup scoreGroup) {
            		CriteriaDetailsPanel.this.onScoreGroupSelected(scoreGroup);            		
            	}
            	
            });
        } else if (criteria instanceof NumberFieldCriteria) {
        	add(new NumberFieldDetailsPanel("specificDetailsPanel", new Model<NumberFieldCriteria>((NumberFieldCriteria) criteria)));
        }
    }

    protected void onStateSetSelected(StateSet stateSet) { }

    protected void onSetsResultSelected(boolean setsResult) { }
    
    protected void onScoreGroupSelected(ScoreGroup scoreGroup) { }
}
