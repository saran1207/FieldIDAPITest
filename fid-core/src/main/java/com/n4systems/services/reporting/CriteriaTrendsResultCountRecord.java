package com.n4systems.services.reporting;

import java.io.Serializable;

public class CriteriaTrendsResultCountRecord implements Serializable {

    private Long count;
    private String resultText;

    public CriteriaTrendsResultCountRecord(Long count, String resultText) {
        this.count = count;
        this.resultText = resultText;
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
}
