package com.n4systems.fieldid.utils;

import com.n4systems.model.Tenant;
import com.n4systems.model.parents.EntityWithTenant;

import rfid.ejb.entity.UserBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * provides static methods to clean up lists that have come in from struts. 
 * @author aaitken
 *
 */
public class StrutsListHelper {

	
	/**
	 * Clears out null values in a list.
	 * @param list	A generic list of objects
	 */
	public static void clearNulls( List<?> list ) {
		
		if(list != null) {
			while(list.remove(null)) {}
		}
	}
	
	/**
	 * Clears out empty and null Strings from a list
	 * @see #clearNulls(List)
	 * @param list	A list of Strings
	 */
	public static void clearEmpties(List<String> list) {
		
		clearNulls(list);
		
		if(list != null) {
			while(list.remove("")) {}
		}
	}
	
	public static <T extends EntityWithTenant> void setSecurity( Collection<T> list, Tenant tenant, UserBean modifiedBy ) {
		for( EntityWithTenant entity : list ) {
			if( entity != null ) { 
				entity.setTenant( tenant );
				
				// if the modifiedBy user is null, we don't want to wipe what could already be there
				if(modifiedBy != null) {
					entity.setModifiedBy(modifiedBy);
				}
			} 
		}
	}
	
	public static <T extends EntityWithTenant> void setTenants( Collection<T> list, Tenant tenant ) {
		setSecurity(list, tenant, null);
	}
	
	/**
	 * Returns the count of non-null elements in the Collection.
	 * @param list	A Collection
	 * @return the number of non-null elements
	 */
	public static <T> int countNotNull(Collection<T> list) {
		int n = 0;
		for (T element: list) {
			if (element != null) {
				n++;
			}
		}
		return n;
	}

	public static void removeMarkedEntries(String deletePrefixMarker, List<String> list) {
		List<String> entriesToRemove = new ArrayList<String>();
		
		for (String entry : list) {
			if (entry != null && entry.startsWith(deletePrefixMarker)) {
				entriesToRemove.add(entry);
			}
		}
		
		list.removeAll(entriesToRemove);
	}
	
	
	
	
	
}
