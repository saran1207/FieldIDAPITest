package com.n4systems.fieldid.wicket.pages.widgets.config;

import com.n4systems.fieldid.wicket.components.assettype.GroupedAssetTypePicker;
import com.n4systems.fieldid.wicket.components.org.OrgLocationPicker;
import com.n4systems.fieldid.wicket.components.renderer.EventTypeChoiceRenderer;
import com.n4systems.fieldid.wicket.components.user.GroupedUserPicker;
import com.n4systems.fieldid.wicket.model.assettype.GroupedAssetTypesForTenantModel;
import com.n4systems.fieldid.wicket.model.eventtype.EventTypesForTenantModel;
import com.n4systems.fieldid.wicket.model.user.GroupedVisibleUsersModel;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.ThingEventType;
import com.n4systems.model.dashboard.widget.WorkWidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

public class WorkConfigPanel extends WidgetConfigPanel<WorkWidgetConfiguration> {

    public WorkConfigPanel(String id, final IModel<WorkWidgetConfiguration> configModel) {
        super(id, configModel);

        addConfigElement(new OrgLocationPicker("org", new PropertyModel<BaseOrg>(configModel, "org")));
        addConfigElement(new GroupedUserPicker("user", new PropertyModel<User>(configModel, "user"), new GroupedVisibleUsersModel(), false).setNullValid(true));
        addConfigElement(new GroupedAssetTypePicker("assetType", new PropertyModel<AssetType>(configModel, "assetType"), new GroupedAssetTypesForTenantModel(new Model<AssetTypeGroup>()), false).setNullValid(true));
        addConfigElement(new DropDownChoice<ThingEventType>("eventType", new PropertyModel<ThingEventType>(configModel,"eventType"), new EventTypesForTenantModel(), new EventTypeChoiceRenderer()).setNullValid(true));
    }

}

