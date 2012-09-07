package com.n4systems.fieldid.selenium.persistence;

import com.n4systems.fieldid.selenium.persistence.saver.SeleniumAssetSaver;
import com.n4systems.fieldid.selenium.persistence.saver.SeleniumAssetTypeSaver;
import com.n4systems.model.*;
import com.n4systems.model.api.Saveable;
import com.n4systems.model.assetstatus.AssetStatusSaver;
import com.n4systems.model.assettype.AssetTypeGroupSaver;
import com.n4systems.model.catalog.Catalog;
import com.n4systems.model.catalog.CatalogSaver;
import com.n4systems.model.columns.ActiveColumnMapping;
import com.n4systems.model.columns.ColumnLayout;
import com.n4systems.model.columns.saver.ActiveColumnMappingSaver;
import com.n4systems.model.columns.saver.ColumnLayoutSaver;
import com.n4systems.model.criteria.CriteriaSaver;
import com.n4systems.model.criteria.CriteriaSectionSaver;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.downloadlink.DownloadLinkSaver;
import com.n4systems.model.event.EventFormSaver;
import com.n4systems.model.event.SimpleEventSaver;
import com.n4systems.model.eventbook.EventBookSaver;
import com.n4systems.model.eventschedule.EventScheduleSaver;
import com.n4systems.model.eventstatus.EventStatusSaver;
import com.n4systems.model.eventtype.AssociatedEventTypeSaver;
import com.n4systems.model.eventtype.EventTypeSaver;
import com.n4systems.model.eventtypegroup.EventTypeGroupSaver;
import com.n4systems.model.orgs.*;
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

	@SuppressWarnings("rawtypes")
	private static HashMap<Class<? extends Saveable>, Saver> classToSaverMap = new HashMap<Class<? extends Saveable>, Saver>();

    static {
        classToSaverMap.put(Asset.class, new SeleniumAssetSaver());
        classToSaverMap.put(AssetStatus.class, new AssetStatusSaver());
        classToSaverMap.put(EventStatus.class, new EventStatusSaver());
        classToSaverMap.put(AssetType.class, new SeleniumAssetTypeSaver());
        classToSaverMap.put(Event.class, new SimpleEventSaver());
        classToSaverMap.put(SubEvent.class, new SimpleEventSaver());
        classToSaverMap.put(User.class, new UserSaver());
        classToSaverMap.put(EventType.class, new EventTypeSaver());
        classToSaverMap.put(EventBook.class, new EventBookSaver());
        classToSaverMap.put(EventTypeGroup.class, new EventTypeGroupSaver());
        classToSaverMap.put(EventForm.class, new EventFormSaver());
        classToSaverMap.put(OrgConnection.class, new OrgConnectionSaver(ConfigContext.getCurrentContext().getLong(ConfigEntry.HOUSE_ACCOUNT_PRIMARY_ORG_ID)));

        classToSaverMap.put(CustomerOrg.class, new OrgSaver());
        classToSaverMap.put(PrimaryOrg.class, new OrgSaver());
        classToSaverMap.put(SecondaryOrg.class, new OrgSaver());
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
        classToSaverMap.put(ComboBoxCriteria.class, new CriteriaSaver());
        classToSaverMap.put(DateFieldCriteria.class, new CriteriaSaver());
        classToSaverMap.put(NumberFieldCriteria.class, new CriteriaSaver());
        classToSaverMap.put(EventSchedule.class, new EventScheduleSaver());
        classToSaverMap.put(StateSet.class, new StateSetSaver());
        classToSaverMap.put(State.class, new StateSaver());
        classToSaverMap.put(AssetTypeGroup.class, new AssetTypeGroupSaver());

        classToSaverMap.put(Project.class, new ProjectSaver());
        classToSaverMap.put(ColumnLayout.class, new ColumnLayoutSaver());
        classToSaverMap.put(ActiveColumnMapping.class, new ActiveColumnMappingSaver());
        classToSaverMap.put(DownloadLink.class, new DownloadLinkSaver());
    }

	@SuppressWarnings("rawtypes")
	public static Saver makeSaverFor(Class<? extends Saveable> clazz) {
        Saver saver = classToSaverMap.get(clazz);
        if (saver == null) {
            throw new RuntimeException("No saver registered for class: " + clazz + "! Do so in " + SaverMap.class.getName());
        }
        return saver;
    }

}
