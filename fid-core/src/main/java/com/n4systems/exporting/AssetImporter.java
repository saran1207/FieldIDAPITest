package com.n4systems.exporting;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.asset.AssetToModelConverter;
import com.n4systems.api.model.AssetView;
import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.Validator;
import com.n4systems.api.validation.validators.AssetViewValidator;
import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.persistence.Transaction;
import com.n4systems.services.asset.AssetSaveService;
import com.n4systems.util.ServiceLocator;

import java.util.List;

public class AssetImporter extends AbstractImporter<AssetView> {
	private final AssetSaveService saver;
	private final AssetToModelConverter converter;
	private EventScheduleManager eventScheduleManager;

	
	public AssetImporter(MapReader mapReader, Validator<ExternalModelView> validator, AssetSaveService saver, AssetToModelConverter converter, EventScheduleManager eventScheduleManager) {
		super(AssetView.class, mapReader, validator);
		this.saver = saver;
		this.converter = converter;
		this.eventScheduleManager = eventScheduleManager;
		validator.getValidationContext().put(AssetViewValidator.ASSET_TYPE_KEY, converter.getType());
	}
	
	public AssetImporter(MapReader mapReader, Validator<ExternalModelView> validator, AssetSaveService saver, AssetToModelConverter converter) {
		this(mapReader, validator, saver, converter, ServiceLocator.getEventScheduleManager());
	}

	@Override
	protected void importView(Transaction transaction, AssetView view) throws ConversionException {
		Asset asset = converter.toModel(view, transaction);
		saver.setAsset(asset);
		asset = saver.createWithoutHistory();
		autoScheduleEvents(asset);
	}

	private void autoScheduleEvents(Asset asset) {
		List<Event> autoScheduledEvents = eventScheduleManager.getAutoEventSchedules(asset);
		for (Event schedule: autoScheduledEvents) {
			if (schedule != null) {
				eventScheduleManager.update( schedule );
			}
		}
	}
}
