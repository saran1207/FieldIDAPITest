package com.fieldid.index;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;

/**
 * Created by jheath on 2015-07-20.
 */
public class IndexExaminer implements Runnable {

    public static void main(String[] args) {
        IndexExaminer me = new IndexExaminer();
        me.run();
    }

    @Override
    public void run() {
        try {
            Directory dir = FSDirectory.open(new File("/var/fieldid/private/indexes/n4/assets"));

            IndexReader reader = DirectoryReader.open(dir);

            System.out.println("There appear to be " + reader.numDocs() + " assets indexed here.");
        } catch (IOException e) {
            System.out.println("Bummer!  Couldn't find that directory for some reason!!");
        }
    }
}
