package com.n4systems.fieldid.wicket.pages.widgets;

import com.n4systems.fieldid.wicket.components.Agenda;
import com.n4systems.fieldid.wicket.pages.widgets.config.WidgetConfigPanel;
import com.n4systems.model.Event;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.WidgetConfiguration;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class WorkWidget extends Widget<WidgetConfiguration> {

    public WorkWidget(String id, WidgetDefinition<WidgetConfiguration> widgetDefinition) {
		super(id, new Model<WidgetDefinition<WidgetConfiguration>>(widgetDefinition));

        add(new Agenda("agenda"));
	}

    @Override
    public void renderHead(IHeaderResponse response) {

    }

    private IColumn<Event> createLinkColumn() {
        return new PropertyColumn<Event>(new Model<String>(""), "id") {
            @Override public void populateItem(Item item, String componentId, IModel rowModel) {
                item.add(new Link(componentId, Model.of("hello")) {
                    @Override public void onClick() {
                        // TODO : hook this up to open event.  (goto summary page/events tab)
                        System.out.println("hello");
                    }
                });
            }
        };
    }

	@Override
    public Component createConfigPanel(String id) {
		IModel<WidgetConfiguration> configModel = new Model<WidgetConfiguration>(getWidgetDefinition().getObject().getConfig());		
		return new WidgetConfigPanel<WidgetConfiguration>(id, configModel);
	}

}
