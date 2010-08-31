package com.n4systems.taskscheduling.task;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.n4systems.model.downloadlink.AllDownloadLinksByDateLoader;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.downloadlink.DownloadLinkSaver;
import com.n4systems.taskscheduling.ScheduledTask;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.DateHelper;

public class DownloadCleanupTask extends ScheduledTask {
	private final AllDownloadLinksByDateLoader downloadsLoader;
	private final DownloadLinkSaver downloadSaver;
	
	public DownloadCleanupTask(AllDownloadLinksByDateLoader downloadsLoader, DownloadLinkSaver downloadSaver) {
		super(15, TimeUnit.MINUTES);
		this.downloadsLoader = downloadsLoader;
		this.downloadSaver = downloadSaver;
	}

	public DownloadCleanupTask() {
		this(new AllDownloadLinksByDateLoader(), new DownloadLinkSaver());
	}
	
	@Override
	protected void runTask() throws Exception {
		Integer expireTTL = ConfigContext.getCurrentContext().getInteger(ConfigEntry.DOWNLOAD_TTL_DAYS);
		
		Date olderThanDate = DateHelper.increment(DateHelper.getToday(), DateHelper.DAY, expireTTL * -1);
		
		List<DownloadLink> downloads = downloadsLoader.setOlderThanDate(olderThanDate).load();
		
		for (DownloadLink download: downloads) {
			try {
				download.getFile().delete();
				downloadSaver.remove(download);
			} catch(Exception e) {
				logger.warn(String.format("Could not remove old download link [%s]", download.toString()), e);
			}
		}
	}

}
