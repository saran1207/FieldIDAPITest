package com.n4systems.fieldid.wicket.components.eventform;

import com.n4systems.fieldid.utils.Predicate;
import com.n4systems.fieldid.wicket.components.AppendToClassIfCondition;
import com.n4systems.fieldid.wicket.components.eventform.util.CriteriaCopyUtil;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.TextFieldCriteria;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import java.util.Arrays;
import java.util.List;

public class CriteriaPanel extends Panel {

    private int currentlySelectedIndex = 1;

    private FeedbackPanel feedbackPanel;
    private CriteriaAddForm criteriaAddForm;

    public CriteriaPanel(String id) {
        super(id);
        setOutputMarkupPlaceholderTag(true);
        add(new ListView<Criteria>("criteria", new PropertyModel<List<Criteria>>(CriteriaPanel.this, "criteriaSection.criteria")) {
            @Override
            protected void populateItem(final ListItem<Criteria> item) {
                item.add(new EditCopyDeleteItemPanel("editCopyDeletePanel", new PropertyModel<String>(item.getModel(), "displayText"), new PropertyModel<String>(item.getModel(), "typeDescription")) {
                    @Override
                    protected void onViewLinkClicked(AjaxRequestTarget target) {
                        currentlySelectedIndex = item.getIndex();
                        onCriteriaSelected(target, item.getModelObject());
                    }

                    @Override
                    protected void onDeleteButtonClicked(AjaxRequestTarget target) {
                        getCriteriaSection().getCriteria().remove(item.getIndex());
                        if (item.getIndex() == currentlySelectedIndex) {
                            currentlySelectedIndex = -1;
                            onCurrentCriteriaDeleted(target);
                        } else if (item.getIndex() < currentlySelectedIndex) {
                            // Shift the selection by one if an item higher in the list was deleted
                            currentlySelectedIndex -= 1;
                        }
                        onCriteriaListUpdated(target);
                    }

                    @Override
                    protected void onCopyLinkClicked(AjaxRequestTarget target) {
                        Criteria copiedCriteria = new CriteriaCopyUtil().copyCriteria(item.getModelObject(), getCriteriaSection().getCriteria());
                        getCriteriaSection().getCriteria().add(copiedCriteria);
                        onCriteriaAdded(target, copiedCriteria, getCriteriaSection().getCriteria().size() - 1);
                    }

                    @Override
                    public String getDeleteImage() {
                        return "images/small-x.png";
                    }
                });

                item.add(new AppendToClassIfCondition("selectedCriteria", new Predicate() {
                    @Override
                    public boolean evaluate() {
                        return item.getIndex() == currentlySelectedIndex;
                    }
                }));
            }
        });
        add(criteriaAddForm = new CriteriaAddForm("criteriaAddForm"));
        add(feedbackPanel = new FeedbackPanel("feedbackPanel"));
        feedbackPanel.setOutputMarkupId(true);
    }

    public CriteriaSection getCriteriaSection() {
        return (CriteriaSection) getDefaultModelObject();
    }

    class CriteriaAddForm extends Form {
        private List<String> criteriaTypes = Arrays.asList("One-Click", "Text Field");
        protected TextField addTextField;
        private String criteriaName;
        private String criteriaType;

        public CriteriaAddForm(String id) {
            super(id);
            add(new DropDownChoice<String>("criteriaType", new PropertyModel<String>(this, "criteriaType"), criteriaTypes).setRequired(true));
            add(addTextField = new RequiredTextField<String>("criteriaName", new PropertyModel<String>(this, "criteriaName")));
            addTextField.setOutputMarkupId(true);
            add(new AjaxButton("submitButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    Criteria criteria = null;
                    if ("One-Click".equals(criteriaType)) {
                        criteria = new OneClickCriteria();
                    } else if ("Text Field".equals(criteriaType)) {
                        criteria = new TextFieldCriteria();
                    }
                    criteria.setDisplayText(criteriaName);
                    criteriaName = null;
                    getCriteriaSection().getCriteria().add(criteria);
                    onCriteriaAdded(target, criteria, getCriteriaSection().getCriteria().size() - 1);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.addComponent(feedbackPanel);
                }
            });
        }
    }

    protected void onCriteriaAdded(AjaxRequestTarget target, Criteria criteria, int newIndex) { }

    protected void onCriteriaSelected(AjaxRequestTarget target, Criteria criteria) { }

    protected void onCurrentCriteriaDeleted(AjaxRequestTarget target) { }

    protected void onCriteriaListUpdated(AjaxRequestTarget target) { }

    public String getAddTextFieldId() {
        return criteriaAddForm.addTextField.getMarkupId();
    }

    public void clearSelection() {
        currentlySelectedIndex = -1;
    }

    public void setSelectedIndex(int newIndex) {
        this.currentlySelectedIndex = newIndex;
    }

}
