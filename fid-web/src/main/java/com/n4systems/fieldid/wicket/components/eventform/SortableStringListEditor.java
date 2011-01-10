package com.n4systems.fieldid.wicket.components.eventform;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.odlabs.wiquery.ui.sortable.SortableAjaxBehavior;

import com.n4systems.fieldid.utils.Predicate;
import com.n4systems.fieldid.wicket.components.AppendToClassIfCondition;
import com.n4systems.fieldid.wicket.components.TwoStateAjaxLink;

public class SortableStringListEditor extends SortableListPanel {
	
	private ListView<String> stringList;
    private SortableAjaxBehavior sortableAjaxBehavior;
    private boolean reorderState = false;

	public SortableStringListEditor(String id, IModel<List<String>> listModel) {
		super(id, listModel);
        setOutputMarkupId(true);
        
        WebMarkupContainer sortableCriteriaContainer = new WebMarkupContainer("sortableCriteriaContainer");
        sortableCriteriaContainer.add(stringList = new ListView<String>("stringList", listModel) {
            @Override
            protected void populateItem(final ListItem<String> item) {
            	item.setOutputMarkupId(true);
                item.add(new EditCopyDeleteItemPanel("itemEditor", item.getModel(), false) {
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
                        target.addComponent(SortableStringListEditor.this);
                    }

                    @Override
                    public String getReorderImage() {
                        return "images/small-x.png";
                    }
                    
                    @Override
                    protected boolean isReorderState() {
                        return reorderState;
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
                target.addComponent(SortableStringListEditor.this);
                sortableAjaxBehavior.setDisabled(true);
                reorderState = false;
            }

            @Override
            protected void onEnterSecondaryState(AjaxRequestTarget target) {
                target.addComponent(SortableStringListEditor.this);
                sortableAjaxBehavior.setDisabled(false);
                reorderState = true;
            }
        });

        sortableCriteriaContainer.add(sortableAjaxBehavior = makeSortableBehavior());
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

        public AddStringForm(String id) {
            super(id);
            add(new RequiredTextField<String>("string", new PropertyModel<String>(this, "string")));
            add(new AjaxButton("addButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    getStringList().add(string);
                    string = null;
                    target.addComponent(SortableStringListEditor.this);
                }
            });
        }
        
        @Override
        public boolean isVisible() {
            return !reorderState;
        }
    }


	@Override
	protected int getIndexOfComponent(Component component) {
		String listItem = (String) component.getDefaultModelObject();
		return getStringList().indexOf(listItem);
	}

	@Override
	protected String getSortableContainmentCss() {
		return "#" + getMarkupId();
	}

	@Override
	protected void onItemMoving(int oldIndex, int newIndex,	AjaxRequestTarget target) {
		String movingListItem = getStringList().remove(oldIndex);
		getStringList().add(newIndex, movingListItem);
		target.addComponent(this);
	}

}
