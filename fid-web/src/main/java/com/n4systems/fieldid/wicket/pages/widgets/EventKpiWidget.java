package com.n4systems.fieldid.wicket.pages.widgets;

import com.n4systems.fieldid.wicket.components.dashboard.subcomponents.EventKpiTable;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.widgets.OrgSubtitleHelper.SubTitleModelInfo;
import com.n4systems.fieldid.wicket.pages.widgets.config.EventKPIConfigPanel;
import com.n4systems.model.dashboard.WidgetDefinition;
import com.n4systems.model.dashboard.widget.EventKPIWidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.utils.DateRange;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class EventKpiWidget extends Widget<EventKPIWidgetConfiguration> implements HasDateRange {

	@SpringBean 
	private OrgDateRangeSubtitleHelper orgDateRangeSubtitleHelper;

    public EventKpiWidget(String id, WidgetDefinition<EventKPIWidgetConfiguration> widgetDefinition) {
        super(id, new Model<WidgetDefinition<EventKPIWidgetConfiguration>>(widgetDefinition));
		setOutputMarkupId(true);

		List<BaseOrg> orgList = getOrgList();
		ContextImage arrow;
		add(arrow = new ContextImage("arrow", "images/dashboard/arrow.png"));
		
		if(orgList.isEmpty()) {
			Label message= new Label("eventKpiContent", new FIDLabelModel("message.configure_widget"));
			message.add(new SimpleAttributeModifier("class", "configureMessage"));
			add(message);
			arrow.setVisible(true);
		} else {
			add(new EventKpiTable("eventKpiContent", orgList, getConfig(), widgetDefinition));
			arrow.setVisible(false);
		}
	}

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/legacy/dashboard/widgets/eventkpi.css");
        response.renderJavaScriptReference("javascript/jquery-ui-1.8.20.no-autocomplete.min.js");
        response.renderJavaScriptReference("javascript/tipsy/jquery.tipsy.js");
        response.renderCSSReference("style/legacy/tipsy/tipsy.css");
        response.renderOnDomReadyJavaScript("chartWidgetFactory.updateKpi()");
    }

    private List<BaseOrg> getOrgList() {
		EventKPIWidgetConfiguration config = getConfig();

		if(config == null) {
			return new ArrayList<BaseOrg>();
		} else {
			return config.getOrgs();
		}
	}

	private EventKPIWidgetConfiguration getConfig() {
		return getWidgetDefinition().getObject().getConfig();
	}

	@Override
    public Component createConfigPanel(String id) {
		EventKPIWidgetConfiguration configCopy = getConfig().copy();
		IModel<EventKPIWidgetConfiguration> configModel = new Model<EventKPIWidgetConfiguration>(configCopy);
		return new EventKPIConfigPanel(id, configModel, getWidgetDefinition());
	}
	
	@Override
	protected IModel<String> getSubTitleModel() {
		SubTitleModelInfo info = orgDateRangeSubtitleHelper.getSubTitleModel(getWidgetDefinition(), null, getConfig().getRangeType());
		return new StringResourceModel(info.getKey(), this, null, info.getModels().toArray() );
	}

    @Override
    public DateRange getDateRange() {
        EventKPIWidgetConfiguration config = getWidgetDefinition().getObject().getConfig();
        return getTimeZoneDateRange(config.getRangeType());
    }
}
