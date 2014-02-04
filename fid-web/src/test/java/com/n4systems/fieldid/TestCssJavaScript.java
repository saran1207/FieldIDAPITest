package com.n4systems.fieldid;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertNull;


public class TestCssJavaScript extends WebAppTest {

    private String INCLUDE_JAVASCRIPT_PATTERN = "<script(.*?)>";
    private String INCLUDE_CSS_PATTERN = "<link href\\s*=(.*?)\\s*.*?>";
    private Pattern cssPattern;
    private Pattern jsPattern;

    private String dangerousCss = "css";
    private String dangerousJavascript = "src";
    private String[] safeJavascript = {"https://maps.googleapis","use.typekit.net/usa4tou.js"};

    private String[] filesToSkip = {
            "includeStyle.ftl",   // valid low level use.
            "script-open.ftl",   // unsure of this one???
            "googleAnalytics.ftl"
        };

    @Before
    public void setUp() {
        jsPattern = Pattern.compile(INCLUDE_JAVASCRIPT_PATTERN, Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
        cssPattern = Pattern.compile(INCLUDE_CSS_PATTERN, Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
    }


    @Test
    public void test_all_ftl_files() throws IOException {
        FileFilter filter = new FileFilter() {
            @Override public boolean accept(File pathname) {
                if (pathname==null) { return true; }
                String fileName = pathname.getName();
                for (String name:filesToSkip) {
                    if (fileName.endsWith(name)) {
                        return false;
                    }
                }
                if (pathname.getName().equals("show.ftl") && pathname.getParentFile().getName().equals("eventCrud")) {
                    return false;
                }
                return true;
            }
        };
        List<File> ftl = getWebAppFiles(filter, "ftl");
        for (File file : ftl) {
            String match = matchesAny(file, cssPattern, dangerousCss, "");
            assertNull(file.getCanonicalPath() + " contains illegal CSS near [\n " + match + "\n ] ", match );
            match = matchesAny(file, jsPattern, dangerousJavascript, safeJavascript);
            assertNull(file.getCanonicalPath() + " contains illegal JAVASCRIPT near [\n " + match + " \n]", match);
        }
    }

    private String dump(File file) throws IOException {
        return getCharBuffer(file).toString();
    }

    private String matchesAny(File file, Pattern pattern, String dangerous, String... safe) throws IOException {
        if (!file.isFile()) {
            return null;
        }

        String contents = getCharBuffer(file).toString();
        CharBuffer cbuf = getCharBuffer(file);
        Matcher matcher = pattern.matcher(cbuf);
        scan: while (matcher.find()) {
            String match = contents.substring(matcher.start(), matcher.end());
            for (String safeText:safe) {
                if (match.contains(safeText)) continue scan;
            }
            if (match.contains(dangerous)) {
                return match;
            }
        }
        return null;
    }

    private CharBuffer getCharBuffer(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        FileChannel fc = fis.getChannel();

        ByteBuffer bbuf = fc.map(FileChannel.MapMode.READ_ONLY, 0, (int)fc.size());
        return Charset.forName("8859_1").newDecoder().decode(bbuf);
    }
}
