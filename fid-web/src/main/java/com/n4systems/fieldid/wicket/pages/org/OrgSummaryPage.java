package com.n4systems.fieldid.wicket.pages.org;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.EventType;
import com.n4systems.model.GpsLocation;
import com.n4systems.model.builders.EventTypeBuilder;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.List;

import static ch.lambdaj.Lambda.on;

// terminology note : you might also think of this as "places" page.  we might want to refactor code to have common words at some point.
public class OrgSummaryPage extends FieldIDFrontEndPage {

    private EntityModel<BaseOrg> model;

    public <T extends BaseOrg> OrgSummaryPage(T org) {
        this(org.getId());
    }

    public OrgSummaryPage(Long id) {
        super();
        model = new EntityModel(BaseOrg.class, id);
        add(new Label("header", ProxyModel.of(model, on(BaseOrg.class).getName())));
        add(createActions("actions"));
        add(createLeftPanel("left"));
        add(createRightPanel("right"));
    }

    public OrgSummaryPage(PageParameters params) {
        this(params.get("id").toLong());
    }



    private MarkupContainer createActions(String id) {

        WebMarkupContainer actions = new WebMarkupContainer(id);

        MarkupContainer editMenu = new WebMarkupContainer("edit")
                .add(new AjaxLink("details") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        setResponsePage((Page) null);  //  new OrgEditPage(EditSection.DETAILS)
                    }
                })
                .add(new AjaxLink("recurring") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        setResponsePage(new OrgConfigurePage(model, OrgConfigurePage.PageState.RECURRING_EVENTS));
                    }
                })
                .add(new AjaxLink("eventTypes") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        setResponsePage(new OrgConfigurePage(model, OrgConfigurePage.PageState.EVENT_TYPES));
                    }
                });

        MarkupContainer scheduleMenu = new WebMarkupContainer("schedule")
                .add(new AjaxLink("oneTime") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        //show schedule picker dialog.
                    }
                })
                .add(new AjaxLink("recurring") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        // show create recurring action dialog.
                    }
                });


        List<EventType> eventTypes = Lists.newArrayList(EventTypeBuilder.anEventType().named("foobar").build());
        MarkupContainer startMenu = new WebMarkupContainer("start")
                .add(new ListView<EventType>("types", eventTypes) {
                    @Override
                    protected void populateItem(ListItem<EventType> item) {
                        EventType eventType = item.getModelObject();
                        item.add(new AjaxLink("type") {
                            @Override
                            public void onClick(AjaxRequestTarget target) {
                                // start event of this type....setResponsePage(new PerformEvent(this.place,eventType);
                            }
                        }.add(new Label("name", Model.of(eventType.getName()))));
                    }
                });

        return actions.add(startMenu.setRenderBodyOnly(true),editMenu.setRenderBodyOnly(true),scheduleMenu.setRenderBodyOnly(true));
    }


    private Component createLeftPanel(String id) {
        // TODO DD : base this on state?
        return new ViewDetailsLeftPanel(id);
    }

    private Component createRightPanel(String id) {
        // TODO DD : base this on state?
        return new SummaryPanel(id);
    }


    @Override
    public void renderHead(IHeaderResponse response) {
        // for starters, i am basing this page on asset summary page.  later on need to either refactor common css or create my own file.
        response.renderCSSReference("style/newCss/asset/asset.css");
    }


    class ViewDetailsLeftPanel extends Fragment {
        IModel<BaseOrg> model;

        ViewDetailsLeftPanel(String id) {
            super(id,"viewDetails",OrgSummaryPage.this);
            //add(new GoogleMap("map",ProxyModel.of(model, on(BaseOrg.class).getGpsLocation())));
            add(new GoogleMap("map", Model.of(new GpsLocation(43.70263, -79.46654))));
            // add name, email, phone, fax, etc... here..
            add(new TextArea("comments",Model.of("comments go here")));
        }
    }


    class EditPanel extends Fragment {
        IModel<BaseOrg> model;

        EditPanel(String id) {
            super(id, "edit", OrgSummaryPage.this);
//            add(new TextField("address", ProxyModel.of(model,on(BaseOrg.getLocation().getAddress()))));
            add(new TextField("address", Model.of("111 queen street east")));
            add(new TextField("type", Model.of("commercial")));
            add(new TextField("status", Model.of("sold")));
            add(new TextField("name", Model.of("joe smith")));
            add(new TextField("email", Model.of("jsmith@foo.com")));
            add(new TextField("phone", Model.of("123 456 7894")));
            add(new TextField("fax", Model.of("964 745 3528")));
        }
    }



    class SummaryPanel extends Fragment {

        public SummaryPanel(String id) {
            super(id,"summary",OrgSummaryPage.this);
        }
    }

}
