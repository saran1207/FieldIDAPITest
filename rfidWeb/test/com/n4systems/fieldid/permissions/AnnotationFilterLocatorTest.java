package com.n4systems.fieldid.permissions;

import static org.junit.Assert.*;

import org.junit.Test;

import com.n4systems.model.ExtendedFeature;


public class AnnotationFilterLocatorTest {

	
	@Test
	public void should_find_annotation_on_class() throws Exception {
		AnnotationFilterLocator<ExtendedFeatureFilter> sut = new AnnotationFilterLocator<ExtendedFeatureFilter>(PermissionRequiredAction.class, ExtendedFeatureFilter.class);
		assertNotNull(sut.getClassAnnotation());
	}
	
	
	@Test
	public void should_not_find_annotation_on_class() throws Exception {
		AnnotationFilterLocator<ExtendedFeatureFilter> sut = new AnnotationFilterLocator<ExtendedFeatureFilter>(NoPermissionRequiredAction.class, ExtendedFeatureFilter.class);
		assertNull(sut.getClassAnnotation());
	}
	
	@Test
	public void should_find_annotation_on_method() throws Exception {
		AnnotationFilterLocator<ExtendedFeatureFilter> sut = new AnnotationFilterLocator<ExtendedFeatureFilter>(PermissionRequiredAction.class, ExtendedFeatureFilter.class);
		assertNotNull(sut.getMethodAnnotation("methodWithPermission"));
	}
	
	@Test
	public void should_not_find_annotation_on_method() throws Exception {
		AnnotationFilterLocator<ExtendedFeatureFilter> sut = new AnnotationFilterLocator<ExtendedFeatureFilter>(PermissionRequiredAction.class, ExtendedFeatureFilter.class);
		assertNull(sut.getMethodAnnotation("methodWithOutPermission"));
	}
	
	
	@Test
	public void should_find_the_annotation_from_the_method() throws Exception {
		AnnotationFilterLocator<ExtendedFeatureFilter> sut = new AnnotationFilterLocator<ExtendedFeatureFilter>(PermissionRequiredAction.class, ExtendedFeatureFilter.class);
		assertEquals(ExtendedFeature.JobSites, sut.getFilter("methodWithPermission").requiredFeature());
	}
	
	@Test
	public void should_find_the_annotation_from_the_class() throws Exception {
		AnnotationFilterLocator<ExtendedFeatureFilter> sut = new AnnotationFilterLocator<ExtendedFeatureFilter>(PermissionRequiredAction.class, ExtendedFeatureFilter.class);
		assertEquals(ExtendedFeature.Branding, sut.getFilter("methodWithOutPermission").requiredFeature());
	}
	
	@Test
	public void should_not_find_any_annotations() throws Exception {
		AnnotationFilterLocator<ExtendedFeatureFilter> sut = new AnnotationFilterLocator<ExtendedFeatureFilter>(NoPermissionRequiredAction.class, ExtendedFeatureFilter.class);
		assertNull(sut.getFilter("methodWithOutPermission"));
	}
	
	
	@SuppressWarnings("unused")
	@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.Branding)
	private class PermissionRequiredAction {
		@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.JobSites)
		public void methodWithPermission() {
		}
		
		public void methodWithOutPermission() {
		}
	}
	
	@SuppressWarnings("unused")
	private class NoPermissionRequiredAction {
		public void methodWithOutPermission() {
		}
	}
}
