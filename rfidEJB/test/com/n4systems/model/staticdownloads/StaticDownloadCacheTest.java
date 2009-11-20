package com.n4systems.model.staticdownloads;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.n4systems.util.serialization.Serializer;
import com.n4systems.util.serialization.XStreamSerializer;


public class StaticDownloadCacheTest {
	private static final String XML = "<list>" + 
		"<StaticDownload url=\"http://www.google.com\" name=\"Google\" desc=\"The Google Search Engine\" />" +
		"<StaticDownload url=\"http://www.xkcd.com\" name=\"XKCD\" desc=\"Web Comics\" />" +
		"</list>";
	
	@SuppressWarnings("serial")
	class TestStaticDownloadCache extends StaticDownloadCache {
		public TestStaticDownloadCache(File configFile, Serializer<List<StaticDownload>> serializer) {
			super(configFile, serializer);
		}
	}
	
	private File TEST_CONFIG;
	private List<StaticDownload> testDownloads = new ArrayList<StaticDownload>();
	
	@Before
	public void setup_test_downloads() throws IOException {
		testDownloads.add(new StaticDownload("Google", "http://www.google.com", "The Google Search Engine"));
		testDownloads.add(new StaticDownload("XKCD", "http://www.xkcd.com", "Web Comics"));
		
		TEST_CONFIG = File.createTempFile(getClass().getSimpleName(), null);
		
		Writer writer = null;
		try {
			writer = new FileWriter(TEST_CONFIG);
			writer.write(XML);
		} finally {
			IOUtils.closeQuietly(writer);
		}
		
		// set the mod date to 5 sec ago so that the reload test detects a change
		TEST_CONFIG.setLastModified((new Date()).getTime() - 5000);
	}
	
	@After
	public void remove_temp_file() {
		if (TEST_CONFIG.exists()) {
			TEST_CONFIG.delete();
		}
	}
	
	@Test
	public void test_reload_from_xml() {
		TestStaticDownloadCache dlCache = new TestStaticDownloadCache(TEST_CONFIG, new XStreamSerializer<List<StaticDownload>>(StaticDownload.class));
		
		List<StaticDownload> downloads = dlCache.getStaticDownloads();
		
		assertEquals(testDownloads, downloads);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_caches_after_load() {
		Serializer<List<StaticDownload>> mocSerializer = EasyMock.createMock(Serializer.class);
		
		TestStaticDownloadCache dlCache = new TestStaticDownloadCache(TEST_CONFIG, mocSerializer);
		
		EasyMock.expect(mocSerializer.deserialize((InputStream)EasyMock.anyObject())).andReturn(testDownloads);
		EasyMock.replay(mocSerializer);
		
		dlCache.getStaticDownloads();
		dlCache.getStaticDownloads();
		verify(mocSerializer);	
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void test_reloads_on_file_update() throws InterruptedException {
		Serializer<List<StaticDownload>> mocSerializer = EasyMock.createMock(Serializer.class);
		
		TestStaticDownloadCache dlCache = new TestStaticDownloadCache(TEST_CONFIG, mocSerializer);
		
		EasyMock.expect(mocSerializer.deserialize((InputStream)EasyMock.anyObject())).andReturn(testDownloads);
		EasyMock.expect(mocSerializer.deserialize((InputStream)EasyMock.anyObject())).andReturn(testDownloads);
		EasyMock.replay(mocSerializer);
		
		dlCache.getStaticDownloads();
		
		TEST_CONFIG.setLastModified((new Date()).getTime());
		dlCache.getStaticDownloads();
		verify(mocSerializer);	
	}
	
}
