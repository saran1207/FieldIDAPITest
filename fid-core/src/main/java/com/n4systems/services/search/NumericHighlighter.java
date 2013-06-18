package com.n4systems.services.search;

import com.n4systems.services.brainforest.SearchQuery;
import org.apache.lucene.analysis.miscellaneous.EmptyTokenStream;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.TokenGroup;

class NumericHighlighter extends Highlighter  {

    private Formatter formatter;
    private NumericAnalyzer numericAnalyzer;


    public NumericHighlighter(Formatter formatter, Scorer fragmentScorer, SearchQuery searchQuery) {
        super(formatter, fragmentScorer);
        this.formatter = formatter;
        this.numericAnalyzer = new NumericAnalyzer(searchQuery);
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


}
