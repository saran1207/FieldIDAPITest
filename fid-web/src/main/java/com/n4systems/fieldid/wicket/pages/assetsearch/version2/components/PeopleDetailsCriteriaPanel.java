package com.n4systems.fieldid.wicket.pages.assetsearch.version2.components;

import com.n4systems.fieldid.wicket.components.FidDropDownChoice;
import com.n4systems.fieldid.wicket.components.renderer.BlankOptionChoiceRenderer;
import com.n4systems.fieldid.wicket.components.renderer.ListableChoiceRenderer;
import com.n4systems.fieldid.wicket.components.select.GroupedListableDropDownChoice;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.ListWithBlankOptionModel;
import com.n4systems.fieldid.wicket.model.ListingPairToIdModel;
import com.n4systems.fieldid.wicket.model.user.GroupedListableUsersModel;
import com.n4systems.fieldid.wicket.model.user.GroupedUsersForTenantModel;
import com.n4systems.fieldid.wicket.model.user.UsersForTenantModel;
import com.n4systems.model.user.User;
import com.n4systems.util.GroupedListingPair;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class PeopleDetailsCriteriaPanel extends Panel {

    public PeopleDetailsCriteriaPanel(String id, IModel<?> model) {
        super(id, model);

        UsersForTenantModel usersForTenantModel = new UsersForTenantModel();

        IChoiceRenderer<GroupedListingPair> unassignedOrAssigneeRenderer = new BlankOptionChoiceRenderer<GroupedListingPair>(new FIDLabelModel("label.unassigned"), new ListableChoiceRenderer<GroupedListingPair>());
        PropertyModel<Long> assigneeId = new PropertyModel<Long>(getDefaultModel(), "assigneeId");
        ListingPairToIdModel listingPairToIdModel = new ListingPairToIdModel(assigneeId);
        GroupedListableUsersModel groupedUsers = new GroupedListableUsersModel(new GroupedUsersForTenantModel());
        ListWithBlankOptionModel blankOptionUserList = new ListWithBlankOptionModel(groupedUsers);
        add(new GroupedListableDropDownChoice("assigneeId", listingPairToIdModel, blankOptionUserList, unassignedOrAssigneeRenderer).setNullValid(true).add(new AttributeAppender("data-placeholder", " ")));

        add(new FidDropDownChoice<User>("performedBy", usersForTenantModel, new ListableChoiceRenderer<User>()).setNullValid(true));

    }

}
