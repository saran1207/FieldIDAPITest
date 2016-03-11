package com.n4systems.util;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.InvalidTransactionGUIDException;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.fieldid.CopiedToService;
import com.n4systems.fieldid.service.transaction.TransactionService;
import com.n4systems.model.RequestTransaction;
import com.n4systems.model.Tenant;
import org.hibernate.exception.ConstraintViolationException;

@Deprecated // Use TransactionService
@CopiedToService(TransactionService.class)
public class TransactionSupervisor {

	private PersistenceManager persistenceManager;
	
	public TransactionSupervisor( PersistenceManager persistenceManager ) {
		this.persistenceManager = persistenceManager;
	}
	
	public boolean isTransactionCompleted( String guid, Long tenantId ) throws InvalidTransactionGUIDException {
		if( GUIDHelper.isNullGUID( guid ) ) {
			throw new InvalidTransactionGUIDException();
		}
		
		RequestTransaction transaction = persistenceManager.findByName( RequestTransaction.class, tenantId, guid );
		return ( transaction != null );
	}
	
	
	
	public void completeAssetTransaction( String transactionGUID, Tenant tenant ) throws TransactionAlreadyProcessedException {
		completeTransaction( transactionGUID, tenant, "create asset" );
	}
	
	public void completeEventTransaction( String transactionGUID, Tenant tenant ) throws TransactionAlreadyProcessedException {
		completeTransaction( transactionGUID, tenant, "create events" );
	}
	
	private void completeTransaction( String transactionGUID, Tenant tenant, String transactiontype ) throws TransactionAlreadyProcessedException { 
		try {
			
			RequestTransaction transaction = new RequestTransaction();
			transaction.setName( transactionGUID );
			transaction.setTenant( tenant );
			transaction.setType( transactiontype );
			persistenceManager.save( transaction );
		} catch ( ConstraintViolationException c ) {
			throw new TransactionAlreadyProcessedException();
		}
	}
	
}
