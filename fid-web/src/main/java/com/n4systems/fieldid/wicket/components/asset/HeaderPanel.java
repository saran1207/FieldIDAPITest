package com.n4systems.fieldid.wicket.components.asset;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.event.EventScheduleService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.NonWicketIframeLink;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.components.schedule.SchedulePicker;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForAssetTypeModel;
import com.n4systems.fieldid.wicket.model.jobs.EventJobsForTenantModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.asset.AssetEventsPage;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.Event;
import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class HeaderPanel extends Panel {

    @SpringBean
    protected AssetService assetService;

    @SpringBean
    private EventScheduleService eventScheduleService;
    
    private Boolean useContext;

    private Event scheduleToAdd;

    public HeaderPanel(String id, IModel<Asset> assetModel, Boolean isView, Boolean useContext) {
        super(id, assetModel);
        this.useContext = useContext;

        final Asset asset = assetModel.getObject();

        add(new Label("assetType", asset.getType().getName()));
        add(new Label("assetIdentifier", asset.getIdentifier()));
        Label assetStatus;
        if(asset.getAssetStatus() != null) {
            add(assetStatus = new Label("assetStatus", asset.getAssetStatus().getDisplayName()));
        } else {
            add(assetStatus = new Label("assetStatus"));
            assetStatus.setVisible(false);
        }

        BaseOrg owner = asset.getOwner();

        add(new Label("ownerInfo", getOwnerLabel(owner, asset.getAdvancedLocation())));

        BookmarkablePageLink summaryLink;
        BookmarkablePageLink eventHistoryLink;
        
        add(summaryLink = new BookmarkablePageLink("summaryLink", AssetSummaryPage.class, PageParametersBuilder.uniqueId(asset.getId())));

        NonWicketIframeLink traceabilityLink;
        add(traceabilityLink = new NonWicketIframeLink("traceabilityLink", "aHtml/iframe/assetTraceability.action?uniqueID=" + asset.getId() + "&useContext=false", false, 1000, 600, new AttributeModifier("class", "mattButtonMiddle")));
        traceabilityLink.setVisible(assetService.hasLinkedAssets(asset) || isInVendorContext());

        
        add(eventHistoryLink = new BookmarkablePageLink("eventHistoryLink", AssetEventsPage.class, PageParametersBuilder.uniqueId(asset.getId())));

        if(isView) {
            summaryLink.add(new AttributeAppender("class", " mattButtonPressed"));
        } else {
           eventHistoryLink.add(new AttributeAppender("class", " mattButtonPressed"));
        }

        if (FieldIDSession.get().getSessionUser().hasAccess("editevent") && !FieldIDSession.get().getSessionUser().isReadOnlyUser())
            add(new NonWicketLink("editAssetLink", "assetEdit.action?uniqueID=" + asset.getId(), new AttributeModifier("class", "mattButton")));
        else
            add(new NonWicketLink("editAssetLink", "customerInformationEdit.action?uniqueID=" + asset.getId(), new AttributeModifier("class", "mattButton")));

        NonWicketLink startEventLink;
        add(startEventLink = new NonWicketLink("startEventLink", "quickEvent.action?assetId=" + asset.getId(), new AttributeModifier("class", "mattButton blueButton")));
        startEventLink.setVisible(FieldIDSession.get().getSessionUser().hasAccess("createevent"));

        scheduleToAdd = createNewSchedule(asset);

        SchedulePicker schedulePicker;
        add(schedulePicker = new SchedulePicker("schedulePicker", new FIDLabelModel("label.schedule_event"), new PropertyModel<Event>(HeaderPanel.this, "scheduleToAdd"), new EventTypesForAssetTypeModel(new PropertyModel<AssetType>(asset, "type")), new EventJobsForTenantModel(), -487, 28) {
            @Override
            protected void onPickComplete(AjaxRequestTarget target) {
                scheduleToAdd.setTenant(FieldIDSession.get().getSessionUser().getTenant());
                eventScheduleService.createSchedule(scheduleToAdd);
                refreshContentPanel(target);
                scheduleToAdd = createNewSchedule(asset);
            }
        });

        schedulePicker.setVisible(FieldIDSession.get().getSessionUser().hasAccess("createevent"));
    }

    private Event createNewSchedule(Asset asset) {
        Event schedule = new Event();
        schedule.setAsset(asset);
        return schedule;
    }

    protected void refreshContentPanel(AjaxRequestTarget target) {};

    private String getOwnerLabel(BaseOrg owner, Location advancedLocation) {
        StringBuffer buff = new StringBuffer();
        if(owner.isDivision()) {
            buff.append(owner.getCustomerOrg().getName()).append(" (").append(owner.getPrimaryOrg().getName()).append("), ").append(owner.getDivisionOrg().getName());
        } else if(owner.isCustomer()) {
            buff.append(owner.getCustomerOrg().getName()).append(" (").append(owner.getPrimaryOrg().getName()).append(")");
        } else {
            buff.append(owner.getPrimaryOrg().getName());
        }

        if(advancedLocation != null && !advancedLocation.getFullName().isEmpty()) {
            buff.append(", ").append(advancedLocation.getFullName());
        }
        return buff.toString();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/asset/header.css");
    }

    public boolean isInVendorContext() {
        return (FieldIDSession.get().getVendorContext() != null && useContext );
    }

}
