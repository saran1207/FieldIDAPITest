package com.n4systems.api.conversion.event;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.ViewToModelConverter;
import com.n4systems.api.model.CriteriaResultView;
import com.n4systems.api.model.EventView;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.ComboBoxCriteriaResult;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.DateFieldCriteriaResult;
import com.n4systems.model.Deficiency;
import com.n4systems.model.Event;
import com.n4systems.model.EventBook;
import com.n4systems.model.EventForm;
import com.n4systems.model.EventType;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.OneClickCriteriaResult;
import com.n4systems.model.Recommendation;
import com.n4systems.model.SelectCriteria;
import com.n4systems.model.SelectCriteriaResult;
import com.n4systems.model.SignatureCriteriaResult;
import com.n4systems.model.State;
import com.n4systems.model.StateSet;
import com.n4systems.model.Status;
import com.n4systems.model.TextFieldCriteriaResult;
import com.n4systems.model.UnitOfMeasureCriteriaResult;
import com.n4systems.model.asset.SmartSearchLoader;
import com.n4systems.model.assetstatus.AssetStatusByNameLoader;
import com.n4systems.model.eventbook.EventBookFindOrCreateLoader;
import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.OrgByNameLoader;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserByFullNameLoader;
import com.n4systems.persistence.Transaction;

public class EventToModelConverter implements ViewToModelConverter<Event, EventView> {
	public static final String UNIT_OF_MEASURE_SEPARATOR = "|";
	private static final String UNIT_OF_MEASURE_SEPARATOR_REGEX = "\\|";
	
	private final OrgByNameLoader orgLoader;
	private final SmartSearchLoader assetLoader;
	private final AssetStatusByNameLoader assetStatusLoader;
	private final EventBookFindOrCreateLoader eventBookLoader;
	private final UserByFullNameLoader userLoader;
	
	private EventType type;
	
	public EventToModelConverter(OrgByNameLoader orgLoader, SmartSearchLoader assetLoader, AssetStatusByNameLoader assetStatusLoader, EventBookFindOrCreateLoader eventBookLoader, UserByFullNameLoader userLoader) {
		this.orgLoader = orgLoader;
		this.assetLoader = assetLoader;
		this.assetStatusLoader = assetStatusLoader;
		this.eventBookLoader = eventBookLoader;
		this.userLoader = userLoader;
	}

	@Override
	public Event toModel(EventView view, Transaction transaction) throws ConversionException {
		Event model = new Event();
		
		resolveType(model);
		resolveOwner(view, model, transaction);
		
		model.setAdvancedLocation(Location.onlyFreeformLocation(view.getLocation()));
		model.setDate(view.getDatePerformedAsDate());
		model.setComments(view.getComments());
		
		resolveStatus(view.getStatus(), model);
		resolveAsset(view, model, transaction);
		resolvePrintable(view, model);
		resolvePerformedBy(view, model, transaction);
		resolveEventBook(view, model, transaction);
		
		resolveAssetStatus(view, model, transaction);
		
		resolveCriteriaResults(view, model, transaction);
		
		if (type != null) { 
			model.setEventForm(type.getEventForm());
		}
		
		return model;
	}

	
	private void resolveCriteriaResults(EventView view, final Event event, Transaction transaction) {
		Set<CriteriaResult> results  = new HashSet<CriteriaResult>();
		if (view.getCriteriaResults()==null) {
			return;
		}
		for (final CriteriaResultView criteriaResultView:view.getCriteriaResults()) {		
			final Criteria criteria = getCriteria(type.getEventForm(), criteriaResultView);
			
			CriteriaResultFactory criteriaResultFactory = new CriteriaResultFactory(new CriteriaResultPopulator() {				
				@Override
				public CriteriaResult populate(CriteriaResult criteriaResult) {
					criteriaResult.setDeficiencies(getDeficiencies(criteriaResultView));
					criteriaResult.setRecommendations(getRecommendations(criteriaResultView));
					criteriaResult.setCriteria(criteria);
					criteriaResult.setEvent(event);					
					criteriaResult.setTenant(type.getTenant());
					return criteriaResult;
				}

				@Override public CriteriaResult populate(OneClickCriteriaResult result) {
					Validate.isTrue(criteria.isOneClickCriteria());
					StateSet states = ((OneClickCriteria)criteria).getStates();
					State state = states.getState(criteriaResultView.getResultString());
					Validate.notNull(state, "can't find state " + criteriaResultView.getResultString() + " for criteria " + criteria.getDisplayName() + ".  expected one of " + states.getAvailableStates());
					result.setState(state);	
					return result;
				}

				@Override public CriteriaResult populate(ComboBoxCriteriaResult result) {
					result.setValue(criteriaResultView.getResultString());
					return result;					
				}

				@Override public CriteriaResult populate(DateFieldCriteriaResult result) {
					// TODO DD : should prolly format & validate string here???
					
					result.setValue(criteriaResultView.getResultString());
					return result;
				}

				@Override public CriteriaResult populate(SelectCriteriaResult result) {
					Validate.isTrue(criteria instanceof SelectCriteria);
					List<String> options = ((SelectCriteria)criteria).getOptions();
					final String option = criteriaResultView.getResultString();
					int index = Iterators.indexOf(options.iterator(), new Predicate<String>() {
						@Override public boolean apply(String value) {
							return StringUtils.equalsIgnoreCase(value,option);
						}			
					});							
					Validate.isTrue(index>=0, "can't find option '" + option + "' for criteria " + criteria.getDisplayName());  // this case should have already been handled by validator.  just in case.
					result.setValue(options.get(index));
					return result;					
				}

				@Override public CriteriaResult populate(UnitOfMeasureCriteriaResult result) {
					// recall : value in form of "123|456"   or   "123"  (no secondary specified).
					String[] tokens = criteriaResultView.getResultString().split(UNIT_OF_MEASURE_SEPARATOR_REGEX);
					result.setPrimaryValue(tokens[0]);
					if (tokens.length>1) {
						result.setSecondaryValue(tokens[2]);
					}
					return result;
				}

				@Override public CriteriaResult populate(TextFieldCriteriaResult result) {
					result.setValue(criteriaResultView.getResultString());				
					return result;
				}

				@Override public CriteriaResult populate(SignatureCriteriaResult result) {
					throw new UnsupportedOperationException("Importing signatures is not supported.");
				}
				
			});
			
			CriteriaResult criteriaResult = criteriaResultFactory.createCriteriaResult(criteria.getCriteriaType());
			results.add(criteriaResult);		
		}
		event.setCriteriaResults(results);
	}
	
	public List<Recommendation> getRecommendations(CriteriaResultView criteriaResultView) {
		Recommendation result = new Recommendation();
		result.setText(criteriaResultView.getRecommendationString());
		result.setState(com.n4systems.model.Observation.State.COMMENT);
		return Lists.newArrayList(result);		
	}
	
	public List<Deficiency> getDeficiencies(CriteriaResultView criteriaResultView) {
		Deficiency result = new Deficiency();
		result.setText(criteriaResultView.getDeficiencyString());
		result.setState(com.n4systems.model.Observation.State.COMMENT);
		return Lists.newArrayList(result);		
	}

	private Criteria getCriteria(EventForm eventForm, CriteriaResultView criteriaResultView) {
		for (CriteriaSection section:eventForm.getAvailableSections()) {
			for (Criteria criteria:section.getAvailableCriteria()) {
				if (criteria.getDisplayName().equals(criteriaResultView.getDisplayText())) {
					return criteria;
				}
			}
		}
		throw new IllegalStateException("can't find criteria '" + criteriaResultView.getDisplayText() + "'");
	}

	protected void resolveType(Event model) {
		model.setType(type);
		model.setTenant(type.getTenant());
	}
	
	protected void resolveStatus(String statusName, Event model) {
		String cleanStatus = statusName.toUpperCase();
		
		Status status = Status.NA;
		if (cleanStatus.equals("PASS")) {
			status = Status.PASS;
		} else if (cleanStatus.equals("FAIL")) {
			status = Status.FAIL;
		}
		
		model.setStatus(status);
	}

	protected void resolveAsset(EventView view, Event model, Transaction transaction) {
		// the validator will ensure this returns exactly 1 asset
		Asset asset = assetLoader.setSearchText(view.getIdentifier()).load(transaction).get(0);
		model.setAsset(asset);
	}

	protected void resolvePerformedBy(EventView view, Event model, Transaction transaction) {
		// the validator will ensure this returns exactly 1 user
		User performedBy = userLoader.setFullName(view.getPerformedBy()).load(transaction).get(0);
		model.setPerformedBy(performedBy);
	}

	protected void resolvePrintable(EventView view, Event model) {
		if (view.isPrintable() != null) {
			model.setPrintable(view.isPrintable());
		} else {
			model.setPrintable(type.isPrintable());
		}
	}

	protected void resolveEventBook(EventView view, Event model, Transaction transaction) {
		if (view.getEventBook() != null) {
			eventBookLoader.setName(view.getEventBook());
			eventBookLoader.setOwner(model.getOwner());
			EventBook book = eventBookLoader.load(transaction);
			model.setBook(book);
		}
	}

	protected void resolveAssetStatus(EventView view, Event model, Transaction transaction) {
		if (view.getAssetStatus() != null) {
			AssetStatus status = assetStatusLoader.setName(view.getAssetStatus()).load(transaction);
			model.setAssetStatus(status);
		}
	}

	protected void resolveOwner(EventView view, Event model, Transaction transaction) {
		orgLoader.setOrganizationName(view.getOrganization());
		orgLoader.setCustomerName(view.getCustomer());
		orgLoader.setDivision(view.getDivision());
		
		BaseOrg owner = orgLoader.load(transaction);
		model.setOwner(owner);
	}

	public void setType(EventType type) {
		this.type = type;
	}
	
	public EventType getType() {
		return type;
	}

}
