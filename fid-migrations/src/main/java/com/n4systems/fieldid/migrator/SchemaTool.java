package com.n4systems.fieldid.migrator;

//import org.hibernate.ejb.Ejb3Configuration;
//import org.hibernate.tool.hbm2ddl.SchemaExport;

import java.util.List;
import java.util.Properties;

public class SchemaTool {
	
	public static void main(String[] args) {
		try {
			SchemaTool schemaGen = new SchemaTool();
			schemaGen.generateSchema();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void generateSchema() {
//		Properties hibernateProperties = new Properties();
//		hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
//		hibernateProperties.setProperty("hibernate.show_sql", "false");
//		hibernateProperties.setProperty("hibernate.format_sql", "true");
//		hibernateProperties.setProperty("hibernate.use_sql_comments", "false");
//
//		Ejb3Configuration config = new Ejb3Configuration();
//		config.configure("fieldid", hibernateProperties);
//
//		SchemaExport export = new SchemaExport(config.getHibernateConfiguration());
//		export.setDelimiter(";");
//		export.setHaltOnError(true);
//		export.setFormat(true);
//		export.execute(true, false, false, true);
//
//		if (export.getExceptions().size() > 0) {
//			System.err.println("Schema Export failed!");
//			for(Throwable t: (List<Throwable>)export.getExceptions()) {
//				t.printStackTrace(System.err);
//			}
//		}
	}
}
