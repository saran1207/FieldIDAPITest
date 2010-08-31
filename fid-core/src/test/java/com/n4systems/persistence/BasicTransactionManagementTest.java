package com.n4systems.persistence;

import static org.easymock.EasyMock.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.n4systems.handlers.creator.BasicTransactionManagement;
import com.n4systems.handlers.creator.TestDoubleTransactionManager;
import com.n4systems.util.persistence.TestingTransaction;


public class BasicTransactionManagementTest {
	private enum Call {
		Process, Success, Failure, Complete;
	}
	private final class RuntimeExceptionExtension extends RuntimeException {
	}
	private final class BasicTransactionManagementTestExtension extends BasicTransactionManagement {
		 
		

		private boolean shouldThrowException = false;
		
		
		private Exception failureException;
		private List<Call> calls = new ArrayList<Call>();


		private Transaction transaction;

		private BasicTransactionManagementTestExtension(TransactionManager transactionManager) {
			super(transactionManager);
		}
		
		public void runProcess() {
			shouldThrowException = false;
			run();
		}
		

		public void runFailingProcess() {
			shouldThrowException = true;
			run();
		}

		@Override
		protected void doProcess(Transaction transaction) {
			this.transaction = transaction;
			calls.add(Call.Process);
			if (shouldThrowException) {
				throw new RuntimeExceptionExtension();
			}
		}

		
		@Override
		protected void failure(Exception e) {
			failureException = e;
			calls.add(Call.Failure);
		}

		@Override
		protected void finished() {
			calls.add(Call.Complete);
			
		}

		@Override
		protected void success() {
			calls.add(Call.Success);
		}
		
		

	}



	private TransactionManager transactionManager =new TestDoubleTransactionManager();
	
	

	@Test
	public void should_start_and_finish_a_transaction_from_the_transaction_manager_when_about_to_create() throws Exception {
		TestingTransaction transaction = new TestingTransaction();
		
		transactionManager = createStrictMock(TransactionManager.class);
		expect(transactionManager.startTransaction()).andReturn(transaction);
		transactionManager.finishTransaction(transaction);
		replay(transactionManager);
		
		
		BasicTransactionManagementTestExtension sut = new BasicTransactionManagementTestExtension(transactionManager);
		
		sut.runProcess();
		
		verify(transactionManager);
	}
	
	
	@Test
	public void should_rollback_the_transaction_if_the_process_throws_an_exception() throws Exception {
		
		TestingTransaction transaction = new TestingTransaction();
		
		transactionManager = createStrictMock(TransactionManager.class);
		expect(transactionManager.startTransaction()).andReturn(transaction);
		transactionManager.rollbackTransaction(transaction);
		transactionManager.finishTransaction(transaction);
		replay(transactionManager);
		
		
		BasicTransactionManagementTestExtension sut = new BasicTransactionManagementTestExtension(transactionManager);
		
		try {
			sut.runFailingProcess();
		} catch (Exception e) {
			// don't do anything with an exception
		}
		
		verify(transactionManager);
	}
	
	
	@Test
	public void should_call_process_with_the_transaction_from_the_transaction_from_the_transaction_manager() throws Exception {
		
		TestDoubleTransactionManager transactionManager = new TestDoubleTransactionManager();
		Transaction expectedTransaction = transactionManager.getTransaction();
		
		BasicTransactionManagementTestExtension sut = new BasicTransactionManagementTestExtension(transactionManager);
		
		sut.runProcess();
		
		assertThat(sut.transaction, sameInstance(expectedTransaction));
	}
	
	
	@Test
	public void should_call_process_then_success_then_finished_when_no_exception_was_raised() throws Exception {
		
		transactionManager = new TestDoubleTransactionManager();
		
		BasicTransactionManagementTestExtension sut = new BasicTransactionManagementTestExtension(transactionManager) ;
		
		sut.runProcess();
		
		List<Call> expecedCalls = ImmutableList.of(Call.Process, Call.Success, Call.Complete);
		
		assertThat(sut.calls, equalTo(expecedCalls));
	}
	
	@Test
	public void should_call_process_then_failure_then_finished_when_an_exception_is_raised() throws Exception {
		List<Call> expecedCalls = ImmutableList.of(Call.Process, Call.Failure, Call.Complete);
		
		
		BasicTransactionManagementTestExtension sut = new BasicTransactionManagementTestExtension(transactionManager) ;
		
		try {
			sut.runFailingProcess();
		} catch (Exception e) {}
		
		
		assertThat(sut.calls, equalTo(expecedCalls));
	}
	
	@Test
	public void should_call_failure_with_the_exception_raised_by_processed() throws Exception {
		BasicTransactionManagementTestExtension sut = new BasicTransactionManagementTestExtension(transactionManager) ;
		
		try {
			sut.runFailingProcess();
		} catch (Exception e) {}
		
		assertThat(sut.failureException, instanceOf(RuntimeExceptionExtension.class));
	}
	
	
	
}
