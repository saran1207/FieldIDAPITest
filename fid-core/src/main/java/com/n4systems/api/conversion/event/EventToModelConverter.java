package com.n4systems.api.conversion.event;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.ViewToModelConverter;
import com.n4systems.api.model.CriteriaResultView;
import com.n4systems.api.model.EventView;
import com.n4systems.api.validation.validators.LocationValidator;
import com.n4systems.model.*;
import com.n4systems.model.assetstatus.AssetStatusByNameLoader;
import com.n4systems.model.eventbook.EventBookFindOrCreateLoader;
import com.n4systems.model.eventstatus.EventStatusByNameLoader;
import com.n4systems.model.location.Location;
import com.n4systems.model.location.PredefinedLocationTree;
import com.n4systems.model.location.PredefinedLocationTreeLoader;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.OrgByNameLoader;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.safetynetwork.AssetsByIdOwnerTypeLoader;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserByFullNameLoader;
import com.n4systems.persistence.Transaction;
import org.apache.commons.lang.StringUtils;

import java.util.*;

import static com.google.common.base.Preconditions.*;

public class EventToModelConverter implements ViewToModelConverter<ThingEvent, EventView> {
	public static final String UNIT_OF_MEASURE_SEPARATOR = "|";
	private static final String UNIT_OF_MEASURE_SEPARATOR_REGEX = "\\|";
	
	private final OrgByNameLoader orgLoader;
	private final AssetsByIdOwnerTypeLoader assetLoader;
	private final AssetStatusByNameLoader assetStatusLoader;
    private final EventStatusByNameLoader eventStatusLoader;
	private final EventBookFindOrCreateLoader eventBookLoader;
	private final UserByFullNameLoader userLoader;
    private final PredefinedLocationTreeLoader predefinedLocationTreeLoader;

    private final TimeZone timeZone;
	
	private ThingEventType type;
	
	public EventToModelConverter(OrgByNameLoader orgLoader, AssetsByIdOwnerTypeLoader assetLoader, AssetStatusByNameLoader assetStatusLoader, EventStatusByNameLoader eventStatusLoader, EventBookFindOrCreateLoader eventBookLoader, UserByFullNameLoader userLoader, PredefinedLocationTreeLoader predefinedLocationTreeLoader, ThingEventType type, TimeZone timeZone) {
		this.orgLoader = orgLoader;
		this.assetLoader = assetLoader;
		this.assetStatusLoader = assetStatusLoader;
        this.eventStatusLoader = eventStatusLoader;
        this.eventBookLoader = eventBookLoader;
		this.userLoader = userLoader;
        this.predefinedLocationTreeLoader = predefinedLocationTreeLoader;
		this.type = type;
        this.timeZone = timeZone;
	}

	@Override
	public ThingEvent toModel(EventView view, Transaction transaction) throws ConversionException {
        ThingEvent model = new ThingEvent();
		
		resolveType(model);
		resolveOwner(view, model, transaction);

		resolveLocation(view, model, transaction);
		model.setDate(view.getDatePerformedAsDate());
        model.setDueDate(view.getDueDateAsDate());
        model.setComments(view.getComments());
		
		resolveStatus(view.getStatus(), model);
		resolveAsset(view, model, transaction);
		resolvePrintable(view, model);
		resolvePerformedBy(view, model, transaction);
		resolveEventBook(view, model, transaction);
		
		resolveAssetStatus(view, model, transaction);
        resolveEventStatus(view, model, transaction);
		
		// practically speaking, the type should not be null.  it can happen in test classes tho. 
		if (type != null) { 
			// note : it is imperative that *all* criteria have a result.  
			// we can *not* assume that the view will have all the required data so we 
			//  must iterate through the eventForm and check to see if any haven't been populated afterwards. 
			//  they will be set to default/empty values.		
			resolveCriteriaResults(view, model);     
			model.setEventForm(type.getEventForm());
		}
		
		return model;
	}

    protected void resolveLocation(EventView view, ThingEvent model, Transaction transaction) {
        PrimaryOrg org = model.getOwner().getPrimaryOrg();
        if (org.hasExtendedFeature(ExtendedFeature.AdvancedLocation)) {
            PredefinedLocationTree predefinedLocationTree = predefinedLocationTreeLoader.load(transaction);
            Location location = new LocationValidator().getLocation(new LocationSpecification(view.getLocation()), predefinedLocationTree);
            model.setAdvancedLocation(location);
        } else {
            model.setAdvancedLocation(Location.onlyFreeformLocation(view.getLocation()));
        }
    }

    private void resolveCriteriaResults(EventView view, Event event) {
		Set<CriteriaResult> results  = new HashSet<CriteriaResult>();
		if (type.getEventForm()==null || type.getEventForm().getAvailableSections()==null) {
			return;		// nothing to resolve because there are no criteria. 
		}
		for (CriteriaSection section:type.getEventForm().getAvailableSections()) {
			for (Criteria criteria:section.getAvailableCriteria()) {
				CriteriaResult criteriaResult = getCriteriaResultForCriteria(section, criteria, view);
				criteriaResult.setEvent(event);		
				criteriaResult.setCriteria(criteria);
				criteriaResult.setTenant(type.getTenant());
				results.add(criteriaResult);
			}
		}
		event.setCriteriaResults(results);
	}

	private CriteriaResult getCriteriaResultForCriteria(CriteriaSection section, Criteria criteria, EventView view) {
		for (final CriteriaResultView criteriaResultView:view.getCriteriaResults()) {
			if (isSameSectionAndCriteria(section, criteria, criteriaResultView)) { 
				CriteriaResultFactory criteriaResultFactory = createCriteriaFactory(criteriaResultView, criteria);			
				CriteriaResult criteriaResult = criteriaResultFactory.createCriteriaResult(criteria.getCriteriaType());
				return criteriaResult;
			}
		}
		// not found, create a default one. 
		return new CriteriaResultFactory().createCriteriaResult(criteria.getCriteriaType());
	}

	private boolean isSameSectionAndCriteria(CriteriaSection section, Criteria criteria, CriteriaResultView criteriaResultView) {
		checkArgument(criteriaResultView != null && criteria !=null);
		return StringUtils.equalsIgnoreCase(criteriaResultView.getSection(), section.getDisplayName()) && 
				StringUtils.equalsIgnoreCase(criteriaResultView.getDisplayText(), criteria.getDisplayText());
	}

	private CriteriaResultFactory createCriteriaFactory(final CriteriaResultView criteriaResultView, final Criteria criteria) {
		return new CriteriaResultFactory(new CriteriaResultPopulator() {				
			@Override
			public CriteriaResult populate(CriteriaResult criteriaResult) {
				criteriaResult.setDeficiencies(getDeficiencies(criteriaResultView));
				criteriaResult.setRecommendations(getRecommendations(criteriaResultView));
				return criteriaResult;
			}

			@Override 
			public CriteriaResult populate(OneClickCriteriaResult result) {
				checkArgument(criteria.getCriteriaType() == CriteriaType.ONE_CLICK);
				ButtonGroup buttonGroup = ((OneClickCriteria)criteria).getButtonGroup();

                // Find the default state for a one click if result isn't provided to match web interface behavior
                Button button = null;
                if (StringUtils.isBlank(criteriaResultView.getResultString())) {
                    if (buttonGroup.getAvailableButtons().size() > 0)
                        button = buttonGroup.getAvailableButtons().get(0);
                } else {
                    button = buttonGroup.getButton(criteriaResultView.getResultString());
                }

				checkNotNull(button, "Can't find button " + criteriaResultView.getResultString() + " for criteria " + criteria.getDisplayName() + ".  expected one of " + buttonGroup.getAvailableButtons());
				result.setButton(button);
				return result;
			}

			@Override 
			public CriteriaResult populate(ComboBoxCriteriaResult result) {
				result.setValue(criteriaResultView.getResultString());
				return result;					
			}

			@Override 
			public CriteriaResult populate(DateFieldCriteriaResult result) {
				checkArgument(criteriaResultView.getResult()==null ||  criteriaResultView.getResult() instanceof Date); 
				result.setValue((Date)criteriaResultView.getResult());						
				return result;
			}

			@Override 
			public CriteriaResult populate(SelectCriteriaResult result) {
				checkArgument(criteria instanceof SelectCriteria);
				if (StringUtils.isBlank(criteriaResultView.getResultString())) {
					return result;  // leave it as null...that's a valid value.
				}
				List<String> options = ((SelectCriteria)criteria).getOptions();
				final String option = criteriaResultView.getResultString();				
				int index = Iterators.indexOf(options.iterator(), new Predicate<String>() {
					@Override public boolean apply(String value) {
						return StringUtils.equalsIgnoreCase(value,option);
					}			
				});							
				checkState(index>=0, "can't find option '" + option + "' for criteria " + criteria.getDisplayName());  // this case should have already been handled by validator.  just in case.
				result.setValue(options.get(index));
				return result;					
			}

			@Override 
			public CriteriaResult populate(UnitOfMeasureCriteriaResult result) {
				// recall : value in form of "123|456" =    or   "123"  (<--no secondary specified).
				String[] tokens = criteriaResultView.getResultString().split(UNIT_OF_MEASURE_SEPARATOR_REGEX);					
				checkState(tokens.length<=2, "can't specify more than 2 values in '|' delimited Unit Of Measure field : '" + criteriaResultView.getResultString()); 
				result.setPrimaryValue(tokens.length >= 1 ? tokens[0] : null);					
				result.setSecondaryValue(tokens.length >=2 ? tokens[1] : null);					
				return result;
			}

			@Override 
			public CriteriaResult populate(TextFieldCriteriaResult result) {
				result.setValue(criteriaResultView.getResultString());				
				return result;
			}

			@Override 
			public CriteriaResult populate(SignatureCriteriaResult result) {
				throw new UnsupportedOperationException("Importing signatures is not supported.");
			}

			@Override
			public CriteriaResult populate(NumberFieldCriteriaResult result) {
				String resultString  = criteriaResultView.getResultString();
				if (!StringUtils.isBlank(resultString)) {					
					result.setValue(Double.parseDouble(resultString));
				}
				return result;
			}

            @Override
            public CriteriaResult populate(ScoreCriteriaResult result) {
				checkArgument(criteria.getCriteriaType() == CriteriaType.SCORE);
				ScoreGroup scoreGroup = ((ScoreCriteria)criteria).getScoreGroup();
				Score score = scoreGroup.getScore(criteriaResultView.getResultString());
				checkNotNull(score, "Can't find score " + criteriaResultView.getResultString() + " for criteria " + criteria.getDisplayName() + ".  expected one of " + scoreGroup.getScores());
				result.setScore(score);
				return result;
            }
        });
	} 
	
	public List<Recommendation> getRecommendations(CriteriaResultView criteriaResultView) {
		List<Recommendation> result = new ArrayList<Recommendation>();
		String text = StringUtils.trimToEmpty(criteriaResultView.getRecommendationString());
		if (StringUtils.isNotEmpty(text)) { 
			Recommendation recommendation = new Recommendation();
			recommendation.setText(text);
			recommendation.setState(com.n4systems.model.Observation.State.COMMENT);
			result.add(recommendation);
		} 
		return result;		
	}
	
	public List<Deficiency> getDeficiencies(CriteriaResultView criteriaResultView) {
		List<Deficiency> result = new ArrayList<Deficiency>();
		String text = StringUtils.trimToEmpty(criteriaResultView.getDeficiencyString());
		if (StringUtils.isNotEmpty(text)) { 
			Deficiency deficiency = new Deficiency();
			deficiency.setText(StringUtils.trimToEmpty(criteriaResultView.getDeficiencyString()));
			deficiency.setState(com.n4systems.model.Observation.State.COMMENT);
			result.add(deficiency);
		}
		return result;
	}

	protected void resolveType(ThingEvent model) {
		model.setType(type);
		model.setTenant(type.getTenant());
	}
	
	protected void resolveStatus(String statusName, ThingEvent model) {
		String cleanStatus = statusName.toUpperCase();
		
		EventResult eventResult = EventResult.NA;
		if (cleanStatus.equals("PASS")) {
			eventResult = EventResult.PASS;
		} else if (cleanStatus.equals("FAIL")) {
			eventResult = EventResult.FAIL;
		}
		
		model.setEventResult(eventResult);
	}

	protected void resolveAsset(EventView view, ThingEvent model, Transaction transaction) {
		Asset asset = assetLoader.setIdentifier(view.getIdentifier()).setOwner(view.getOrganization(), view.getCustomer(), view.getDivision()).load(transaction).get(0);
		model.setTarget(asset);
	}

	protected void resolvePerformedBy(EventView view, ThingEvent model, Transaction transaction) {
		// the validator will ensure this returns exactly 1 user
		User performedBy = userLoader.setFullName(view.getPerformedBy()).load(transaction).get(0);
		model.setPerformedBy(performedBy);
	}

	protected void resolvePrintable(EventView view, ThingEvent model) {
		if (view.isPrintable() != null) {
			model.setPrintable(view.isPrintable());
		} else {
			model.setPrintable(type.isPrintable());
		}
	}

	protected void resolveEventBook(EventView view, ThingEvent model, Transaction transaction) {
		if (view.getEventBook() != null) {
			eventBookLoader.setName(view.getEventBook());
			eventBookLoader.setOwner(model.getOwner());
			EventBook book = eventBookLoader.load(transaction);
			model.setBook(book);
		}
	}

	protected void resolveAssetStatus(EventView view, ThingEvent model, Transaction transaction) {
		if (view.getAssetStatus() != null) {
			AssetStatus status = assetStatusLoader.setName(view.getAssetStatus()).load(transaction);
			model.setAssetStatus(status);
		}
	}

    protected void resolveEventStatus(EventView view, Event model, Transaction transaction) {
        if (view.getEventStatus() != null) {
            EventStatus status = eventStatusLoader.setName(view.getEventStatus()).load(transaction);
            model.setEventStatus(status);
        }
    }

	protected void resolveOwner(EventView view, ThingEvent model, Transaction transaction) {
		orgLoader.setOrganizationName(view.getNewOrganization());
		orgLoader.setCustomerName(view.getNewCustomer());
		orgLoader.setDivision(view.getNewDivision());
		
		BaseOrg owner = orgLoader.load(transaction);
        //If New Owner fields are blank set it to the original owner.
        if(owner == null) {
            orgLoader.setOrganizationName(view.getOrganization());
            orgLoader.setCustomerName(view.getCustomer());
            orgLoader.setDivision(view.getDivision());
            owner = orgLoader.load(transaction);
            model.setOwner(owner);
        } else {
		    model.setOwner(owner);
        }
	}
	
	public EventType getType() {
		return type;
	}

    public TimeZone getTimeZone() {
        return timeZone;
    }

}
