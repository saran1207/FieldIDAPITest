package com.n4systems.fieldid.wicket.pages.widgets;

import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import com.google.common.collect.Lists;
import com.n4systems.model.dashboard.widget.WidgetConfiguration;
import com.n4systems.model.orgs.BaseOrg;

public abstract class OrgSubtitleHelper {

	private String fromKey;
	private String toKey;
	
	public OrgSubtitleHelper(String fromKey, String toKey) {
		this.fromKey = fromKey;
		this.toKey = toKey;
	}
	
	@SuppressWarnings("unchecked")
	public <W extends WidgetConfiguration> IModel<String> getSubTitleModel(
			Widget<W> widget, BaseOrg org, String keyBase) {
				List<PropertyModel<String>> models = Lists.newArrayList(
						new PropertyModel<String>(widget.getWidgetDefinition(), fromKey),
						new PropertyModel<String>(widget.getWidgetDefinition(), toKey) );				
				StringBuffer key = new StringBuffer(keyBase);
				if (org!=null) {
					models.add(new PropertyModel<String>(widget.getWidgetDefinition(), "config.org.hierarchicalDisplayName"));
					key.append(".org");
				}
				return new StringResourceModel(key.append(".subTitle").toString(), widget, null, models.toArray() );
			}
	

}
