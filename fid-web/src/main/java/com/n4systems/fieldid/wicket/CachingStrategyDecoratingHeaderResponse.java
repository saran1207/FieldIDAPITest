package com.n4systems.fieldid.wicket;

import com.n4systems.fieldid.version.FieldIdVersion;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.IHeaderResponseDecorator;
import org.odlabs.wiquery.core.WiQueryDecoratingHeaderResponse;


/**
 * The existence of this class is for WEB-2812.
 * We want to catch all css & javascript urls and attach a parameter on them to counter over aggressive browser caching.
 * e.g.    ./myStyle.css -->   ./myStyle.css?version1
 * that way when you change the version number a new file will be retrieved.
 *
 * Recall that dealing with resources in wicket goes down two different paths.
 * 1: resources within the war.   (classpath accessible)
 * 2: resources in webapp folder.
 *
 * Case 1 is automatically covered by ResourceSettings.setResourceCachingStrategy()
 * this deals with intercepting urls for case 2.
 */
public class CachingStrategyDecoratingHeaderResponse extends WiQueryDecoratingHeaderResponse {

    private static String suffix = "?"+FieldIdVersion.getVersion();

    public CachingStrategyDecoratingHeaderResponse(IHeaderResponse real) {
        super(real);
    }

    @Override
    public void renderCSSReference(String url) {
        url += suffix;
        super.renderCSSReference(url);
    }

    @Override
    public void renderJavaScriptReference(String url) {
        url += suffix;
        super.renderJavaScriptReference(url);
    }


    public static IHeaderResponseDecorator createHeaderResponseDecorator() {
        return new IHeaderResponseDecorator() {
            public IHeaderResponse decorate(IHeaderResponse response) {
                return new CachingStrategyDecoratingHeaderResponse(response);
            }
        };
    }
}
