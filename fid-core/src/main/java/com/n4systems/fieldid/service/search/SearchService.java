package com.n4systems.fieldid.service.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.persistence.search.SortTerm;
import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.BaseEntity;
import com.n4systems.model.Event;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.search.ColumnMappingView;
import com.n4systems.model.search.EventReportCriteriaModel;
import com.n4systems.model.security.OwnerAndDownFilter;
import com.n4systems.util.StringUtils;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.QueryFilter;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.search.JoinTerm;
import com.n4systems.util.persistence.search.terms.DateRangeTerm;
import com.n4systems.util.persistence.search.terms.NullTerm;
import com.n4systems.util.persistence.search.terms.SearchTermDefiner;
import com.n4systems.util.persistence.search.terms.SimpleTerm;
import com.n4systems.util.persistence.search.terms.WildcardTerm;

public class SearchService extends FieldIdPersistenceService {

    @Transactional(readOnly = true)
    public SearchResult<Event> performEventSearch(EventReportCriteriaModel criteriaModel, Integer pageNumber, Integer pageSize) {
		// create our base query builder (no sort terms yet)
		QueryBuilder<?> searchBuilder = createBaseSearchQueryBuilder(criteriaModel);

		// get/set the total result count now before the sort terms get added
		int totalResultCount = findCount(searchBuilder).intValue();

		// now we can add in our sort terms
		addSortTermsToBuilder(searchBuilder, criteriaModel);

        SearchResult<Event> searchResult = new SearchResult<Event>();
        searchResult.setTotalResultCount(totalResultCount);
        searchResult.setResults((List<Event>) persistenceService.findAll(searchBuilder.setSimpleSelect(), pageNumber, pageSize));
        return searchResult;
    }

    private Long findCount(QueryBuilder<?> searchBuilder) {
        return persistenceService.count(searchBuilder);
    }

    private QueryBuilder<?> createBaseSearchQueryBuilder(EventReportCriteriaModel criteriaModel) {
		// create our QueryBuilder, note the type will be the same as our selectClass
		QueryBuilder<Event> searchBuilder = createUserSecurityBuilder(Event.class);

        ColumnMappingView sortColumn = criteriaModel.getSortColumn();
        if (sortColumn.getJoinExpression() != null) {
            JoinTerm joinTerm = new JoinTerm(JoinTerm.JoinTermType.LEFT, sortColumn.getJoinExpression(), JoinTerm.DEFAULT_SORT_JOIN_ALIAS, true);
            searchBuilder.addJoin(joinTerm.toJoinClause());
        }

        List<SearchTermDefiner> searchTerms = new ArrayList<SearchTermDefiner>();

        addSearchTerms(criteriaModel, searchTerms);

		// convert all our search terms to where parameters
        for (SearchTermDefiner term : searchTerms) {
            for (WhereClause<?> param: term.getWhereParameters()) {
                searchBuilder.addWhere(param);
            }
        }

        List<QueryFilter> searchFilters = new ArrayList<QueryFilter>();

        addOwnerFilter(searchFilters, criteriaModel.getOwner());

        for (QueryFilter filter : searchFilters) {
            filter.applyFilter(searchBuilder);
        }

		return searchBuilder;
    }

    private void addSearchTerms(EventReportCriteriaModel criteriaModel, List<SearchTermDefiner> searchTerms) {
        addWildcardOrStringTerm(searchTerms, "asset.rfidNumber", criteriaModel.getRfidNumber());
        addWildcardOrStringTerm(searchTerms, "asset.identifier", criteriaModel.getIdentifier());
        addWildcardOrStringTerm(searchTerms, "asset.shopOrder.order.orderNumber", criteriaModel.getOrderNumber());
        addWildcardOrStringTerm(searchTerms, "asset.purchaseOrder", criteriaModel.getPurchaseOrder());
        addWildcardOrStringTerm(searchTerms, "asset.customerRefNumber", criteriaModel.getReferenceNumber());
        addWildcardOrStringTerm(searchTerms, "advancedLocation.freeformLocation", criteriaModel.getLocation().getFreeformLocation());
        addSimpleTerm(searchTerms, "asset.type.id", getId(criteriaModel.getAssetType()));
        addSimpleTerm(searchTerms, "asset.type.group.id", getId(criteriaModel.getAssetTypeGroup()));
        addSimpleTerm(searchTerms, "assetStatus.id", getId(criteriaModel.getAssetStatus()));
        addSimpleTerm(searchTerms, "type.id", getId(criteriaModel.getEventType()));
        addSimpleTerm(searchTerms, "type.group.id", getId(criteriaModel.getEventTypeGroup()));
        addSimpleTerm(searchTerms, "performedBy.id", getId(criteriaModel.getPerformedBy()));
        addSimpleTerm(searchTerms, "schedule.project.id", getId(criteriaModel.getJob()));
        addSimpleTerm(searchTerms, "status", criteriaModel.getResult());
        addDateRangeTerm(searchTerms, "date", criteriaModel.getFromDate(), criteriaModel.getToDate());

        if(criteriaModel.getEventBook() != null && criteriaModel.getEventBook().getId() == 0) {
            addNullTerm(searchTerms, "book.id");
        } else {
            addSimpleTerm(searchTerms, "book.id", getId(criteriaModel.getEventBook()));
        }

        Long assignedUserId = getId(criteriaModel.getAssignedTo());
        if (assignedUserId != null) {
            addSimpleTerm(searchTerms, "assignedTo.assignmentApplyed", true);

            if(assignedUserId == 0) {
                addNullTerm(searchTerms, "assignedTo.assignedUser.id");
            } else {
                addSimpleTerm(searchTerms, "assignedTo.assignedUser.id", assignedUserId);
            }
        }
    }

    private void addSortTermsToBuilder(QueryBuilder<?> searchBuilder, EventReportCriteriaModel criteriaModel) {
        ColumnMappingView sortColumn = criteriaModel.getSortColumn();
        SortDirection sortDirection = criteriaModel.getSortDirection();
        if (sortColumn != null) {
            if (sortColumn.getJoinExpression() == null) {
                searchBuilder.getOrderArguments().add(new SortTerm(sortColumn.getSortExpression().replaceAll("\\{.*\\}", ""), sortDirection).toSortField());
            } else {
                String pathExpression = sortColumn.getPathExpression();

                // This is working around an issue caused when we upgraded hibernate versions. It requires us
                // to join on a column we want to sort by when we're looking at a property Y some entity X, when
                // X may be null (eg the name of an Event Book, event.book.name). If this join is omitted, events with a null event
                // book will be excluded from our results.
                SortTerm sortTerm = new SortTerm(JoinTerm.DEFAULT_SORT_JOIN_ALIAS, sortDirection);
                sortTerm.setAlwaysDropAlias(true);
                sortTerm.setFieldAfterAlias(pathExpression.substring(pathExpression.lastIndexOf(".") + 1));
                searchBuilder.getOrderArguments().add(sortTerm.toSortField());
            }
        }

        searchBuilder.getOrderArguments().add(new SortTerm("id", sortDirection).toSortField());
    }

	protected void addOwnerFilter(List<QueryFilter> searchFilters, BaseOrg owner) {
		if (owner != null) {
			searchFilters.add(new OwnerAndDownFilter(owner));
		}
	}

    private void addNullTerm(List<SearchTermDefiner> searchTerms, String field) {
        searchTerms.add(new NullTerm(field));
    }

    private void addDateRangeTerm(List<SearchTermDefiner> terms, String field, Date fromDate, Date toDate) {
        if (fromDate != null || toDate != null) {
            terms.add(new DateRangeTerm(field, fromDate, toDate));
        }
    }

    protected void addWildcardOrStringTerm(List<SearchTermDefiner> terms, String field, String value) {
        String valueString = StringUtils.clean(value);

        if (valueString != null && !"*".equals(value)) {
            if (isWildcard(valueString)) {
                terms.add(new WildcardTerm(field, valueString));
            } else {
                addSimpleTerm(terms, field, valueString);
            }

        }
    }

    protected <T extends BaseEntity> Long getId(T entity) {
        if (entity == null) {
            return null;
        }
        return entity.getId();
    }

    protected <T> void addSimpleTerm(List<SearchTermDefiner> terms, String field, T value) {
        if (value != null) {
            terms.add(new SimpleTerm<T>(field, value));
        }
    }

	protected void addRequiredLeftJoin(List<JoinTerm> joinTerms, String path, String alias) {
		joinTerms.add(new JoinTerm(JoinTerm.JoinTermType.LEFT, path, alias, true));
	}

    protected boolean isWildcard(String value) {
        return value.startsWith("*") || value.endsWith("*");
    }

    @Transactional(readOnly = true)
	public List<Long> idSearch(EventReportCriteriaModel criteria) {
		// construct a search QueryBuilder with Long as the select class since we will force simple select to be "id" later
		QueryBuilder<?> idBuilder = createBaseSearchQueryBuilder(criteria);

		addSortTermsToBuilder(idBuilder, criteria);

		// note that this will fail for entities not implementing BaseEntity (unless you get lucky)
		return (List<Long>) persistenceService.findAll(idBuilder.setSimpleSelect("id"));
	}

    @Transactional(readOnly = true)
	public Integer countPages(EventReportCriteriaModel criteriaModel, Long pageSize) {
		QueryBuilder<?> countBuilder = createBaseSearchQueryBuilder(criteriaModel);

		Long count = (Long) persistenceService.find(countBuilder.setCountSelect());

		return (int)Math.ceil(count.doubleValue() / pageSize.doubleValue());
	}

}
