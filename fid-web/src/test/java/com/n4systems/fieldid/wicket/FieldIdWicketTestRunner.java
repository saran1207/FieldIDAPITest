package com.n4systems.fieldid.wicket;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.google.common.base.CaseFormat;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.wicket.WicketTest.FieldIdWicketTester;


public class FieldIdWicketTestRunner extends Suite {
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface WithUsers {
		public TestUser[] value() default {};
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface WithLocale {
		public String[] value() default {};   
	}
	
	
	
	
	private final ArrayList<Runner> runners= new ArrayList<Runner>();
	private String testContextOverride;
	private ThreadLocal<FieldIdWicketTester> wicketTester = new ThreadLocal<FieldIdWicketTester>();
	

	public FieldIdWicketTestRunner(Class<?> klass) throws Throwable {
		super(klass, Collections.<Runner>emptyList());
		testContextOverride = System.getProperty("testContext", null);
		runners.add(new TestClassRunner(getTestClass().getJavaClass(),FieldIdWicketTestContext.DEFAULT_TEST_CONTEXT));
		for (FieldIdWicketTestContext context:getWicketTestContexts()) { 			
			runners.add(new TestClassRunner(getTestClass().getJavaClass(),context));		
		}		
	}

	private List<FieldIdWicketTestContext> getWicketTestContexts() {
		List<FieldIdWicketTestContext> result = Lists.newArrayList();
		Class<?> testClass = getTestClass().getJavaClass();
		Method[] methods = testClass.getMethods();
		for (Method method:methods) { 
			TestUser[] testUsers = getTestUsersFor(method);
			if (testUsers!=null) { 
				result.addAll(createWicketTestContexts(method, testUsers));
			}
		}
		return result;
	}

	private TestUser[] getTestUsersFor(Method method) {
		WithUsers withUsers = method.getAnnotation(WithUsers.class);
		TestUser[] overridenWithUsers = getTestContextOverride();
		if (overridenWithUsers!=null && withUsers!=null) {
			return overridenWithUsers;
		} else { 
			return withUsers==null ? null : withUsers.value();
		}
	}

	private TestUser[] getTestContextOverride() {
		if (testContextOverride==null) {
			return null;
		} else if (testContextOverride.equalsIgnoreCase("ALL")){
			return TestUser.values();
		} else if (testContextOverride.equalsIgnoreCase("NONE"))  {
			return new TestUser[] {};
		} else {
			return parseTestContextOverrides();
		}
	}

	private TestUser[] parseTestContextOverrides() {
		List<TestUser> result = Lists.newArrayList();
		try { 
			String[] users = testContextOverride.split(",");
			for (String user:users) {
				System.out.println("Overriding context : setting user " + user);
				// e.g. user inputs "NoPermissions, gets translated to NO_PERMISSIONS".
				user = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, user.trim());				
				result.add(TestUser.valueOf(user));
			}
		} catch (Exception e) {  // null pointer or invalidArg
			System.out.println("ERROR : couldn't override context (probably not found in " + TestUser.class.getSimpleName());
			return null;
		}
		return result.toArray(new TestUser[]{});
	}

	private List<FieldIdWicketTestContext> createWicketTestContexts(Method method, TestUser... users) {
		ArrayList<FieldIdWicketTestContext> result = Lists.newArrayList();
		for (TestUser user:users) { 
			result.add(new FieldIdWicketTestContext(method,user));
		}
		return result;
	}

	@Override
	protected List<Runner> getChildren() {
		return runners;
	}


	
	private class TestClassRunner extends BlockJUnit4ClassRunner {

		private FieldIdWicketTestContext context;
		
		TestClassRunner(Class<?> type, FieldIdWicketTestContext context) throws InitializationError {
			super(type);
			this.context = context;
		}

		@Override  
	    protected Statement methodBlock(final FrameworkMethod method) {  
	        return new Statement() {  
	            @Override public void evaluate() throws Throwable {
                    TestClassRunner.super.methodBlock(method).evaluate();  
	            }
	        };
		}
		
		@Override
		protected List<FrameworkMethod> getChildren() {
			List<FrameworkMethod> children = super.getChildren();
			Predicate<FrameworkMethod> filter = new Predicate<FrameworkMethod>() {
		        @Override
		        public boolean apply(FrameworkMethod input) {
		        	WithUsers testUsers = input.getMethod().getAnnotation(WithUsers.class);
		        	// either DEFAULT_CONTEXT for non-annotated methods OR
		        	//        custom context for annotated methods.
		            return (context.equals(FieldIdWicketTestContext.DEFAULT_TEST_CONTEXT) && testUsers==null) || 
		            	(testUsers!=null && input.getMethod().equals(context.getMethod()));  
		        }
		    };
		    return ImmutableList.copyOf(Iterables.filter(children, filter));
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Object createTest() throws Exception {
			Object instance = getTestClass().getOnlyConstructor().newInstance();
			if (instance instanceof WicketTest) {
				WicketTest<?, ?, FieldIdWicketTestContext> wicketTest = ((WicketTest<?,?,FieldIdWicketTestContext>) instance);
				if (getWicketTester()==null) {   
					setWicketTester(wicketTest.createWicketTesterForRunner());
				}
				wicketTest.initializeForRunner(getWicketTester(), context);
				return instance;
			}
			throw new IllegalArgumentException("your test class must be of type " + WicketTest.class.getSimpleName() + " for this runner.");
		}

		private void setWicketTester(FieldIdWicketTester tester) {
			wicketTester.set(tester);
		}

		private FieldIdWicketTester getWicketTester() {			
			return wicketTester.get();
		}

		private FieldIdWicketTestContext getApp() {
			return null;
		}

		@Override
		protected String getName() {
			return String.format("[%s]", context);
		}

		@Override
		protected String testName(final FrameworkMethod method) {
			return String.format("%s[%s]", method.getName(),
					context);
		}

		@Override
		protected void validateConstructor(List<Throwable> errors) {
			validateOnlyOneConstructor(errors);
		}

		@Override
		protected Statement classBlock(RunNotifier notifier) {
			return childrenInvoker(notifier);
		}
	}

	
}
