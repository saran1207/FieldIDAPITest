package com.n4systems.fieldid.service.org;

import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameterGroup;
import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * examples of term =
 * SomeOrg:aCust:dd     yields divisions like dd that have a parent customer like aCust which have a PrimaryOrg like SomeOrg.
 * :aCust              searches for customers under all orgs.
 * ::dd                searches for divisions only under all customers/orgs.
 * :CC:                 searches for divisions under "%CC%"
 * foo                  searches for anything, any type with name like "%foo%"
 * etc...
 */
public class OrgQuery {
    private int PRIMARY_TERM=1, CUSTOMER_TERM=2, TRAILING_TERM=3;
    private Matcher matcher;
    private String term;

    public OrgQuery(String term) {
        this.term = term;
        Pattern pattern = Pattern.compile("(.*?:)(.*?:)?(.*)");
        matcher = pattern.matcher(term);
    }
    
    public Class<? extends BaseOrg> getSearchClass() {
        switch(levelsMatched()) {
            case 1:
                return CustomerOrg.class;
            case 2:
                return DivisionOrg.class;
            default:
                return BaseOrg.class;
        }
    }

    public WhereClause<?> getWhere() {
        if (!matcher.matches()) {   // just use the entire search term if no matches found.
            return new WhereParameter<String>(WhereParameter.Comparator.LIKE, "name", "name", term, WhereParameter.WILDCARD_BOTH, false);
        }

        WhereParameterGroup group = new WhereParameterGroup("name-filter");
        if (StringUtils.isNotBlank(getTrailingTerm())) {
            group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "name", "name", getTerm(TRAILING_TERM), WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.AND));
        }

        // TODO : add if searchTerms.get() !=null....
        switch (levelsMatched()) {
            case 1:
                if (StringUtils.isNotBlank(getPrimaryTerm())) {
                    group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "parent_name", "parent.name", getPrimaryTerm(), WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.AND));
                }
                break;
            case 2:  // search for parent & grandparent.
                if (StringUtils.isNotBlank(getCustomerTerm())) {
                    group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "parent_name", "parent.name", getCustomerTerm(), WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.AND));
                }
                if (StringUtils.isNotBlank(getPrimaryTerm())) {
                    group.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "parent_parent_name", "parent.parent.name", getPrimaryTerm(), WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.AND));
                }
                break;
        }
        return group;
    }

    int levelsMatched() {
        return !matcher.matches() ? 0 :
                matcher.group(CUSTOMER_TERM)!=null ? 2 : 1;
    }

    String getCustomerTerm() {
        return getTerm(CUSTOMER_TERM);
    }

    String getPrimaryTerm() {
        return getTerm(PRIMARY_TERM);
    }

    String getTrailingTerm() {
        return getTerm(TRAILING_TERM);
    }

    private String getTerm(int groupIndex) {
        if (matcher.matches() && matcher.groupCount()>=groupIndex) {
            String token = matcher.group(groupIndex);
            int i = token.lastIndexOf(":");
            return i>=0 ? token.substring(0,i) : token;
        }
        return "";
    }

}
