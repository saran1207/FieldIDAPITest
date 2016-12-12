package com.n4systems.fieldid.selenium;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

import java.io.File;
import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PackageJunitRunner extends ParentRunner<Runner> {
	
	private List<Runner> children;
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@Inherited
	public static @interface SuitePackage {
		/**
		 * @return the package containing the test cases to be run
		 */
		public String value();
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@Inherited
	public static @interface BeforePackage { }

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@Inherited
	public @interface AfterPackage { }

	private static String getTestPackage(Class<?> klass) throws InitializationError {
		SuitePackage annotation= klass.getAnnotation(SuitePackage.class);
		if (annotation == null)
			throw new InitializationError(String.format("class '%s' must have a SuitePackage annotation", klass.getName()));
		return annotation.value();
	}
	
	private static Class<?>[] getTestClasses(String basePackage) throws Exception {
		URL location = PackageJunitRunner.class.getProtectionDomain().getCodeSource().getLocation();
		File f = new File(location.getFile());
		
		List<Class<?>> testClasses = traverseDirectory(basePackage, f, f);
		return testClasses.toArray(new Class[testClasses.size()]);
	}

	public PackageJunitRunner(Class<?> klass, RunnerBuilder builder) throws Exception {
		this(builder, klass, getTestClasses(getTestPackage(klass)));
	}

	protected PackageJunitRunner(RunnerBuilder builder, Class<?> klass, Class<?>[] suiteClasses) throws InitializationError {
		this(klass, builder.runners(klass, suiteClasses));
	}
	
	protected PackageJunitRunner(Class<?> klass, List<Runner> runners) throws InitializationError {
		super(klass);
		this.children = runners;
	}

	public static List<Class<?>> traverseDirectory(String basePackageName, File baseDirectory, File directory) throws Exception {
		List<Class<?>> result = new ArrayList<Class<?>>();
		
		if (!directory.exists() || !directory.isDirectory() || directory.listFiles() == null) {
			return Collections.<Class<?>>emptyList();
		}
		
		for (File f : directory.listFiles()) {
			if (f.isDirectory()) {
				result.addAll(traverseDirectory(basePackageName, baseDirectory, f));
			}
			else if (f.isFile() && f.getName().endsWith(".class")) {
				String fileName = f.getAbsolutePath();
				int baseLength = baseDirectory.getAbsolutePath().length();
				String classFileName = fileName.substring(baseLength + 1).replaceAll(File.separator, ".");
				String className = classFileName.substring(0, classFileName.lastIndexOf("."));
				if (!className.startsWith(basePackageName)) {
					continue;
				}
				Class<?> clazz = Class.forName(className);
				if (!Modifier.isAbstract(clazz.getModifiers()) && hasAtLeastOneTestMethod(clazz) ) {
					result.add(clazz);
				}
			}
		}
		
		return result;
	}

	private static boolean hasAtLeastOneTestMethod(Class<?> clazz) {
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			if (method.getAnnotation(Test.class) != null) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected List<Runner> getChildren() {
		return children;
	}

	@Override
	protected Description describeChild(Runner child) {
		return child.getDescription();
	}

	@Override
	protected void runChild(Runner child, RunNotifier notifier) {
		child.run(notifier);
	}

	@Override
	public void run(RunNotifier notifier) {
		runMethodsWithAnnotation(BeforePackage.class, notifier);
		super.run(notifier);
		runMethodsWithAnnotation(AfterPackage.class, notifier);
	}

	private void runMethodsWithAnnotation(Class<? extends Annotation> annot, RunNotifier notifier) {
		Class<?> clazz = getTestClass().getJavaClass();
		for (Method method : clazz.getDeclaredMethods()) {
			if (Modifier.isStatic(method.getModifiers()) && method.getAnnotation(annot) != null) {
				try {
					method.invoke(null);
				} catch (Throwable e) {
                    notifier.fireTestFailure(new Failure(getDescription(), e));
				}
			}
		}
	}
	
}
