package com.n4systems.fieldid.wicket.pages.widgets.config;

import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.org.AutoCompleteOrgPicker;
import com.n4systems.fieldid.wicket.components.renderer.EventTypeChoiceRenderer;
import com.n4systems.fieldid.wicket.components.user.GroupedUserPicker;
import com.n4systems.fieldid.wicket.model.eventtype.ActionTypesForTenantModel;
import com.n4systems.fieldid.wicket.model.user.GroupedUsersForTenantModel;
import com.n4systems.model.EventType;
import com.n4systems.model.dashboard.widget.ActionsWidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class ActionsConfigPanel extends OrgDateWidgetConfigPanel<ActionsWidgetConfiguration> {

    public ActionsConfigPanel(String id, final IModel<ActionsWidgetConfiguration> configModel) {
        super(id, configModel);
        addConfigElement(new GroupedUserPicker("user", new PropertyModel<User>(configModel, "user"), new GroupedUsersForTenantModel(), true).setNullValid(true));
        addConfigElement(new FidDropDownChoice<EventType>("eventType", new PropertyModel<EventType>(configModel,"actionType"), new ActionTypesForTenantModel(), new EventTypeChoiceRenderer()).setNullValid(true));
    }

    @Override
    protected Component createOrgPicker(String id, IModel<ActionsWidgetConfiguration> configModel) {
        return new AutoCompleteOrgPicker(id, new PropertyModel<BaseOrg>(configModel, "org"));
    }
}

