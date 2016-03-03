package com.fieldid.data.service;


import com.n4systems.model.PrintOut;
import com.n4systems.model.Tenant;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class PrintOutMigrator extends DataMigrator<PrintOut> {

	public PrintOutMigrator() {
		super(PrintOut.class);
	}

	@Override
	@Transactional
	protected PrintOut copy(PrintOut srcPrintOut, Tenant newTenant, String newName) {
		PrintOut dstPrintOut = new PrintOut();
		dstPrintOut.setTenant(newTenant);
		dstPrintOut.setName(newName);
		dstPrintOut.setDescription(srcPrintOut.getDescription());
		dstPrintOut.setCustom(srcPrintOut.isCustom());
		dstPrintOut.setPdfTemplate(srcPrintOut.getPdfTemplate());
		dstPrintOut.setType(srcPrintOut.getType());
		dstPrintOut.setWithSubEvents(srcPrintOut.isWithSubEvents());
		persistenceService.save(dstPrintOut);
		return dstPrintOut;
	}

}