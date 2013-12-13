package com.n4systems.fieldid.wicket.components.admin.tenant;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.table.SimpleDefaultDataTable;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.user.User;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import java.util.List;

public class TenantUserListPanel extends Panel {
	public static final int USERS_PER_PAGE = 10;

//	@SpringBean
//	private UserService userService;

	private FieldIDDataProvider<User> dataProvider = null;
	private SimpleDefaultDataTable table;

	public TenantUserListPanel(String id, FieldIDDataProvider<User> dataProvider) {
		super(id);
		this.dataProvider = dataProvider;
		setOutputMarkupPlaceholderTag(true);
		add(table = new SimpleDefaultDataTable<User>("usersTable", getUserTableColumns(), dataProvider, USERS_PER_PAGE));
		updateVisibility();
	}

	public void updateVisibility() {
		table.setVisible(dataProvider.size() > 0);
	}

	private List<IColumn<User>> getUserTableColumns () {
		List<IColumn<User>> columns = Lists.newArrayList();


		columns.add(new PropertyColumn<User>(new FIDLabelModel("label.userid"), "userID", "userID"));
		columns.add(new PropertyColumn<User>(new FIDLabelModel("label.name"), "lastName, firstName", "fullName"));
		columns.add(new PropertyColumn<User>(new FIDLabelModel("label.emailaddress"), "emailAddress", "emailAddress"));
		columns.add(new PropertyColumn<User>(new FIDLabelModel("label.type"), "userType", "userType.label"));
		columns.add(new PropertyColumn<User>(new FIDLabelModel("label.owner"), "owner", "owner.hierarchicalDisplayName"));
		columns.add(new PropertyColumn<User>(new FIDLabelModel("label.locked_out"), "locked", "locked"));
		columns.add(new AbstractColumn<User>(new FIDLabelModel("")) {
			@Override
			public void populateItem(Item<ICellPopulator<User>> cellItem, String componentId, IModel<User> rowModel) {
				cellItem.add(new LoginAsCell(componentId, rowModel));
			}
		});


//		columns.add(new LastLoginColumn(new FIDLabelModel("label.lastlogin")));

//		columns.add(new ActionsColumn(this));

		return columns;
	}

}
