package com.n4systems.taskscheduling.task;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.mail.MessagingException;


import com.n4systems.exceptions.EmptyReportException;
import com.n4systems.mail.MailManager;
import com.n4systems.model.Asset;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.user.User;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.FilteredIdLoader;
import com.n4systems.persistence.utils.LazyLoadingList;
import com.n4systems.reporting.ProductCertificateReportGenerator;

public class PrintAllProductCertificatesTask extends DownloadTask {
	private final ProductCertificateReportGenerator certGen;
	private final FilteredIdLoader<Asset> productLoader;
	
	private List<Long> productIdList;
	
	public PrintAllProductCertificatesTask(DownloadLink downloadLink, String downloadUrl, ProductCertificateReportGenerator certGen, FilteredIdLoader<Asset> productLoader) {
		super(downloadLink, downloadUrl, "printAllProductCerts");
		this.certGen = certGen;
		this.productLoader = productLoader;
	}
	
	public PrintAllProductCertificatesTask(DownloadLink downloadLink, String downloadUrl) {
		this(downloadLink, downloadUrl, new ProductCertificateReportGenerator(), new FilteredIdLoader<Asset>(downloadLink.getUser().getSecurityFilter(), Asset.class));
	}
	
	@Override
	protected void generateFile(File downloadFile, User user, String downloadName) throws Exception {
		Transaction transaction = null;
		try {
			transaction = PersistenceManager.startTransaction();
						
			List<Asset> products = loadProducts(user, transaction);
			certGen.setUser(user);
			certGen.generate(products, new FileOutputStream(downloadFile), downloadName, transaction);
				
			PersistenceManager.finishTransaction(transaction);
		} catch (Exception e) {
			PersistenceManager.rollbackTransaction(transaction);
			throw e;
		}
	}
	
	@Override
	protected void sendFailureNotification(MailManager mailManager, DownloadLink downloadLink, Exception cause) throws MessagingException {
		// if the failure was caused by an empty report, we send a message.  Otherwise the failure is silent to the end user
		if (cause instanceof EmptyReportException) {
			mailManager.sendMessage(downloadLink.generateMailMessage("We're sorry, your report did not conain any Products with manufacturer certificates."));
		}
	}
	
	private List<Asset> loadProducts(User user, Transaction transaction) {
		return new LazyLoadingList<Asset>(productIdList, productLoader, transaction);
	}

	public void setProductIdList(List<Long> productIdList) {
		this.productIdList = productIdList;
	}
	
}
