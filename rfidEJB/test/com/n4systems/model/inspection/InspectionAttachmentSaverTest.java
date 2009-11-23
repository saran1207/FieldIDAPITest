package com.n4systems.model.inspection;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;

import javax.persistence.EntityManager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.exceptions.NotImplementedException;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Inspection;
import com.n4systems.model.SubInspection;
import com.n4systems.model.builders.FileAttachmentBuilder;
import com.n4systems.model.builders.InspectionBuilder;
import com.n4systems.model.builders.SubInspectionBuilder;
import com.n4systems.reporting.PathHandler;
import com.n4systems.testutils.DummyEntityManager;
import com.n4systems.testutils.TestConfigContext;
import com.n4systems.testutils.TestHelper;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;

public class InspectionAttachmentSaverTest {
	private File appRoot;
	private InspectionAttachmentSaver saver;
	private Inspection inspection;
	private SubInspection subInspection;
	private FileAttachment attachment;
	private byte[] attachmentData;
	
	private void setup_config_context() {
		appRoot = new File(System.getProperty("java.io.tmpdir"), TestHelper.randomString());
		
		TestConfigContext.newContext().setEntry(ConfigEntry.GLOBAL_APPLICATION_ROOT, appRoot.getPath());
		assertEquals("ConfigContext isn't setup properly,", appRoot, ConfigContext.getCurrentContext().getAppRoot());
	}
	
	@Before
	public void setup_test_config() {
		setup_config_context();
		
		attachment = FileAttachmentBuilder.aFileAttachment().build();
		attachmentData = TestHelper.randomString().getBytes();
		
		subInspection = SubInspectionBuilder.aSubInspection("bleh").build();
		subInspection.setCreated(new Date());
		subInspection.setTenant(attachment.getTenant());
		
		inspection = InspectionBuilder.anInspection().withSubInspections(Arrays.asList(subInspection)).build();
		inspection.setCreated(subInspection.getCreated());
		inspection.setTenant(attachment.getTenant());
		
		saver = new InspectionAttachmentSaver();
		saver.setData(attachmentData);
		saver.setInspection(inspection);
	}
	
	@After
	public void cleanup_temp_files() throws IOException {
		if (appRoot.exists()) {
			FileUtils.deleteDirectory(appRoot);
		}
	}
	
	@Test
	public void test_saves_against_regular_inspection() throws IOException {		
		EntityManager em = EasyMock.createMock(EntityManager.class);
		em.persist(attachment);
		EasyMock.expect(em.merge(inspection)).andReturn(inspection);
		EasyMock.replay(em);
		
		saver.save(em, attachment);
		EasyMock.verify(em);
		
		assertTrue(inspection.getAttachments().contains(attachment));
		
		verifyData(PathHandler.getInspectionAttachmentFile(inspection, attachment));
	}
	
	@Test
	public void test_saves_against_sub_inspection() throws IOException {
		saver.setSubInspection(subInspection);
		
		EntityManager em = EasyMock.createMock(EntityManager.class);
		em.persist(attachment);
		EasyMock.expect(em.merge(subInspection)).andReturn(subInspection);
		EasyMock.replay(em);
		
		saver.save(em, attachment);
		EasyMock.verify(em);
		
		assertTrue(subInspection.getAttachments().contains(attachment));
		
		verifyData(PathHandler.getInspectionAttachmentFile(inspection, subInspection, attachment));
	}
	
	@Test(expected=NotImplementedException.class)
	public void update_throws_exception() {
		saver.update(new DummyEntityManager(), attachment);
	}
	
	@Test(expected=NotImplementedException.class)
	public void remove_throws_exception() {
		saver.remove(new DummyEntityManager(), attachment);
	}
	
	private void verifyData(File attachment) throws IOException {
		InputStream in = null;
		byte[] writtenData = null;
		
		try {
			in = new FileInputStream(attachment);
			writtenData = IOUtils.toByteArray(in);
		} finally {
			IOUtils.closeQuietly(in);
		}
		
		assertArrayEquals(attachmentData, writtenData);
	}
}
