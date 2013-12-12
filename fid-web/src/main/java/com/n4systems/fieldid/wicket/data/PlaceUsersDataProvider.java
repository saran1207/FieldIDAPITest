package com.n4systems.fieldid.wicket.data;

import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.user.User;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Iterator;
import java.util.List;

public class PlaceUsersDataProvider extends FieldIDDataProvider<User> {

	@SpringBean
	private UserService userService;
	private IModel<BaseOrg> orgModel;

	public PlaceUsersDataProvider(IModel<BaseOrg> orgModel, String order, SortOrder sortOrder) {
		this.orgModel = orgModel;
		setSort(order, sortOrder);
	}

	@Override
	public Iterator<? extends User> iterator(int first, int count) {
		List<User> users = userService.getOrgUsers(orgModel.getObject(), getSort().getProperty(), getSort().isAscending(), first, count);
		return users.iterator();
	}

	@Override
	public int size() {
		return userService.countOrgUsers(orgModel.getObject()).intValue();
	}

	@Override
	public IModel<User> model(final User object) {
		return new AbstractReadOnlyModel<User>() {
			@Override
			public User getObject() {
				return object;
			}
		};
	}

}
