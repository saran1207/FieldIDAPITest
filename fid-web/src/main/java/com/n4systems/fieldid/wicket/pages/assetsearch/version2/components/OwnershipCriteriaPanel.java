package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.org.AutoCompleteOrgPicker;
import com.n4systems.fieldid.wicket.components.user.GroupedUserPicker;
import com.n4systems.fieldid.wicket.model.user.GroupedUsersForTenantModel;
import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class OwnershipCriteriaPanel extends Panel {

    private ModalLocationPicker locationPicker;
    private PropertyModel<Location> locationModel;

    public OwnershipCriteriaPanel(String id, IModel<?> model) {
        super(id, model);
        add(new AutoCompleteOrgPicker("owner",  new PropertyModel<BaseOrg>(getDefaultModel(), "owner")) {
            @Override protected void onUpdate(AjaxRequestTarget target, String hiddenInput, String fieldInput) {
                updateOwner(target, (BaseOrg) getDefaultModelObject());
            }
        }.withAutoUpdate(true).inScrollableContainers("#left-panel .form"));
        locationModel = new PropertyModel<Location>(getDefaultModel(), "location");
        add(locationPicker = new ModalLocationPicker("location", locationModel));

        WebMarkupContainer assignedUserContainer = new WebMarkupContainer("assignedToContainer");
        GroupedUserPicker groupedUserPicker = new GroupedUserPicker("assignedTo", new PropertyModel<User>(getDefaultModel(), "assignedTo"), new GroupedUsersForTenantModel());
        groupedUserPicker.setNullValid(true);
        groupedUserPicker.add(new AttributeAppender("data-placeholder", " "));
        assignedUserContainer.add(groupedUserPicker);
        assignedUserContainer.setVisible(FieldIDSession.get().getSecurityGuard().isAssignedToEnabled());
        
        add(assignedUserContainer);
    }

    private void updateOwner(AjaxRequestTarget target, BaseOrg owner) {
        locationPicker.setOwner(owner);
        // TODO DD : validate location owner and set to null if it isn't under given owner hierarchy.
    }

}
