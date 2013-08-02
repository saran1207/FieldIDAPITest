package com.n4systems.services.reporting;

import com.n4systems.util.chart.StringChartable;

public class CriteriaTrendsResultCountByCriteriaRecord extends StringChartable implements Comparable<CriteriaTrendsResultCountByCriteriaRecord> {

    private String criteriaName;
    private Long count;
    private String resultText;

    public CriteriaTrendsResultCountByCriteriaRecord(Long count, String resultText, String criteriaName) {
        super( criteriaName, count);
        this.count = count;
        this.resultText = resultText;
        this.criteriaName = criteriaName;
    }

    public String getCriteriaName() {
        return criteriaName;
    }

    public void setCriteriaName(String criteriaName) {
        this.criteriaName = criteriaName;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getResultText() {
        return resultText;
    }

    public void setResultText(String resultText) {
        this.resultText = resultText;
    }

    @Override
    public int compareTo(CriteriaTrendsResultCountByCriteriaRecord o) {
        return count.compareTo(o.count);
    }
}
