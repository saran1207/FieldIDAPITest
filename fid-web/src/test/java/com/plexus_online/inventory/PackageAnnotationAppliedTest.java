package com.plexus_online.inventory;

import org.junit.Test;

import javax.xml.bind.annotation.XmlSchema;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;



public class PackageAnnotationAppliedTest {

	
	@Test
	public void should_find_the_required_XML_Schema_annotation_on_the_package() throws Exception {
		assertThat(Package.getPackage("com.plexus_online.inventory").getAnnotation(XmlSchema.class), notNullValue());
	}
}
