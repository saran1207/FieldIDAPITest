package com.n4systems.fieldid.config;

import com.n4systems.fieldid.service.remover.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FieldIdEntityRemovalConfig {

    @Bean
    public AllEventsOfTypeRemovalService allEventsOfTypeRemovalService() {
        return new AllEventsOfTypeRemovalService();
    }

    @Bean
    public AssociatedEventTypeRemovalService associatedEventTypeRemovalService() {
        return new AssociatedEventTypeRemovalService();
    }

    @Bean
    public CatalogItemRemovalService catalogItemRemovalService() {
        return new CatalogItemRemovalService();
    }

    @Bean
    public EventFrequenciesRemovalService eventFrequenciesRemovalService() {
        return new EventFrequenciesRemovalService();
    }

    @Bean
    public EventTypeRemovalService eventTypeRemovalService() {
        return new EventTypeRemovalService();
    }

    @Bean
    public NotificationSettingRemovalService notificationSettingRemovalService() {
        return new NotificationSettingRemovalService();
    }

    @Bean
    public ScheduleListRemovalService scheduleListRemovalService() {
        return new ScheduleListRemovalService();
    }

    @Bean
    public PlaceEventsOfTypeRemovalService placeEventsOfTypeRemovalService() {
        return new PlaceEventsOfTypeRemovalService();
    }

    @Bean
    public AssociatedPlaceEventTypeRemovalService associatedPlaceEventTypeRemovalService() {
        return new AssociatedPlaceEventTypeRemovalService();
    }

    @Bean
    public ProcedureAuditEventTypeRemovalService procedureAuditEventTypeRemovalService() {
        return new ProcedureAuditEventTypeRemovalService();
    }
}
