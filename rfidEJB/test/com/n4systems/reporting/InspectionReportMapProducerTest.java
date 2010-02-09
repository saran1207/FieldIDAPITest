package com.n4systems.reporting;

import static com.n4systems.model.builders.InspectionBuilder.*;
import static com.n4systems.model.builders.InspectionTypeBuilder.*;
import static com.n4systems.model.builders.SubInspectionBuilder.*;

import java.util.TimeZone;

import org.junit.Test;

import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.SubInspection;
import com.n4systems.model.builders.ProductBuilder;
import com.n4systems.model.utils.DateTimeDefiner;
import com.n4systems.test.helpers.Asserts;
import com.n4systems.util.ReportMap;

public class InspectionReportMapProducerTest {

	@Test
	public void test_sub_inspetion_map_creation() {
		InspectionType inspectionType = anInspectionType().named("test").build();
		SubInspection targetInspection = aSubInspection("bob").withType(inspectionType).build();
		DateTimeDefiner d = new DateTimeDefiner("yyyy-MM-dd", TimeZone.getDefault());
		
		ReportMap<Object> expectedReportMap = new ReportMap<Object>();
		expectedReportMap.put("productLabel", "bob");
		expectedReportMap.put("type", "test");
		ReportMapProducer sut = new InspectionReportMapProducer(targetInspection, d);
		ReportMap<Object> actualReportMap = sut.produceMap();
		
		Asserts.assertConatainsExpectedValues(expectedReportMap, actualReportMap);
	}

	@Test
	public void test_inspetion_map_creation() {
		InspectionType inspectionType = anInspectionType().named("test").build();
		Product targetProduct = ProductBuilder.aProduct().build();
		Inspection targetInspection = anInspection().ofType(inspectionType).on(targetProduct).build();
		DateTimeDefiner d = new DateTimeDefiner("yyyy-MM-dd", TimeZone.getDefault());
		
		ReportMap<Object> expectedReportMap = new ReportMap<Object>();
		expectedReportMap.put("productLabel", null);
		expectedReportMap.put("type", "test");
		ReportMapProducer sut = new InspectionReportMapProducer(targetInspection, d);
		ReportMap<Object> actualReportMap = sut.produceMap();
		
		Asserts.assertConatainsExpectedValues(expectedReportMap, actualReportMap);
	}
	
	
	
	
}
