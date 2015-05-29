package com.n4systems.fieldid.actions.users;

import com.n4systems.api.model.UserView;
import com.n4systems.api.validation.validators.YNValidator.YNField;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exporting.beanutils.ExportMapMarshaller;
import com.n4systems.exporting.io.ExcelXSSFMapWriter;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.fieldid.actions.downloaders.AbstractDownloadAction;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.model.utils.StreamUtils;
import com.n4systems.reporting.PathHandler;
import com.n4systems.security.PasswordComplexityChecker;
import com.n4systems.security.UserType;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@SuppressWarnings("serial")
public class ExampleUserExportAction extends AbstractDownloadAction {
	private final Logger logger = Logger.getLogger(ExampleUserExportAction.class);
	
	private final File exampleFile = PathHandler.getTempFile();
	
	public ExampleUserExportAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	protected UserView createExampleUser() {
		UserView view = new UserView();
		String Y = YNField.Y.toString();
		view.setEmailAddress(getText("example.customer.contact.email"));
		view.setFirstName(getText("example.customer.first.name"));
		view.setLastName(getText("example.customer.last.name"));
		view.setOrganization(getCurrentUser().getOwner().getName());
		view.setAssignPassword(Y);
		view.setPassword(PasswordComplexityChecker.createDefault().generateValidPassword());
		view.setUserID(getCurrentUser().getUserID());
		view.setSendWelcomeEmail(Y);
		view.setIdentifyAssets(Y);
		view.setEditEvents(Y);
		view.setCreateEvents(Y);
		view.setAccountType(UserType.FULL.getLabel());
		return view;
	}
	
	@Override
	public File getFile() {
		return exampleFile;
	}

	@Override
	public String getFileName() {
		return ContentType.EXCEL.prepareFileName(getText("label.export_file.user"));
	}

	@Override
	protected boolean initializeDownload() {
		ExportMapMarshaller<UserView> marshaler = new ExportMapMarshaller<>(UserView.class);
		
		MapWriter writer = null;
		try {
			writer = new ExcelXSSFMapWriter(new DateTimeDefiner(getCurrentUser()));
			writer.write(marshaler.toBeanMap(createExampleUser()));
			((ExcelXSSFMapWriter)writer).writeToStream(new FileOutputStream(exampleFile));
		} catch (Exception e) {
			logger.error("Failed generating example user export", e);
			return false;
		} finally {
			StreamUtils.close(writer);
		}
		
		return true;
	}

	@Override
	protected String onFileNotFoundException(FileNotFoundException e) {
		logger.error("Cannot find generated customer export file", e);
		return MISSING;
	}

}
