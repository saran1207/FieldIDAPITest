package com.n4systems.services.search.parser;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SearchQuery implements Serializable {

    public enum Conjunction {
        AND,OR;

        public static Conjunction fromString(String text) {
            text = text.toLowerCase();
            for (Conjunction j:values()) {
                if (j.name().toLowerCase().equals(text)) {
                    return j;
                }
            }
            return null;
        }
    };

    private List<QueryTerm> terms = Lists.newArrayList();
    private List<Conjunction> conjunctions = Lists.newArrayList();
    private Set<String> suggestions = Sets.newHashSet();

    public SearchQuery add(QueryTerm term) {
        terms.add(term);
        return this;
    }

    public List<QueryTerm> getQueryTerms() {
        return terms;
    }

    public SearchQuery addSuggestion(List<String> suggestions) {
        for (String suggestion:suggestions) {
            if (StringUtils.isNotBlank(suggestion)) {
                this.suggestions.add(suggestion);
            }
        }
        return this;
    }

    public Set<String> getSuggestions() {
        return suggestions;
    }

    public SearchQuery add(String conjunction) {
        conjunctions.add(Conjunction.fromString(conjunction));
        return this;
    }

    public SearchQuery add(Conjunction conjunction) {
        conjunctions.add(conjunction);
        return this;
    }

    public Iterator<Conjunction> conjunctions() {
        Preconditions.checkState(conjunctions.size() == terms.size() - 1, "somethings wrong with this query...need to have N-1 AND/OR conjunctions for N terms.");
        return conjunctions.iterator();
    }

    public int getNumberOfTerms() {
        return terms.size();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        QueryTerm term = terms.get(0);
        builder.append(term.toString());
        for (int i=1;i<terms.size();i++) {
            Conjunction conjunction = conjunctions.get(i-1);
            builder.append(" ").append(conjunction).append("  ");
            term = terms.get(i);
            builder.append(term.toString());
        }
        return builder.toString();
    }

    public QueryTerm getTermForAttribute(String attribute) {
        if (attribute==null) {
            return null;
        }
        // if user just inputs "foo", then we'll generate a "attribute=foo" term on the fly.
        if (terms.size()==1 && terms.get(0).getAttribute()==null) {
            return new QueryTerm(attribute, QueryTerm.Operator.EQ, terms.get(0).getValue());
        }
        for (QueryTerm term:terms) {
            if (attribute.equals(term.getAttribute())) {
                return term;
            }
        }
        return null;
    }

    public boolean usesAttribute(String attribute) {
        if (attribute==null) {
            return false;
        }
        for (QueryTerm term:terms) {
            String termAttribute = term.getAttribute();
            if (attribute.equalsIgnoreCase(termAttribute) || termAttribute==null) {
                return true;
            }
        }
        return false;
    }

}
