package com.n4systems.services.search;

import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.Scorer;

class NumericHighlighter extends Highlighter  {

    private Formatter formatter;
    private NumericAnalyzer numericAnalyzer = new NumericAnalyzer();


    public NumericHighlighter(Formatter formatter, Scorer fragmentScorer) {
        super(formatter, fragmentScorer);
        this.formatter = formatter;
    }

    public Formatter getFormatter() {
        return formatter;
    }

    public String getBestNumericFragment(String fieldName, Number value) {
        return null;
    }

}
