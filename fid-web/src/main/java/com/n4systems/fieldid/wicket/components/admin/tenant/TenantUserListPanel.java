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
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class TenantUserListPanel extends Panel {
	public static final int USERS_PER_PAGE = 10;

	private FieldIDDataProvider<User> dataProvider = null;
	private SimpleDefaultDataTable table;
	private DateFormat df = DateFormat.getDateTimeInstance();

	public TenantUserListPanel(String id, FieldIDDataProvider<User> dataProvider) {
		super(id);
		this.dataProvider = dataProvider;
		this.df.setTimeZone(TimeZone.getTimeZone("America/Toronto")); // TODO: Storing the timezone along with the admin user

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
		columns.add(new AbstractColumn<User>(new FIDLabelModel("label.lastlogin"), "lastLogin") {
			@Override
			public void populateItem(Item<ICellPopulator<User>> item, String componentId, IModel<User> rowModel) {
				Date lastLogin = rowModel.getObject().getLastLogin();
				String date = (lastLogin != null) ? df.format(rowModel.getObject().getLastLogin()) : "Never";
				item.add(new Label(componentId, date));
			}
		});
		columns.add(new PropertyColumn<User>(new FIDLabelModel("label.locked_out"), "locked", "locked"));
		columns.add(new AbstractColumn<User>(new FIDLabelModel("label.is_logged_in")) {
			@Override
			public void populateItem(Item<ICellPopulator<User>> cellItem, String componentId, IModel<User> rowModel) {
				cellItem.add(new IsLoggedInCell(componentId, rowModel));
			}
		});
		columns.add(new AbstractColumn<User>(new FIDLabelModel("")) {
			@Override
			public void populateItem(Item<ICellPopulator<User>> cellItem, String componentId, IModel<User> rowModel) {
				cellItem.add(new LoginAsCell(componentId, rowModel));
			}
		});

		return columns;
	}

}
