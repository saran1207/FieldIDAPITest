package com.n4systems.model.orders;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.model.LineItem;
import com.n4systems.model.Order;

public class NonIntegrationLineItemSaverTest {
	private NonIntegrationLineItemSaver saver = new NonIntegrationLineItemSaver();
	private LineItem line;
	
	@Before
	public void setup_line_item() {
		line = new LineItem();
		line.setOrder(new Order());
	}
	
	@Test
	public void saves_order_than_line_item() {
		EntityManager em = createMock(EntityManager.class);
		em.persist(line.getOrder());
		em.persist(line);
		replay(em);
		
		saver.save(em, line);
		verify(em);
	}

	@Test
	public void updates_order_than_line_item() {
		EntityManager em = createMock(EntityManager.class);
		expect(em.merge(line.getOrder())).andReturn(line.getOrder());
		expect(em.merge(line)).andReturn(line);
		replay(em);
		
		saver.update(em, line);
		verify(em);
	}
	
	@Test
	public void update_sets_merged_order_back_on_lineitem() {
		Order mergedOrder = new Order();
		
		EntityManager em = createMock(EntityManager.class);
		expect(em.merge(line.getOrder())).andReturn(mergedOrder);
		expect(em.merge(line)).andReturn(line);
		replay(em);
		
		saver.update(em, line);
		assertSame(mergedOrder, line.getOrder());
	}
}
