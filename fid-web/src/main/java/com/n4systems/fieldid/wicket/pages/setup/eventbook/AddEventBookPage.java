package com.n4systems.fieldid.wicket.pages.setup.eventbook;

import com.n4systems.fieldid.service.event.EventBookService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.org.OrgLocationPicker;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.setup.AssetsAndEventsPage;
import com.n4systems.model.EventBook;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

/**
 * This page allows the user to add a new EventBook and save it.
 *
 * These records are comprised of only three editable fields, so the form is quite small.
 *
 * Created by Jordan Heath on 07/08/14.
 */
public class AddEventBookPage extends FieldIDTemplatePage {

    @SpringBean
    protected EventBookService eventBookService;

    protected IModel<EventBook> thisBook;

    //This warning lies.  This IS assigned, but not clearly enough for the IDE to realise it.
    protected BaseOrg thisBaseOrg;


    public AddEventBookPage() {
        this.thisBook = Model.of(getEventBook());
    }

    @Override
    public void onInitialize() {
        super.onInitialize();

        Form<Void> form = new Form<Void>("form") {

            @Override
            public void onSubmit() {
                //The OrgPicker control is kind of weird... you need to handle the BaseOrg object outside of the
                //EventBook, then set that field on the EventBook before saving.
                thisBook.getObject().setOwner(thisBaseOrg);

                if(isEdit()) {
                    eventBookService.updateEventBook(thisBook.getObject(), getCurrentUser());
                } else {
                    eventBookService.saveEventBook(thisBook.getObject(), getCurrentUser());
                }

                EventBooksListAllPage nextPage = new EventBooksListAllPage();
                nextPage.info(new FIDLabelModel("message.saved_event_book",
                                                thisBook.getObject().getName()).getObject());
                setResponsePage(nextPage);
            }
        };

        //Now to add the fields.
        RequiredTextField<String> name;
        form.add(name = new RequiredTextField<String>("name",
                                                      new PropertyModel<String>(thisBook.getObject(),
                                                                                "name")));
        //This also must have a unique name, so we need a unique name validator.
        name.add(new EventBookUniqueNameValidator(thisBook.getObject().getId()));

        form.add(new OrgLocationPicker("orgPickerField", new PropertyModel<BaseOrg>(this, "thisBaseOrg")).withAutoUpdate().setRequired(true));

        form.add(new CheckBox("openCheckBox",
                              new PropertyModel<Boolean>(thisBook, "open")));

        Button submitButton;
        form.add(submitButton = new Button("saveButton"));
        submitButton.setOutputMarkupId(true);
        form.add(new Link("cancelLink") {
            @Override
            public void onClick() {
                setResponsePage(new EventBooksListAllPage());
            }
        });

        add(form);

        add(new FIDFeedbackPanel("feedbackPanel"));
    }


    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.add_event_book"));
    }


    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        BookmarkablePageLink<Void> pageLink = new BookmarkablePageLink<Void>(linkId, AssetsAndEventsPage.class);
        pageLink.add(new FlatLabel(linkLabelId, new FIDLabelModel("label.back_to_setup")));
        return pageLink;
    }


    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                        aNavItem().label(new FIDLabelModel("nav.view_all.count",
                                eventBookService.getActiveEventBookCount()))
                                .page(EventBooksListAllPage.class)
                                .build(),

                        aNavItem().label(new FIDLabelModel("nav.view_all_archived.count",
                                eventBookService.getArchivedEventBookCount()))
                                .page(EventBooksListArchivedPage.class)
                                .build(),

                        aNavItem().label(new FIDLabelModel("nav.add"))
                                .page(AddEventBookPage.class)
                                .onRight()
                                .build()
                )
        );
    }


    /**
     * This method simply determines whether the page is used for Editing or adding a new EventBook.
     *
     * If you extend this page, you can override this method to return a new value.
     *
     * @return A boolean value indicating whether or not the page is in "Edit" mode.
     */
    protected boolean isEdit() {
        return false;
    }



    private class EventBookUniqueNameValidator extends AbstractValidator<String> {
        private Long id;

        public EventBookUniqueNameValidator(Long id) {
            this.id = id;
        }

        @Override
        protected void onValidate(IValidatable<String> stringIValidatable) {
            String name = stringIValidatable.getValue();

            if(eventBookService.exists(name, id)) {
                ValidationError error = new ValidationError();
                error.addMessageKey("error.eventbookduplicate");
                stringIValidatable.error(error);
            }
        }
    }


    private EventBook getEventBook() {
        EventBook returnMe = new EventBook();
        returnMe.setTenant(getTenant());
        return returnMe;
    }
}
