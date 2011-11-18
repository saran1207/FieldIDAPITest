package com.n4systems.fieldid.service.certificate;

import java.io.File;
import java.io.FilenameFilter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;

import org.apache.commons.io.FileUtils;

public class ReportCompiler {
	
	private final FilenameFilter jrxmlFiler = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".jrxml");
		}
	};
	
	public void compileReports(File directory) throws JRException {
		for (File jrxmlFile: directory.listFiles(jrxmlFiler)) {
			String reportName = jrxmlFile.getName().replace(".jrxml", "");
			
			File jasperFile = new File(directory, reportName + ".jasper");
			
			if (!jasperFile.exists() || FileUtils.isFileNewer(jrxmlFile, jasperFile)) {
				JasperCompileManager.compileReportToFile(jrxmlFile.getAbsolutePath(), jasperFile.getAbsolutePath());
			}
		}
	}
	
}
