package com.n4systems.fieldid.wicket.pages;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.IWicketTester;
import com.n4systems.fieldid.wicket.WicketHarness;
import org.apache.wicket.Component;

import java.util.List;

@SuppressWarnings("unchecked")
public class BasicPageHarness extends WicketHarness {

    public BasicPageHarness(String pathContext, IWicketTester tester) {
        super(pathContext, tester);
        appendPathContext("content");
    }




    public List<Component> getCssLinks() {
        List<Component> cssLinks = Lists.newArrayList();

//        add(new CachingStrategyLink("resetCss"));
//
//        WebMarkupContainer legacyCss = new WebMarkupContainer("legacyCss") {
//            { setRenderBodyOnly(true);
//            }
//            @Override public boolean isVisible() {
//                return useLegacyCss();
//            }
//        };
//        legacyCss.add(new CachingStrategyLink("fieldIdCss"));
//        legacyCss.add(new CachingStrategyLink("fieldIdIE6Css"));
//        legacyCss.add(new CachingStrategyLink("fieldIdIE7Css"));
//        add(legacyCss);
//
//        WebMarkupContainer newCss = new WebMarkupContainer("newCss") {
//            { setRenderBodyOnly(true); }
//
//            @Override public boolean isVisible() {
//                return !useLegacyCss();
//            }
//        };
//        newCss.add(new CachingStrategyLink("layoutCss"));
//        newCss.add(new CachingStrategyLink("feedbackErrorsCss"));
//        add(newCss);
//
//        WebMarkupContainer siteWideCss = new WebMarkupContainer("siteWideCss") {
//            { setRenderBodyOnly(true); }
//
//            @Override public boolean isVisible() {
//                return useSiteWideCss();
//            }
//        };
//        siteWideCss.add(new CachingStrategyLink("siteWideCss"));
//        add(siteWideCss);
//
//        add(new CachingStrategyLink("defaultCss"));
//        add(new CachingStrategyLink("commonIEJs","src"));  ///FIX SO APPENDS TO SRC ATTRIBUTE!!!
//        add(new CachingStrategyLink("dropDownCss"));
//        add(new CachingStrategyLink("dropDownIECss"));


        cssLinks.add(getWicketTester().getComponentFromLastRenderedPage("resetCss"));
        return cssLinks;
    }

    public Component getGoogleAnalytics() {
        return getWicketTester().getComponentFromLastRenderedPage("googleAnalyticsScripts");
    }


}
