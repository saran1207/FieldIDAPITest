package com.n4systems.fieldid.wicket.pages.widgets;

import com.google.common.base.CaseFormat;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.WidgetConfiguration;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

public class ErrorLoadingWidget extends Widget<WidgetConfiguration> {

    public ErrorLoadingWidget(String id, WidgetDefinition<WidgetConfiguration> widgetDefinition, Class<? extends Widget> clazz) {
        super(id, Model.of(widgetDefinition));
        String type = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, widgetDefinition.getWidgetType().toString());
        add(new Label("msg",Model.of("error loading " + type + " widget.  ("  + clazz.getSimpleName() + ")")));
    }

    @Override
    public Component createConfigPanel(String id) {
        return new Label(id, Model.of("error : can't configure panel because it wasn't constructed properly."));
    }
}
