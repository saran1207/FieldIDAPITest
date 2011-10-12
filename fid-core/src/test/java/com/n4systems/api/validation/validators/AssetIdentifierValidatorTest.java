package com.n4systems.api.validation.validators;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.api.model.ExternalModelView;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.asset.SmartSearchLoader;
import com.n4systems.model.builders.AssetBuilder;
import com.n4systems.model.builders.AssetTypeBuilder;
import com.n4systems.model.builders.EventTypeBuilder;
import com.n4systems.model.eventtype.AssociatedEventTypeExistsLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.ListLoader;

public class AssetIdentifierValidatorTest {

    private static final String TEST_SEARCH_TEXT = "serial number";

    private Map<String, Object> validationContext;
    private EventType eventType;
    private AssetType assetType;
    private Asset asset;

    @Before
    public void setUp() {
        validationContext = new HashMap<String,Object>();
        eventType = EventTypeBuilder.anEventType().build();
        validationContext.put("eventType", eventType);
        assetType = AssetTypeBuilder.anAssetType().build();
        asset = AssetBuilder.anAsset().ofType(assetType).build();
    }
	
	@Test
	public void validate_passes_on_null_value() {
        SmartSearchLoader smartSearchLoader = createMockSmartSearchLoaderReturning(Arrays.asList(asset));
        AssociatedEventTypeExistsLoader associateDoesExistLoader = createMockAssociatedEventTypeExistsLoaderReturning(true);
        AssetIdentifierValidator validator = createTestIdentifierValidator(smartSearchLoader, associateDoesExistLoader);
		
		assertTrue(validator.validate(null, null, null, null, null, validationContext).isPassed());
    }

	@Test
	public void validate_passes_when_one_asset_found_if_association_exists() {
		SmartSearchLoader loader = createMockSmartSearchLoaderReturning(Arrays.asList(asset));
		replay(loader);

        AssociatedEventTypeExistsLoader existsLoader = createMockAssociatedEventTypeExistsLoaderReturning(true);
        replay(existsLoader);

		AssetIdentifierValidator validator = createTestIdentifierValidator(loader, existsLoader);

		assertTrue(validator.validate(TEST_SEARCH_TEXT, null, null, null, null, validationContext).isPassed());
		verify(loader);
        verify(existsLoader);
	}

    @Test
    public void validate_fails_when_one_asset_found_if_association_doesnt_exist() {
        SmartSearchLoader loader = createMockSmartSearchLoaderReturning(Arrays.asList(asset));
        replay(loader);

        AssociatedEventTypeExistsLoader existsLoader = createMockAssociatedEventTypeExistsLoaderReturning(false);
        replay(existsLoader);

        AssetIdentifierValidator validator = createTestIdentifierValidator(loader, existsLoader);

        assertFalse(validator.validate(TEST_SEARCH_TEXT, null, null, null, null, validationContext).isPassed());
        verify(loader);
        verify(existsLoader);
    }

	
	@Test
	public void validate_fails_when_no_assets_found() {
        SmartSearchLoader smartSearchLoader = createMockSmartSearchLoaderReturning(Collections.<Asset>emptyList());
        replay(smartSearchLoader);
        AssociatedEventTypeExistsLoader existsLoader = createMockAssociatedEventTypeExistsLoaderReturning(false);
        AssetIdentifierValidator validator = createTestIdentifierValidator(smartSearchLoader, existsLoader);

		assertFalse(validator.validate(TEST_SEARCH_TEXT, null, null, null, null, validationContext).isPassed());
		verify(smartSearchLoader);
	}
	
	@Test
	public void validate_fails_when_more_than_one_asset_found() {
        SmartSearchLoader smartSearchLoader = createMockSmartSearchLoaderReturning(Arrays.asList(asset, asset));
        replay(smartSearchLoader);

        AssociatedEventTypeExistsLoader existsLoader = createMockAssociatedEventTypeExistsLoaderReturning(true);
   		expect(existsLoader.setAssetType(asset.getType())).andReturn(existsLoader);        
   		expect(existsLoader.load()).andReturn(true);        
        replay(existsLoader);
        
        AssetIdentifierValidator validator = createTestIdentifierValidator(smartSearchLoader, existsLoader);
        
		assertFalse(validator.validate(TEST_SEARCH_TEXT, null, null, null, null, validationContext).isPassed());
		verify(smartSearchLoader);
	}

    private AssetIdentifierValidator createTestIdentifierValidator(final SmartSearchLoader smartSearchLoader, final AssociatedEventTypeExistsLoader associatedEventTypeExistsLoader) {
        return new AssetIdentifierValidator() {
            @Override
            protected <V extends ExternalModelView> ListLoader<Asset> createLoader(SecurityFilter filter, V view, String identifier) {
            	smartSearchLoader.setMaxResults(2);
            	smartSearchLoader.setSearchText(identifier);            	
                return smartSearchLoader;
            }

            @Override
            protected AssociatedEventTypeExistsLoader createAssociatedEventTypeExistsLoader(SecurityFilter filter, EventType eventType) {
           		associatedEventTypeExistsLoader.setEventType(eventType);
                return associatedEventTypeExistsLoader;
            }
        };
    }

    private AssociatedEventTypeExistsLoader createMockAssociatedEventTypeExistsLoaderReturning(final boolean result) {
        AssociatedEventTypeExistsLoader loader = createMock(AssociatedEventTypeExistsLoader.class);
        expect(loader.setAssetType(assetType)).andReturn(loader);
        expect(loader.setEventType(eventType)).andReturn(loader);
        expect(loader.load()).andReturn(result);
        return loader;
    }

    private SmartSearchLoader createMockSmartSearchLoaderReturning(List<Asset> assets) {
        SmartSearchLoader smartSearchLoader = createMock(SmartSearchLoader.class);
        expect(smartSearchLoader.setSearchText(TEST_SEARCH_TEXT)).andReturn(smartSearchLoader);
        smartSearchLoader.setMaxResults(2);
		expect(smartSearchLoader.load()).andReturn(assets);
        return smartSearchLoader;
    }
	
}
