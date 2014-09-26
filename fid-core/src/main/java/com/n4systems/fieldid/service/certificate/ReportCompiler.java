package com.n4systems.fieldid.service.certificate;

import net.sf.jasperreports.engine.JRException;

import java.io.File;
import java.io.FilenameFilter;

public class ReportCompiler {
	
	private final FilenameFilter jrxmlFiler = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".jrxml");
		}
	};
	
	public void compileReports(File directory) throws JRException {

        //We will no longer compile the jaspers through FieldiD.  The assumption is that the jasper file will exist on S3.

        /*
        for (File jrxmlFile: directory.listFiles(jrxmlFiler)) {
			String reportName = jrxmlFile.getName().replace(".jrxml", "");

			File jasperFile = new File(directory, reportName + ".jasper");

			if (!jasperFile.exists() || FileUtils.isFileNewer(jrxmlFile, jasperFile)) {
				JasperCompileManager.compileReportToFile(jrxmlFile.getAbsolutePath(), jasperFile.getAbsolutePath());
			}
		}*/
	}
	
}