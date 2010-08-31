package com.plexus_online.inventory;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import javax.xml.bind.annotation.XmlSchema;

import org.junit.Test;



public class PackageAnnotationAppliedTest {

	
	@Test
	public void should_find_the_required_XML_Schema_annotation_on_the_package() throws Exception {
		assertThat(Package.getPackage("com.plexus_online.inventory").getAnnotation(XmlSchema.class), notNullValue());
	}
}
