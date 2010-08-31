package com.n4systems.tools;

import java.io.File;
import java.util.List;
import java.util.Properties;

import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

public class SchemaTool {
	
	private String oldFile = "schema/ddl-schema-last.sql";
	private String outputFile = "schema/ddl-schema.sql";
	
	public SchemaTool() {}
	
	public static void main(String[] args) {
		try {
			
			SchemaTool schemaGen = new SchemaTool();
			schemaGen.moveOldFile();
			schemaGen.generateSchema();
			
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public void moveOldFile() {
		File schemaFile = new File(outputFile);
		File schemaFileOld = new File(oldFile);
		
		if(schemaFile.exists()) {
			schemaFile.renameTo(schemaFileOld);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void generateSchema() {
		Properties hibernateProperties = new Properties();
		
		hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
		hibernateProperties.setProperty("hibernate.show_sql", "false");
		hibernateProperties.setProperty("hibernate.format_sql", "true");
		hibernateProperties.setProperty("hibernate.use_sql_comments", "false");
		
		Ejb3Configuration config = new Ejb3Configuration();
		
		config.configure("fieldid", hibernateProperties);
		
		SchemaExport export = new SchemaExport(config.getHibernateConfiguration());
		
		export.setOutputFile(getOutputFile());
		export.setDelimiter(";");
		export.setHaltOnError(true);
		export.setFormat(true);

		export.create(true, false);

		if (export.getExceptions().size() > 0) {
			System.err.println("Schema Export failed!");
			for(Throwable t: (List<Throwable>)export.getExceptions()) {
				t.printStackTrace(System.err);
			}
		}
	}
	
	public String getOutputFile() {
		return outputFile;
	}

	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}
}
