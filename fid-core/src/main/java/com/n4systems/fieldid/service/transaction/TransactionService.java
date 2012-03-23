package com.n4systems.fieldid.service.transaction;

import com.n4systems.exceptions.InvalidTransactionGUIDException;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.RequestTransaction;
import com.n4systems.model.Tenant;
import com.n4systems.util.GUIDHelper;
import org.hibernate.exception.ConstraintViolationException;

public class TransactionService extends FieldIdPersistenceService {

    public boolean isTransactionCompleted( String guid ) throws InvalidTransactionGUIDException {
        if( GUIDHelper.isNullGUID(guid) ) {
            throw new InvalidTransactionGUIDException();
        }

        RequestTransaction transaction = persistenceService.findByName( RequestTransaction.class, guid );
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
            persistenceService.save( transaction );
        } catch ( ConstraintViolationException c ) {
            throw new TransactionAlreadyProcessedException();
        }
    }
}
