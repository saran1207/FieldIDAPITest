package com.n4systems.fieldid.wicket.components;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

public class AutoCompleteResult implements Serializable {
    
    private String valueId;
    private String label;
    private String desc;
    private Integer matchStart;
    private Integer matchCount;
    private String tooltip;
    private String descClass;

    public AutoCompleteResult(String id, String label, String desc, String term, String tooltip) {
        // BUG : i need to escape these strings.
        // e.g. if label is "&hello" it will not render correctly.
        this.valueId = id;
        this.label = label;
        this.desc = desc;
        descClass = "category";
        this.tooltip = tooltip;
        matchStart = calculateMatchStart(term);
        matchCount = calculateMatchCount(term);
    }
    
    private Integer calculateMatchStart(String term) {
        if (StringUtils.isNotBlank(term)) {
            return getLabel().toLowerCase().indexOf(term.toLowerCase());
        }
        return -1;
    }

    private Integer calculateMatchCount(String term) {
        return term.length();
    }

    public Integer getMatchCount() {
        return matchCount;
    }

    public Integer getMatchStart() {
        return matchStart;
    }

    public String getTooltip() {
        return tooltip;
    }

    public String getDescClass() {
        return descClass;
    }

    public String getDesc() {
        return desc;
    }

    public String getLabel() {
        return label;
    }

    public String getValueId() {
        return valueId;
    }
}

