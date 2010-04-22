package com.n4systems.fieldid.actions.projects;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.EmployeeAlreadyAttachedException;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.exceptions.NonEmployeeUserException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.Project;
import com.n4systems.model.user.UserListableLoader;
import com.n4systems.security.Permissions;
import com.n4systems.services.JobResourceService;
import com.n4systems.tools.Pager;
import com.n4systems.tools.SillyPager;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;

@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.Projects)
@UserPermissionFilter(userRequiresOneOf={Permissions.ManageJobs})
public class JobResourcesCrud extends AbstractCrud {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(JobResourcesCrud.class);
	
	private UserBean employee;
	private Project job;
	private Pager<UserBean> resources;
	private List<ListingPair> employees;
	
	
	public JobResourcesCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}
	
	@Override
	protected void initMemberFields() {
		employee = new UserBean();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		employee = persistenceManager.findLegacy(UserBean.class, uniqueId, getSecurityFilter());
	}
	
	private void testRequiredEntities(boolean employeeNeeded) {
		if (job == null) {
			addActionErrorText("error.noproject");
			throw new MissingEntityException();
		}
		
		if (employeeNeeded && (employee == null || !employee.isEmployee() || employee.getId() == null)) {
			addActionErrorText("error.noemployee");
			throw new MissingEntityException();
		}
	}

	@SkipValidation
	public String doList() {
		return SUCCESS;
	}

	@SkipValidation
	public String doAdd() {
		testRequiredEntities(false);
		return SUCCESS;
	}

	public String doCreate() {
		testRequiredEntities(true);
		JobResourceService service = new JobResourceService(job, persistenceManager, fetchCurrentUser());
		
		try { 
			service.attach(employee);
			addFlashMessageText("message.employeeassignedtojob");
			return SUCCESS;
		} catch (NonEmployeeUserException e) {
			addActionErrorText("error.onlyemployeescanbeassignedtojob");
		} catch (EmployeeAlreadyAttachedException e) {
			addActionErrorText("error.employeealreadyassignedtojob");
		} catch (Exception e) {
			logger.error("could not attach employee to job", e);
			addActionErrorText("error.assigningemployeetojob");
		}
		return ERROR;
	}

	
	@SkipValidation
	public String doDelete() {
		testRequiredEntities(true);
		JobResourceService service = new JobResourceService(job, persistenceManager, fetchCurrentUser());
		
		try { 
			service.dettach(employee);
			addActionMessageText("message.employee_removed_from_job");
			return SUCCESS;
		} catch (NonEmployeeUserException e) {
			addActionErrorText("error.only_employees_can_be_assigned_from_job");
		} catch (Exception e) {
			logger.error("could not attach employee to job", e);
			addActionErrorText("error.removing_employee_from_job");
		}
		return ERROR;
	}

	public List<ListingPair> getEmployees() {
		if (employees == null) {
			UserListableLoader loader = createUserListLoader();
			loader.setNoDeleted(true);
			employees = ListHelper.longListableToListingPair(loader.load());
			
			List<ListingPair> assignedEmployees = ListHelper.longListableToListingPair(getPage().getList());
			employees.removeAll(assignedEmployees);
		}
		return employees;
	}

	protected UserListableLoader createUserListLoader() {
		UserListableLoader loader = getLoaderFactory().createUserListableLoader();
		return loader;
	}

	public Long getJobId() {
		return (job != null) ? job.getId() : null;
	}
	
	public Project getProject() {
		return getJob();
	}
	public Project getJob() {
		return job;
	}

	public void setJobId(Long jobId) {
		if (jobId == null) {
			job = null;
		} else if (job == null || !jobId.equals(job.getId())) {
			job = persistenceManager.find(Project.class, jobId, getSecurityFilter());
		}
	}

	public UserBean getEmployee() {
		return employee;
	}

	public Pager<UserBean> getPage() {
		if (resources == null) {
			persistenceManager.reattchAndFetch(job, "resources");
			resources = new SillyPager<UserBean>(job.getResources());
		}
		return resources;
	}
	
	public List<UserBean> getResources() {
		return getPage().getList();
	
	}

}
