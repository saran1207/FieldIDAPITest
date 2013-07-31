package com.n4systems.fieldid.wicket.pages.widgets.config;

import com.n4systems.fieldid.wicket.components.org.OrgLocationPicker;
import com.n4systems.fieldid.wicket.util.EnumDropDownChoiceRenderer;
import com.n4systems.model.dashboard.widget.WidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.chart.RangeType;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Arrays;

public abstract class OrgDateWidgetConfigPanel<T extends WidgetConfiguration> extends WidgetConfigPanel<T> {

    private IModel<T> configModel;
    protected Component orgPicker;
    protected DropDownChoice<RangeType> dateRangeSelect;
    
    public OrgDateWidgetConfigPanel(String id, final IModel<T> configModel) {
        super(id, configModel);
        this.configModel = configModel;       
        addConfigElement(orgPicker = createOrgPicker("picker", configModel));
		addConfigElement(dateRangeSelect = createDateRangeSelect()); 
    }

	protected Component createOrgPicker(String id, final IModel<T> configModel) {
        return new OrgLocationPicker<BaseOrg>(id, new PropertyModel<BaseOrg>(configModel, "org"));
	}

	protected DropDownChoice<RangeType> createDateRangeSelect() {
		DropDownChoice<RangeType> d = new DropDownChoice<RangeType>("dateRangeSelect", new PropertyModel<RangeType>(configModel,"rangeType"),
                Arrays.asList(RangeType.allFloatingButFutureTypesAndCustom().toArray(new RangeType[]{})), new EnumDropDownChoiceRenderer());
		d.setNullValid(false);
		return d;
	}	

}
