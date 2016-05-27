package com.n4systems.fieldid.service.jmx;

import org.hibernate.stat.*;

public class StatisticsService implements StatisticsServiceMBean {

	private final Statistics stats;

	public StatisticsService(Statistics stats) {
		this.stats = stats;
	}

	@Override
	public void clear() {
		stats.clear();
	}

	@Override
	public EntityStatistics getEntityStatistics(String entityName) {
		return stats.getEntityStatistics(entityName);
	}

	@Override
	public CollectionStatistics getCollectionStatistics(String role) {
		return stats.getCollectionStatistics(role);
	}

	@Override
	public SecondLevelCacheStatistics getSecondLevelCacheStatistics(String regionName) {
		return stats.getSecondLevelCacheStatistics(regionName);
	}

	@Override
	public NaturalIdCacheStatistics getNaturalIdCacheStatistics(String regionName) {
		return stats.getNaturalIdCacheStatistics(regionName);
	}

	@Override
	public QueryStatistics getQueryStatistics(String hql) {
		return stats.getQueryStatistics(hql);
	}

	@Override
	public long getEntityDeleteCount() {
		return stats.getEntityDeleteCount();
	}

	@Override
	public long getEntityInsertCount() {
		return stats.getEntityInsertCount();
	}

	@Override
	public long getEntityLoadCount() {
		return stats.getEntityLoadCount();
	}

	@Override
	public long getEntityFetchCount() {
		return stats.getEntityFetchCount();
	}

	@Override
	public long getEntityUpdateCount() {
		return stats.getEntityUpdateCount();
	}

	@Override
	public long getQueryExecutionCount() {
		return stats.getQueryExecutionCount();
	}

	@Override
	public long getQueryCacheHitCount() {
		return stats.getQueryCacheHitCount();
	}

	@Override
	public long getQueryExecutionMaxTime() {
		return stats.getQueryExecutionMaxTime();
	}

	@Override
	public long getQueryCacheMissCount() {
		return stats.getQueryCacheMissCount();
	}

	@Override
	public long getQueryCachePutCount() {
		return stats.getQueryCachePutCount();
	}

	@Override
	public long getNaturalIdQueryExecutionCount() {
		return stats.getNaturalIdQueryExecutionCount();
	}

	@Override
	public long getNaturalIdQueryExecutionMaxTime() {
		return stats.getNaturalIdQueryExecutionMaxTime();
	}

	@Override
	public String getNaturalIdQueryExecutionMaxTimeRegion() {
		return stats.getNaturalIdQueryExecutionMaxTimeRegion();
	}

	@Override
	public long getNaturalIdCacheHitCount() {
		return stats.getNaturalIdCacheHitCount();
	}

	@Override
	public long getNaturalIdCacheMissCount() {
		return stats.getNaturalIdCacheMissCount();
	}

	@Override
	public long getNaturalIdCachePutCount() {
		return stats.getNaturalIdCachePutCount();
	}

	@Override
	public long getUpdateTimestampsCacheHitCount() {
		return stats.getUpdateTimestampsCacheHitCount();
	}

	@Override
	public long getUpdateTimestampsCacheMissCount() {
		return stats.getUpdateTimestampsCacheMissCount();
	}

	@Override
	public long getUpdateTimestampsCachePutCount() {
		return stats.getUpdateTimestampsCachePutCount();
	}

	@Override
	public long getFlushCount() {
		return stats.getFlushCount();
	}

	@Override
	public long getConnectCount() {
		return stats.getConnectCount();
	}

	@Override
	public long getSecondLevelCacheHitCount() {
		return stats.getSecondLevelCacheHitCount();
	}

	@Override
	public long getSecondLevelCacheMissCount() {
		return stats.getSecondLevelCacheMissCount();
	}

	@Override
	public long getSecondLevelCachePutCount() {
		return stats.getSecondLevelCachePutCount();
	}

	@Override
	public long getSessionCloseCount() {
		return stats.getSessionCloseCount();
	}

	@Override
	public long getSessionOpenCount() {
		return stats.getSessionOpenCount();
	}

	@Override
	public long getCollectionLoadCount() {
		return stats.getCollectionLoadCount();
	}

	@Override
	public long getCollectionFetchCount() {
		return stats.getCollectionFetchCount();
	}

	@Override
	public long getCollectionUpdateCount() {
		return stats.getCollectionUpdateCount();
	}

	@Override
	public long getCollectionRemoveCount() {
		return stats.getCollectionRemoveCount();
	}

	@Override
	public long getCollectionRecreateCount() {
		return stats.getCollectionRecreateCount();
	}

	@Override
	public long getStartTime() {
		return stats.getStartTime();
	}

	@Override
	public boolean isStatisticsEnabled() {
		return stats.isStatisticsEnabled();
	}

	@Override
	public void setStatisticsEnabled(boolean enable) {
		stats.setStatisticsEnabled(enable);
	}

	@Override
	public void logSummary() {
		stats.logSummary();
	}

	@Override
	public String[] getCollectionRoleNames() {
		return stats.getCollectionRoleNames();
	}

	@Override
	public String[] getEntityNames() {
		return stats.getEntityNames();
	}

	@Override
	public String[] getQueries() {
		return stats.getQueries();
	}

	@Override
	public String[] getSecondLevelCacheRegionNames() {
		return stats.getSecondLevelCacheRegionNames();
	}

	@Override
	public long getSuccessfulTransactionCount() {
		return stats.getSuccessfulTransactionCount();
	}

	@Override
	public long getTransactionCount() {
		return stats.getTransactionCount();
	}

	@Override
	public long getCloseStatementCount() {
		return stats.getCloseStatementCount();
	}

	@Override
	public long getPrepareStatementCount() {
		return stats.getPrepareStatementCount();
	}

	@Override
	public long getOptimisticFailureCount() {
		return stats.getOptimisticFailureCount();
	}

	@Override
	public String getQueryExecutionMaxTimeQueryString() {
		return stats.getQueryExecutionMaxTimeQueryString();
	}
}
