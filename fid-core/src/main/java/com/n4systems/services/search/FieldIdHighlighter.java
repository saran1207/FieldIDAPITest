package com.n4systems.services.search;

import com.n4systems.services.brainforest.SearchQuery;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.EmptyTokenStream;
import org.apache.lucene.search.highlight.*;

import java.io.IOException;

class FieldIdHighlighter extends Highlighter  {

    private Formatter formatter;
    private NumericAnalyzer numericAnalyzer;
    private final SearchQuery searchQuery;


    public FieldIdHighlighter(Formatter formatter, Scorer fragmentScorer, SearchQuery searchQuery) {
        super(formatter, fragmentScorer);
        this.formatter = formatter;
        this.numericAnalyzer = new NumericAnalyzer(searchQuery);
        this.searchQuery = searchQuery;
    }

    public String getBestNumericFragment(String fieldName, Number numberValue, String text) {
        if (numericAnalyzer.matches(fieldName, numberValue)) {
            // basically, just want the formatter to do it's simple string appending job...  i.e.   hello  -->   <span class="hilite">hello</span>
            // ...alas need all this workaround mocking stuff because it takes a peek at TokenGroup before doing so. (.: can't pass in null)
            return formatter.highlightTerm(text,new TokenGroup(new EmptyTokenStream()) {
                @Override public float getTotalScore() {
                    return 1;
                }
            });
        }
        return null;
    }

    public String getBestTextFragment(Analyzer analyzer, String fieldName, String value) throws IOException, InvalidTokenOffsetsException {
        if (searchQuery.usesAttribute(fieldName)) {
            return super.getBestFragment(analyzer,fieldName,value);
        }
        return null;
    }

}
