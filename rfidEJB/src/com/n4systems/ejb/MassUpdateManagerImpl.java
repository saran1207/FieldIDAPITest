package com.n4systems.ejb;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import rfid.ejb.entity.UserBean;

import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.exceptions.UpdateConatraintViolationException;
import com.n4systems.exceptions.UpdateFailureException;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.Product;
import com.n4systems.model.Project;
import com.n4systems.model.InspectionSchedule.ScheduleStatus;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.services.InspectionScheduleServiceImpl;
import com.n4systems.tools.Pager;
import com.n4systems.util.ReportCriteria;
import com.n4systems.util.SearchCriteria;
import com.n4systems.util.persistence.JoinClause;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SimpleSelect;
import com.n4systems.util.persistence.WhereParameter.Comparator;

@Stateless
public class MassUpdateManagerImpl implements MassUpdateManager {

	@PersistenceContext(unitName = "rfidEM")
	private EntityManager em;

	@EJB
	private PersistenceManager persistenceManager;

	private Long updateInspectionSchedules(Set<Long> scheduleIds, InspectionSchedule inspectionSchedule, Map<String, Boolean> values) throws UpdateFailureException {
		if (scheduleIds.size() == 0) {
			return 0L;
		}

		Long result = 0L;
		String updateJpql;
		Query updateStmt;
		try {
			// this date will be used to updated the modified time of our
			// entities
			Date modDate = new Date();
			updateJpql = "UPDATE " + InspectionSchedule.class.getName() + " SET modified = :modDate";

			Map<String, Object> bindParams = new HashMap<String, Object>();
			// construct our update statement and populate our list of bind
			// parameters
			for (String paramKey : values.keySet()) {
				// check to see if the modify flag was set and skip over if not
				if (!values.get(paramKey)) {
					continue;
				}

				if (paramKey.equals("nextDate")) {
					updateJpql += ", nextDate = :" + paramKey;
					bindParams.put(paramKey, inspectionSchedule.getNextDate());
				}

				if (paramKey.equals("location")) {
					updateJpql += ", location = :" + paramKey;
					bindParams.put(paramKey, inspectionSchedule.getLocation());
				}
				if (paramKey.equals("owner")) {
					
					updateJpql += ", owner = :owner ";
					bindParams.put("owner", inspectionSchedule.getOwner());
					
				}

			}
			updateJpql += " WHERE id IN ( :scheduleIds )";

			// create our update statement
			updateStmt = em.createQuery(updateJpql);

			// bind in our list of ids and param values
			updateStmt.setParameter("scheduleIds", scheduleIds);
			updateStmt.setParameter("modDate", modDate);
			for (Map.Entry<String, Object> entry : bindParams.entrySet()) {
				updateStmt.setParameter(entry.getKey(), entry.getValue());
			}

			// execute the update
			result = new Long(updateStmt.executeUpdate());
			
			//update products as well.
			QueryBuilder<Long> productIdQuery = new QueryBuilder<Long>(InspectionSchedule.class, new OpenSecurityFilter());
			productIdQuery.setSimpleSelect("product.id").addWhere(Comparator.IN, "ids", "id", scheduleIds);
			modifyProudcts(persistenceManager.findAll(productIdQuery));
			
		} catch (Exception e) {
			throw new UpdateFailureException(e);
		}

		return result;
	}

	public Long modifyProudcts(List<Long> ids) {
		if (ids == null || ids.size() == 0) {
			return 0L;
		}
		
		String updateQueryString = "UPDATE " + Product.class.getName() + " SET modified = :now WHERE id IN (:ids)";
		return new Long(em.createQuery(updateQueryString).setParameter("now", new Date()).setParameter("ids", ids).executeUpdate());
	}
	
	public Long updateInspectionSchedules(List<Long> ids, InspectionSchedule inspectionSchedule, Map<String, Boolean> values) throws UpdateFailureException {
		return updateInspectionSchedules(new TreeSet<Long>(ids), inspectionSchedule, values);
	}

	public Long updateProducts(List<Long> ids, Product product, Map<String, Boolean> values, Long userId) throws UpdateFailureException, UpdateConatraintViolationException {
		Long result = 0L;
		try {
			if (ids.size() == 0) {
				return 0L;
			}

			String updateQueryString = "UPDATE " + Product.class.getName() + " SET modified = :now";
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("now", new Date());
			boolean ownershipBeingSet = false;
			
			for (Map.Entry<String, Boolean> entry : values.entrySet()) {
				if (entry.getValue() == true) {
					updateQueryString += ", ";

					if (entry.getKey().equals("owner")) {
						ownershipBeingSet = true;

						updateQueryString += " owner = :owner ";
						parameters.put("owner", product.getOwner());
					}

					if (entry.getKey().equals("assignedUser")) {
						if (product.getAssignedUser() == null) {
							updateQueryString += " assignedUser = null ";
						} else {
							updateQueryString += " assignedUser = :assignedUser ";
							parameters.put("assignedUser", product.getAssignedUser());
						}
					}

					if (entry.getKey().equals("productStatus")) {
						if (product.getProductStatus() == null) {
							updateQueryString += " productStatus = null ";
						} else {
							updateQueryString += " productStatus = :productStatus ";
							parameters.put("productStatus", product.getProductStatus());
						}
					}

					if (entry.getKey().equals("purchaseOrder")) {
						updateQueryString += " purchaseOrder = :purchaseOrder ";
						parameters.put("purchaseOrder", product.getPurchaseOrder());
					}

					if (entry.getKey().equals("location")) {
						ownershipBeingSet = true;
						updateQueryString += " location = :location ";
						parameters.put("location", product.getLocation());
					}

					if (entry.getKey().equals("identified")) {
						updateQueryString += " identified = :identified ";
						parameters.put("identified", product.getIdentified());
					}

				}
			}

			if (product.getModifiedBy() == null) {
				updateQueryString += ", modifiedBy = null ";
			} else {
				updateQueryString += ", modifiedBy = :modifiedBy ";
				parameters.put("modifiedBy", em.find(UserBean.class, userId));
			}
			ids.add(null);
			Query updateQuery = em.createQuery(updateQueryString + " WHERE id IN ( :ids )");
			updateQuery.setParameter("ids", ids);
			for (Map.Entry<String, Object> entry : parameters.entrySet()) {
				updateQuery.setParameter(entry.getKey(), entry.getValue());
			}

			result = new Long(updateQuery.executeUpdate());
			
			if (ownershipBeingSet) {
				QueryBuilder<Long> scheduleIds = new QueryBuilder<Long>(InspectionSchedule.class, new OpenSecurityFilter()).setSimpleSelect("id").addWhere(Comparator.IN, "products", "product.id", ids).addWhere(Comparator.NE, "status","status", ScheduleStatus.COMPLETED);
				Map<String, Boolean> selectedAttributes = getOwnerShipSelectedAttributes(values);
				InspectionSchedule schedule = new InspectionSchedule();
				schedule.setProduct(product);
				updateInspectionSchedules(persistenceManager.findAll(scheduleIds), schedule, selectedAttributes);
			}

		} catch (InvalidQueryException iqe) {
			throw new UpdateFailureException(iqe);
		} catch (EntityExistsException cve) {
			throw new UpdateConatraintViolationException(cve);
		}

		return result;
	}

	public Long updateInspections(List<Long> ids, Inspection inspection, Map<String, Boolean> values, Long userId) throws UpdateFailureException {
		Long result = 0L;
		try {
			if (ids.size() == 0) {
				return 0L;
			}

			String updateQueryString = "UPDATE Inspection SET modified = :now";
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("now", new Date());
			boolean ownershipBeingSet = false;
			
			for (Map.Entry<String, Boolean> entry : values.entrySet()) {
				if (entry.getValue() == true) {
					updateQueryString += ", ";

					if (entry.getKey().equals("owner")) {
						ownershipBeingSet = true;

						updateQueryString += " owner = :owner ";
						parameters.put("owner", inspection.getOwner());
					}
					
					if (entry.getKey().equals("inspectionBook")) {
						if (inspection.getBook() == null) {
							updateQueryString += " book = null ";
						} else {
							updateQueryString += " book = :inspectionBook ";
							parameters.put("inspectionBook", inspection.getBook());
						}
					}

					if (entry.getKey().equals("location")) {
						ownershipBeingSet = true;
						updateQueryString += " location = :location";
						parameters.put("location", inspection.getLocation());
					}

					if (entry.getKey().equals("printable")) {
						updateQueryString += " printable = :printable";
						parameters.put("printable", inspection.isPrintable());
					}
				}
			}

			if (userId == null) {
				updateQueryString += ", modifiedBy = null ";
			} else {
				updateQueryString += ", modifiedBy = :modifiedBy ";
				parameters.put("modifiedBy", em.find(UserBean.class, userId));
			}

			Query updateQuery = em.createQuery(updateQueryString + " WHERE id IN ( :ids )");
			updateQuery.setParameter("ids", ids);
			for (Map.Entry<String, Object> entry : parameters.entrySet()) {
				updateQuery.setParameter(entry.getKey(), entry.getValue());
			}

			updateQuery.executeUpdate();

			result = new Long(ids.size());
			
			if (ownershipBeingSet) {
				QueryBuilder<Long> scheduleIds = new QueryBuilder<Long>(InspectionSchedule.class, new OpenSecurityFilter()).setSimpleSelect("id").addWhere(Comparator.IN, "inspections", "inspection.id", ids).addSimpleWhere("status", ScheduleStatus.COMPLETED);
				Map<String, Boolean> selectedAttributes = getOwnerShipSelectedAttributes(values);
				InspectionSchedule schedule = new InspectionSchedule();
				schedule.completed(inspection);
				updateInspectionSchedules(persistenceManager.findAll(scheduleIds), schedule, selectedAttributes);
			}

		} catch (InvalidQueryException iqe) {
			throw new UpdateFailureException(iqe);
		}

		return result;
	}

	private Map<String, Boolean> getOwnerShipSelectedAttributes(Map<String, Boolean> values) {
		Map<String, Boolean> selectedAttributes = new HashMap<String, Boolean>();
		selectedAttributes.put("owner", (values.get("owner") != null) ? values.get("owner") : false );
		selectedAttributes.put("location", (values.get("location") != null) ? values.get("location") : false);
		return selectedAttributes;
	}

	public Long assignToJob(List<Long> scheduleIds, Project project, Long userId) throws UpdateFailureException, UpdateConatraintViolationException {
		Long result = 0L;
		
		if (scheduleIds == null || scheduleIds.isEmpty()) {
			return 0L;
		}
		
		try {
			String updateQueryString = "UPDATE " + InspectionSchedule.class.getName() + " SET modified = :now ";
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("now", new Date());
			if (project == null) {
				updateQueryString += ", project = null";
			} else {
				updateQueryString += ", project = :project";
				parameters.put("project", project);
			}

			if (userId == null) {
				updateQueryString += ", modifiedBy = null ";
			} else {
				updateQueryString += ", modifiedBy = :modifiedBy ";
				parameters.put("modifiedBy", em.find(UserBean.class, userId));
			}
			updateQueryString += " WHERE id IN (:ids)";
			parameters.put("ids", scheduleIds);

			Query updateQuery = em.createQuery(updateQueryString);
			for (Map.Entry<String, Object> entry : parameters.entrySet()) {
				updateQuery.setParameter(entry.getKey(), entry.getValue());
			}

			updateQuery.executeUpdate();

			result = new Long(scheduleIds.size());
		} catch (InvalidQueryException iqe) {
			throw new UpdateFailureException(iqe);
		}
		return result;
	}

	public List<Long> createSchedulesForInspections(List<Long> inspectionIds, Long userId) throws UpdateFailureException, UpdateConatraintViolationException {
		QueryBuilder<Inspection> query = new QueryBuilder<Inspection>(Inspection.class, new OpenSecurityFilter());
		query.addWhere(Comparator.IN, "ids", "id", inspectionIds).addLeftJoin("schedule", "schedule").addWhere(Comparator.NULL, "scheduleId", "schedule.id", "true").addOrder("id");
		int page = 1;
		int pageSize = 100;

		Pager<Inspection> inspections = null;

		do {
			inspections = persistenceManager.findAllPaged(query, page, pageSize);
			for (Inspection inspection : inspections.getList()) {
				InspectionSchedule schedule = new InspectionSchedule(inspection);
				new InspectionScheduleServiceImpl(persistenceManager).createSchedule(schedule);
			}
			page++;
		} while (inspections.isHasNextPage());

		QueryBuilder<Long> scheduleQuery = new QueryBuilder<Long>(InspectionSchedule.class, new OpenSecurityFilter());
		scheduleQuery.setSimpleSelect("id");
		scheduleQuery.addWhere(Comparator.IN, "ids", "inspection.id", inspectionIds).addOrder("id");
		try {
			return persistenceManager.findAll(scheduleQuery);
		} catch (InvalidQueryException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public Long updateProducts(SearchCriteria searchCriteria, Product product, Map<String, Boolean> values, Long userId) throws UpdateFailureException, UpdateConatraintViolationException {
		return updateProducts((List<Long>) searchCriteria.getIdQueryBuilder().createQuery(em).getResultList(), product, values, userId);
	}

	@SuppressWarnings("unchecked")
	public Long updateInspections(ReportCriteria reportCriteria, Inspection inspection, Map<String, Boolean> values, Long userId) throws UpdateFailureException {
		QueryBuilder<Inspection> builder = reportCriteria.getSearchQueryBuilder();
		builder.setSelectArgument(new SimpleSelect("id"));
		builder.getSelectArgument().setDistinct(true);

		for (JoinClause joinClauses : builder.getJoinArguments()) {
			joinClauses.setType(JoinClause.JoinType.LEFT);
		}
		builder.getOrderArguments().clear();

		Query idQuery = builder.createQuery(em);
		return updateInspections((List<Long>) idQuery.getResultList(), inspection, values, userId);
	}


}
