package com.n4systems.fieldid.wicket.pages.asset;

import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.behavior.SimpleSortableAjaxBehavior;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.SubAsset;
import com.n4systems.model.eventschedule.NextEventScheduleLoader;
import com.n4systems.model.user.User;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class AssetViewPage extends AssetPage {

    private DateFormat df = new SimpleDateFormat(FieldIDSession.get().getSessionUser().getDateFormat());
    private DateFormat dtf = new SimpleDateFormat(FieldIDSession.get().getSessionUser().getDateTimeFormat());

    public AssetViewPage(PageParameters params) {
        super(params);
           
        Asset asset = assetModel.getObject();
        
        if(asset.getImageName() == null) {
            add(new ExternalImage("assetImage", ContextAbsolutizer.toContextAbsoluteUrl("/file/downloadAssetTypeImage.action?uniqueID=" + asset.getType().getId())));
        } else {
            add(new ExternalImage("assetImage", ContextAbsolutizer.toContextAbsoluteUrl("/file/downloadAssetImage.action?uniqueID=" + assetId)));
        }

        add(new Label("nextEventMsg", getNextEventMessage()));

        add(new Label("lastEventMsg", getLastEventMessage()));

        WebMarkupContainer linkedAssetList = new WebMarkupContainer("linkedAssets");
        linkedAssetList.setOutputMarkupId(true);
        final IModel<List<SubAsset>> linkedAssetsModel = createLinkedAssetsModel();
        
        linkedAssetList.add(new ListView<SubAsset>("linkedAssetList", linkedAssetsModel) {
            @Override
            protected void populateItem(final ListItem<SubAsset> item) {
                item.setOutputMarkupId(true);
                item.add(new Label("assetType", new PropertyModel(item.getModelObject(), "asset.type.name")));
                Link linkedAssetLink;
                item.add(linkedAssetLink = new BookmarkablePageLink("linkedAssetLink", AssetViewPage.class, new PageParameters().add("uniqueID", item.getModelObject().getAsset().getId())));
                linkedAssetLink.add(new Label("assetIdentifier", new PropertyModel<Object>(item.getModelObject(), "asset.identifier")));
                item.add(new Link<Void>("removeLinkedAssetLink") {
                    @Override
                    public void onClick() {
                        linkedAssetsModel.getObject().remove(item.getModelObject());

                        Asset asset = assetModel.getObject();
                        asset.setSubAssets(linkedAssetsModel.getObject());
                        try {
                            asset = assetService.update(asset, getUser());
                            assetModel.setObject(asset);
                        } catch (SubAssetUniquenessException e) {
                            error("Unable to remove Linked Asset:" + item.getModelObject().getAsset().getIdentifier());
                        }
                    }
                });
            }
        });

        add(linkedAssetList);

        Behavior sortableBehavior = new SimpleSortableAjaxBehavior<WebMarkupContainer>() {
            @Override
            public void onUpdate(WebMarkupContainer component, int index, AjaxRequestTarget target) {
                SubAsset item = (SubAsset) component.getDefaultModelObject();
                int oldIndex = linkedAssetsModel.getObject().indexOf(item);

                linkedAssetsModel.getObject().remove(oldIndex);
                linkedAssetsModel.getObject().add(index, item);

                Asset asset = assetModel.getObject();
                asset.setSubAssets(linkedAssetsModel.getObject());
                try {
                    asset = assetService.update(asset, getUser());
                    assetModel.setObject(asset);
                } catch (SubAssetUniquenessException e) {
                    error("Unable to reorder Linked Assets");
                }

            }
        };

        linkedAssetList.add(sortableBehavior);
    }

    private LoadableDetachableModel<List<SubAsset>> createLinkedAssetsModel() {
        return new LoadableDetachableModel<List<SubAsset>>() {
            @Override
            protected List<SubAsset> load() {
                return assetModel.getObject().getSubAssets();
            }
        };
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.asset").getObject() + " / " + assetModel.getObject().getIdentifier());
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        Asset asset = assetModel.getObject();
        if(asset.getGpsLocation() != null) {
            response.renderJavaScriptReference("https://maps.googleapis.com/maps/api/js?sensor=false");
            response.renderJavaScriptReference("javascript/googleMaps.js");

            response.renderOnDomReadyJavaScript("googleMapFactory.createAndShowWithLocation('mapCanvas'," + asset.getGpsLocation().getLatitude() + ", " + asset.getGpsLocation().getLongitude() + ")");
        }
        super.renderHead(response);
    }

    private String getNextEventMessage() {
        String nextEventMessage = null;
        EventSchedule nextEvent = new NextEventScheduleLoader().setAssetId(assetId).load();

        if(nextEvent != null) {
            String eventType = nextEvent.getEventType().getName();

            String nextDate = df.format(nextEvent.getNextDate());

            String label = "";
            if(nextEvent.isPastDue()) {
                label = "label.nexteventdate_pastdue";
            }else if(nextEvent.getDaysToDue() == 0) {
                label="label.nexteventdate_due_today";
            }else {
                label="label.nexteventdate_msg";
            }

            nextEventMessage =  new FIDLabelModel(label, eventType, nextDate).getObject();
        }

        return nextEventMessage;
    }

    private String getLastEventMessage() {
        String lastEventMessage = null;
        Event lastEvent = assetService.findLastEvents(assetModel.getObject(), FieldIDSession.get().getSessionUser().getSecurityFilter());
    
        if(lastEvent != null) {
            String eventType = lastEvent.getType().getName();
            String lastEventDate = dtf.format(lastEvent.getDate());
            lastEventMessage = new FIDLabelModel("label.lasteventdate_msg", eventType, lastEventDate).getObject();
        }

        return lastEventMessage;
    }

    private User getUser() {
        return userService.getUser(FieldIDSession.get().getSessionUser().getId());
    }


}
