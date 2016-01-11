package com.n4systems.fieldid.wicket.pages.widgets.config;

import com.n4systems.fieldid.wicket.components.org.OrgLocationPicker;
import com.n4systems.fieldid.wicket.components.renderer.EnumDropDownChoiceRenderer;
import com.n4systems.model.dashboard.WidgetDefinition;
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

    public OrgDateWidgetConfigPanel(String id, final IModel<T> configModel, IModel<WidgetDefinition<T>> def) {
        super(id, configModel, def);
        this.configModel = configModel;       
        addConfigElement(orgPicker = createOrgPicker("picker", configModel));
		addConfigElement(dateRangeSelect = createDateRangeSelect()); 
    }

    public OrgDateWidgetConfigPanel(String id, final IModel<T> configModel, boolean isProceduresConfig, IModel<WidgetDefinition<T>> def) {
        super(id, configModel, def);
        this.configModel = configModel;
        if(isProceduresConfig) {
            addConfigElement(dateRangeSelect = createDateRangeSelect());
        } else {
            addConfigElement(orgPicker = createOrgPicker("picker", configModel));
        }
    }

	protected Component createOrgPicker(String id, final IModel<T> configModel) {
        return new OrgLocationPicker(id, new PropertyModel<BaseOrg>(configModel, "org"));
	}

	protected DropDownChoice<RangeType> createDateRangeSelect() {
		DropDownChoice<RangeType> d = new DropDownChoice<RangeType>("dateRangeSelect", new PropertyModel<RangeType>(configModel,"rangeType"),
                Arrays.asList(RangeType.allFloatingButFutureTypesAndCustom().toArray(new RangeType[]{})), new EnumDropDownChoiceRenderer());
		d.setNullValid(false);
		return d;
	}	

}
