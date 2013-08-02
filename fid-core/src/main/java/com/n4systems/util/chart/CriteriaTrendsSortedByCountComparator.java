package com.n4systems.util.chart;

import com.n4systems.services.reporting.CriteriaTrendsResultCountByCriteriaRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CriteriaTrendsSortedByCountComparator implements Comparator<String> {

    private List<String> criteriaNamesInOrderOfCount;

    // This is pretty meh but the API is inflexible right now. We should revisit this charting API as more are required
    public CriteriaTrendsSortedByCountComparator(List<CriteriaTrendsResultCountByCriteriaRecord> criteriaTrendsByCriteria) {
        Collections.sort(criteriaTrendsByCriteria);
        criteriaNamesInOrderOfCount = new ArrayList<String>();
        for (CriteriaTrendsResultCountByCriteriaRecord record : criteriaTrendsByCriteria) {
            criteriaNamesInOrderOfCount.add(record.getCriteriaName());
        }
    }

    @Override
    public int compare(String s1, String s2) {
        Integer a = criteriaNamesInOrderOfCount.indexOf(s2);
        return a.compareTo(criteriaNamesInOrderOfCount.indexOf(s1));
    }

}
