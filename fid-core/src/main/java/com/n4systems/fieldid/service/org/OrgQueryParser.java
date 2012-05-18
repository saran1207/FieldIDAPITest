package com.n4systems.fieldid.service.org;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

/**
 * examples of term =
 * SomeOrg:aCust:dd     yields divisions like dd that have a parent customer like aCust which have a PrimaryOrg like SomeOrg.
 * :aCust              searches for customers under all orgs.
 * ::dd                searches for divisions like "%dd%" only under all customers/orgs.
 * :CC:                 searches for divisions under "%CC%"
 * foo                  searches for anything, any type with name like "%foo%"
 * etc...
 */
public class OrgQueryParser {
    private String term;
    private List<String> parentTerms;
    private String searchTerm;   
    
    public OrgQueryParser(String term) {
        this.term = term.toLowerCase();
        int index = term.lastIndexOf(":");
        if (index!=-1) {
            searchTerm = term.substring(index+1).trim();
            parentTerms = Arrays.asList(term.substring(0,index).split(":"));
        } else {
            searchTerm = term;
            parentTerms = Lists.newArrayList();
        }
    }
    
    public String getSearchTerm() {
        return searchTerm;
    }
    
    public List<String> getParentTerms() {
        // "A:B:C"  --> { "A",  "B",  "C" }   complement of getSearchTerm().
        return parentTerms;
    }

    public String getLastParentTerm() {
        return parentTerms.size()==0 ? "" : parentTerms.get(parentTerms.size()-1);
    }
}
