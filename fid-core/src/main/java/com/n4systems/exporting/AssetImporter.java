package com.n4systems.exporting;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.asset.AssetToModelConverter;
import com.n4systems.api.model.AssetView;
import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.validation.Validator;
import com.n4systems.api.validation.validators.AssetViewValidator;
import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.ejb.impl.EventScheduleManagerImpl;
import com.n4systems.exceptions.SubAssetUniquenessException;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.fieldid.service.asset.AssetImportService;
import com.n4systems.model.Asset;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.UserSecurityFilter;
import com.n4systems.persistence.Transaction;

import java.util.List;

import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.services.SecurityContext;
import org.apache.log4j.Logger;

public class AssetImporter extends AbstractImporter<AssetView> {

	private Logger logger = Logger.getLogger(AssetImporter.class);

	private AssetImportService saver;
	private final AssetToModelConverter converter;
	private EventScheduleManager eventScheduleManager;
	private SecurityContext securityContext;
	private SecurityContext localSecurityContext;
	private SecurityFilter securityFilter;

	public AssetImporter(MapReader mapReader, Validator<ExternalModelView> validator, AssetImportService saver,
						 AssetToModelConverter converter, EventScheduleManager eventScheduleManager,
						 SecurityContext securityContext, SecurityFilter securityFilter) {
		super(AssetView.class, mapReader, validator);
		this.saver = saver;
		this.converter = converter;
		this.eventScheduleManager = eventScheduleManager;
		this.securityContext = securityContext;
		this.securityFilter = securityFilter;
		validator.getValidationContext().put(AssetViewValidator.ASSET_TYPE_KEY, converter.getType());
	}
	
	public AssetImporter(MapReader mapReader, Validator<ExternalModelView> validator,
						 AssetToModelConverter converter, SecurityContext securityContext, SecurityFilter securityFilter) {
		super(AssetView.class, mapReader, validator);
		saver = null;
		this.converter = converter;
		eventScheduleManager = null;
		this.securityContext = securityContext;
		this.securityFilter = securityFilter;
		validator.getValidationContext().put(AssetViewValidator.ASSET_TYPE_KEY, converter.getType());
	}

	/**
	 * Create versions of the eventScheduleManager and AssetSaveService that will use the same transaction
	 * as that provided by the import framework for all db interactions.
	 * @param transaction
	 */
	@Override
	protected void preImport(Transaction transaction) {
		/* Create SecurityContext object without Spring proxy for use in this non Spring thread */
		localSecurityContext = new SecurityContext();
		localSecurityContext.setUserSecurityFilter(new UserSecurityFilter(securityContext.getUserSecurityFilter()));
		localSecurityContext.setTenantSecurityFilter(securityContext.getTenantSecurityFilter());

		if (eventScheduleManager == null)
			eventScheduleManager = new EventScheduleManagerImpl(transaction.getEntityManager());
		if (saver == null)
			saver = new AssetImportService(transaction.getEntityManager(), localSecurityContext);

		/* Set asset type object in converter to one obtained in this transaction */
		converter.setType(new LoaderFactory(securityFilter).createAssetTypeLoader().
				setStandardPostFetches().setId(converter.getType().getId()).load(transaction.getEntityManager()));
	}

	@Override
	protected void importView(Transaction transaction, AssetView view) throws ConversionException {
		Asset asset = converter.toModel(view, transaction);
		if (asset.isNew()) {
			try {
				saver.create(asset);
			}
			catch(SubAssetUniquenessException ex) {
				logger.error("Error in creating asset", ex);
				throw new ConversionException(ex);
			}
			autoScheduleEvents(asset);
		}
		else {
			try {
				saver.updateWithSubassets(asset);
			}
			catch(SubAssetUniquenessException ex) {
				logger.error("Error in updating asset", ex);
				throw new ConversionException(ex);
			}
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
