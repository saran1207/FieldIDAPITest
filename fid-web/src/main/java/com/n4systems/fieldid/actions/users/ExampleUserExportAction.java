package com.n4systems.fieldid.actions.users;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.log4j.Logger;

import com.n4systems.api.model.UserView;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exporting.beanutils.ExportMapMarshaler;
import com.n4systems.exporting.io.ExcelMapWriter;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.fieldid.actions.downloaders.AbstractDownloadAction;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.utils.StreamUtils;
import com.n4systems.reporting.PathHandler;

@SuppressWarnings("serial")
public class ExampleUserExportAction extends AbstractDownloadAction {
	private final Logger logger = Logger.getLogger(ExampleUserExportAction.class);
	
	private final File exampleFile = PathHandler.getTempFile();
	
	public ExampleUserExportAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	protected UserView createExampleUser() {
		UserView view = new UserView();
		view.setContactEmail(getText("example.customer.contact.email"));
		view.setFirstName(getText("example.customer.first.name"));
		view.setLastName(getText("example.customer.last.name"));
		view.setOrganization(getUser().getOwner().getName());
		view.setUserName(getUser().getUserLabel());  // FIXME DD : which field should this be? ask matt/mark 
		
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
		ExportMapMarshaler<UserView> marshaler = new ExportMapMarshaler<UserView>(UserView.class);
		
		MapWriter writer = null;
		try {
			writer = new ExcelMapWriter(new FileOutputStream(exampleFile), getPrimaryOrg().getDateFormat());
			writer.write(marshaler.toBeanMap(createExampleUser()));
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
