package com.n4systems.fieldid.wicket.pages.admin.connections;

import com.fieldid.jdbc.ActiveConnection;
import com.fieldid.jdbc.ConnectionInterceptor;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.modal.FIDModalWindow;
import com.n4systems.fieldid.wicket.pages.admin.FieldIDAdminPage;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.time.Duration;

import java.util.Iterator;
import java.util.TreeSet;

public class ConnectionViewPage extends FieldIDAdminPage {

	private boolean autoRefreshEnabled = true;

	public ConnectionViewPage() {
		add(new FIDFeedbackPanel("feedbackPanel"));

		final FIDModalWindow detailsPanel = new FIDModalWindow("detailsPanel", getDefaultModel(), 1024, 768);
		add(detailsPanel);

		IModel<String> autoRefreshButtonModel = new AbstractReadOnlyModel<String>() {
			@Override
			public String getObject() {
				return "Auto Refresh: " + (autoRefreshEnabled ? "ON" : "OFF");
			}
		};

		add(new Button("autoRefresh", autoRefreshButtonModel)
				.add(new AjaxEventBehavior("onclick") {
					@Override
					protected void onEvent(AjaxRequestTarget target) {
						autoRefreshEnabled = !autoRefreshEnabled;
						target.add(getComponent());
					}
				})
		);

		MarkupContainer tableContainer = new WebMarkupContainer("tableContainer");
		tableContainer.setOutputMarkupId(true);
		tableContainer.add(new AbstractAjaxTimerBehavior(Duration.seconds(1)) {
			@Override
			protected void onTimer(AjaxRequestTarget target) {
				if (autoRefreshEnabled)
					target.add(getComponent());
			}
		});

		tableContainer.add(new DataView<ActiveConnection>("connectionViewTable", new ActiveConnectionDataProvider()) {
			@Override
			protected void populateItem(final Item<ActiveConnection> item) {
				AjaxLink<ActiveConnection> link = new AjaxLink("details", new PropertyModel<Long>(item.getModel(), "id")) {
					@Override
					public void onClick(AjaxRequestTarget target) {
						detailsPanel.setContent(new ConnectionViewDetailsPanel(FIDModalWindow.CONTENT_ID, item.getModel()));
						detailsPanel.show(target);
					}
				};
				link.add(new Label("mysqlId", new PropertyModel<Long>(item.getModel(), "id")));

				item.add(link);
				item.add(new Label("runtime", new PropertyModel<Long>(item.getModel(), "runtime")));
				item.add(new Label("thread", new PropertyModel<String>(item.getModel(), "threadName")));
				item.add(new Label("currentStack", new PropertyModel<String>(item.getModel(), "lastExecutedStatement.stackTrace")));
				item.add(new Label("lastSql", new FormattedStatementModel(new PropertyModel<String>(item.getModel(), "lastExecutedStatement.sql"))));
			}
		});
		add(tableContainer);
	}

	public class ActiveConnectionDataProvider extends SortableDataProvider<ActiveConnection> {

		@Override
		public Iterator<? extends ActiveConnection> iterator(int first, int count) {
			return new TreeSet<ActiveConnection>(ConnectionInterceptor.getConnectionsInUse().values()).iterator();
		}

		@Override
		public int size() {
			return ConnectionInterceptor.getConnectionsInUse().size();
		}

		@Override
		public IModel<ActiveConnection> model(ActiveConnection activeConnection) {
			return new ActiveConnectionModel(activeConnection);
		}
	}

}
