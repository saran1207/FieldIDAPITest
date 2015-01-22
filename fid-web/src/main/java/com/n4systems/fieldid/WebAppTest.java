package com.n4systems.fieldid;

import com.google.common.collect.Lists;
import com.opensymphony.xwork2.interceptor.annotations.Before;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.util.Iterator;
import java.util.List;

public class WebAppTest {

    private static final Logger logger= Logger.getLogger(WebAppTest.class);

    private File webAppDirectory = new File("fid-web/src/main/webapp");


    @Before
    public void setUp() {
        if (!webAppDirectory.isDirectory()) {
            throw new IllegalStateException("tests assume webapp directory exists " + webAppDirectory.getPath());
        }
    }

    public List<File> getWebAppFiles(String... extensions) {
        return getWebAppFiles(null, extensions);
    }

    public List<File> getWebAppFiles(FileFilter filter, String... extensions) {
        List<File> result = Lists.newArrayList();
        Iterator<File> iterator = FileUtils.iterateFiles(webAppDirectory, extensions, true);
        while (iterator.hasNext()) {
            File file = iterator.next();
            if (filter==null || filter.accept(file)) {
                result.add(file);
            }
        }
        return result;
    }
}
