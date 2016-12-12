package com.n4systems.export;

import com.n4systems.model.Tenant;
import com.n4systems.model.tenant.TenantByNameLoader;
import com.n4systems.persistence.PersistenceManager;
import org.apache.commons.cli.*;
import org.apache.commons.io.IOUtils;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class Exporter {
	private static String defaultDbUrl = "jdbc:mysql://localhost/fieldid";
	private static String defaultDbUser = "fieldid-ro";
	private static String defaultDbPass = "makemore$";
	
	public static void main(String[] args) {
		System.setProperty("user.timezone", "UTC");

		try {
			CommandLine cmd = parseArgs(args);
			String url = cmd.getOptionValue("d", defaultDbUrl);
			String user = cmd.getOptionValue("u", defaultDbUser);
			String pass = cmd.getOptionValue("p", defaultDbPass);

			Exporter exporter = new Exporter(url, user, pass);
			exporter.export(cmd.getArgs()[0]);
		} catch (Throwable t) {
			t.printStackTrace(System.err);
			System.exit(1);
		}
	}

	private static CommandLine parseArgs(String[] args) throws ParseException {
		Options opts = new Options();		
		opts.addOption("d", true, "Database URL (Default: [" + defaultDbUrl + "]");
		opts.addOption("u", true, "Database User (Default: [" + defaultDbUser + "]");
		opts.addOption("p", true, "Database Pass (Default: [ma.....]");
		opts.addOption("h", "help", false, "This help message");

		CommandLine cmd = new PosixParser().parse(opts, args);
		
		if (cmd.hasOption("h")) {
			showHelp(opts);
		}

		if (cmd.getArgs().length == 0) {
			System.err.println("Must specisfy a TenantName");
			showHelp(opts);
		}
		return cmd;
	}
	
	private static void showHelp(Options opts) {
		HelpFormatter formatter = new HelpFormatter();
    	formatter.printHelp("./exporter [opts] <TenantName>", opts);
    	System.exit(0);
	}
	
	private Exporter(String dbUrl, String dbUser, String dbPass) {
		configurePersistence(dbUrl, dbUser, dbPass);
	}
	
	private void configurePersistence(String dbUrl, String dbUser, String dbPass) {
		System.out.println(String.format("Using DB URL [%s], User [%s]", dbUrl, dbUser));
		PersistenceManager.persistenceUnit = PersistenceManager.TESTING_PERSISTENCE_UNIT;
        PersistenceManager.properties.put("hibernate.connection.url", dbUrl);
        PersistenceManager.properties.put("hibernate.connection.username", dbUser);
        PersistenceManager.properties.put("hibernate.connection.password", dbPass);
	}
	
	public void export(String tenantName) throws Exception {
		Tenant tenant = loadTenant(tenantName);
		if (tenant == null) {
			throw new IllegalArgumentException("Could not find tenant for name [" + tenantName + "]");
		}
		System.out.println("Found Tenant: " + tenant);
		
        OutputStream out = null;
        try {
        	String outFile = createFileName(tenantName);
        	System.out.println("Exporting to: " + outFile);
        	out = new FileOutputStream(outFile);
        	ExportTask export = new ExportTask(tenant.getId(), out);
        	export.doExport();
        } finally {
        	IOUtils.closeQuietly(out);
        }
	}
	
	private Tenant loadTenant(String tenantName) {
    	Tenant tenant = ReadonlyTransactionExecutor.load(new TenantByNameLoader().setTenantName(tenantName));
    	return tenant;
	}

	private String createFileName(String tenantName) {
		return tenantName + ".xml";
	}
}
