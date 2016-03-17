package com.n4systems.fieldid.wicket.components.eventform;

import com.n4systems.fieldid.wicket.behavior.UpdateComponentOnChange;
import com.n4systems.fieldid.wicket.components.TooltipImage;
import com.n4systems.fieldid.wicket.components.eventform.details.*;
import com.n4systems.fieldid.wicket.components.eventform.details.number.NumberCriteriaLogicForm;
import com.n4systems.fieldid.wicket.components.eventform.details.number.NumberFieldDetailsPanel;
import com.n4systems.fieldid.wicket.components.eventform.details.oneclick.OneClickCriteriaLogicForm;
import com.n4systems.fieldid.wicket.components.eventform.details.oneclick.OneClickDetailsPanel;
import com.n4systems.fieldid.wicket.components.eventform.details.select.SelectCriteriaLogicPanel;
import com.n4systems.fieldid.wicket.components.modal.DialogModalWindow;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.*;
import com.n4systems.model.criteriarules.CriteriaRule;
import com.n4systems.model.criteriarules.NumberFieldCriteriaRule;
import com.n4systems.model.criteriarules.OneClickCriteriaRule;
import com.n4systems.model.criteriarules.SelectCriteriaRule;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CriteriaDetailsPanel extends Panel {

    private CheckBox requiredCheckBox;
    private WebMarkupContainer scoreRequiredLabel;

    private DialogModalWindow modalWindow;

    public CriteriaDetailsPanel(String id, IModel<Criteria> criteriaModel) {
        super(id, criteriaModel);
        add(new TooltipImage("tooltip", new StringResourceModel("label.tooltip.criteria_settings", this, null)));
        add(requiredCheckBox = new CheckBox("required", new PropertyModel<>(criteriaModel, "required")) {
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
        modalWindow.setInitialWidth(350);

    }

    @Override
    protected void onModelChanged() {
        if (get("specificDetailsPanel") != null) {
            remove("specificDetailsPanel");
        }

        Criteria criteria = (Criteria) getDefaultModelObject();
        if (criteria instanceof OneClickCriteria) {
            add(new OneClickDetailsPanel("specificDetailsPanel", new Model<>((OneClickCriteria) criteria)) {
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
                   OneClickCriteriaRule criteriaRule =
                           criteria.getRules()
                                   .stream()
                                   .filter(rule -> ((OneClickCriteriaRule) rule).getButton().equals(button))
                                   .map(convertMe -> (OneClickCriteriaRule)convertMe)
                                   .findFirst()
                                   .orElse(new OneClickCriteriaRule(criteria, button));

                    modalWindow.setContent(new OneClickCriteriaLogicForm(modalWindow.getContentId(), Model.of(criteriaRule)) {
                        @Override
                        public void onSaveRule(AjaxRequestTarget target, CriteriaRule rule) {
                            //Here, we want to remove any logic pointing to the same button state as the rule we're
                            //saving.  Otherwise, we end up with multiples... and that's just wrong.
                            criteria.getRules().removeAll(criteria.getRules()
                                                                  .stream()
                                                                  .filter(r -> ((OneClickCriteriaRule) r).getButton().getId().equals(((OneClickCriteriaRule)rule).getButton().getId()))
                                                                  .collect(Collectors.toList()));

                            rule.setCriteria(criteria);
                            criteria.getRules().add(rule);
                            modalWindow.close(target);
                            target.add(CriteriaDetailsPanel.this);
                        }

                        @Override
                        public void onRemoveRule(AjaxRequestTarget target, CriteriaRule rule) {
                            criteria.getRules().remove(rule);
                            modalWindow.close(target);
                            target.add(CriteriaDetailsPanel.this);
                        }

                        @Override
                        public boolean hasRule(CriteriaRule rule) {
                            return criteria.getRules().contains(rule);
                        }

                        @Override
                        public void onCancel(AjaxRequestTarget target) {
                            modalWindow.close(target);
                        }
                    });
                    modalWindow.show(target);
                }
            });
        } else if (criteria instanceof TextFieldCriteria) {
            add(new TextFieldDetailsPanel("specificDetailsPanel", new Model<>((TextFieldCriteria) criteria)));
        } else if (criteria instanceof SelectCriteria) {
        	add(new SelectDetailsPanel("specificDetailsPanel", new Model<>((SelectCriteria) criteria)) {
                @Override
                protected void onConfigureCriteriaLogic(AjaxRequestTarget target, String selectValue) {
                    //We only need this list because <-- Incomplete thoughts are confusing.
                    SelectCriteriaRule rule =
                            criteria.getRules()
                                    //Process the rules in Stream mode...
                                    .stream()
                                    //Filter out any rule already using this Select Value
                                    .filter(criteriaRule -> criteriaRule instanceof SelectCriteriaRule &&
                                                            ((SelectCriteriaRule)criteriaRule).getSelectValue().equals(selectValue))
                                    //Now convert these to the right type, to make it easier to deal with.
                                    .map(criteriaRule -> (SelectCriteriaRule)criteriaRule)
                                    //Now get the "first" one, which in this case is guaranteed to be the ONLY one...
                                    .findFirst()
                                    //...unless there are NONE, in which case create a new one.
                                    .orElse(new SelectCriteriaRule(criteria, selectValue));


                    modalWindow.setContent(new SelectCriteriaLogicPanel(modalWindow.getContentId(), Model.of(rule)) {
                        @Override
                        protected void onSaveRule(AjaxRequestTarget target, CriteriaRule rule) {
                            //Here, we want to remove any rule/logic pointing to the same Select value as the rule we're
                            //about to save... again... to prevent duplicates.
                            criteria.getRules().removeAll(criteria.getRules()
                                                                  .stream()
                                                                  .filter(r -> ((SelectCriteriaRule)r).getSelectValue().equals(selectValue))
                                                                  .collect(Collectors.toList()));

                            rule.setCriteria(criteria);
                            criteria.getRules().add(rule);
                            modalWindow.close(target);
                            target.add(CriteriaDetailsPanel.this);
                        }

                        @Override
                        protected void onRemoveRule(AjaxRequestTarget target, CriteriaRule rule) {
                            criteria.getRules().remove(rule);
                            modalWindow.close(target);
                            target.add(CriteriaDetailsPanel.this);
                        }

                        @Override
                        protected boolean hasRule(CriteriaRule rule) {
                            return criteria.getRules().contains(rule);
                        }

                        @Override
                        protected void onCancel(AjaxRequestTarget target) {
                            modalWindow.close(target);
                        }
                    });
                    modalWindow.show(target);
                }

                @Override
                protected boolean isRuleExists(String selectValue) {
                    return criteria.getRules()
                                   .stream()
                                   .filter(criteriaRule -> criteriaRule instanceof SelectCriteriaRule &&
                                            ((SelectCriteriaRule)criteriaRule).getSelectValue().equals(selectValue))
                                   .map(criteriaRule -> (SelectCriteriaRule)criteriaRule)
                                   .findFirst()
                                   .isPresent();
                }
            });
        } else if (criteria instanceof ComboBoxCriteria) {
        	add(new ComboBoxDetailsPanel("specificDetailsPanel", new Model<>((ComboBoxCriteria) criteria)));
        } else if (criteria instanceof UnitOfMeasureCriteria) {
            add(new UnitOfMeasureDetailsPanel("specificDetailsPanel", new Model<>((UnitOfMeasureCriteria) criteria)));
        } else if (criteria instanceof SignatureCriteria) {
            add(new SignatureDetailsPanel("specificDetailsPanel", new Model<>((SignatureCriteria) criteria)));
        } else if (criteria instanceof DateFieldCriteria) {
            add(new DateFieldDetailsPanel("specificDetailsPanel", new Model<>((DateFieldCriteria) criteria)));
        } else if (criteria instanceof ScoreCriteria) {
            add(new ScoreDetailsPanel("specificDetailsPanel", new Model<>((ScoreCriteria) criteria)){
            	@Override
            	protected void onScoreGroupSelected(ScoreGroup scoreGroup) {
            		CriteriaDetailsPanel.this.onScoreGroupSelected(scoreGroup);            		
            	}
            	
            });
        } else if (criteria instanceof NumberFieldCriteria) {
        	add(new NumberFieldDetailsPanel("specificDetailsPanel", new Model<>((NumberFieldCriteria) criteria)) {
                @Override
                public void onConfigureCriteriaLogic(AjaxRequestTarget target) {
                    NumberFieldCriteriaRule rule =
                            criteria.getRules()
                                    .stream()
                                    .filter(criteriaRule -> criteriaRule instanceof NumberFieldCriteriaRule &&
                                            criteriaRule.getCriteria().equals(criteria))
                                    .map(criteriaRule -> (NumberFieldCriteriaRule)criteriaRule)
                                    .findFirst()
                                    .orElse(new NumberFieldCriteriaRule(criteria));

                    modalWindow.setContent(new NumberCriteriaLogicForm(modalWindow.getContentId(), Model.of(rule)) {
                        @Override
                        public void onCancel(AjaxRequestTarget target) {
                            modalWindow.close(target);
                        }

                        @Override
                        public boolean hasRule() {
                            return !criteria.getRules().isEmpty();
                        }

                        @Override
                        public void onRemoveRule(AjaxRequestTarget target, NumberFieldCriteriaRule rule) {
                            criteria.getRules().remove(rule);
                            modalWindow.close(target);
                            updateAddEditLinkLabel();
                            target.add(CriteriaDetailsPanel.this);
                        }

                        @Override
                        public void onSaveRule(AjaxRequestTarget target, NumberFieldCriteriaRule rule) {
                            //We can only ever have one rule on this kind of criteria... so unlike other places, what
                            //we want to do here is REPLACE the "list" of one rule with the new "list" of one rule.
                            rule.setCriteria(criteria);
                            List<CriteriaRule> rules = new ArrayList<>();

                            //This just ensures we're putting clean data into the DB.
                            if(!rule.getComparisonType().equals(NumberFieldCriteriaRule.ComparisonType.BT)) {
                                rule.setValue2(null);
                            }
                            rules.add(rule);
                            criteria.setRules(rules);
                            modalWindow.close(target);
                            updateAddEditLinkLabel();
                            target.add(CriteriaDetailsPanel.this);
                        }
                    });
                    modalWindow.show(target);
                }

                @Override
                public boolean hasRule() {
                    return !criteria.getRules().isEmpty();
                }
            });
        } else if (criteria instanceof ObservationCountCriteria) {
            add(new ObservationCountDetailsPanel("specificDetailsPanel", new Model<>((ObservationCountCriteria) criteria)));
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
