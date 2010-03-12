package com.n4systems.taskscheduling.task;

import com.n4systems.ejb.MailManager;
import com.n4systems.exporting.Exporter;
import com.n4systems.exporting.ProductExporter;
import com.n4systems.exporting.io.MapWriterFactory;
import com.n4systems.model.Product;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.downloadlink.DownloadLinkSaver;
import com.n4systems.persistence.loaders.ListLoader;

public class ProductExportTask extends AbstractExportTask {
	private static final String TEMPLATE_NAME = "productExport";

	public ProductExportTask(DownloadLink downloadLink, String downloadUrl, ListLoader<Product> productLoader) {
		super(downloadLink, downloadUrl, TEMPLATE_NAME, new ProductExporter(productLoader));
	}

	public ProductExportTask(DownloadLink downloadLink, String downloadUrl, DownloadLinkSaver linkSaver, MailManager mailManager, MapWriterFactory writerFactory, Exporter exporter) {
		super(downloadLink, downloadUrl, TEMPLATE_NAME, linkSaver, mailManager, writerFactory, exporter);
	}

}
