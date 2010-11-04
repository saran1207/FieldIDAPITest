package com.n4systems.api.validation.validators;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.n4systems.model.EventType;
import com.n4systems.model.builders.AssetTypeBuilder;
import org.junit.Test;

import com.n4systems.model.Asset;
import com.n4systems.model.builders.EventTypeBuilder;
import com.n4systems.model.builders.AssetBuilder;
import com.n4systems.model.eventtype.AssociatedEventTypeExistsLoader;
import com.n4systems.model.asset.SmartSearchLoader;
import com.n4systems.model.security.SecurityFilter;

public class AssociatedEventTypeValidatorTest {

	@Test
	public void validate_passes_on_null_value() { 
		AssociatedEventTypeValidator validator = new AssociatedEventTypeValidator();
		
		assertTrue(validator.validate(null, null, null, null, (EventType)null).isPassed());
	}

	@Test
	public void validate_passes_when_associated_event_type_found() {
		String searchText = "serial number";
		
		Asset asset = AssetBuilder.anAsset().build();
		asset.setType(AssetTypeBuilder.anAssetType().build());
		EventType inspType = EventTypeBuilder.anEventType().build();
		
		final SmartSearchLoader ssLoader = createMock(SmartSearchLoader.class);
		expect(ssLoader.setSearchText(searchText)).andReturn(ssLoader);
		expect(ssLoader.load()).andReturn(Arrays.asList(asset));
		replay(ssLoader);
		
		final AssociatedEventTypeExistsLoader aitLoader = createMock(AssociatedEventTypeExistsLoader.class);
		expect(aitLoader.setEventType(inspType)).andReturn(aitLoader);
		expect(aitLoader.setAssetType(asset.getType())).andReturn(aitLoader);
		expect(aitLoader.load()).andReturn(true);
		replay(aitLoader);
		
		AssociatedEventTypeValidator validator = new AssociatedEventTypeValidator() {
			protected SmartSearchLoader createSmartSearchLoader(SecurityFilter filter) {
				return ssLoader;
			}
			
			protected AssociatedEventTypeExistsLoader createAssociatedEventTypeExistsLoader(SecurityFilter filter) {
				return aitLoader;
			}
		};
		
		
		Map<String, Object> context = new HashMap<String, Object>();
		context.put(EventViewValidator.EVENT_TYPE_KEY, inspType);
		
		assertTrue(validator.validate(searchText, null, null, null, context).isPassed());
		verify(ssLoader);
		verify(aitLoader);
	}
	
	@Test
	public void validate_fails_when_associated_event_type_not_found() {
		String searchText = "serial number";
		
		Asset asset = AssetBuilder.anAsset().build();
		asset.setType(AssetTypeBuilder.anAssetType().build());
		EventType inspType = EventTypeBuilder.anEventType().build();
		
		final SmartSearchLoader ssLoader = createMock(SmartSearchLoader.class);
		expect(ssLoader.setSearchText(searchText)).andReturn(ssLoader);
		expect(ssLoader.load()).andReturn(Arrays.asList(asset));
		replay(ssLoader);
		
		final AssociatedEventTypeExistsLoader aitLoader = createMock(AssociatedEventTypeExistsLoader.class);
		expect(aitLoader.setEventType(inspType)).andReturn(aitLoader);
		expect(aitLoader.setAssetType(asset.getType())).andReturn(aitLoader);
		expect(aitLoader.load()).andReturn(false);
		replay(aitLoader);
		
		AssociatedEventTypeValidator validator = new AssociatedEventTypeValidator() {
			protected SmartSearchLoader createSmartSearchLoader(SecurityFilter filter) {
				return ssLoader;
			}
			
			protected AssociatedEventTypeExistsLoader createAssociatedEventTypeExistsLoader(SecurityFilter filter) {
				return aitLoader;
			}
		};
		
		
		Map<String, Object> context = new HashMap<String, Object>();
		context.put(EventViewValidator.EVENT_TYPE_KEY, inspType);
		
		assertFalse(validator.validate(searchText, null, null, null, context).isPassed());
		verify(ssLoader);
		verify(aitLoader);
	}
}
