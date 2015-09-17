package com.n4systems.fieldid.wicket.pages.admin.connections;

import org.apache.wicket.model.IModel;
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;

public class FormattedStatementModel implements IModel<String> {

	private IModel<String> sql;

	public FormattedStatementModel(IModel<String> sql) {
		this.sql = sql;
	}

	@Override
	public String getObject() {
		return (sql.getObject() != null ? new BasicFormatterImpl().format(sql.getObject()) : "");
	}

	@Override
	public void setObject(String object) {
		sql.setObject(object);
	}

	@Override
	public void detach() {
		sql.detach();
	}
}
