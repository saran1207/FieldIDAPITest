package com.n4systems.model.downloadlink;

import static org.junit.Assert.*;

import java.io.File;
import java.util.UUID;

import org.junit.Test;

import rfid.ejb.entity.UserBean;

import com.n4systems.model.builders.UserBuilder;

public class DownloadLinkTest {

	@Test(expected=IllegalStateException.class)
	public void get_file_throws_exception_on_null_user() {
		DownloadLink link = new DownloadLink();
		link.setId(123L);
		link.getFile(false);
	}
	
	@Test(expected=IllegalStateException.class)
	public void get_file_throws_exception_on_null_id() {
		DownloadLink link = new DownloadLink();
		link.setUser(UserBuilder.anEmployee().build());
		link.getFile(false);
	}
	
	@SuppressWarnings("serial")
	@Test
	public void test_get_file_no_parent_create() {
		UserBean user = new UserBean() {
			public File getPrivateDir() {
				return new File(".");
			}
		};
		
		DownloadLink link = new DownloadLink();
		link.setId(123L);
		link.setUser(user);
		
		assertEquals("./000123.dl", link.getFile(false).getPath());
	}
	
	@SuppressWarnings("serial")
	@Test
	public void test_get_file_with_parent_create() {
		final File userDir = new File(System.getProperty("java.io.tmpdir"), "test_" + UUID.randomUUID().toString().substring(25));
		userDir.deleteOnExit();
		
		UserBean user = new UserBean() {
			public File getPrivateDir() {
				return userDir;
			}
		};
		
		DownloadLink link = new DownloadLink();
		link.setId(123L);
		link.setUser(user);
		
		File downloadFile = new File(userDir, "000123.dl");
		
		assertEquals(downloadFile, link.getFile());
		assertTrue(downloadFile.getParentFile().exists());
	}
	
	@Test(expected=IllegalStateException.class)
	public void test_prepare_file_name_throw_exception_on_null_name() {
		DownloadLink link = new DownloadLink();
		link.setContentType(ContentType.EXCEL);
		
		link.prepareFileName();
	}
	
	@Test(expected=IllegalStateException.class)
	public void test_prepare_file_name_throw_exception_on_null_content_type() {
		DownloadLink link = new DownloadLink();
		link.setName("Download Name");
		
		link.prepareFileName();
	}
	
	@Test
	public void test_prepare_file_name() {
		DownloadLink link = new DownloadLink();
		link.setName("Download Name");
		link.setContentType(ContentType.EXCEL);
		
		assertEquals("Download Name." + ContentType.EXCEL.getExtension(), link.prepareFileName());
	}
}
