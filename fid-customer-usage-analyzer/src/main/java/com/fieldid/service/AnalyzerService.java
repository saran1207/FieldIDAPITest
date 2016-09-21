package com.fieldid.service;

import com.fieldid.model.CountByTenant;
import com.fieldid.service.asset.AssetUsageAnalyzerService;
import com.fieldid.service.event.EventUsageAnalyzerService;
import com.fieldid.service.procedure.ProcedureUsageAnalyzerService;
import com.fieldid.service.procedure.definition.ProcedureDefinitionUsageAnalyzerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 *
 * Created by Jordan Heath on 2016-09-20.
 */
@Service
public class AnalyzerService {
    private static final Logger logger = Logger.getLogger(AnalyzerService.class);

    private AssetUsageAnalyzerService assetService;
    private EventUsageAnalyzerService eventService;
    private ProcedureUsageAnalyzerService procedureService;
    private ProcedureDefinitionUsageAnalyzerService procedureDefinitionService;

    @Autowired
    public AnalyzerService(AssetUsageAnalyzerService assetService,
                           EventUsageAnalyzerService eventService,
                           ProcedureUsageAnalyzerService procedureService,
                           ProcedureDefinitionUsageAnalyzerService procedureDefinitionService) {
        this.assetService = assetService;
        this.eventService = eventService;
        this.procedureService = procedureService;
        this.procedureDefinitionService = procedureDefinitionService;
    }

    public void analyze() {
        analyzeAssets();
        analyzeEvents();
        analyzeProcedureDefinitions();
        analyzeProcedures();
    }

    private void analyzeProcedures() {
        logger.info("Procedures Analysis Start");

        logger.info("Since the beginning of time...");
        printResults(procedureService.getTotalProcedureCountByTenant());

        logger.info("In the last month...");
        printResults(procedureService.getPastMonthProcedureCountByTenant());

        for (int i = 0; i < 13; i++) {
            printResults(procedureService.getMonthlyProcedureCountByTenant(i));
        }

        logger.info("Procedures Analysis End");
    }

    private void analyzeProcedureDefinitions() {
        logger.info("Procedure Definitions Analysis Start");

        logger.info("Since the beginning of time...");
        printResults(procedureDefinitionService.getTotalProcedureDefinitionCountByTenant());

        logger.info("In the last month...");
        printResults(procedureDefinitionService.getPastMonthProcedureDefinitionCountByTenant());

        logger.info("Procedure Definitions Analysis End");
    }

    private void analyzeEvents() {
        logger.info("Event Analysis Start");

        logger.info("Since the beginning of time...");
        printResults(eventService.getTotalEventCountByTenant());

        logger.info("In the last month...");
        printResults(eventService.getPastMonthEventCountByTenant());

        logger.info("In the past 12 months...");
        for (int i = 0; i < 13; i++) {
            eventService.getTotalEventCountByTenantByMonth(i);
        }

        logger.info("Event Analysis End");
    }

    private void analyzeAssets() {
        logger.info("Asset Analysis Start");

        logger.info("Since the beginning of time...");
        printResults(assetService.getTotalAssetCountByTenant());

        logger.info("In the last month...");
        printResults(assetService.getPastMonthAssetCountByTenant());

        logger.info("Asset Analysis End");
    }

    private void printResults(List<CountByTenant> countOfSomeSort) {
        logger.info("There are " + countOfSomeSort.size() + " results!");

        countOfSomeSort.forEach(countByTenant ->
            System.out.format("| %-40s|%10d |\n",
                                 countByTenant.getName(),
                                 countByTenant.getCount())
        );

        logger.info("-----------------------");
    }
}
