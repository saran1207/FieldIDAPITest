package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import com.n4systems.fieldid.wicket.components.AutoCompleteOrgPicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.user.GroupedUserPicker;
import com.n4systems.fieldid.wicket.model.user.GroupedUsersForTenantModel;
import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;

public class OwnershipCriteriaPanel extends Panel {

	public OwnershipCriteriaPanel(String id, IModel<?> model) {
        super(id, model);
        add(new AutoCompleteOrgPicker("owner",  new PropertyModel<BaseOrg>(getDefaultModel(), "owner")).inScrollableContainers("#left-panel .form"));
        add(new ModalLocationPicker("location", new PropertyModel<Location>(getDefaultModel(), "location")));
        
        WebMarkupContainer assignedUserContainer = new WebMarkupContainer("assignedToContainer");
        GroupedUserPicker groupedUserPicker = new GroupedUserPicker("assignedTo", new PropertyModel<User>(getDefaultModel(), "assignedTo"), new GroupedUsersForTenantModel());
        groupedUserPicker.setNullValid(true);        
        assignedUserContainer.add(groupedUserPicker);
        assignedUserContainer.setVisible(FieldIDSession.get().getSecurityGuard().isAssignedToEnabled());
        
        add(assignedUserContainer);
    }	

}
