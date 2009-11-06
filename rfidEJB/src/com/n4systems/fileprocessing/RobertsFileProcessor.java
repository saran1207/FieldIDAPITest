package com.n4systems.fileprocessing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.n4systems.exceptions.FileProcessingException;
import com.n4systems.fileprocessing.roberts.RobertsParser;
import com.n4systems.fileprocessing.roberts.V7RobertsParser;
import com.n4systems.fileprocessing.roberts.V8RobertsParser;
import com.n4systems.fileprocessing.roberts.V9RobertsParser;
import com.n4systems.tools.FileDataContainer;


public class RobertsFileProcessor extends FileProcessor {
	private static Logger logger = Logger.getLogger(RobertsFileProcessor.class);
	
	public RobertsFileProcessor() {}
	
	public void processFile(FileDataContainer fileDataContainer, InputStream file) throws FileProcessingException {
		if (file == null) {
			// TODO: This is the way this class has always operated.  Really it should simply catch the IOException and throw a FileProcessingException
			return;
		}
		
		logger.debug(String.format("Parsing Roberts file [%s]", fileDataContainer.getFileName()));
		
		try {
			List<String> lines = readAllLines(file);
			
			// version is always the first line in the file
			RobertsParser parser = getVersionParser(parseVersion(lines.get(0)));
			
			parser.extractData(fileDataContainer, lines);
			
		} catch (FileProcessingException e) {
			// need to catch and rethrow this so we don't double encapsulate the exception
			throw e;
		} catch (Exception e) {
			throw new FileProcessingException("Unable to parse Roberts file", e);
		}
	}
	
	private List<String> readAllLines(InputStream in) throws IOException {
		BufferedReader buff = null;
		List<String> lines = new ArrayList<String>();
		
		try {
			buff = new BufferedReader(new InputStreamReader(in));
			
			String line;
			while ((line = buff.readLine()) != null) {
				// trim now so we won't have to later
				lines.add(line.trim());
			}
		} finally {
			IOUtils.closeQuietly(buff);
		}
		
		return lines;
	}
	
	private RobertsParser getVersionParser(String version) throws FileProcessingException {
		RobertsParser parser;
		
		if (version.equals("7")) {
			logger.debug("Detected Roberts version 7");
			parser = new V7RobertsParser();
		} else if (version.equals("8")) {
			logger.debug("Detected Roberts version 8");
			parser = new V8RobertsParser();
		} else if (version.equals("9")) {
			logger.debug("Detected Roberts version 9");
			parser = new V9RobertsParser();
		} else {
			throw new FileProcessingException("Unknown Roberts version " + version);
		}
		
		return parser;
	}
	
	private String parseVersion(String versionLine) {
		return versionLine.substring(versionLine.indexOf('=') + 1, versionLine.length()).trim();
	}
	
}
