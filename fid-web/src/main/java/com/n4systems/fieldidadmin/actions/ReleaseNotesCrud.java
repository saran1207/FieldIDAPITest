package com.n4systems.fieldidadmin.actions;

import com.n4systems.model.ui.releasenotes.ReleaseNoteFileSystemRepository;
import com.n4systems.model.ui.releasenotes.ReleaseNotes;
import com.n4systems.reporting.PathHandler;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class ReleaseNotesCrud extends AbstractAdminAction implements ModelDriven<ReleaseNotes>, Preparable {

	private ReleaseNotesDecorator releaseNotes;
	private ReleaseNoteFileSystemRepository releaseNoteRepository;
	

	public ReleaseNotesCrud() {
		releaseNoteRepository = new ReleaseNoteFileSystemRepository(PathHandler.getReleaseNotesPath());
	}

	public void prepare() throws Exception {
		releaseNotes = new ReleaseNotesDecorator(read());
	}
	
	
	public ReleaseNotes getModel() {
		return releaseNotes;
	}
	
	
	public String doShow() {
		return SUCCESS;
	}
	
	public String doEdit() {
		return SUCCESS;
	}
	
	
	public String doUpdate() {
		releaseNotes.cleanInputs();
		try {
			write();
		} catch (Exception e) {
			addActionError("Could not write to the release note file");
			return ERROR;
		}
		addActionMessage("updated");
		return SUCCESS;
	}
	
	
	
	private void write()  {
		releaseNoteRepository.save(releaseNotes.getDelegate());
	}

	private ReleaseNotes read() {
		return releaseNoteRepository.load();
	}
	

}
