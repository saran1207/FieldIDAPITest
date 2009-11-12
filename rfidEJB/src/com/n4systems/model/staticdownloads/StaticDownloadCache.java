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
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

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
	
	private final File configFile;
	
	private List<StaticDownload> staticDownloads = new CopyOnWriteArrayList<StaticDownload>();

	private transient long lastReload = NEVER;
	
	private StaticDownloadCache() {
		configFile = PathHandler.getCommonConfigFile(CONFIG_FILE_NAME);
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
	
	@SuppressWarnings("unchecked")
	private synchronized void reloadStaticDownloads() {
		XStream xstream = new XStream(new DomDriver());
		xstream.autodetectAnnotations(true);
		xstream.processAnnotations(StaticDownload.class);

		InputStream configIn = null;
		try {
			configIn = new BufferedInputStream(new FileInputStream(configFile));
		
			List<StaticDownload> downloadList = (List<StaticDownload>)xstream.fromXML(configIn);
			
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
