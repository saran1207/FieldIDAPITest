package com.fieldid.service;

import com.fieldid.model.CountByTenant;
import com.fieldid.service.asset.AssetUsageAnalyzerService;
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

    @Autowired
    public AnalyzerService(AssetUsageAnalyzerService assetService) {
        this.assetService = assetService;
    }

    public void analyze() {
        logger.info("Since the beginning of time...");
        printResults(assetService.getTotalAssetCountByTenant());

        logger.info("In the last month...");
        printResults(assetService.getPastMonthAssetCountByTenant());
    }

    private void printResults(List<CountByTenant> countOfSomeSort) {
        logger.info("There are " + countOfSomeSort.size() + " results!");

        countOfSomeSort.forEach(countByTenant -> {
            System.out.format("| %-40s|%10d |\n", countByTenant.getName(), countByTenant.getCount());
        });
        logger.info("-----------------------");
    }
}
