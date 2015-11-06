package com.n4systems.fieldid.wicket.pages.admin.connections;

import com.fieldid.jdbc.ActiveConnection;
import com.fieldid.jdbc.ConnectionInterceptor;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.pages.admin.FieldIDAdminPage;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.ListDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.resource.StringResourceStream;
import org.apache.wicket.util.time.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class ConnectionViewPage extends FieldIDAdminPage {

	private transient List<ActiveConnection> activeConnections;
	private boolean autoRefreshEnabled = true;

	public ConnectionViewPage() {
		activeConnections = new ArrayList<>();
		refreshConnections();

		add(new FIDFeedbackPanel("feedbackPanel"));
		IModel<String> refreshButtonModel = new AbstractReadOnlyModel<String>() {
			@Override
			public String getObject() {
				return autoRefreshEnabled ? "Pause" : "Resume";
			}
		};
		add(new Button("autoRefresh", refreshButtonModel)
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
				if (autoRefreshEnabled) {
					refreshConnections();
					target.add(getComponent());
				}
			}
		});
		tableContainer.add(new DataView<ActiveConnection>("connectionViewTable", new ListDataProvider<>(activeConnections)) {
			@Override
			protected void populateItem(final Item<ActiveConnection> item) {
				final AjaxDownloadBehavior download = new AjaxDownloadBehavior(item.getModel());
				item.add(download);

				AjaxLink<Long> link = new AjaxLink<Long>("details", new PropertyModel<>(item.getModel(), "id")) {
					@Override
					public void onClick(AjaxRequestTarget target) {
						download.initiate(target);
					}
				};
				link.add(new Label("mysqlId", new PropertyModel<Long>(item.getModel(), "id")));

				item.add(link);
				item.add(new Label("runtime", new PropertyModel<Long>(item.getModel(), "runtime")));
				item.add(new Label("thread", new PropertyModel<String>(item.getModel(), "threadName")));
				item.add(new Label("currentStack", new PropertyModel<String>(item.getModel(), "lastExecutedStatement.stackTrace")));
				item.add(new Label("lastSql", new FormattedStatementModel(new PropertyModel<>(item.getModel(), "lastExecutedStatement.sql"))));
			}
		});
		add(tableContainer);
	}

	private void refreshConnections() {
		activeConnections.clear();
		activeConnections.addAll(new TreeSet<>(ConnectionInterceptor.getConnectionsInUse().values()));
	}

	public class AjaxDownloadBehavior extends AbstractAjaxBehavior {
		private final IModel<ActiveConnection> connection;

		public AjaxDownloadBehavior(IModel<ActiveConnection> connection) {
			this.connection = connection;
		}

		public void initiate(AjaxRequestTarget target) {
			String url = getCallbackUrl().toString();
			url += (url.contains("?") ? "&" : "?") + '_' + System.currentTimeMillis();
			// the timeout is needed to let Wicket release the channel
			target.appendJavaScript("setTimeout(\"window.location.href='" + url + "'\", 100);");
		}

		@Override
		public void onRequest() {
			String fileName = "transaction_" + connection.getObject().getId() + ".log";
			ResourceStreamRequestHandler handler = new ResourceStreamRequestHandler(new StringResourceStream(connection.getObject().toString()), fileName);
			handler.setContentDisposition(ContentDisposition.ATTACHMENT);
			getComponent().getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
		}
	}

}
