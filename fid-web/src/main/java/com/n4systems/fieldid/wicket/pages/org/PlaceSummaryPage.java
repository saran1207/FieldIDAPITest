package com.n4systems.fieldid.wicket.pages.org;

import com.n4systems.fieldid.service.org.PlaceService;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.components.addressinfo.AddressPanel;
import com.n4systems.fieldid.wicket.components.form.InlineEditableForm;
import com.n4systems.fieldid.wicket.components.form.LinkFieldsBehavior;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.model.Address;
import com.n4systems.model.GpsLocation;
import com.n4systems.model.PlaceEvent;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class PlaceSummaryPage extends PlacePage {

    private @SpringBean PlaceService placeService;

    // BOGUS TEST DATA
    Address address = new Address("111 queen street, east");
    String name="susan richardson",
            email="foo@bar.com",
            phone="123 456 7890",
            fax,
            orgName = "111 Queen St.",
            orgId,
            notes;
    // ---------------


    private final GoogleMap map;

    public PlaceSummaryPage(PageParameters params) {
        super(params);

        //add(new GoogleMap("map",ProxyModel.of(model, on(BaseOrg.class).getGpsLocation())));
        add(map = new GoogleMap("map", Model.of(new GpsLocation(43.70263, -79.46654))));

        add(createFutureEventsListView());

        add(new Link("viewAll") {
            @Override public void onClick() {
                setResponsePage(new PlaceEventsPage(PageParametersBuilder.id(getOrg().getId()).add(PlaceEventsPage.OPEN_PARAM,"true")));
            }
        });

        add(new AjaxLink("attachmentsLink") {
            @Override public void onClick(AjaxRequestTarget target) {
                //updateContent(Content.ATTACHMENTS, target);  // make this more of settings thang...?
            }
        }.add(new Label("label", "8 attachments")));
        
        add(new ContextImage("img", "images/add-photo-slate.png"));

        add(new InlineEditableForm("contact").withSaveCancelEditLinks()
                .add(new TextField("name", new PropertyModel(PlaceSummaryPage.this, "name")))
                .add(new TextField("email", new PropertyModel(PlaceSummaryPage.this, "email")))
                .add(new AddressPanel("address", new PropertyModel(PlaceSummaryPage.this, "address")).withExternalMap(map.getJsVar()))
                .add(new TextField("phone", new PropertyModel(PlaceSummaryPage.this, "phone")))
                .add(new TextField("fax", new PropertyModel(PlaceSummaryPage.this, "fax"))));

        add(new InlineEditableForm("general").withSaveCancelEditLinks()
                .add(new TextField("name", new PropertyModel(PlaceSummaryPage.this, "orgName")).add(new LinkFieldsBehavior(".js-title-label").forTextField()))
                .add(new TextField("id", new PropertyModel(PlaceSummaryPage.this, "orgId")))
                .add(new TextArea<String>("notes", new PropertyModel(PlaceSummaryPage.this, "notes")))
        );
    }

    @Override
    public String getMainCss() {
        return "place-summary";
    }

    private Component createFutureEventsListView() {
        final FutureEventsModel model = new FutureEventsModel();
        ListView<PlaceEvent> view = new ListView<PlaceEvent>("events", model ) {
            @Override protected void populateItem(ListItem<PlaceEvent> item) {
                PlaceEvent event = item.getModelObject();
                item.add(new Label("due", Model.of(event.getDueDate())));
                // TODO : for debugging only. remove this when we know all data is valid.
                if ( event.getEventType()==null) {
                    item.add(new Label("type", "ERROR : event '" + event.getId() + "' has null type???"));
                } else {
                    item.add(new Label("type", Model.of(event.getEventType().getDisplayName())));
                }
                item.add(new Label("assignee", Model.of("joe smith")));
            }

            @Override public boolean isVisible() {
                return model.getObject().size()>0;
            }
        };
        return view;
    }

    class FutureEventsModel extends LoadableDetachableModel<List<PlaceEvent>> {

        @Override
        protected List<PlaceEvent> load() {
            return placeService.getOpenEventsFor(getOrg(), 7);
        }
    }
}
