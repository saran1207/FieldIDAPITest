package com.n4systems.fileprocessing;


/*
 * XXX - this factory is no longer necessary as the UploadFileType enum now covers this functionality
 */
public class FileProcessorFactory {
	
	/** 
	 * Returns the appropriate file processor to deal with the given file type
	 * @param eventType
	 * @return
	 */
	public static FileProcessor getFileProcessor(String fileType) {

		if (fileType == null) return null;
		
		if (fileType.equals("roberts")) {
			return new RobertsFileProcessor();
		}
		
		if (fileType.equals("nationalautomation")) {
			return new NationalAutomationFileProcessor();
		}
		
		if (fileType.equals("chant")) {
			return new ChantFileProcessor();
		}
		
		if (fileType.equals("wirop")) {
			return new WiropFileProcessor();
		}
		
		return null;
		
	}

}
