package com.n4systems.ejb;

import java.util.List;

import com.n4systems.exceptions.AssetAlreadyAttachedException;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.model.*;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.tools.Pager;

public interface ProjectManager {
	public int attachAsset(Asset asset, Project project, Long modifiedBy) throws AssetAlreadyAttachedException;

	public int detachAsset(Asset asset, Project project, Long modifiedBy);

	public Pager<Asset> getAssetsPaged(Project project, SecurityFilter filter, int page, int pageSize);

	public Pager<Event> getSchedulesPaged(Project project, SecurityFilter filter, int page, int pageSize, List<Event.EventState> statuses);
	
	public Long getIncompleteSchedules(Project project, SecurityFilter filter);
	
	public Long getCompleteSchedules(Project project, SecurityFilter filter);

	public FileAttachment attachNote(FileAttachment note, Project project, Long modifiedBy) throws FileAttachmentException;

	public int detachNote(FileAttachment note, Project project, Long modifiedBy) throws FileAttachmentException;

	public Pager<FileAttachment> getNotesPaged(Project project, int page, int pageSize);

	public List<Project> getProjectsForAsset(Asset asset, SecurityFilter filter);
	
}
