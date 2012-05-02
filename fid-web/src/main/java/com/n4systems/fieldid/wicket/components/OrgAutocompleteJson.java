package com.n4systems.fieldid.wicket.components;

import com.n4systems.fieldid.service.org.OrgQuery;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.OrgEnum;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
import org.odlabs.wiquery.ui.autocomplete.AutocompleteJson;

public class OrgAutocompleteJson extends AutocompleteJson {

    private String cssClass;
    private String category;
    private Integer matchStart;
    private Integer matchCount;
    private String tooltip;

    public OrgAutocompleteJson(Integer id, String category, String cssClass) {
        super(id+"","");
        this.category = category;
        this.cssClass = cssClass;
    }
    
    public OrgAutocompleteJson(BaseOrg org, OrgQuery orgQuery, String category) {
        super(org.getId()+"", org.getName());
        this.category = category;
        this.cssClass = org.isPrimary() ? "primary" :
                org.isSecondary() ? "secondary" :
                        org.isDivision() ? "division" : "unknown-org-type";
        // used as rendering hints on javascript/client side.
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

    public String getCssClass() {
        return cssClass;
    }
    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
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
}

