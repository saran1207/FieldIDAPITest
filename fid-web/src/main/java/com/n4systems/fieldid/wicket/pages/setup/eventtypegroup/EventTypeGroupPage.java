package com.n4systems.fieldid.wicket.pages.setup.eventtypegroup;

import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

/**
 * Created by rrana on 2014-08-04.
 */
public class EventTypeGroupPage extends FieldIDTemplatePage {

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.event_type_group_list_type"));
    }

    @Override
    protected void addNavBar(String navBarId) {
        add(new NavigationBar(navBarId,
                aNavItem().label(new FIDLabelModel("nav.view_all")).page(EventTypeGroupListPage.class).build(),
                aNavItem().label(new FIDLabelModel("nav.view_all_archived")).page(EventTypeGroupListArchivePage.class).build(),
                aNavItem().label(new FIDLabelModel("nav.add")).page(EventTypeGroupAddPage.class).onRight().build()
        ));
    }

}