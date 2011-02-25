package com.n4systems.fieldid.selenium.persistence;

import com.n4systems.fieldid.selenium.persistence.saver.SeleniumAssetSaver;
import com.n4systems.fieldid.selenium.persistence.saver.SeleniumAssetTypeSaver;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.Event;
import com.n4systems.model.EventBook;
import com.n4systems.model.EventForm;
import com.n4systems.model.EventGroup;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.LineItem;
import com.n4systems.model.LineItemSaver;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.Order;
import com.n4systems.model.OrderSaver;
import com.n4systems.model.Project;
import com.n4systems.model.SelectCriteria;
import com.n4systems.model.State;
import com.n4systems.model.StateSet;
import com.n4systems.model.TextFieldCriteria;
import com.n4systems.model.UnitOfMeasureCriteria;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.assetstatus.AssetStatusSaver;
import com.n4systems.model.catalog.Catalog;
import com.n4systems.model.catalog.CatalogSaver;
import com.n4systems.model.criteria.CriteriaSaver;
import com.n4systems.model.criteria.CriteriaSectionSaver;
import com.n4systems.model.event.EventFormSaver;
import com.n4systems.model.event.EventGroupSaver;
import com.n4systems.model.event.SimpleEventSaver;
import com.n4systems.model.eventbook.EventBookSaver;
import com.n4systems.model.eventschedule.EventScheduleSaver;
import com.n4systems.model.eventtype.AssociatedEventTypeSaver;
import com.n4systems.model.eventtype.EventTypeSaver;
import com.n4systems.model.eventtypegroup.EventTypeGroupSaver;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.project.ProjectSaver;
import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.model.safetynetwork.OrgConnectionSaver;
import com.n4systems.model.stateset.StateSaver;
import com.n4systems.model.stateset.StateSetSaver;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserSaver;
import com.n4systems.persistence.savers.Saver;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

import java.util.HashMap;

public class SaverMap {

    @SuppressWarnings("unchecked")
	private static HashMap<Class<? extends Saveable>, Saver> classToSaverMap = new HashMap<Class<? extends Saveable>, Saver>();

    static {
        classToSaverMap.put(Asset.class, new SeleniumAssetSaver());
        classToSaverMap.put(AssetStatus.class, new AssetStatusSaver());
        classToSaverMap.put(AssetType.class, new SeleniumAssetTypeSaver());
        classToSaverMap.put(Event.class, new SimpleEventSaver());
        classToSaverMap.put(User.class, new UserSaver());
        classToSaverMap.put(EventType.class, new EventTypeSaver());
        classToSaverMap.put(EventGroup.class, new EventGroupSaver());
        classToSaverMap.put(EventBook.class, new EventBookSaver());
        classToSaverMap.put(EventTypeGroup.class, new EventTypeGroupSaver());
        classToSaverMap.put(EventForm.class, new EventFormSaver());
        classToSaverMap.put(OrgConnection.class, new OrgConnectionSaver(ConfigContext.getCurrentContext().getLong(ConfigEntry.HOUSE_ACCOUNT_PRIMARY_ORG_ID)));

        classToSaverMap.put(CustomerOrg.class, new OrgSaver());
        classToSaverMap.put(PrimaryOrg.class, new OrgSaver());
        classToSaverMap.put(DivisionOrg.class, new OrgSaver());
        classToSaverMap.put(Order.class, new OrderSaver());
        classToSaverMap.put(LineItem.class, new LineItemSaver());
        classToSaverMap.put(Catalog.class, new CatalogSaver());
        classToSaverMap.put(CriteriaSection.class, new CriteriaSectionSaver());
        classToSaverMap.put(OneClickCriteria.class, new CriteriaSaver());
        classToSaverMap.put(AssociatedEventType.class, new AssociatedEventTypeSaver());
        classToSaverMap.put(TextFieldCriteria.class, new CriteriaSaver());
        classToSaverMap.put(UnitOfMeasureCriteria.class, new CriteriaSaver());
        classToSaverMap.put(SelectCriteria.class, new CriteriaSaver());
        classToSaverMap.put(EventSchedule.class, new EventScheduleSaver());
        classToSaverMap.put(StateSet.class, new StateSetSaver());
        classToSaverMap.put(State.class, new StateSaver());

        classToSaverMap.put(Project.class, new ProjectSaver());
    }

    @SuppressWarnings("unchecked")
	public static Saver makeSaverFor(Class<? extends Saveable> clazz) {
        Saver saver = classToSaverMap.get(clazz);
        if (saver == null) {
            throw new RuntimeException("No saver registered for class: " + clazz + "! Do so in " + SaverMap.class.getName());
        }
        return saver;
    }

}
