package com.n4systems.model.staticdownloads;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;

import com.n4systems.reporting.PathHandler;
import com.n4systems.util.serialization.Serializer;
import com.n4systems.util.serialization.XStreamSerializer;

public class StaticDownloadCache implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final long NEVER = -1;
	private static final String CONFIG_FILE_NAME = "staticDownloads.xml";
	private static Logger logger = Logger.getLogger(StaticDownloadCache.class); 
	
	private static StaticDownloadCache self;
	
	public static synchronized StaticDownloadCache getInstance() {
		if (self == null) {
			self = new StaticDownloadCache();
		}
		return self;
	}
	
	public static synchronized void setInstance(StaticDownloadCache cache) {
		self = cache;
	}
	
	private final Serializer<List<StaticDownload>> serializer;
	private final File configFile;
	
	private List<StaticDownload> staticDownloads = new CopyOnWriteArrayList<StaticDownload>();

	private transient long lastReload = NEVER;
	
	protected StaticDownloadCache(File configFile, Serializer<List<StaticDownload>> serializer) {
		this.configFile = configFile;
		this.serializer = serializer;
	}
	
	protected StaticDownloadCache() {
		this(
			PathHandler.getCommonConfigFile(CONFIG_FILE_NAME), 
			new XStreamSerializer<List<StaticDownload>>(StaticDownload.class)
		);
	}
	
	public List<StaticDownload> getStaticDownloads() { 
		if (reloadNow()) {
			reloadStaticDownloads();
		}
		return staticDownloads;
	}
	
	private boolean reloadNow() {
		return (configFile.lastModified() > lastReload);
	}
	
	private synchronized void reloadStaticDownloads() {
		InputStream configIn = null;
		try {
			configIn = new BufferedInputStream(new FileInputStream(configFile));
		
			List<StaticDownload> downloadList = serializer.deserialize(configIn);
			
			staticDownloads.clear();
			staticDownloads.addAll(downloadList);
			lastReload = configFile.lastModified();
		} catch(Exception e) {
			logger.error("Could not realod the StaticDownloadCache", e);
		} finally {
			IOUtils.closeQuietly(configIn);
		}
	}

}
