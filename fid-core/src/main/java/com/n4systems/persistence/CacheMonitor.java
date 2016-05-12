package com.n4systems.persistence;

import com.n4systems.fieldid.config.CacheConfigurator;
import com.n4systems.fieldid.service.PersistenceService;
import org.apache.log4j.Logger;
import org.hibernate.stat.SecondLevelCacheStatistics;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Arrays;


public class CacheMonitor {
	private static Logger logger = Logger.getLogger("cache-info");


	private static final double B_TO_MB = 1024d * 1024d;

	@Autowired
	protected PersistenceService persistenceService;

	@Scheduled(fixedRate = 30000)
	public void logStats() {
		if (!CacheConfigurator.isCacheEnabled()) return;

		Statistics stats = persistenceService.getHibernateSession().getSessionFactory().getStatistics();
		stats.setStatisticsEnabled(true);
		logStats("fieldid", stats);
	}
	
	public void logStats(String unitName, Statistics stats) {
		logger.info(String.format("============================================================== %-8s ========================================================================", unitName));

		logHitMissCounts("QueryCache", stats.getQueryCacheHitCount(), stats.getQueryCacheMissCount(), stats.getQueryCachePutCount());
		logHitMissCounts("SecondLevel", stats.getSecondLevelCacheHitCount(), stats.getSecondLevelCacheMissCount(), stats.getSecondLevelCachePutCount());
		
		String[] regions = stats.getSecondLevelCacheRegionNames();
		Arrays.sort(regions);
		
		for (String name: regions) {
			logSecondLevelRegionStats(name, stats.getSecondLevelCacheStatistics(name));
		}
		
		logger.info("================================================================================================================================================");
	}
	
	public void logSecondLevelRegionStats(String name, SecondLevelCacheStatistics stats) {
		String extraInfo = String.format("SizeInMemMB [%8.2f], ElementsInMem [%5d], ElementsOnDisk [%5d]", stats.getSizeInMemory() / B_TO_MB, stats.getElementCountInMemory(), stats.getElementCountOnDisk());

		logHitMissCounts(name, stats.getHitCount(), stats.getMissCount(), stats.getPutCount(), extraInfo);
	}
	
	private double calcHitRatio(double hits, double misses) {
		double total = hits + misses;
		return (total > 0) ? (hits / total * 100.0) : 0.0;
	}
	
	private void logHitMissCounts(String cacheName, double hits, double misses, double puts) {
		logHitMissCounts(cacheName, hits, misses, puts, null);
	}
	
	private void logHitMissCounts(String cacheName, double hits, double misses, double puts, String extra) {
		extra = (extra != null) ? " (" + extra + ")" : ""; 
			
		double ratio = calcHitRatio(hits, misses);
		
		String message = String.format("%-65s: HitPct [%5.1f%%], Hit [%5.0f], Miss [%5.0f], Puts [%5.0f]%s", cacheName, ratio, hits, misses, puts, extra);
		logger.info(message);
	}
	
}
