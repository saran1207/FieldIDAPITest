package com.n4systems.services.search;

import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.Version;

import java.util.Arrays;
import java.util.List;

public class StopWordSet extends CharArraySet {

    // WARNING : obviously this doesn't take other languages into account...
    //   ...what to do if we want to index data in different languages???


    // ***********************************************************
    //   COPIED FROM org.apache.lucene.analysis.core.StopAnalyzer
    // ***********************************************************
    static final List<String> stopWords = Arrays.asList(
                "a", "an", "and", "are", "as", "at", "be", "but", "by",
                "for", "if", "in", "into", "is", "it",
                "no", "not", "of", "on", "or", "such",
                "that", "the", "their", "then", "there", "these",
                "they", "this", "to", "was", "will", "with"
        );

    public StopWordSet() {
        super(Version.LUCENE_43, stopWords, false);
    }

}
