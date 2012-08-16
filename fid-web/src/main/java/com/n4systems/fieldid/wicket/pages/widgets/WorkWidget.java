package com.n4systems.fieldid.wicket.pages.widgets;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.components.Agenda;
import com.n4systems.fieldid.wicket.pages.widgets.config.WorkConfigPanel;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.WorkWidgetConfiguration;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import java.util.List;

public class WorkWidget extends Widget<WorkWidgetConfiguration> {

    public WorkWidget(String id, WidgetDefinition<WorkWidgetConfiguration> widgetDefinition) {
		super(id, new Model<WidgetDefinition<WorkWidgetConfiguration>>(widgetDefinition));
        add(new Agenda("agenda", new PropertyModel<WorkWidgetConfiguration>(widgetDefinition, "config")).withSelectedDay(dateService.now()));
	}

	@Override
    public Component createConfigPanel(String id) {
		IModel<WorkWidgetConfiguration> configModel = new Model<WorkWidgetConfiguration>(getWidgetDefinition().getObject().getConfig());
		return new WorkConfigPanel(id, configModel);
	}

    @Override
    protected IModel<String> getSubTitleModel() {
        WorkWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
        List<IModel<String>> models = Lists.newArrayList();
        if (config.getUser()!=null) {
            models.add(new PropertyModel(config, "user.displayName"));
        } else {
            models.add(Model.of(""));
        }
        if (config.getOrg()!=null) {
            models.add(new PropertyModel(config, "org.displayName"));
        } else {
            models.add(Model.of(""));
        }
        if (config.getEventType()!=null) {
            new PropertyModel<String>(config, "eventType.displayName");
        } else {
            models.add(Model.of(""));
        }
        if (config.getAssetType()!=null) {
            models.add(new PropertyModel<String>(config, "assetType.displayName"));
        } else {
            models.add(Model.of(""));
        }

        return new StringResourceModel("work.subtitle", this, null, models.toArray() );
    }
}
