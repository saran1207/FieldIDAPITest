package com.n4systems.fieldid.wicket.pages.org;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.org.PlaceService;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.components.addressinfo.AddressPanel;
import com.n4systems.fieldid.wicket.components.form.InlineEditableForm;
import com.n4systems.fieldid.wicket.components.form.LinkFieldsBehavior;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.Contact;
import com.n4systems.model.PlaceEvent;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.net.URL;
import java.util.List;

import static ch.lambdaj.Lambda.on;

public class PlaceSummaryPage extends PlacePage {

    private @SpringBean PlaceService placeService;
    private @SpringBean S3Service s3Service;


    private final GoogleMap map;
    private WebMarkupContainer futureEventsListContainer;
    private ListView<PlaceEvent> futureEventsListView;
    private Contact contact;

    public PlaceSummaryPage(PageParameters params) {
        super(params);

        add(map = new GoogleMap("map", ProxyModel.of(orgModel, on(BaseOrg.class).getAddressInfo().getGpsLocation())));

        add(futureEventsListContainer = new WebMarkupContainer("eventsListContainer") {
            @Override public boolean isVisible() {
                return futureEventsListView.getList().size() > 0;
            }
        });
        futureEventsListContainer.add(futureEventsListView = createFutureEventsListView());
        futureEventsListContainer.setOutputMarkupPlaceholderTag(true);

        add(new Link("viewAll") {
            @Override
            public void onClick() {
                setResponsePage(new PlaceEventsPage(PageParametersBuilder.id(getOrg().getId()).add(PlaceEventsPage.OPEN_PARAM, "true")));
            }
        });

        // TODO : change this to fileUpoadWidget.
        add(new AjaxLink("attachmentsLink") {
            @Override public void onClick(AjaxRequestTarget target) {
                //updateContent(Content.ATTACHMENTS, target);  // make this more of settings thang...?
            }
        }.add(new Label("label", "image")));
        
        URL imageUrl = s3Service.getAttachmentUrl(getOrg().getImage());
        if (imageUrl==null) {
            add(new ContextImage("img", "images/add-photo-slate.png"));
        } else {
            add(new ExternalImage("img", imageUrl));
        }

        contact = new Contact(getOrg().getContact());
        add(new InlineEditableForm("contact") {
            @Override protected void onSave(AjaxRequestTarget target) {
                super.onSave(target);
                target.add(map);
            }
            @Override protected void onCancel(AjaxRequestTarget target) {
                super.onCancel(target);
            }
        }       .withSaveCancelEditLinks()
                .add(new TextField("name", ProxyModel.of(contact,on(Contact.class).getName())))
                .add(new TextField("email", ProxyModel.of(contact,on(Contact.class).getEmail())))
                .add(new AddressPanel("address", ProxyModel.of(orgModel, on(BaseOrg.class).getAddressInfo())).withExternalMap(map.getJsVar()).hideIfChildrenHidden())
                .add(new TextField("phone", ProxyModel.of(orgModel,on(BaseOrg.class).getAddressInfo().getPhone1())))
                .add(new TextField("phone2", ProxyModel.of(orgModel,on(BaseOrg.class).getAddressInfo().getPhone2())))
                .add(new TextField("fax", ProxyModel.of(orgModel,on(BaseOrg.class).getAddressInfo().getFax1()))));

        add(new InlineEditableForm("general").withSaveCancelEditLinks()
                .add(new TextField("name", ProxyModel.of(orgModel,on(BaseOrg.class).getName())).add(new LinkFieldsBehavior(".js-title-label").forTextField()))
                .add(new TextArea<String>("notes", ProxyModel.of(orgModel,on(BaseOrg.class).getNotes())))
        );
        // TOOD : override save method for both forms to actually save the stuff. make sure to
        // 1:update map.
        // 2:copy over Contact information into org from temp backing model.

    }

    @Override
    protected void refreshContent(AjaxRequestTarget target) {
        futureEventsListContainer.setVisible(futureEventsListView.getList().size() > 0);
        target.add(futureEventsListContainer);
    }

    @Override
    public String getMainCss() {
        return "place-summary";
    }

    private ListView<PlaceEvent> createFutureEventsListView() {
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
