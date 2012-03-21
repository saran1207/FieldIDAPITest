package com.n4systems.fieldid.service.download;

import com.n4systems.exceptions.ReportException;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.task.AsyncService;
import com.n4systems.mail.MailManager;
import com.n4systems.mail.SMTPMailManager;
import com.n4systems.model.downloadlink.ContentType;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.downloadlink.DownloadLinkFactory;
import com.n4systems.model.downloadlink.DownloadState;
import com.n4systems.model.search.SearchCriteria;
import com.n4systems.util.mail.TemplateMailMessage;
import com.n4systems.util.selection.MultiIdSelection;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

import javax.mail.MessagingException;

public abstract class DownloadService<T extends SearchCriteria> extends FieldIdPersistenceService {

	protected Logger logger = Logger.getLogger(getClass());

	protected MailManager mailManager;

    private final String templateName;
    private final ContentType type;

    @Autowired
    private AsyncService asyncService;

    public DownloadService(String templateName, ContentType type) {
        this.templateName = templateName;
        this.type = type;
        this.mailManager = new SMTPMailManager();
    }

    public DownloadLink startTask(final T criteria, final String linkName, final String downloadUrl) {
        final DownloadLink downloadLink = createDownloadLink(linkName);

        AsyncService.AsyncTask<Void> task = asyncService.createTask(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                run(criteria, downloadUrl, downloadLink);
                return null;
            }
        });

        asyncService.run(task);

        return downloadLink;
    }

    @Transactional
	private void run(T criteria, String downloadUrl, DownloadLink downloadLink) {
		logger.info(String.format("Download Task Started [%s]", downloadLink));

		updateDownloadLinkState(downloadLink, DownloadState.INPROGRESS);

		try {
			generateFile(criteria, downloadLink.getFile(), downloadLink.getName());

			updateDownloadLinkState(downloadLink, DownloadState.COMPLETED);

			try {
				// we don't want exceptions coming from the notification to
				// hit the failure block
				sendSuccessNotification(downloadUrl, mailManager, downloadLink);
			} catch(Exception e) {
				logger.error("Failed to send success notification, the download has not been affected", e);
			}
		} catch(Exception e) {
			logger.error("Failed to generate download", e);

			updateDownloadLinkState(downloadLink, DownloadState.FAILED);
			try {
				sendFailureNotification(mailManager, downloadLink, e);
			} catch(MessagingException me) {
				logger.error("Failed to send failure notification", me);
			}
		}

		logger.info(String.format("Download Task Finished [%s]", downloadLink));
	}

    protected abstract void generateFile(T criteria, File file, String linkName) throws ReportException;

    private void updateDownloadLinkState(DownloadLink downloadLink, DownloadState state) {
		downloadLink.setState(state);
		persistenceService.update(downloadLink);
	}

	protected void sendSuccessNotification(String downloadUrl, MailManager mailManager, DownloadLink downloadLink) throws MessagingException {
		TemplateMailMessage tMail = new TemplateMailMessage(downloadLink.getName(), templateName);
		tMail.getToAddresses().add(downloadLink.getUser().getEmailAddress());
		tMail.getTemplateMap().put("downloadLink", downloadLink);
		tMail.getTemplateMap().put("downloadUrl", downloadUrl + downloadLink.getId());

		mailManager.sendMessage(tMail);
	}

	protected void sendFailureNotification(MailManager mailManager, DownloadLink downloadLink, Exception cause) throws MessagingException {
		// sub classes may override this to send failure notices
	}

    @Transactional
	protected DownloadLink createDownloadLink(String name) {
		DownloadLink link = new DownloadLinkFactory().createDownloadLink(getCurrentUser(), name, type);
		persistenceService.save(link);
		return link;
	}

    protected List<Long> sortSelectionBasedOnIndexIn(MultiIdSelection selection, final List<Long> idList) {
        final List<Long> selectedIds = selection.getSelectedIds();
        Collections.sort(selectedIds, new Comparator<Long>() {
            @Override
            public int compare(Long id1, Long id2) {
                return new Integer(idList.indexOf(id1)).compareTo(idList.indexOf(id2));
            }
        });
        return selectedIds;
    }

}
