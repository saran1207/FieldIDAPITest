package com.n4systems.handlers.removers;
import static com.n4systems.model.builders.InspectionTypeBuilder.*;
import static org.easymock.EasyMock.*;

import org.junit.Before;
import org.junit.Test;

import com.n4systems.handlers.remover.InspectionFrequenciesDeleteHandler;
import com.n4systems.handlers.remover.InspectionFrequenciesDeleteHandlerImpl;
import com.n4systems.model.InspectionType;
import com.n4systems.model.ProductTypeSchedule;
import com.n4systems.model.inspectiontype.InspectionFrequencySaver;
import com.n4systems.model.producttype.InspectionFrequencyListLoader;
import com.n4systems.persistence.FieldIdTransaction;
import com.n4systems.persistence.Transaction;
import com.n4systems.test.helpers.FluentArrayList;


public class InspectionFrequenciesDeleteHandlerImplTest {

	private Transaction mockTransaction; 
	
	private void mockTransaction() {
		mockTransaction = createMock(FieldIdTransaction.class);
		mockTransaction.commit();
		expectLastCall().once();
		replay(mockTransaction);
	}
	
	@Before
	public void setup() {
		mockTransaction();
	}
	
	@Test
	public void should_remove_all_inspection_frequencies_for_inspection_type() {
		
		InspectionType inspectionTypeToBeRemoved = anInspectionType().build();
		ProductTypeSchedule scheduleToBeRemoved = new ProductTypeSchedule();
		
		InspectionFrequencyListLoader mockListLoader = createMock(InspectionFrequencyListLoader.class);
		expect(mockListLoader.setInspectionTypeId(inspectionTypeToBeRemoved.getId())).andReturn(mockListLoader);
		expect(mockListLoader.load(mockTransaction)).andReturn(new FluentArrayList<ProductTypeSchedule>().stickOn(scheduleToBeRemoved));
		replay(mockListLoader);
		
		InspectionFrequencySaver mockSaver = createMock(InspectionFrequencySaver.class);
		mockSaver.remove(mockTransaction, scheduleToBeRemoved);
		replay(mockSaver);
		
		InspectionFrequenciesDeleteHandler sut = new InspectionFrequenciesDeleteHandlerImpl(mockListLoader, mockSaver);
		
		sut.forInspectionType(inspectionTypeToBeRemoved).remove(mockTransaction);
		
		verify(mockListLoader);
		verify(mockSaver);
	}
	
	
	
	
	
	
	
	
	
}
