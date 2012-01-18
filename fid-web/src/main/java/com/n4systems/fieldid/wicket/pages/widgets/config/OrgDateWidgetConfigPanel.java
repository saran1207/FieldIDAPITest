package com.n4systems.fieldid.wicket.pages.widgets.config;

import java.util.Arrays;

import com.n4systems.util.chart.FloatingDateRange;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.n4systems.fieldid.wicket.components.org.OrgPicker;
import com.n4systems.fieldid.wicket.utils.EnumDropDownChoiceRenderer;
import com.n4systems.model.dashboard.widget.WidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;

@SuppressWarnings("serial")
public abstract class OrgDateWidgetConfigPanel<T extends WidgetConfiguration> extends WidgetConfigPanel<T> {

    private IModel<T> configModel;
    protected OrgPicker orgPicker;
    protected DropDownChoice<FloatingDateRange> dateRangeSelect;
    
    public OrgDateWidgetConfigPanel(String id, final IModel<T> configModel) {
        super(id, configModel);
        this.configModel = configModel;       
        addConfigElement(orgPicker = createOrgPicker(configModel));        
		addConfigElement(dateRangeSelect = createDateRangeSelect()); 
    }

	private OrgPicker createOrgPicker(final IModel<T> configModel) {
        OrgPicker picker = new OrgPicker("picker", new PropertyModel<BaseOrg>(configModel, "org"));
        picker.setTranslateContainerSelector(".w_content");
        return picker;
	}

	protected DropDownChoice<FloatingDateRange> createDateRangeSelect() {
		DropDownChoice<FloatingDateRange> d = new DropDownChoice<FloatingDateRange>("dateRangeSelect", new PropertyModel<FloatingDateRange>(configModel,"dateRange"), Arrays.asList(FloatingDateRange.chartDateRanges()), new EnumDropDownChoiceRenderer());
		d.setNullValid(false);
		return d;
	}	

}
