package com.n4systems.exporting;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.asset.AssetToModelConverter;
import com.n4systems.api.model.AssetView;
import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.Validator;
import com.n4systems.api.validation.validators.AssetViewValidator;
import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.ejb.impl.EventScheduleManagerImpl;
import com.n4systems.ejb.legacy.impl.LegacyAssetManager;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.model.Asset;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.user.User;
import com.n4systems.persistence.Transaction;
import com.n4systems.services.asset.AssetSaveService;

import java.util.List;

public class AssetImporter extends AbstractImporter<AssetView> {
	private AssetSaveService saver;
	private final AssetToModelConverter converter;
	private EventScheduleManager eventScheduleManager;
	private User user;

	
	public AssetImporter(MapReader mapReader, Validator<ExternalModelView> validator, AssetSaveService saver, AssetToModelConverter converter, EventScheduleManager eventScheduleManager) {
		super(AssetView.class, mapReader, validator);
		this.saver = saver;
		this.converter = converter;
		this.eventScheduleManager = eventScheduleManager;
		validator.getValidationContext().put(AssetViewValidator.ASSET_TYPE_KEY, converter.getType());
	}
	
	public AssetImporter(MapReader mapReader, Validator<ExternalModelView> validator, User user, AssetToModelConverter converter) {
		super(AssetView.class, mapReader, validator);
		saver = null;
		this.converter = converter;
		eventScheduleManager = null;
		this.user = user;
		validator.getValidationContext().put(AssetViewValidator.ASSET_TYPE_KEY, converter.getType());
	}

	@Override
	protected void preImport(Transaction transaction) {
		if (eventScheduleManager == null)
			eventScheduleManager = new EventScheduleManagerImpl(transaction.getEntityManager());
		if (saver == null)
			saver = new AssetSaveService(new LegacyAssetManager(transaction.getEntityManager()), user);
	}

	@Override
	protected void importView(Transaction transaction, AssetView view) throws ConversionException {
		Asset asset = converter.toModel(view, transaction);
		if (asset.isNew()) {
			saver.setAsset(asset);
			asset = saver.createWithoutHistory();
			autoScheduleEvents(asset);
		}
		else {
			saver.setAsset(asset);
			saver.update();
		}
	}

	private void autoScheduleEvents(Asset asset) {
		List<ThingEvent> autoScheduledEvents = eventScheduleManager.getAutoEventSchedules(asset);
		for (ThingEvent schedule: autoScheduledEvents) {
			if (schedule != null) {
				eventScheduleManager.update( schedule );
			}
		}
	}
}
