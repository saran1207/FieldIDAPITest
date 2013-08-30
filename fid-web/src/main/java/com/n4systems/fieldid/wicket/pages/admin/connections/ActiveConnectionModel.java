package com.n4systems.fieldid.wicket.pages.admin.connections;

import com.fieldid.jdbc.ActiveConnection;
import com.fieldid.jdbc.ConnectionInterceptor;
import org.apache.wicket.model.LoadableDetachableModel;

public class ActiveConnectionModel extends LoadableDetachableModel<ActiveConnection> {

	private Long id;

	public ActiveConnectionModel(Long id) {
		this.id = id;
	}

	public ActiveConnectionModel(ActiveConnection connection) {
		super(connection);
		this.id = connection.getId();
	}

	@Override
	protected ActiveConnection load() {
		return ConnectionInterceptor.getConnectionsInUse().get(id);
	}

}
