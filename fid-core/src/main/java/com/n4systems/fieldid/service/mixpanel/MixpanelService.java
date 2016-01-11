package com.n4systems.fieldid.service.mixpanel;

import com.mixpanel.mixpanelapi.ClientDelivery;
import com.mixpanel.mixpanelapi.MessageBuilder;
import com.mixpanel.mixpanelapi.MixpanelAPI;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.user.User;
import com.n4systems.services.config.ConfigService;
import com.n4systems.util.ConfigEntry;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class MixpanelService extends FieldIdPersistenceService {

    private static final Logger log = Logger.getLogger(MixpanelService.class);

    public static final String LOGGED_IN = "Logged In";
    public static final String NEW_ASSET_CREATED = "New Asset Created";
    public static final String VIEWED_ASSET = "Viewed Asset";
    public static final String VIEWED_ASSET_EVENTS_LIST = "Viewed Asset Events List";
    public static final String VIEWED_ASSET_EVENTS_MAP = "Viewed Asset Events Map";
    public static final String VIEWED_COMPLETED_EVENT = "Viewed Completed Event";

    @Autowired
    private ConfigService configService;

    @Transactional
    public void sendEvent(String eventName) {
        sendEvent(eventName, getCurrentUser());
    }

    @Transactional
    public void sendEvent(String eventName, User user) {
        if (!configService.getBoolean(ConfigEntry.MIXPANEL_ENABLED) || user.isSystem()) {
            return;
        }

        try {
            String tenantName = getCurrentTenant().getName();
            String userId = user.getUserID();

            String userIdentifier = tenantName + ":" + userId;

            JSONObject properties = new JSONObject();
            properties.put("username", userId);
            properties.put("tenant", tenantName);
            properties.put("user_type", user.getUserType().getLabel());

            MessageBuilder mb = new MessageBuilder(configService.getString(ConfigEntry.MIXPANEL_TOKEN));
            JSONObject event = mb.event(userIdentifier, eventName, properties);

            ClientDelivery delivery = new ClientDelivery();
            delivery.addMessage(event);

            new MixpanelAPI().deliver(delivery);
        } catch (Exception e) {
            // If we can't reach mixpanel, just log and continue
            log.warn("Unable to deliver message to mixpanel", e);
        }
    }

}
