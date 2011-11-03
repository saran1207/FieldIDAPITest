package com.n4systems.fieldid.wicket.pages.widgets.config;

import java.util.Arrays;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.n4systems.fieldid.wicket.components.org.OrgPicker;
import com.n4systems.model.dashboard.widget.AssetsIdentifiedWidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.chart.ChartDateRange;

@SuppressWarnings("serial")
public class AssetsIdentifiedConfigPanel extends WidgetConfigPanel<AssetsIdentifiedWidgetConfiguration> {

    private IModel<AssetsIdentifiedWidgetConfiguration> configModel;
    private DropDownChoice<ChartDateRange> dateRangeSelect;
    private OrgPicker orgPicker;

    public AssetsIdentifiedConfigPanel(String id, final IModel<AssetsIdentifiedWidgetConfiguration> configModel) {
        super(id, configModel );
        this.configModel = configModel;

        addConfigElement(orgPicker = new OrgPicker("picker", new PropertyModel<BaseOrg>(configModel, "org")));
        
        IChoiceRenderer<ChartDateRange> renderer = new IChoiceRenderer<ChartDateRange>() {
			@Override public Object getDisplayValue(ChartDateRange object) {
				return object.toString();
			}
			@Override public String getIdValue(ChartDateRange object, int index) {
				return object.name();
			}
		};
		addConfigElement(new DropDownChoice<ChartDateRange>("dateRangeSelect", new PropertyModel<ChartDateRange>(configModel,"dateRange"), Arrays.asList(ChartDateRange.values()), renderer).setNullValid(false));        
    }

}

