package com.n4systems.ejb;

import java.util.List;

import javax.ejb.Local;

import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.AssetAlreadyAttachedException;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.Product;
import com.n4systems.model.Project;
import com.n4systems.tools.Pager;
import com.n4systems.util.SecurityFilter;

@Local
public interface ProjectManager {
	public int attachAsset(Product asset, Project project, Long modifiedBy) throws AssetAlreadyAttachedException;

	public int detachAsset(Product asset, Project project, Long modifiedBy);

	public Pager<Product> getAssetsPaged(Project project, SecurityFilter filter, int page, int pageSize);

	public Pager<InspectionSchedule> getSchedulesPaged(Project project, SecurityFilter filter, int page, int pageSize, List<InspectionSchedule.ScheduleStatus> statuses);
	
	public Long getIncompleteSchedules(Project project, SecurityFilter filter);
	
	public Long getCompleteSchedules(Project project, SecurityFilter filter);

	public FileAttachment attachNote(FileAttachment note, Project project, Long modifiedBy) throws FileAttachmentException;

	public int detachNote(FileAttachment note, Project project, Long modifiedBy) throws FileAttachmentException;

	public Pager<FileAttachment> getNotesPaged(Project project, int page, int pageSize);

	public List<Project> getProjectsForAsset(Product asset, SecurityFilter filter);
	
	public Pager<UserBean> getResourcesPaged(Project project, SecurityFilter filter, int page, int pageSize);
}
