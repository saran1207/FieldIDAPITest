package com.n4systems.fieldid.wicket.components.eventform;

import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.TooltipImage;
import com.n4systems.fieldid.wicket.components.eventform.details.*;
import com.n4systems.fieldid.wicket.components.eventform.details.oneclick.OneClickCriteriaLogicForm;
import com.n4systems.fieldid.wicket.components.eventform.details.oneclick.OneClickDetailsPanel;
import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.fieldid.wicket.components.modal.FIDModalWindow;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.*;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

public class CriteriaDetailsPanel extends Panel {

    private CheckBox requiredCheckBox;
    private WebMarkupContainer scoreRequiredLabel;

    private DialogModalWindow modalWindow;

    public CriteriaDetailsPanel(String id, IModel<Criteria> criteriaModel) {
        super(id, criteriaModel);
        add(new TooltipImage("tooltip", new StringResourceModel("label.tooltip.criteria_settings", this, null)));
        add(requiredCheckBox = new CheckBox("required", new PropertyModel<Boolean>(criteriaModel, "required")) {
            {
                setOutputMarkupId(true);
                add(new UpdateComponentOnChange());
            }
        });
        setOutputMarkupPlaceholderTag(true);
        add(scoreRequiredLabel = new WebMarkupContainer("scoreRequired"));
        scoreRequiredLabel.setOutputMarkupPlaceholderTag(true);
        scoreRequiredLabel.setVisible(false);

        add(modalWindow = new DialogModalWindow("modalWindow"));
        modalWindow.setTitle(new FIDLabelModel("title.criteria_logic_setup"));
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
                protected void onStateSetSelected(ButtonGroup buttonGroup) {
                    // we need to notify when buttons are selected since new one click criteria need to have the previous choice
                    // pre selected as they're created. We could generalize this if it became necessary for other criteria types
                    CriteriaDetailsPanel.this.onStateSetSelected(buttonGroup);
                }

                @Override
                protected void onSetsResultSelected(boolean setsResult) {
                    CriteriaDetailsPanel.this.onSetsResultSelected(setsResult);
                }


                @Override
                protected void onConfigureCriteriaLogic(AjaxRequestTarget target, Button button) {
                    modalWindow.setContent(new OneClickCriteriaLogicForm(modalWindow.getContentId(), Model.of(button)));
                    modalWindow.show(target);
                    modalWindow.setInitialWidth(300);
                }
            });
        } else if (criteria instanceof TextFieldCriteria) {
            add(new TextFieldDetailsPanel("specificDetailsPanel", new Model<TextFieldCriteria>((TextFieldCriteria) criteria)));
        } else if (criteria instanceof SelectCriteria) {
        	add(new SelectDetailsPanel("specificDetailsPanel", new Model<SelectCriteria>((SelectCriteria) criteria)) {
                @Override
                protected void onConfigureCriteriaLogic() {
                    //TODO Open modal window and set content to appropriate panel
                }
            });
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
        	add(new NumberFieldDetailsPanel("specificDetailsPanel", new Model<NumberFieldCriteria>((NumberFieldCriteria) criteria)) {
                @Override
                protected void onConfigureCriteriaLogic() {
                    //TODO Open modal window and set content to appropriate panel
                }
            });
        } else if (criteria instanceof ObservationCountCriteria) {
            add(new ObservationCountDetailsPanel("specificDetailsPanel", new Model<ObservationCountCriteria>((ObservationCountCriteria) criteria)));
        }

        if (criteria instanceof ScoreCriteria) {
            requiredCheckBox.setVisible(false);
            scoreRequiredLabel.setVisible(true);
        } else {
            requiredCheckBox.setVisible(isRequiredCriteria(criteria));
            scoreRequiredLabel.setVisible(false);
        }
    }

    protected void onStateSetSelected(ButtonGroup buttonGroup) { }

    protected void onSetsResultSelected(boolean setsResult) { }
    
    protected void onScoreGroupSelected(ScoreGroup scoreGroup) { }

    private boolean isRequiredCriteria(Criteria criteria) {
        return !(criteria instanceof ObservationCountCriteria) && !(criteria instanceof OneClickCriteria);
    }
}
