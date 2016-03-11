package com.n4systems.fileprocessing.roberts;

import com.n4systems.exceptions.FileProcessingException;
import com.n4systems.tools.FileDataContainer;

import java.util.List;

public interface RobertsParser {
	public void extractData(FileDataContainer fileDataContainer, List<String> lines) throws FileProcessingException;
}
