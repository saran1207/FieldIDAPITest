package com.fieldid.data.service;

import com.n4systems.model.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.n4systems.util.FunctionalUtils.*;

@Component
public class EventTypeMigrator extends DataMigrator<EventType> {
	private static Logger logger = Logger.getLogger(EventTypeMigrator.class);

	public EventTypeMigrator() {
		super(EventType.class);
	}

	@Override
	@Transactional
	protected EventType copy(EventType srcEventType, Tenant newTenant, String newEventTypeName) {
		logger.info("Coping EventType [" + srcEventType.getTenant() + ": " + srcEventType + "] to [" + newTenant + ": " + newEventTypeName + "]");
		EventType<?> dstEventType;
		if (srcEventType instanceof ActionEventType) {
			dstEventType = new ActionEventType();
		} else if (srcEventType instanceof PlaceEventType) {
			dstEventType = new PlaceEventType();
		} else if (srcEventType instanceof ProcedureAuditEventType) {
			dstEventType = new ProcedureAuditEventType();
		} else if (srcEventType instanceof ThingEventType) {
			dstEventType = new ThingEventType();
			((ThingEventType) dstEventType).setMaster(((ThingEventType) srcEventType).isMaster());
			((ThingEventType) dstEventType).getSupportedProofTests().addAll(((ThingEventType) srcEventType).getSupportedProofTests());
		} else {
			throw new IllegalArgumentException("Unhandled EventType: " + srcEventType.getClass());
		}
		dstEventType.setTenant(newTenant);
		dstEventType.setGroup(findByName(EventTypeGroup.class, srcEventType.getGroup().getName(), newTenant));
		dstEventType.setName(newEventTypeName);
		dstEventType.setDescription(srcEventType.getDescription());
		dstEventType.setPrintable(srcEventType.isPrintable());
		dstEventType.setAssignedToAvailable(srcEventType.isAssignedToAvailable());
		dstEventType.getInfoFieldNames().addAll(srcEventType.getInfoFieldNames());
		dstEventType.setFormVersion(srcEventType.getFormVersion());
		dstEventType.setDisplayScoreSectionTotals(srcEventType.isDisplayScoreSectionTotals());
		dstEventType.setDisplayScorePercentage(srcEventType.isDisplayScorePercentage());
		dstEventType.setDisplayObservationSectionTotals(srcEventType.isDisplayObservationSectionTotals());
		dstEventType.setDisplayObservationPercentage(srcEventType.isDisplayObservationPercentage());
		dstEventType.setEventForm(
			ifNotNull(srcEventType.getEventForm(), bind(this::migrateEventForm, newTenant)).orElse(null)
		);
		persistenceService.save(dstEventType);
		return dstEventType;
	}

	private EventForm migrateEventForm(Tenant newTenant, EventForm srcForm) {
		EventForm dstForm = new EventForm();
		dstForm.setTenant(newTenant);
		ifNotNull(srcForm.getObservationCountGroup(), countGroup -> {
			ObservationCountGroup newCountGroup = findByName(ObservationCountGroup.class, countGroup.getName(), newTenant);
			dstForm.setObservationCountGroup(newCountGroup);

			BiConsumer<Supplier<ObservationCount>, Consumer<ObservationCount>> observationCountResolver = (countGetter, countSetter) ->
				ifNotNull(countGetter.get(), count -> {
					countSetter.accept(
						find(newCountGroup.getObservationCounts(), ObservationCount::getName, count.getName())
							.orElseThrow(() -> new IllegalStateException("Unable to find ObservationCount for name: '" + count.getName() + "' under ObservationCountGroup: " + newCountGroup))
					);
				});

			observationCountResolver.accept(srcForm::getObservationCountPass, dstForm::setObservationCountPass);
			observationCountResolver.accept(srcForm::getObservationCountFail, dstForm::setObservationCountFail);
		});
		dstForm.getSections().addAll(map(srcForm.getAvailableSections(), bind(this::migrateCriteriaSection, newTenant)));
		persistenceService.save(dstForm);
		return dstForm;
	}

	private CriteriaSection migrateCriteriaSection(Tenant newTenant, CriteriaSection srcSection) {
		CriteriaSection dstSection = new CriteriaSection();
		dstSection.setTenant(newTenant);
		dstSection.setTitle(srcSection.getTitle());
		dstSection.setRetired(srcSection.isRetired());
		dstSection.setOptional(srcSection.isOptional());
		dstSection.setRequired(srcSection.isRequired());
		dstSection.getCriteria().addAll(map(srcSection.getCriteria(), bind(this::migrateCriteria, newTenant)));
		return dstSection;
	}

	private Criteria migrateCriteria(Tenant newTenant, Criteria srcCriteria) {
		Criteria dstCriteria;
		switch (srcCriteria.getCriteriaType()) {
			case COMBO_BOX:
				dstCriteria = new ComboBoxCriteria();
				((ComboBoxCriteria) dstCriteria).getOptions().addAll(((ComboBoxCriteria) srcCriteria).getOptions());
				break;
			case DATE_FIELD:
				dstCriteria = new DateFieldCriteria();
				((DateFieldCriteria) dstCriteria).setIncludeTime(((DateFieldCriteria) srcCriteria).isIncludeTime());
				break;
			case NUMBER_FIELD:
				dstCriteria = new NumberFieldCriteria();
				((NumberFieldCriteria) dstCriteria).setDecimalPlaces(((NumberFieldCriteria) srcCriteria).getDecimalPlaces());
				break;
			case OBSERVATION_COUNT:
				dstCriteria = new ObservationCountCriteria();
				((ObservationCountCriteria) dstCriteria).setObservationCountGroup(findByName(ObservationCountGroup.class, ((ObservationCountCriteria) srcCriteria).getObservationCountGroup().getName(), newTenant));
				break;
			case ONE_CLICK:
				dstCriteria = new OneClickCriteria();
				((OneClickCriteria) dstCriteria).setPrincipal(((OneClickCriteria) srcCriteria).isPrincipal());
				((OneClickCriteria) dstCriteria).setButtonGroup(findByName(ButtonGroup.class, ((OneClickCriteria) srcCriteria).getButtonGroup().getName(), newTenant));
				break;
			case SCORE:
				dstCriteria = new ScoreCriteria();
				((ScoreCriteria) dstCriteria).setScoreGroup(findByName(ScoreGroup.class, ((ScoreCriteria) srcCriteria).getScoreGroup().getName(), newTenant));
				break;
			case SELECT:
				dstCriteria = new SelectCriteria();
				((SelectCriteria) dstCriteria).getOptions().addAll(((SelectCriteria) srcCriteria).getOptions());
				break;
			case SIGNATURE:
				dstCriteria = new SignatureCriteria();
				break;
			case TEXT_FIELD:
				dstCriteria = new TextFieldCriteria();
				break;
			case UNIT_OF_MEASURE:
				dstCriteria = new UnitOfMeasureCriteria();
				((UnitOfMeasureCriteria) dstCriteria).setPrimaryUnit(((UnitOfMeasureCriteria) srcCriteria).getPrimaryUnit());
				((UnitOfMeasureCriteria) dstCriteria).setSecondaryUnit(((UnitOfMeasureCriteria) srcCriteria).getSecondaryUnit());
				break;
			default:
				throw new IllegalArgumentException("Unhandled EventType: " + srcCriteria.getClass());
		}
		dstCriteria.setTenant(newTenant);
		dstCriteria.setDisplayText(srcCriteria.getDisplayText());
		dstCriteria.setRetired(srcCriteria.isRetired());
		dstCriteria.setInstructions(srcCriteria.getInstructions());
		dstCriteria.setRequired(srcCriteria.isRequired());
		dstCriteria.getRecommendations().addAll(srcCriteria.getRecommendations());
		dstCriteria.getDeficiencies().addAll(srcCriteria.getDeficiencies());
		return dstCriteria;
	}

}
