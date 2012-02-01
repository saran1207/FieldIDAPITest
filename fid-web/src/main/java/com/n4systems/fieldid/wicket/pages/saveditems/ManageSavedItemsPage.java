package com.n4systems.fieldid.wicket.pages.saveditems;

import java.util.Date;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.sortable.SortableAjaxBehavior;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.SimpleSortableAjaxBehavior;
import com.n4systems.fieldid.wicket.behavior.TooltipBehavior;
import com.n4systems.fieldid.wicket.components.DateTimeLabel;
import com.n4systems.fieldid.wicket.components.TwoStateAjaxLink;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.RunSavedSearchPage;
import com.n4systems.fieldid.wicket.pages.reporting.RunSavedReportPage;
import com.n4systems.model.saveditem.SavedItem;
import com.n4systems.model.saveditem.SavedReportItem;
import com.n4systems.model.saveditem.SavedSearchItem;
import com.n4systems.model.user.User;

@SuppressWarnings("serial")
public class ManageSavedItemsPage extends FieldIDFrontEndPage {

	private static final String HIGHLIGHT_REORDER_JS = "$('.list').delegate('tr', 'mousedown', function(event) { if(event.which==1) $(this).css('background-color','#eee'); } );";
	private static final String UNHIGHLIGHT_REORDER_JS = "$('.list').delegate('tr', 'mouseup', function(event) { if(event.which==1) $(this).css('background-color', '#fff' ); } );";
	    
	
    @SpringBean
    private UserService userService;

    @SpringBean
    private PersistenceService persistenceService;

    private WebMarkupContainer itemsListContainer;
    private SortableAjaxBehavior<WebMarkupContainer> sortableBehavior;

    private boolean reorderState = false;
    private boolean displayLastSearches;

    private List<SavedItem> savedItems;

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.manage_my_saved_items"));
    }

    private void setSorting(AjaxRequestTarget target, boolean sorting) {
        this.reorderState = sorting;
        target.add(itemsListContainer);
        sortableBehavior.setDisabled(!sorting);
    }

    public ManageSavedItemsPage() {
        displayLastSearches = getUser().isDisplayLastRunSearches();

        add(new AjaxCheckBox("displayLastRunSearches", new PropertyModel<Boolean>(this, "displayLastSearches")) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                final User user = userService.getUser(getUser().getId());
                user.setDisplayLastRunSearches(displayLastSearches);
                persistenceService.save(user);
            }
        });

        // if in "reorder" mode, then turn off row highlighting.
        IModel<String> reorderModel = new AbstractReadOnlyModel<String>() {
			@Override public String getObject() {
				return reorderState ? "no-highlight" : "";
			}        	
        };
        itemsListContainer = new WebMarkupContainer("itemsListContainer");
        itemsListContainer.add(new AttributeAppender("class", reorderModel, " "));        
        itemsListContainer.setOutputMarkupId(true);
        add(itemsListContainer);

        add(new TwoStateAjaxLink("reorderButton", new FIDLabelModel("label.reorder"), new FIDLabelModel("label.donereordering")) {
            @Override
            protected void onEnterInitialState(AjaxRequestTarget target) {
                setSorting(target, false);
            }

            @Override
            protected void onEnterSecondaryState(AjaxRequestTarget target) {
                setSorting(target, true);
            }
        });

        final IModel<List<SavedItem>> savedItemsModel = createSavedItemsModel();
        itemsListContainer.add(new ListView<SavedItem>("itemsList", savedItemsModel) {
            @Override
            protected void populateItem(final ListItem<SavedItem> item) {
                final EditItemNameForm editItemNameForm = new EditItemNameForm("editNameForm", item.getModel());
                item.setOutputMarkupId(true);

                WebMarkupContainer firstColumn = new WebMarkupContainer("firstColumn");
                firstColumn.add(createColspanModifier());
                firstColumn.add(editItemNameForm);
                item.add(firstColumn);
                item.add(new DateTimeLabel("modifiedDate", new PropertyModel<Date>(item.getModel(), "modified")) {
                    @Override
                    public boolean isVisible() {
                        return !reorderState;
                    }
                });
                item.add(new Label("type", new FIDLabelModel(new PropertyModel<String>(item.getModel(), "titleLabelKey"))));

                item.add(new BookmarkablePageLink<Void>("shareLink", ShareSavedItemPage.class, PageParametersBuilder.id(item.getModelObject().getId())));

                item.add(new AjaxLink<Void>("editNameLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        editItemNameForm.setEditMode(target, true);
                    }
                });

                item.add(new AjaxLink("deleteLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        final User user = getUser();
                        user.getSavedItems().remove(item.getModelObject());
                        target.add(itemsListContainer);
                        persistenceService.save(user);
                    }
                });

                IModel<String> sharedTooltip = new StringResourceModel("shared_by", this, new Model<SavedItem>(item.getModelObject()));
                final ContextImage sharedImage = new ContextImage("sharedByImage", "images/shared-icon.png");
                sharedImage.setVisible(item.getModelObject().getSharedByName() != null);
                sharedImage.add(new TooltipBehavior(sharedTooltip));
                item.add(sharedImage);
            }
        });

        sortableBehavior = new SimpleSortableAjaxBehavior<WebMarkupContainer>() {
            public void onUpdate(WebMarkupContainer component, int index, AjaxRequestTarget target) {
                SavedItem item = (SavedItem) component.getDefaultModelObject();
                int oldIndex = savedItemsModel.getObject().indexOf(item);
                
                savedItemsModel.getObject().remove(oldIndex);
                savedItemsModel.getObject().add(index, item);

                final User user = userService.getUser(getUser().getId());
                user.setSavedItems(savedItemsModel.getObject());
                persistenceService.save(user);

                target.add(itemsListContainer);
            }
        };

        sortableBehavior.setDisabled(true);
        itemsListContainer.add(sortableBehavior);
    }

    private Link createLink(final IModel<SavedItem> model) {
        final PageParameters params = PageParametersBuilder.id(model.getObject().getId());
        Link link = null;

        if (model.getObject() instanceof SavedReportItem) {
            link = new BookmarkablePageLink<Void>("viewItemLink", RunSavedReportPage.class, params);
        } else if (model.getObject() instanceof SavedSearchItem) {
            link = new BookmarkablePageLink<Void>("viewItemLink", RunSavedSearchPage.class, params);
        }

        if (link == null) {
            throw new RuntimeException("don't know how to link to view page for: " + model.getObject().getClass());
        }

        link.add(new Label("itemName", new PropertyModel<String>(model, "name")));
        link.setOutputMarkupPlaceholderTag(true);

        return link;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/component/manage_saved_items.css");
        response.renderOnDomReadyJavaScript(HIGHLIGHT_REORDER_JS);
        response.renderOnDomReadyJavaScript(UNHIGHLIGHT_REORDER_JS);
    }

    class EditItemNameForm extends Form {

        private Link viewLink;
        private WebMarkupContainer editContainer;

        public EditItemNameForm(String id, final IModel<SavedItem> itemModel) {
            super(id);

            add(new ContextImage("reorderImage", "images/reorder-small.png") {
                @Override
                public boolean isVisible() {
                    return reorderState;
                }
            });
            add(viewLink = createLink(itemModel));
            add(editContainer = new WebMarkupContainer("editContainer"));
            editContainer.setVisible(false);
            editContainer.setOutputMarkupPlaceholderTag(true);

            editContainer.add(new RequiredTextField<String>("itemName", new PropertyModel<String>(itemModel, "name")));
            editContainer.add(new AjaxButton("saveNameButton") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    persistenceService.save(itemModel.getObject());
                    setEditMode(target, false);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                }
            });
            editContainer.add(new AjaxLink("cancelLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    setEditMode(target, false);
                }
            });
        }

        public void setEditMode(AjaxRequestTarget target, boolean editMode) {
            target.add(viewLink.setVisible(!editMode));
            target.add(editContainer.setVisible(editMode));
        }

    }

    private LoadableDetachableModel<List<SavedItem>> createSavedItemsModel() {
        return new LoadableDetachableModel<List<SavedItem>>() {
            @Override
            protected List<SavedItem> load() {
                return getUser().getSavedItems();
            }
        };
    }

    private User getUser() {
        return userService.getUser(FieldIDSession.get().getSessionUser().getId());
    }

    private Behavior createColspanModifier() {
        return new AttributeModifier("colspan", "4") {
            @Override
            public boolean isEnabled(Component component) {
                return reorderState;
            }
        };
    }

}
