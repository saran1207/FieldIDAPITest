package com.n4systems.fieldid.wicket.pages.widgets;

import com.google.common.collect.Lists;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public abstract class OrgSubtitleHelper {

	// CAVEAT : the ChartDateRange enumeration is tied directly in to the resource key names. 
	//  .: if you refactor the date ranges (i.e. change their names) you will have to update the property files accordingly.
	// e.g. if you rename ChartDateRange.FOREVER to ALL_OF_TIME then the FIeldIDWicketApp.properties file will have to 
	//    be updated to include dateRange.all_of_time.subTitle=...
	
	private String key1;
	private String key2;
	
	public OrgSubtitleHelper(String fromKey, String toKey) {
		this.key1 = fromKey;
		this.key2 = toKey;
	}
	
	@SuppressWarnings("unchecked")
	public SubTitleModelInfo getSubTitleModel(Object model, BaseOrg org, String keyBase) {
		List<PropertyModel<String>> models = Lists.newArrayList(
				new PropertyModel<String>(model, key1),
				new PropertyModel<String>(model, key2) );
		StringBuffer key = new StringBuffer(keyBase);
		if (org!=null) {
			models.add(new PropertyModel<String>(model, "config.org.hierarchicalDisplayName"));
			key.append(".org");
		}
		return new SubTitleModelInfo(key.append(".subTitle").toString(), models );
	}
	
	public class SubTitleModelInfo { 
		private String key;
		private List<PropertyModel<String>> models;
		
		public SubTitleModelInfo(String key, List<PropertyModel<String>> models) {
			this.key = key;
			this.models = models;
		}

		public String getKey() {
			return key;
		}

		public List<PropertyModel<String>> getModels() {
			return models;
		} 
	}

}
