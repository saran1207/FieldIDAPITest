package com.n4systems.fieldid.wicket.panels;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.asset.AssetImportPanel;
import com.n4systems.model.AssetType;
import com.n4systems.model.assettype.AssetTypeListLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.LoaderFactory;
import org.apache.wicket.Component;
import org.apache.wicket.Localizer;
import org.apache.wicket.Session;
import org.apache.wicket.mock.MockApplication;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.util.tester.TagTester;
import org.apache.wicket.util.tester.WicketTester;
import org.castor.core.util.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.MissingResourceException;


/**
 * Created by agrabovskis on 2017-12-18.
 */
public class AssetImportPanelTest {

    public static final String ASSET_IMPORT_PANEL_ID = "assetImportPanel";
    private WicketTester tester;

    @Before
    public void setUp() {
        WebApplication webApp = new MockApplication() {
            public Session newSession(Request request, Response resposne) {
                return new FieldIDSession(request);
            }
        };
        tester = new WicketTester(webApp);
        Session session = Session.get();
        assertTrue(session instanceof FieldIDSession);
         webApp.getResourceSettings().setLocalizer(new Localizer() {

            @Override
            public String getString(final String key, final Component component, final String defaultValue) throws MissingResourceException {
                String val = super.getString(key, component, defaultValue);
                if (val.equals(defaultValue))
                    val = new FIDLabelModel(key).getObject();
                return val;

            }
            @Override
            public String getString(final String key, final Component component, final IModel<?> model)
                    throws MissingResourceException {
                try {
                    return super.getString(key, component, model);
                }
                catch(MissingResourceException ex) {
                    return new FIDLabelModel(key).getObject();
                }
            }
        });
    }

    @Test
    public void verifyPanelRenders() {

        AssetImportPanel assetImportPanel = createAssetImportPanel();
        Object obj = tester.startComponentInPage(assetImportPanel);
        Assert.notNull(obj, "Did AssetImportPanel get created");
    }

    @Test
    public void verifyDownloadChoicesAppear() {

        AssetImportPanel assetImportPanel = createAssetImportPanel();
        tester.startComponentInPage(assetImportPanel);
        tester.assertEnabled("assetImportPanel:downloadChoiceContainer:downloadDataChoice");
        tester.assertEnabled("assetImportPanel:downloadChoiceContainer:downloadTemplateChoice");
    }

    @Test
    public void verifyDownloadLinksIntialState() {

        AssetImportPanel assetImportPanel = createAssetImportPanel();
        tester.startComponentInPage(assetImportPanel);
        assert componentHasDisabledClass("downloadDataLink");
        assert !componentHasDisabledClass("downloadTemplateLink");
    }

    @Test
    public void verifyExportSectionEnabling() {

        AssetImportPanel assetImportPanel = createAssetImportPanel();
        tester.startComponentInPage(assetImportPanel);

        String body = tester.getLastResponseAsString();
        assertEquals(true, containsDisabledCssClass(body, "downloadDataContainer"));
        assertEquals(false, containsDisabledCssClass(body, "downloadTemplateContainer"));

        Component dataChoiceRadioButton = tester.getComponentFromLastRenderedPage("assetImportPanel:downloadChoiceContainer:downloadDataChoice");
        tester.executeAjaxEvent(dataChoiceRadioButton, "onclick");

        body = tester.getLastResponseAsString();
        assertEquals(false, containsDisabledCssClass(body, "downloadDataContainer"));
        assertEquals(true, containsDisabledCssClass(body, "downloadTemplateContainer"));

        Component templateChoiceRadioButton = tester.getComponentFromLastRenderedPage("assetImportPanel:downloadChoiceContainer:downloadTemplateChoice");
        tester.executeAjaxEvent(templateChoiceRadioButton, "onclick");
        tester.getLastRenderedPage().renderPage();

        body = tester.getLastResponseAsString();
        assertEquals(containsDisabledCssClass(body, "downloadDataContainer"), true);
        assertEquals(containsDisabledCssClass(body, "downloadTemplateContainer"), false);
    }

    private boolean containsDisabledCssClass(String body, String tag) {
        int i = body.indexOf("wicket:id=\"" + tag + "\" ");
        if (i > -1) {
            i = body.indexOf("class=\"", i);
            int j = body.indexOf("\"", i+7);
            String cssClasses = body.substring(i+7, j);
            return  (cssClasses.equals("disabled") || cssClasses.startsWith("disabled ")
                    || cssClasses.contains(" disabled ") || cssClasses.endsWith(" disabled"));
        }
        return false;
    }

    private boolean componentHasDisabledClass(String id) {

        System.out.println("checking for disabled class for id '" + id + "'");
        TagTester tagTester = tester.getTagByWicketId(id);
        return componentHasDisabledClass(tagTester);
    }

    private boolean componentHasDisabledClass(TagTester tagTester) {
        String cssClasses = tagTester.getAttribute("class");
        System.out.println("Component  classes: '" + cssClasses + "'" );
        if (cssClasses == null)
            return false;
        else
            return  (cssClasses.equals("disabled") || cssClasses.startsWith("disabled ")
                    || cssClasses.contains(" disabled ") || cssClasses.endsWith(" disabled"));
    }

    private AssetImportPanel createAssetImportPanel() {
        AssetImportPanel assetImportPanel = new AssetImportPanel(ASSET_IMPORT_PANEL_ID, null, new Model(null),
                new Model(null), new Model(null),
                new Model(null), new Model(null)) {
            public LoaderFactory getLoaderFactory() {
                LoaderFactory loaderFactory = new LoaderFactory(null) {
                    public AssetTypeListLoader createAssetTypeListLoader() {
                        return new AssetTypeListLoader(null) {

                            @Override
                            public List<AssetType> load() {
                                return load((EntityManager) null, (SecurityFilter) null);
                            }
                            @Override
                            protected List<AssetType> load(EntityManager em, SecurityFilter filter) {
                                AssetType assetType = new AssetType();
                                assetType.setId(1L);
                                assetType.setName("assetType01");
                                return Arrays.asList(assetType);
                            }
                        };
                    }
                };
                return loaderFactory;
            }
        };
        return assetImportPanel;
    }
}
