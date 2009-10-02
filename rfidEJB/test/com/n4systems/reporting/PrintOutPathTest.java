package com.n4systems.reporting;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.PrintOut;
import com.n4systems.model.Tenant;
import com.n4systems.model.PrintOut.PrintOutType;

public class PrintOutPathTest {

	private static String REPORT_DIR = "/var/fieldid/private/reports";
	private static String DEFAULT_DIR = REPORT_DIR + "/all_tenants";
	
	private PrintOut printOut;
	private Tenant tenant;

	@Before
	public void setUp() throws Exception {
		printOut = new PrintOut();
		printOut.setPdfTemplate("default_inspection_cert");
		printOut.setType(PrintOutType.CERT);
		printOut.setCustom(false);
		tenant = new Tenant();
		tenant.setName("tenant1");
	}

	@After
	public void tearDown() throws Exception {
	}
	
	private String getAbsolutePathFor(String fullFileName) {
		File file = new File(fullFileName);
		return file.getAbsolutePath();
	}

	@Test
	public void test_get_report_file_for_inspection_type_that_is_printable() {
		File file = PathHandler.getPrintOutFile(printOut);
		assertEquals(getAbsolutePathFor(DEFAULT_DIR+ "/default_inspection_cert.jasper"), file.getAbsolutePath());
	}
	
	@Test
	public void test_get_report_file_for_inspection_type_that_is_custom() {
		createCustomPrintOut();
		
		File file = PathHandler.getPrintOutFile(printOut);
		assertEquals(getAbsolutePathFor(REPORT_DIR + "/" + tenant.getName() + "/custom_inspection_cert.jasper"), file.getAbsolutePath());
	}

	private void createCustomPrintOut() {
		printOut.setCustom(true);
		printOut.setTenant(tenant);
		printOut.setPdfTemplate("custom_inspection_cert");
	}
	
	
	@Test
	public void test_get_preview_image_for_default_print_out() {
		File file = PathHandler.getPreviewImage(printOut);
		assertEquals(getAbsolutePathFor(DEFAULT_DIR + "/default_inspection_cert_preview.jpg"), file.getAbsolutePath());
	}
	
	@Test
	public void test_get_preview_image_for_custom_print_out() {
		createCustomPrintOut();
		File file = PathHandler.getPreviewImage(printOut);
		
		assertEquals(getAbsolutePathFor(REPORT_DIR + "/" + tenant.getName() +"/custom_inspection_cert_preview.jpg"), file.getAbsolutePath());
	}
	
	
	@Test
	public void test_get_preview_thumb_for_default_print_out() {
		File file = PathHandler.getPreviewThumb(printOut);
		assertEquals(getAbsolutePathFor(DEFAULT_DIR + "/default_inspection_cert_thumb.jpg"), file.getAbsolutePath());
	}
	
	@Test
	public void test_get_preview_thumb_for_custom_print_out() {
		createCustomPrintOut();
		File file = PathHandler.getPreviewThumb(printOut);
		
		assertEquals(getAbsolutePathFor(REPORT_DIR + "/" + tenant.getName() +"/custom_inspection_cert_thumb.jpg"), file.getAbsolutePath());
	}
	
	

}
