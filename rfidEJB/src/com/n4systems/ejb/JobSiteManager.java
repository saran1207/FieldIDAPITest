package com.n4systems.ejb;

import javax.ejb.Local;

import com.n4systems.exceptions.UpdateConatraintViolationException;
import com.n4systems.exceptions.UpdateFailureException;
import com.n4systems.model.JobSite;

@Local
public interface JobSiteManager {
	public Long save( JobSite jobSite, Long userId );
	
	public JobSite update( JobSite jobSite, Long userId ) throws UpdateFailureException, UpdateConatraintViolationException;
}
