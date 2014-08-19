package com.n4systems.fieldid.wicket.pages.setup.eventbook;

import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.model.EventBook;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

/**
 * This page allows the user to edit an existing EventBook.
 *
 * They may change the title, the state (Open/Closed) and the Owner/BaseOrg.
 *
 * Created by Jordan Heath on 07/08/14.
 */
public class EditEventBookPage extends AddEventBookPage {

    public EditEventBookPage(PageParameters params) {
        this.thisBook = Model.of(getEventBook(params));
        this.thisBaseOrg = thisBook.getObject().getOwner();
    }

    @Override
    protected boolean isEdit() {
        return true;
    }


    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.edit_event_book"));
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

                        aNavItem().label(new FIDLabelModel("nav.edit",
                                eventBookService.getArchivedEventBookCount()))
                                .page(EditEventBookPage.class)
                                .params(PageParametersBuilder.param("eventBookId",
                                        this.thisBook
                                                .getObject()
                                                .getId()))
                                .build(),

                        aNavItem().label(new FIDLabelModel("nav.add"))
                                .page(AddEventBookPage.class)
                                .onRight()
                                .build()
                )
        );
    }

    private EventBook getEventBook(PageParameters params) {
        if(params.get("eventBookId") != null) {
            return eventBookService.getEventBookById(params.get("eventBookId").toLong());
        }

        error(new FIDLabelModel("Error retrieving Event Book... creating a new one instead!!"));

        EventBook returnMe = new EventBook();
        returnMe.setTenant(getTenant());

        return returnMe;
    }
}
