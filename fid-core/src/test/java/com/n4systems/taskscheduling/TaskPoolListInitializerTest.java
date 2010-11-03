package com.n4systems.taskscheduling;

import static org.junit.Assert.*;

import com.n4systems.taskscheduling.task.PrintAllEventCertificatesTask;
import com.n4systems.taskscheduling.task.PrintEventSummaryReportTask;
import com.n4systems.util.properties.HierarchicalProperties;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TaskPoolListInitializerTest {
	
	private class _TaskPoolListInitializer extends TaskPoolListInitializer {
		@Override
        public void initClasses(HierarchicalProperties classDefs, TaskPool pool) {
	        super.initClasses(classDefs, pool);
        }
		@Override
        public TaskPool initPool(HierarchicalProperties poolDef) {
	        return super.initPool(poolDef);
        }
	}
	
	private _TaskPoolListInitializer initializer = new _TaskPoolListInitializer();

	@Test
	public void test_init_all() {
		List<String> poolNames = Arrays.asList("pool_1", "pool_2", "pool_3");
		List<Integer> poolSizes = Arrays.asList(2, 4, 6);
		
		HierarchicalProperties conf = new HierarchicalProperties();
		conf.setProperty("pool.0.name", poolNames.get(0));
		conf.setProperty("pool.0.size", String.valueOf(poolSizes.get(0)));
		conf.setProperty("pool.0.class.0", PrintAllEventCertificatesTask.class.getName());
		
		conf.setProperty("pool.1.name", poolNames.get(1));
		conf.setProperty("pool.1.size", String.valueOf(poolSizes.get(1)));
		conf.setProperty("pool.1.class.0", PrintEventSummaryReportTask.class.getName());

		conf.setProperty("pool.2.name", poolNames.get(1));
		conf.setProperty("pool.2.size", String.valueOf(poolSizes.get(1)));

		
		List<TaskPool> pools = initializer.initAll(conf);
		
		assertNotNull(pools);
		
		assertEquals(3, pools.size());
		
		assertTrue(poolNames.contains(pools.get(0).getName()));
		assertTrue(poolSizes.contains(pools.get(0).getSize()));
		assertEquals(1, pools.get(0).getExecutableTasks().size());
		
		assertTrue(poolNames.contains(pools.get(1).getName()));
		assertTrue(poolSizes.contains(pools.get(1).getSize()));
		assertEquals(1, pools.get(1).getExecutableTasks().size());
		
		assertTrue(poolNames.contains(pools.get(2).getName()));
		assertTrue(poolSizes.contains(pools.get(2).getSize()));
		assertEquals(0, pools.get(2).getExecutableTasks().size());
		
	}

	@Test
	public void test_init_pool() {
		String poolName = "pool_name";
		int poolSize = 5;
		
		HierarchicalProperties conf = new HierarchicalProperties();
		conf.setProperty("name", poolName);
		conf.setProperty("size", String.valueOf(poolSize));
		conf.setProperty("class.0", PrintEventSummaryReportTask.class.getName());
				
		TaskPool pool = initializer.initPool(conf);
		
		assertEquals(poolName, pool.getName());
		
		assertEquals(poolSize, pool.getSize());
		
		assertEquals(1, pool.getExecutableTasks().size());
		
		assertNotNull(pool.getExecutor());
		
	}

	@SuppressWarnings("unchecked")
    @Test
	public void test_init_classes() {
		TaskPool pool = new TaskPool("name", 1);

		Class<?>[] classes = {PrintAllEventCertificatesTask.class, PrintEventSummaryReportTask.class};
		
		HierarchicalProperties conf = new HierarchicalProperties();
		conf.setProperty("0", classes[0].getName());
		conf.setProperty("1", classes[1].getName());
		
		initializer.initClasses(conf, pool);
		
		assertTrue(pool.canExecute((Class<? extends Runnable>)classes[0]));
		assertTrue(pool.canExecute((Class<? extends Runnable>)classes[1]));
	}

    @Test
	public void test_invalid_classes_not_added() {
		TaskPool pool = new TaskPool("name", 1);

		HierarchicalProperties conf = new HierarchicalProperties();
		conf.setProperty("0", "as.djnsjda.jasdnas");
		conf.setProperty("1", String.class.getName());
		
		initializer.initClasses(conf, pool);
		
		assertEquals(0, pool.getExecutableTasks().size());
	}
}
