package com.n4systems.fieldid.service.jmx;


import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.beans.factory.annotation.Autowired;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class StatisticsServiceMBeanInitializer {

	@Autowired
	private SessionFactory sessionFactory;

	public void init() {
		try {
			final Statistics statistics = sessionFactory.getStatistics();
			statistics.setStatisticsEnabled(true);
			StatisticsService statisticsMBean = new StatisticsService(statistics);
			MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
			mbeanServer.registerMBean(statisticsMBean, new ObjectName("Hibernate:application=Statistics"));
		} catch (Exception e) {
			Logger.getLogger(StatisticsServiceMBeanInitializer.class).warn("Unable to publish hibernate statistics mbean", e);
		}
	}
}
