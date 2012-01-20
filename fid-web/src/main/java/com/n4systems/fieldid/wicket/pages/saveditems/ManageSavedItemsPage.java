package com.n4systems.fieldid.wicket.pages.saveditems;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.DateTimeLabel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.reporting.RunSavedReportPage;
import com.n4systems.model.saveditem.SavedItem;
import com.n4systems.model.saveditem.SavedReportItem;
import com.n4systems.model.user.User;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.List;

public class ManageSavedItemsPage extends FieldIDFrontEndPage {

    @SpringBean
    private UserService userService;

    @SpringBean
    private PersistenceService persistenceService;

    private WebMarkupContainer itemsListContainer;

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.manage_my_saved_items"));
    }

    public ManageSavedItemsPage() {
        itemsListContainer = new WebMarkupContainer("itemsListContainer");
        itemsListContainer.setOutputMarkupId(true);
        add(itemsListContainer);

        itemsListContainer.add(new ListView<SavedItem>("itemsList", createSavedItemsModel()) {
            @Override
            protected void populateItem(final ListItem<SavedItem> item) {
                final EditItemNameForm editItemNameForm = new EditItemNameForm("editNameForm", item.getModel());

                item.add(editItemNameForm);
                item.add(new DateTimeLabel("modifiedDate", new PropertyModel<Date>(item.getModel(), "modified")));
                item.add(new Label("type", new FIDLabelModel(new PropertyModel<String>(item.getModel(), "titleLabelKey"))));
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
            }
        });
    }

    private Link createLink(final IModel<SavedItem> model) {
        final PageParameters params = PageParametersBuilder.id(model.getObject().getId());
        Link link = null;

        if (model.getObject() instanceof SavedReportItem) {
            link = new BookmarkablePageLink<Void>("viewItemLink", RunSavedReportPage.class, params);
        }

        if (link == null) {
            throw new RuntimeException("don't know how to link to view page for: " + model.getObject().getClass());
        }

        link.add(new Label("itemName", new PropertyModel<String>(model, "name")));
        link.setOutputMarkupPlaceholderTag(true);

        return link;
    }

    class EditItemNameForm extends Form {

        private Link viewLink;
        private WebMarkupContainer editContainer;

        public EditItemNameForm(String id, final IModel<SavedItem> itemModel) {
            super(id);

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

}
