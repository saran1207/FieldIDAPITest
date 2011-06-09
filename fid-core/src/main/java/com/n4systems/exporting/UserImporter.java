package com.n4systems.exporting;

import org.apache.log4j.Logger;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.model.UserView;
import com.n4systems.api.validation.Validator;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.model.user.UserSaver;
import com.n4systems.persistence.Transaction;

public class UserImporter extends AbstractImporter<UserView> {
	private static final Logger logger = Logger.getLogger(UserImporter.class);
	// FIXME DD : define these services later...need a user saver/converter. 
	private final UserSaver saver;
	private final Object converter;
	
	
	public UserImporter(MapReader mapReader, Validator<ExternalModelView> validator, UserSaver saver, Object converter) {
		// FIXME DD : need to create concrete class of ExternalModelView. 
		super(UserView.class, mapReader, validator);
		this.saver = saver;
		this.converter = converter;
	
		// FIXME DD : create a UserViewValidator.
//		validator.getValidationContext().put(AssetViewValidator.ASSET_TYPE_KEY, converter.getType());
	}

	@Override
	protected void importView(Transaction transaction, UserView view) throws ConversionException {
		// FIXME DD : do stuff here.   just doing plumbing for now.
		
//		saver.save(converter.toModel(view, transaction)));
		
		
//		User asset = converter.toModel(view, transaction);
//		saver.setUser(user);
//		saver.createWithoutHistory();
		logger.error("FIXME DD : importView called....not implemented yet");
	}

}
