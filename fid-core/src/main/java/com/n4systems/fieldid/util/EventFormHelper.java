package com.n4systems.fieldid.util;

import com.n4systems.model.*;

import java.util.*;

public class EventFormHelper {

	private Map<AbstractEvent, List<CriteriaSection>> availableSections = new HashMap<AbstractEvent, List<CriteriaSection>>();
	private List<CriteriaSection> currentCriteriaSections;
	
	private Map<AbstractEvent, Map<CriteriaSection, List<CriteriaResult>>> sections = new HashMap<AbstractEvent, Map<CriteriaSection, List<CriteriaResult>>>();
    private Map<AbstractEvent, Map<CriteriaSection, Double>> eventsSectionsScoresMap = new HashMap<AbstractEvent, Map<CriteriaSection, Double>>();
    private Map<AbstractEvent, Map<CriteriaSection, Double>> eventsSectionsScoresPercentageMap = new HashMap<AbstractEvent, Map<CriteriaSection, Double>>();

	public List<CriteriaSection> getAvailableSections(AbstractEvent event) {
		if (availableSections.get(event) == null) {
			availableSections.put(event, new ArrayList<CriteriaSection>());
			getVisibleResults(event);

            EventForm eventForm = event.getEventForm();
            if (eventForm != null && !eventForm.getSections().isEmpty()) {
				for (CriteriaSection section : eventForm.getSections()) {
					if (!sections.get(event).isEmpty()) {
						if (sections.get(event).containsKey(section)) {
							availableSections.get(event).add(section);
						}
					} else if (event.isNew() || isOpenEvent(event)) {
						if (!section.isRetired()) {
							availableSections.get(event).add(section);
						}
					}
				}
			}
		}
		currentCriteriaSections = availableSections.get(event);
		return availableSections.get(event);
	}

    private boolean isOpenEvent(AbstractEvent event) {
        if (event instanceof Event) {
            return ((Event)event).getWorkflowState() == WorkflowState.OPEN;
        }
        return false;
    }

    public List<CriteriaSection> getCurrentCriteriaSections() {
		return currentCriteriaSections;
	}

	public Map<CriteriaSection, List<CriteriaResult>> getVisibleResults(AbstractEvent<ThingEventType,Asset> event) {
		if (sections.get(event) == null) {
			sections.put(event, new HashMap<CriteriaSection, List<CriteriaResult>>());
            EventForm eventForm = event.getEventForm();
			if (eventForm != null && !eventForm.getSections().isEmpty() && !event.getResults().isEmpty()) {
				for (CriteriaSection section : eventForm.getSections()) {
					List<CriteriaResult> results = new ArrayList<CriteriaResult>();
					for (Criteria criteria : section.getCriteria()) {
						for (CriteriaResult criteriaResult : event.getResults()) {
							if (criteriaResult.getCriteria().equals(criteria)) {
								results.add(criteriaResult);
							}
						}
					}
					if (!results.isEmpty()) {
						sections.get(event).put(section, results);
					}
				}
			}
		}
	
		return sections.get(event);
	}

    public Double calculateMaxScoreForEvent(AbstractEvent event) {
        Map<CriteriaSection, List<CriteriaResult>> visibleResults = getVisibleResults(event);

        double maxPossibleScore = 0.0;
        for (CriteriaSection section : visibleResults.keySet()) {
            int critIndex = 0;
            for (Criteria criteria : section.getAvailableCriteria()) {
                if (criteria instanceof ScoreCriteria) {
                    if(!((ScoreCriteriaResult)visibleResults.get(section).get(critIndex)).getScore().isNa()) {
                        maxPossibleScore += getMaxScoreValue(((ScoreCriteria)criteria).getScoreGroup());
                    }
                }
                critIndex++;
            }
        }
        return maxPossibleScore;
    }

    private Map<CriteriaSection, Double> calculateScoresForSections(AbstractEvent event) {
        Map<CriteriaSection, Double> sectionsScores = new HashMap<CriteriaSection, Double>();
        final Map<CriteriaSection, List<CriteriaResult>> visibleResults = getVisibleResults(event);
        for (CriteriaSection criteriaSection : visibleResults.keySet()) {
            double total = 0;
            int countScoreCriteria = 0;
            for (CriteriaResult result : visibleResults.get(criteriaSection)) {
                if (result instanceof ScoreCriteriaResult) {
                    countScoreCriteria++;
                    Score score = ((ScoreCriteriaResult) result).getScore();
                    if (!score.isNa()) {
                        total += score.getValue();
                    }
                }
            }

            if (countScoreCriteria > 0) {
                sectionsScores.put(criteriaSection, total);
            }
        }

        return sectionsScores;

    }

    public Map<CriteriaSection, Double> getScoresForSections(AbstractEvent event) {
        if (!eventsSectionsScoresMap.containsKey(event)) {
            eventsSectionsScoresMap.put(event, calculateScoresForSections(event));
        }
        return eventsSectionsScoresMap.get(event);
    }
    
    private Map<CriteriaSection, Double> calculateScorePercentageForSections(AbstractEvent event) {
        Map<CriteriaSection, Double> sectionsScores = getScoresForSections(event);
        Map<CriteriaSection, Double> sectionsScorePercentages = new HashMap<CriteriaSection, Double>();

        Map<CriteriaSection, List<CriteriaResult>> visibleResults = getVisibleResults(event);

        for (CriteriaSection section : visibleResults.keySet()) {
            double total = 0.0;
            int critIndex= 0;
            for(Criteria criteria: section.getAvailableCriteria()) {
                if(criteria instanceof ScoreCriteria) {
                    if(!((ScoreCriteriaResult)visibleResults.get(section).get(critIndex)).getScore().isNa()) {
                        total += getMaxScoreValue(((ScoreCriteria)criteria).getScoreGroup());
                    }
                }
                critIndex++;
            }
            if (total > 0.0) {
                double percentage = sectionsScores.get(section) / total;
                sectionsScorePercentages.put(section, percentage);
            }
        }
        return sectionsScorePercentages;
    }

    public Map<CriteriaSection, Double> getScorePercentageForSections(AbstractEvent event) {
        if (!eventsSectionsScoresPercentageMap.containsKey(event)) {
            eventsSectionsScoresPercentageMap.put(event, calculateScorePercentageForSections(event));
        }
        return eventsSectionsScoresPercentageMap.get(event);
    }

    public Double getEventFormScorePercentage(AbstractEvent event) {
        double total = calculateMaxScoreForEvent(event);

        if (total <= 0.0)
            return 0.0;
        else
            return event.getScore() / total;
    }

    private Double getMaxScoreValue(ScoreGroup group) {

        if(group.getScores() == null || group.getScores().isEmpty())
            return 0.0;

        return getMaxScoreValue(group.getScores());
    }

    private Double getMaxScoreValue(List<Score> scores) {
        Collections.sort(scores, new Comparator<Score>() {
            @Override
            public int compare(Score o1, Score o2) {
                if (o1.isNa())
                    return o2.isNa() ? 0 : 1;
                else if (o2.isNa())
                    return -1;
                else
                    return o2.getValue().compareTo(o1.getValue());
            }
        });

        if (scores.get(0).isNa()) {
            return 0.0;
        }

        return scores.get(0).getValue() > 0.0 ? scores.get(0).getValue() : 0.0;
    }


}
