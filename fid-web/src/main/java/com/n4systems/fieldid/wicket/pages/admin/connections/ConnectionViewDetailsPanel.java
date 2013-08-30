package com.n4systems.fieldid.wicket.pages.admin.connections;

import com.fieldid.jdbc.ActiveConnection;
import com.fieldid.jdbc.ExecutedStatement;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class ConnectionViewDetailsPanel extends Panel {

	public ConnectionViewDetailsPanel(String id, IModel<ActiveConnection> model) {
		super(id, model);
		setOutputMarkupPlaceholderTag(true);

		add(new FIDFeedbackPanel("feedbackPanel"));

		add(new ListView<ExecutedStatement>("statementList", new PropertyModel<List<ExecutedStatement>>(model, "statements")) {
			@Override
			protected void populateItem(ListItem<ExecutedStatement> item) {
				item.add(new Label("timestamp", new PropertyModel<Long>(item.getModel(), "timestamp")));
				item.add(new Label("stackTrace", new PropertyModel<String>(item.getModel(), "stackTrace")));
				item.add(new Label("sql", new FormattedStatementModel(new PropertyModel<String>(item.getModel(), "sql"))));
			}
		});
	}

}
