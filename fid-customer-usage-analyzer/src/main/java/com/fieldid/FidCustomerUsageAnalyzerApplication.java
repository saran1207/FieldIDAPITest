package com.fieldid;

import com.fieldid.service.AnalyzerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FidCustomerUsageAnalyzerApplication implements CommandLineRunner {
	private static final Logger logger = Logger.getLogger(FidCustomerUsageAnalyzerApplication.class);

	@Autowired
	private AnalyzerService analyzerService;

	public static void main(String[] args) {
		SpringApplication.run(FidCustomerUsageAnalyzerApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		logger.info("Let's do some analysis!!");

		analyzerService.analyze();
	}
}
