package com.n4systems.fieldid.viewhelpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.n4systems.model.search.ColumnMappingGroupView;
import com.n4systems.model.search.ColumnMappingView;
import org.apache.log4j.Logger;

import com.n4systems.model.Tenant;
import com.n4systems.util.properties.HierarchicalProperties;
import com.n4systems.util.properties.HirarchicalPropertiesLoader;

public class ColumnMappingFactory {
	private static final ConcurrentMap<Class<?>, SortedSet<ColumnMappingGroupView>> classDataMap = new ConcurrentHashMap<Class<?>, SortedSet<ColumnMappingGroupView>>();
	private static final Logger logger = Logger.getLogger(ColumnMappingFactory.class);
	
	// all methods static, hide constructor
	private ColumnMappingFactory() {}
	
	/**
	 * Loads a List of ColumnMappingGroups for a given class from a resource based property file
	 * @see HierarchicalProperties
	 * @see HirarchicalPropertiesLoader#load(Class)
	 * @param clazz			The class to load properties from
	 * @return				A list of ColumnMappingGroups populated with ColumnMappings
	 */
	public static SortedSet<ColumnMappingGroupView> getMappings(Class<?> clazz, Tenant tenant) {
		return getMappings(clazz, tenant, true);
	}
	
	/**
	 * Loads a List of ColumnMappingGroups for a given class from a resource based property file
	 * @see HierarchicalProperties
	 * @see HirarchicalPropertiesLoader#load(Class)
	 * @param clazz			The class to load properties from
	 * @param forceReload	Forces a reload from the properties file
	 * @return				A list of ColumnMappingGroups populated with ColumnMappings
	 */
	public static SortedSet<ColumnMappingGroupView> getMappings(Class<?> clazz, Tenant tenant, boolean forceReload) {
		
		SortedSet<ColumnMappingGroupView> mappings = classDataMap.get(clazz);
		if(mappings == null || forceReload) {
			mappings = loadMappings(clazz, tenant);
			classDataMap.put(clazz, mappings);
		}
		
		return mappings;
	}
	
	/**
	 * Loads a List of ColumnMappingGroups for a given class from a resource based property file
	 * @see HierarchicalProperties
	 * @see HirarchicalPropertiesLoader#load(Class)
	 * @param clazz		The class to load properties from
	 * @return			A SortedSet of ColumnMappingGroups populated with ColumnMappings
	 */
	private static SortedSet<ColumnMappingGroupView> loadMappings(Class<?> clazz, Tenant tenant) {
		HierarchicalProperties properties = HirarchicalPropertiesLoader.load(clazz, tenant);
		
		// we'll start by populating a map as it'll make it easier to add the mappings later
		Map<String, ColumnMappingGroupView> groups = new HashMap<String, ColumnMappingGroupView>();
	
		for(HierarchicalProperties props: properties.getPropertiesList("group")) {
			groups.put(props.getParent(), new ColumnMappingGroupView(props.getParent(), props.getString("label"), props.getInteger("order"), null));
		}
		
		String groupId;
		for(HierarchicalProperties props: properties.getPropertiesList("mapping")) {
			groupId = props.getString("group");
			try {
				groups.get(groupId).getMappings().add(
					new ColumnMappingView(
						props.getParent(), 
						props.getString("label"), 
						props.getString("pathExpression"),
						props.getString("sortExpression"),
						props.getString("outputHandler"),
						props.getBoolean("sortable"), 
						props.getBoolean("onByDefault"), 
						props.getInteger("order"), 
						props.getString("requiredExtendedFeature"),
                            null, null, null)
				);
			} catch (NullPointerException e) {
				logger.error(String.format("Unable to create column mapping: group=%s, properties=%s", groupId, props), e);
			}
		}
		
		// convert the values of our map into a treeset
		return new TreeSet<ColumnMappingGroupView>(groups.values());
	}
	
	public static List<String> covertToStorageList(SortedSet<ColumnMappingView> mappings) {
		List<String> list = new ArrayList<String>();
		for (ColumnMappingView mapping : mappings) {
			list.add(mapping.getId());
		}
		
		return list;
	}
	
}
