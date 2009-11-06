package com.n4systems.fileprocessing.roberts;

import java.util.List;

import com.n4systems.exceptions.FileProcessingException;
import com.n4systems.tools.FileDataContainer;

public interface RobertsParser {
	public void extractData(FileDataContainer fileDataContainer, List<String> lines) throws FileProcessingException;
}
