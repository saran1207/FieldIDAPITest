package com.n4systems.fieldid.wicket.pages.setup.loto;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.model.IsolationPointSourceType;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;

public class LotoSetupPage extends FieldIDTemplatePage {

    public LotoSetupPage() {

        add(new ListView<IsolationPointSourceType>("device", Lists.newArrayList(IsolationPointSourceType.values())) {

            @Override
            protected void populateItem(ListItem<IsolationPointSourceType> item) {
                item.add(new Label("name", new PropertyModel<>(item.getModel(), "identifier")));
                item.add(new BookmarkablePageLink<ManageDevicePage>("editLink", ManageDevicePage.class, PageParametersBuilder.param("type", item.getModelObject().name())));
            }
        });

        add(new BookmarkablePageLink<ManageDevicePage>("editSharedLink", ManageDevicePage.class, PageParametersBuilder.param("type", "all")));

        add(new BookmarkablePageLink<LotoDetailsSetupPage>("editDetailsLink", LotoDetailsSetupPage.class));

    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.loto_setup"));
    }
}
