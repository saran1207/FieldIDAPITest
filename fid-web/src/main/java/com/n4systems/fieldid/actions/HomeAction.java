package com.n4systems.fieldid.actions;

import java.util.List;

import org.apache.log4j.Logger;

import rfid.web.helper.Constants;

import com.n4systems.ejb.InstructionalVidoeHelper;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.InstructionalVideo;
import com.n4systems.model.Project;
import com.n4systems.model.ui.releasenotes.ReleaseNoteFileSystemRepository;
import com.n4systems.model.ui.releasenotes.ReleaseNoteLoader;
import com.n4systems.model.ui.releasenotes.ReleaseNotes;
import com.n4systems.reporting.PathHandler;
import com.n4systems.services.JobListService;
import com.n4systems.tools.Pager;
import com.n4systems.util.ConfigEntry;

public class HomeAction extends AbstractAction {
	private static final Logger logger = Logger.getLogger(HomeAction.class);
	private static final long serialVersionUID = 1L;

	private JobListService jobListService;
	private List<InstructionalVideo> summary;
	private Pager<Project> myJobs;
	
	private ReleaseNotes currentReleaseNotes;
	private ReleaseNoteLoader loader;

	public HomeAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
		loader = new ReleaseNoteFileSystemRepository(PathHandler.getReleaseNotesPath());
		
	}

	public String execute() {
		jobListService = new JobListService(persistenceManager, getSecurityFilter(), Constants.SUMMARY_SIZE);
		jobListService.setPageNumber(1);
		jobListService.setOrderBy("created");
		return SUCCESS;
	}

	public List<InstructionalVideo> getSummary() {
		if (summary == null) {
			try {
				summary = new InstructionalVidoeHelper(persistenceManager).getSummary();
			} catch (Exception e) {
				logger.error("couldn't load the list of instructionalvideos ", e);
			}
		}
		return summary;
	}

	public Pager<Project> getMyJobs() {
		if (myJobs == null) {
			try {
			myJobs = jobListService.getList(true, true, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} 
		return myJobs;
	}

	public ReleaseNotes getCurrentReleaseNotes() {
		if (currentReleaseNotes == null) {
			currentReleaseNotes = loader.load();
		}
		return currentReleaseNotes;
	}
	
	public String doError() {
		return ERROR;
	}
	
	public String doPageNotFound() {
		return ERROR;
	}
	
	public String doRestricted() {
		return ERROR;
	}
	
	public String getHelpUrl(){
		return getConfigContext().getString(ConfigEntry.HELP_SYSTEM_URL);
	}
}
