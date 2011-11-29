package com.n4systems.fieldid.wicket;

import static com.google.common.base.Preconditions.*;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import junit.framework.Assert;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.Component.IVisitor;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.resource.loader.IStringResourceLoader;
import org.apache.wicket.util.tester.TagTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;


public abstract class WicketTest<T extends WicketHarness,F extends Component> implements IWicketTester, IFixtureFactory<F> {
	
	private static final String CLASS = "class";
	protected static final String PAGE_CONTEXT = "";
	protected static final String PANEL_CONTEXT = "panel:";
	protected static final String COMPONENT_CONTEXT = "panel:";
		
	protected WicketTester wicketTester;
	protected ComponentTestInjector injector;
	private T harness;
	private String pathContext;	
	

	@Before
	protected void setUp() throws Exception { 
		injector = ComponentTestInjector.make();
	}	
	
    protected void verifyMocks(Object... mocks) {
    	for (Object mock:mocks) { 
    		verify(mock);
    	}
    }

	public void initializeApplication(WebApplication application) {		
		wicketTester = new WicketTester(application);
		Application.get().getResourceSettings().addStringResourceLoader(new IStringResourceLoader() {
			@Override public String loadStringResource(Class<?> clazz, String key, Locale locale, String style) {return key;}			
			@Override public String loadStringResource(Component component, String key) { return key; }
		});				
	}	
		
	protected F createFixture(IFixtureFactory<F> factory) {
		return factory.createFixture("id");		
	}
	
	protected abstract T createHarness(String pathContext, IWicketTester wicketTester); 
	
	public WicketTest<T,F> wire(Object bean, String fieldName) {
		// wire up bean to all fields with given name.  
		injector.wire(fieldName, bean);
		return this;
	}

	public abstract void renderFixture(IFixtureFactory<F> factory);
		
	protected T getHarness() {
		if (harness==null) { 
			harness=createHarness(getPathContext(), this);
		}
		return harness;
	}			

	// use this if you expect the class to be *ONLY* the expected value, as opposed to having it 
	//   be one of many classes.   typically you'll use assertClassContains() instead.
	protected void assertClassExactly(String expected, Component component) {
		assertAttributeEquals(CLASS, expected, component);
	}
	
	protected void assertClassContains(String expected, Component component) {
		assertAttributeContains(CLASS, expected, component);
	}
	
	protected void assertInDocument(String expected) {
		String document = (wicketTester.getServletResponse()).getDocument();
		assertTrue(document.indexOf(expected)!=-1);
	}
	
	/**
	 *  CAVEAT : note that these attribute testing (specifically the TagTester) code has bugs. 
	 * if you try to get the component after an ajax event it won't find it.
	 * @see #assertAttributeContains(String, String, Component) 
	 */
	protected void assertAttributeEquals(String attribute, String expected, Component component) {
		TagTester tagTester = getWicketTester().getTagByWicketId(component.getId());
		String actual = tagTester.getAttribute(attribute);
		assertEquals(expected, actual);
	}

	/**
	 * @see #assertAttributeEquals(String, String, Component) for bug notes
	 */
	protected void assertAttributeContains(String attribute, String expected, Component component) {
		TagTester x = getWicketTester().getTagByWicketId(component.getId());
		String actual = x.getAttribute(attribute);
		assertNotNull(actual);
		assertTrue(actual.contains(expected));
	}
	
	private String getPathContext() {
		checkNotNull(pathContext);
		return pathContext;
	}
	
	protected void setPathContext(String pathContext) {
		this.pathContext = pathContext;
	}
	
	protected void assertInvisible(Component component) {
		assertNull("component should not be visible", component);
	}
	
	protected void assertErrorMessages(String[] messages) {
		getWicketTester().assertErrorMessages(messages);
	}
	
	protected void assertVisible(Component component) {
		assertNotNull(component);
	}
	
	protected void assertChecked(Boolean isChecked, CheckBox checkBox) {
		Assert.assertEquals(isChecked, checkBox.getModelObject());
	}
	
	protected void assertModelValue(Component component, Object modelObject) {
		assertEquals(component.getDefaultModelObject(), modelObject);
	}	

	protected void assertListView(ListView<List<?>> listView, List<?> expected ) {
		assertEquals(expected, listView.getList());
	}
	
	protected void assertDropDownChoices(List<? extends Serializable> expected, DropDownChoice<? extends Serializable> actual) {
		assertEquals(expected, actual.getChoices());
	}	
	
	protected void assertComponent(Component component, Class<? extends Component> clazz) {
		assertNotNull("can't find component ", component);
		assertTrue("expected component class " + clazz.getSimpleName() + " but was " + component.getClass(), clazz.isAssignableFrom(component.getClass()));
	}

	protected void assertLabel(Label label, String expectedLabelText) {
		assertEquals(expectedLabelText, label.getDefaultModelObjectAsString());
	}
	
	protected void assertRenderedPage(Class<? extends Page> clazz) {
		assertComponent(getWicketTester().getLastRenderedPage(), clazz );		
	}

	protected void assertContainsPropertyValue(MarkupContainer container, String value) {
		final List<String> values = new ArrayList<String>();
		
		container.visitChildren(new IVisitor<Component>() {
			@Override
			public Object component(Component component) {
				IModel<?> defaultModel = component.getDefaultModel();
				if (defaultModel instanceof PropertyModel) {
					PropertyModel<?> propertyModel = (PropertyModel<?>)defaultModel;
					Class<? extends Object> klass = propertyModel.getTarget().getClass();
					try {
						Method declaredMethod = klass.getDeclaredMethod(propertyModel.getPropertyGetter().getName(), new Class[0]);
						Object result = declaredMethod.invoke(propertyModel.getTarget(), new Object[0]);
						values.add((String)result);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return null;
			}
		});
		
		assertTrue("\"" + value + "\" not found", values.contains(value));
	}
	
	@Override
	public WicketTester getWicketTester() {
		Preconditions.checkNotNull(wicketTester, "you must call initializeApplication() in setup method (@Before) in order for test to run.");
		return wicketTester;
	}

	public <M> M wire(Class<M> typeToMock, String fieldName) {
		M mock = createMock(typeToMock);
		wire(mock,fieldName);
		return mock;
	}	

	protected <M> M wire(Class<M> typeToMock) {
		M mock = createMock(typeToMock);
		String fieldName = mock.getClass().getSimpleName();
		// note : classname will have all that mock gobbledygook appended. let's strip it off. 
		//   e.g.    User$$EnhancerByCGLIB  -->  User
		fieldName = fieldName.substring(0, fieldName.indexOf("$$EnhancerByCGLIB"));
		fieldName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, fieldName);
		wire(mock,fieldName);
		return mock;
	}	


	/*	@Override
		public void dumpFailureInfo(Description description, Failure failure) throws Exception {
			StringWriter sw = new StringWriter();
			try {
				PrintWriter pw = new PrintWriter(sw);
				pw.println("");
				pw.println("################################################################################");		
				collectFailureInfo(pw, description, failure);
				pw.println("################################################################################");
				pw.flush();
				LOGGER.error(sw.toString());
			} finally {
				sw.close();
			}		
		}
	*/	
	/*	protected void collectFailureInfo(PrintWriter pw, Description description, Failure failure) throws IOException {
			pw.println("##### FAILURE Wicket Test:" + description + " #####");
			if(failure != null) {
				pw.println("FAILURE: " + failure.getMessage());
			}
			
			String fileContents = getHarness().getLastRenderedPageAsHtml();
			File dumpedFile = dumpFile(description, fileContents);
			pw.println("\nRendered Html File =====> " + dumpedFile.getAbsolutePath() + "\n");
//			pw.println(fileContents);				
		}*/
		
	/*	private File dumpFile(Description description, String dump) throws IOException {
			String shortDescription=description.getDisplayName().substring(0,description.getDisplayName().indexOf("("));
			File file = File.createTempFile(shortDescription+System.currentTimeMillis(),".html" );
			FileOutputStream out = new FileOutputStream(file);
			try {
				out.write(dump.getBytes("UTF-8"));    	
				out.flush();
			} finally {
				out.close();
			}
			return file;
		}*/	
	
}
