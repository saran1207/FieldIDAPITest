package com.n4systems.fieldid.wicket.components;

import com.n4systems.fieldid.service.org.OrgQuery;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.commons.lang.StringUtils;
import org.odlabs.wiquery.ui.autocomplete.AutocompleteJson;

public class OrgAutocompleteJson extends AutocompleteJson {

    private String desc;
    private Integer matchStart;
    private Integer matchCount;
    private String tooltip;
    private String descClass;

    public OrgAutocompleteJson(String desc, String descClass) {
        super("bogusId","");
        this.desc = desc;
        this.descClass = descClass;
    }
    
    public OrgAutocompleteJson(String id, String name, String desc, String tooltip) {
        super(id, name);
        this.desc = desc;
        descClass = "category";
        this.tooltip = tooltip;
    }
    
    public OrgAutocompleteJson(BaseOrg org, OrgQuery orgQuery, String desc) {
        super(org.getId()+"", org.getName());
        this.desc = desc;
        // properties used as rendering hints on javascript/client side.
        descClass ="category";
        matchStart = calculateMatchStart(orgQuery);
        matchCount = calculateMatchCount(orgQuery);
        tooltip = generateTooltip(org);
    }

    private String generateTooltip(BaseOrg org) {
        return org.getHierarchicalDisplayName();
    }

    private Integer calculateMatchStart(OrgQuery orgQuery) {
        String term = orgQuery.getSearchTerm();
        if (StringUtils.isNotBlank(term)) {
            return getLabel().toLowerCase().indexOf(term);
        }
        return -1;
    }

    private Integer calculateMatchCount(OrgQuery orgQuery) {
        return orgQuery.getSearchTerm().length();
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
}

