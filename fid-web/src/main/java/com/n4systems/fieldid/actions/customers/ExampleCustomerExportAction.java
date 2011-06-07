package com.n4systems.fieldid.actions.customers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.log4j.Logger;

import com.n4systems.api.model.FullExternalOrgView;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exporting.beanutils.ExportMapMarshaler;
import com.n4systems.exporting.io.ExcelMapWriter;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.fieldid.actions.downloaders.AbstractDownloadAction;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.utils.StreamUtils;
import com.n4systems.reporting.PathHandler;

@SuppressWarnings("serial")
public class ExampleCustomerExportAction extends AbstractDownloadAction {
	private Logger logger = Logger.getLogger(ExampleCustomerExportAction.class);
	
	private File exampleFile = PathHandler.getTempFile();
	
	public ExampleCustomerExportAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	protected FullExternalOrgView createExampleCustomer() {
		FullExternalOrgView view = new FullExternalOrgView();
		view.setTypeToCustomer();
		
		view.setName(getText("example.customer.name"));
		view.setCode(getText("example.customer.code"));
		view.setParentOrg(getUser().getOwner().getName());
		view.setContactName(getText("example.customer.contact.name"));
		view.setContactEmail(getText("example.customer.contact.email"));
		view.setStreetAddress(getText("example.customer.addr"));
		view.setCity(getText("example.customer.city"));
		view.setState(getText("example.customer.state"));
		view.setCountry(getText("example.customer.country"));
		view.setZip(getText("example.customer.zip"));
		view.setPhone1(getText("example.customer.phone1"));
		view.setPhone2(getText("example.customer.phone2"));
		view.setFax1(getText("example.customer.fax"));
		view.setNotes(getText("example.customer.notes"));
		
		return view;
	}

	protected FullExternalOrgView createExampleDivision() {
		FullExternalOrgView view = new FullExternalOrgView();
		view.setTypeToDivision();
		
		view.setName(getText("example.division.name"));
		view.setCode(getText("example.division.code"));
		view.setContactName(getText("example.division.contact.name"));
		view.setContactEmail(getText("example.division.contact.email"));
		view.setStreetAddress(getText("example.division.addr"));
		view.setCity(getText("example.division.city"));
		view.setState(getText("example.division.state"));
		view.setCountry(getText("example.division.country"));
		view.setZip(getText("example.division.zip"));
		view.setPhone1(getText("example.division.phone1"));
		view.setPhone2(getText("example.division.phone2"));
		view.setFax1(getText("example.division.fax"));
		view.setNotes(getText("example.division.notes"));
		
		return view;
	}
	
	@Override
	public File getFile() {
		return exampleFile;
	}

	@Override
	public String getFileName() {
		return ContentType.EXCEL.prepareFileName(getText("label.export_file.customer"));
	}

	@Override
	protected boolean initializeDownload() {
		ExportMapMarshaler<FullExternalOrgView> marshaler = new ExportMapMarshaler<FullExternalOrgView>(FullExternalOrgView.class);
		
		MapWriter writer = null;
		try {
			writer = new ExcelMapWriter(new FileOutputStream(exampleFile), getPrimaryOrg().getDateFormat());
			writer.write(marshaler.toBeanMap(createExampleCustomer()));
			writer.write(marshaler.toBeanMap(createExampleDivision()));
		} catch (Exception e) {
			logger.error("Failed generating example customer export", e);
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
