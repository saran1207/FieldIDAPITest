package com.n4systems.fieldid.wicket.components.eventform;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.utils.Predicate;
import com.n4systems.fieldid.wicket.behavior.ClickOnComponentWhenEnterKeyPressedBehavior;
import com.n4systems.fieldid.wicket.components.AppendToClassIfCondition;
import com.n4systems.fieldid.wicket.components.TwoStateAjaxLink;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;
import org.odlabs.wiquery.ui.sortable.SortableAjaxBehavior;

import java.util.List;

public class SortableStringListEditor extends SortableListPanel {
	
	private ListView<String> stringList;
    private SortableAjaxBehavior sortableAjaxBehavior;
    private boolean reorderState = false;
    private FIDFeedbackPanel feedbackPanel;

    public SortableStringListEditor(String id, IModel<List<String>> listModel, IModel<String> addItemLabelModel) {
        this(id, listModel, addItemLabelModel, false);
    }


	public SortableStringListEditor(String id, IModel<List<String>> listModel, IModel<String> addItemLabelModel, boolean displayAddLogicLabels) {
		super(id, listModel);
        setOutputMarkupId(true);

        add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));

        add(new Label("addItemTitle", addItemLabelModel));
        
        WebMarkupContainer sortableCriteriaContainer = new WebMarkupContainer("sortableCriteriaContainer");
        sortableCriteriaContainer.add(stringList = new ListView<String>("stringList", listModel) {
            @Override
            protected void populateItem(final ListItem<String> item) {
            	item.setOutputMarkupId(true);
                item.add(new EditCopyDeleteItemPanel("itemEditor", item.getModel(), null, false, displayAddLogicLabels) {
                    @Override
                    protected void onViewLinkClicked(AjaxRequestTarget target) {
                        currentlySelectedIndex = item.getIndex();
                    }
                	
                	@Override
                    protected void onDeleteButtonClicked(AjaxRequestTarget target) {
                        getStringList().remove(item.getIndex());
                        if (item.getIndex() == currentlySelectedIndex) {
                            currentlySelectedIndex = -1;
                        } else if (item.getIndex() < currentlySelectedIndex) {
                            // Shift the selection by one if an item higher in the list was deleted
                            currentlySelectedIndex -= 1;
                        }
                        target.add(SortableStringListEditor.this);
                    }

                    @Override
                    public String getReorderImage() {
                        return "images/reorder-icon.png";
                    }
                    
                    @Override
                    protected boolean isReorderState() {
                        return reorderState;
                    }

                    @Override
                    protected void onAddLogicClicked(AjaxRequestTarget target, String selectValue) {
                        onAddLogicLinkClicked(target, selectValue);
                    }

                    @Override
                    protected boolean isRuleExists(String selectValue) {
                        return isExistingRule(selectValue);
                    }
                });
                
                item.add(new AppendToClassIfCondition("selectedOption", new Predicate() {
                    @Override
                    public boolean evaluate() {
                        return item.getIndex() == currentlySelectedIndex && !reorderState;
                    }
                }));
            }
        });
        
        add(new TwoStateAjaxLink("reorderCriteriaButton", "Reorder Options", "Done Reordering") {
            @Override
            protected void onEnterInitialState(AjaxRequestTarget target) {
                target.add(SortableStringListEditor.this);
                sortableAjaxBehavior.setDisabled(true);
                reorderState = false;
            }

            @Override
            protected void onEnterSecondaryState(AjaxRequestTarget target) {
                target.add(SortableStringListEditor.this);
                sortableAjaxBehavior.setDisabled(false);
                reorderState = true;
            }
        });

        sortableCriteriaContainer.add(sortableAjaxBehavior = makeSortableBehavior("#" + getMarkupId()));
        add(sortableCriteriaContainer);
        stringList.setOutputMarkupId(true);
        add(new AddStringForm("addStringForm"));
	}
	
    @SuppressWarnings("unchecked")
	public List<String> getStringList() {
        return (List<String>)getDefaultModelObject();
    }
    
    class AddStringForm extends Form {

        private String string;
        private TextField<String> addItemTextField;

        public AddStringForm(String id) {
            super(id);
            add(addItemTextField = new RequiredTextField<String>("string", new PropertyModel<String>(this, "string")));
            addItemTextField.add(new StringValidator.MaximumLengthValidator(getTextFieldLengthLimit()));
            addItemTextField.setOutputMarkupId(true);
            AjaxButton addButton;
            add(addButton = new AjaxButton("addButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    getStringList().add(string);
                    string = null;
                    target.add(SortableStringListEditor.this);
                    focusOnAddItemTextField(target);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedbackPanel);
                }
            });
            addItemTextField.add(new ClickOnComponentWhenEnterKeyPressedBehavior(addButton));
        }
        
        @Override
        public boolean isVisible() {
            return !reorderState;
        }

        private void focusOnAddItemTextField(AjaxRequestTarget target) {
            target.focusComponent(addItemTextField);
        }

    }

	@Override
	protected int getIndexOfComponent(Component component) {
		String listItem = (String) component.getDefaultModelObject();
		return getStringList().indexOf(listItem);
	}


    @Override
	protected void onItemMoving(int oldIndex, int newIndex,	AjaxRequestTarget target) {
        //TODO We have to copy the list before calling add(index, object) due to this bug https://hibernate.atlassian.net/browse/HHH-10375
        List <String> stringList = Lists.newArrayList();
        stringList.addAll(getStringList());

		String movingListItem = stringList.remove(oldIndex);
        stringList.add(newIndex, movingListItem);

        getStringList().clear();
        getStringList().addAll(stringList);
		target.add(this);
	}

    protected int getTextFieldLengthLimit() {
        return 256;
    }

    protected void onAddLogicLinkClicked(AjaxRequestTarget target, String selectValue) {}

    protected boolean isExistingRule(String selectValue) {
        return false;
    }

}
