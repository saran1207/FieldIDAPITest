package com.n4systems.fieldid.wicket.pages.pentaho;

import com.n4systems.fieldid.service.pentaho.PentahoService;
import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.navigation.BreadCrumbBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.NavigationItem;
import com.n4systems.fieldid.wicket.pages.DashboardPage;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.template.TemplatePage;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.InlineFrame;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.net.URLEncoder;

/**
 * Created by rrana on 2016-03-07.
 */
public class PentahoTestPage extends FieldIDTemplatePage {

    @SpringBean
    protected PentahoService pentahoService;

    public String urlPerformedBy = "";
    public String urlLocation = "";
    public String urlEventStatus = "";
    public String urlEventResult = "";
    public String urlEventType = "";
    public String urlAssetType = "";
    public String urlAssetStatus = "";

    public String finalViewerUrl = "";
    public String finalEditorUrl = "";

    public String viewer = "http://localhost:8081/pentaho/api/repos/%3Ahome%3Aadmin%3Atest.xanalyzer/viewer?";
    public String editor = "http://localhost:8081/pentaho/api/repos/%3Ahome%3Aadmin%3Atest.xanalyzer/editor?";

    public FidDropDownChoice<String> performedBy;
    public FidDropDownChoice<String> location;
    public FidDropDownChoice<String> eventstatus;
    public FidDropDownChoice<String> eventresult;
    public FidDropDownChoice<String> eventtype;
    public FidDropDownChoice<String> assettype;
    public FidDropDownChoice<String> assetstatus;

    public RedirectPage page;
    public RedirectPage page2;

    public InlineFrame frame;
    public InlineFrame frame2;

    public PentahoTestPage() {

        performedBy = new FidDropDownChoice<String>("performedBy", Model.of(urlPerformedBy), pentahoService.getPerformedByList());
        location = new FidDropDownChoice<String>("location", Model.of(urlLocation), pentahoService.getLocation());
        location.setNullValid(true);
        eventstatus = new FidDropDownChoice<String>("eventstatus", Model.of(urlEventStatus), pentahoService.getEventStatus());
        eventresult = new FidDropDownChoice<String>("eventresult", Model.of(urlEventResult), pentahoService.getEventResult());
        eventtype = new FidDropDownChoice<String>("eventtype", Model.of(urlEventType), pentahoService.getEventType());
        assettype = new FidDropDownChoice<String>("assettype", Model.of(urlAssetType), pentahoService.getAssetType());
        assetstatus = new FidDropDownChoice<String>("assetstatus", Model.of(urlAssetStatus), pentahoService.getAssetStatus());

        add(performedBy);
        add(location);
        add(eventstatus);
        add(eventresult);
        add(eventtype);
        add(assettype);
        add(assetstatus);

        finalViewerUrl = viewer;
        finalEditorUrl = editor;

        //Viewer
        WebMarkupContainer container = new WebMarkupContainer("myFrame");
        container.add(new AttributeAppender("src", Model.of(finalViewerUrl)));
        add(container);
        container.setOutputMarkupId(true);

        //Editor
        WebMarkupContainer container2 = new WebMarkupContainer("myFrame2");
        container2.add(new AttributeAppender("src", Model.of(finalEditorUrl)));
        add(container2);
        container2.setOutputMarkupId(true);

        //Viewer without Filter Panel
        WebMarkupContainer container3 = new WebMarkupContainer("myFrame3");
        container3.add(new AttributeAppender("src", Model.of(finalViewerUrl + "&disableFilterPanel=true")));
        add(container3);
        container3.setOutputMarkupId(true);


        performedBy.add(buildBehavior(container, container2, container3));
        location.add(buildBehavior(container, container2, container3));
        eventstatus.add(buildBehavior(container, container2, container3));
        eventresult.add(buildBehavior(container, container2, container3));
        eventtype.add(buildBehavior(container, container2, container3));
        assettype.add(buildBehavior(container, container2, container3));
        assetstatus.add(buildBehavior(container, container2, container3));
    }

    public String buildQueryParams() {

        String params = "";

        if(performedBy.getConvertedInput() != null) {
            try {
                urlPerformedBy = "Performedby=" + URLEncoder.encode(performedBy.getConvertedInput(), "UTF-8") + "&";
            } catch (Exception e) {
                System.err.println(e);
            }
        }

        if(location.getConvertedInput() != null) {
            try {
                urlLocation = "Location=" + URLEncoder.encode(location.getConvertedInput(), "UTF-8") + "&";
            } catch (Exception e) {
                System.err.println(e);
            }
        }

        if(eventstatus.getConvertedInput() != null) {
            try {
                urlEventStatus = "Eventstatus=" + URLEncoder.encode(eventstatus.getConvertedInput(), "UTF-8") + "&";
            } catch (Exception e) {
                System.err.println(e);
            }
        }

        if(eventresult.getConvertedInput() != null) {
            try {
                urlEventResult = "Eventresult=" + URLEncoder.encode(eventresult.getConvertedInput(), "UTF-8") + "&";
            } catch (Exception e) {
                System.err.println(e);
            }
        }

        if(eventtype.getConvertedInput() != null) {
            try {
                urlEventType = "Eventtype=" + URLEncoder.encode(eventtype.getConvertedInput(), "UTF-8") + "&";
            } catch (Exception e) {
                System.err.println(e);
            }
        }

        if(assettype.getConvertedInput() != null) {
            try {
                urlAssetType = "Assettype=" + URLEncoder.encode(assettype.getConvertedInput(), "UTF-8") + "&";
            } catch (Exception e) {
                System.err.println(e);
            }
        }

        if(assetstatus.getConvertedInput() != null) {
            try {
                urlAssetStatus = "Assetstatus=" + URLEncoder.encode(assetstatus.getConvertedInput(), "UTF-8") + "&";
            } catch (Exception e) {
                System.err.println(e);
            }
        }

        return urlPerformedBy + urlLocation + urlEventStatus + urlEventResult + urlEventType + urlAssetType + urlAssetStatus;
    }

    public OnChangeAjaxBehavior buildBehavior(WebMarkupContainer container, WebMarkupContainer container2, WebMarkupContainer container3){
        OnChangeAjaxBehavior onChangeAjaxBehavior = new OnChangeAjaxBehavior()
        {
            @Override
            protected void onUpdate(AjaxRequestTarget target)
            {
                String params = buildQueryParams();
                finalViewerUrl = viewer + params;
                finalEditorUrl = editor + params;

                container.add(new AttributeModifier("src", Model.of(finalViewerUrl)));
                container2.add(new AttributeModifier("src", Model.of(finalEditorUrl)));
                container3.add(new AttributeModifier("src", Model.of(finalEditorUrl + "&disableFilterPanel=true")));

                target.add(container);
                target.add(container2);
                target.add(container3);
            }
        };

        return onChangeAjaxBehavior;
    }

    @Override
    protected void addBreadCrumbBar(String breadCrumbBarId) {
        add(new BreadCrumbBar(breadCrumbBarId,
                new NavigationItem(new FIDLabelModel("label.dashboard"), DashboardPage.class),
                new NavigationItem(Model.of("Template Page"), TemplatePage.class)
        ));
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, "Sample Charts");
    }
}
