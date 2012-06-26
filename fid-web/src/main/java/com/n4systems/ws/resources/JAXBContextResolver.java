package com.n4systems.ws.resources;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import com.n4systems.ws.model.assettype.WsAssetStatus;
import com.n4systems.ws.model.autoattribute.WsAutoAttributeCriteria;
import com.n4systems.ws.model.autoattribute.WsAutoAttributeDefinition;
import com.n4systems.ws.model.commenttemplate.WsCommentTemplate;
import com.n4systems.ws.model.eventbook.WsEventBook;
import com.n4systems.ws.model.eventtype.WsComboBoxCriteria;
import com.n4systems.ws.model.eventtype.WsCriteria;
import com.n4systems.ws.model.eventtype.WsDateFieldCriteria;
import com.n4systems.ws.model.eventtype.WsEventForm;
import com.n4systems.ws.model.eventtype.WsEventStatus;
import com.n4systems.ws.model.eventtype.WsEventType;
import com.n4systems.ws.model.eventtype.WsNumberFieldCriteria;
import com.n4systems.ws.model.eventtype.WsOneClickCriteria;
import com.n4systems.ws.model.eventtype.WsScoreCriteria;
import com.n4systems.ws.model.eventtype.WsSelectCriteria;
import com.n4systems.ws.model.eventtype.WsSignatureCriteria;
import com.n4systems.ws.model.eventtype.WsTextFieldCriteria;
import com.n4systems.ws.model.eventtype.WsUnitOfMeasureCriteria;
import com.n4systems.ws.model.lastmod.WsLastModified;
import com.n4systems.ws.model.org.WsOrg;
import com.n4systems.ws.model.setupdata.WsUser;
import com.n4systems.ws.model.unitofmeasure.WsUnitOfMeasure;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

@Provider
public class JAXBContextResolver implements ContextResolver<JAXBContext> {
	/*
	 * Any type that is returned directly from a resource method (not embedded types) should be listed here.
	 * This sets the JSON formatter to natural which ensures that lists/arrays with a single element 
	 * get properly encapsulated in []'s.  Otherwise list/array fields will return 'field: singlevalue' rather
	 * than 'field: [ singlevalue ]' when there is only one element in the collection.
	 */
	private static final Class<?>[] rootTypes = {
		WsEventType.class,
		WsAutoAttributeCriteria.class,
		WsAutoAttributeDefinition.class,
		WsAssetStatus.class,
		WsCommentTemplate.class,
		WsUnitOfMeasure.class,
		WsOrg.class,
		WsEventBook.class,
		WsLastModified.class,
		WsEventForm.class,
		WsCriteria.class,
		WsSelectCriteria.class,
		WsOneClickCriteria.class,
		WsTextFieldCriteria.class,
		WsUnitOfMeasureCriteria.class,
		WsComboBoxCriteria.class,
		WsSignatureCriteria.class,
		WsDateFieldCriteria.class,
		WsNumberFieldCriteria.class,
		WsScoreCriteria.class,
		WsUser.class,
		WsEventStatus.class
	};
	
	private final JAXBContext context;
	
	public JAXBContextResolver() throws Exception {
		context = new JSONJAXBContext(JSONConfiguration.natural().build(), rootTypes);
	}

	public JAXBContext getContext(Class<?> objectType) {
		for (Class<?> type: rootTypes) {
			if (type == objectType) {
				return context;
			}
		}
		return null;
	}

}
