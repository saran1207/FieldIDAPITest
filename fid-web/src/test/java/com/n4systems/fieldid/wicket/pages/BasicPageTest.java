package com.n4systems.fieldid.wicket.pages;

import com.n4systems.fieldid.version.FieldIdVersion;
import com.n4systems.fieldid.wicket.CachingStrategyDecoratingHeaderResponse;
import com.n4systems.fieldid.wicket.FieldIdPageTest;
import com.n4systems.fieldid.wicket.IFixtureFactory;
import com.n4systems.fieldid.wicket.IWicketTester;
import org.apache.wicket.request.resource.caching.QueryStringWithVersionResourceCachingStrategy;
import org.apache.wicket.request.resource.caching.version.MessageDigestResourceVersion;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

//@RunWith(FieldIdWicketTestRunner.class)
public class BasicPageTest extends FieldIdPageTest<BasicPageHarness, FieldIDTemplatePage> implements IFixtureFactory<FieldIDTemplatePage> {


    @Override
	@Before
    public void setUp() throws Exception {
    	super.setUp();
    }

    /**
     * this is not an 100% guaranteed test.  it's possible to get false negative in the case when you really
     * don't want to add parameters to your .js or .css links.
     *
     * it checks for html that looks like this
     *      "..../foo.css"  or ".../bar.js"
     * and warns you that they should probably look like
     *      "..../foo.css?versionX"  or ".../bar.js?versionX"      <---FieldIDVersion.getVersion()
     */
    @Test
    public void testRender_cachingStrategyRefs() throws MalformedURLException {
        expectingUserService();
        expectingS3Service();
        expectingConfig();
        expectingTenantSettingsService();

        getWicketTester().getApplication().setHeaderResponseDecorator(CachingStrategyDecoratingHeaderResponse.createHeaderResponseDecorator());
        getWicketTester().getApplication().getResourceSettings().setCachingStrategy(new QueryStringWithVersionResourceCachingStrategy(new MessageDigestResourceVersion()));
        renderFixture(this);

        // might as well check for google analytics while i'm here.
        // note that i've stuck a bogus parameter on the end just to make life easier for testing.
        assertVisible(getHarness().getGoogleAnalytics());
        assertInDocument("<script src=\"https://ssl.google-analytics.com/ga.js?placeholderParam\" type=\"text/javascript\"></script>");

        assertTrue(validateCssUrls(getHarness().getLastResponseAsString()));
        assertTrue(validateJavascriptUrls(getHarness().getLastResponseAsString()));
    }

    @Test
    public void testRender_NocachingStrategyRefs() throws MalformedURLException {
        expectingUserService();
        expectingS3Service();
        expectingConfig();
        expectingTenantSettingsService();

        renderFixture(this);

        assertVisible(getHarness().getGoogleAnalytics());
        assertInDocument("<script src=\"https://ssl.google-analytics.com/ga.js?placeholderParam\" type=\"text/javascript\"></script>");

        assertFalse(validateCssUrls(getHarness().getLastResponseAsString()));
        assertFalse(validateJavascriptUrls(getHarness().getLastResponseAsString()));
    }

	@Test
	public void testRender_noGoogleAnalytics() throws MalformedURLException {
        withGoogleAnalytics(false);
        expectingConfig();
        expectingUserService();
        expectingS3Service();
        expectingTenantSettingsService();

        renderFixture(this);
        assertInvisible(getHarness().getGoogleAnalytics());
    }

    private boolean validateJavascriptUrls(String html) {
        // NOTE : one exception to this rule is that we include....
        //   src="//use.typekit.net/usa4tou.js"
        // in our files. for this case we don't care about the lack of url parameter (i.e. potential caching problems).
        html = html.replace("src=\"//use.typekit.net/usa4tou.js\"","{ignoreThisJsReference}");
        return html.indexOf("?"+FieldIdVersion.getVersion())>=0 &&  html.indexOf(".js\"")==-1;
    }

    private boolean validateCssUrls(String html) {
        return html.indexOf("?"+FieldIdVersion.getVersion())>=0 && html.indexOf(".css\"")==-1;
    }

	@Override
	public FieldIDTemplatePage createFixture(String id) {
		return new FieldIDTemplatePage(configurationProvider);
	}

	@Override
	protected BasicPageHarness createHarness(String pathContext, IWicketTester wicketTester) {
		return new BasicPageHarness(pathContext, wicketTester);
	}

}
