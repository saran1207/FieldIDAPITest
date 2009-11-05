package com.n4systems.persistence;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.hibernate.stat.SecondLevelCacheStatistics;
import org.hibernate.stat.Statistics;

import com.n4systems.util.ServiceLocator;

public class CacheManager {
	private static Logger logger = Logger.getLogger("cache-info");
	
	private static CacheManager self = new CacheManager();
	
	public static CacheManager getInstance() {
		return self;
	}
	
	public static void setInstance(CacheManager cm) {
		self = cm;
	}
	
	protected CacheManager() {}
	
	public void logStats() {
		logStats("fieldid", PersistenceManager.getHibernateStats());
		logStats("rfidEM", ServiceLocator.getPersistenceManager().getHibernateStats());
		
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
		String extraInfo = String.format("SizeInMem [%8d], ElementsInMem [%5d], ElementsOnDisk [%5d]", stats.getSizeInMemory(), stats.getElementCountInMemory(), stats.getElementCountOnDisk());

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
		
		String message = String.format("%-95s: HitPct [%5.1f%%], Hit [%5.0f], Miss [%5.0f], Puts [%5.0f]%s", cacheName, ratio, hits, misses, puts, extra);
		logger.info(message);
	}
	
}
