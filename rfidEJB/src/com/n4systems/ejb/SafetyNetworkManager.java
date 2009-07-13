package com.n4systems.ejb;

import java.util.List;

import javax.ejb.Local;


import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectorOrganization;
import com.n4systems.model.Organization;
import com.n4systems.model.Product;

@Local
public interface SafetyNetworkManager {

	public String findNewFidAC();

	
	public InspectorOrganization findByFidAC( String fidAC );
	
	public String findProductLink( String rfidNumber, Organization tenant ) ;
	
	public List<Product> findLinkedProducts( Product product );
	public List<Inspection> findLinkedProductInspections( Product product ) throws InvalidQueryException;
	
	public Inspection findLinkedProductInspection( Product product, Long inspectionDocId ) throws InvalidQueryException;
	public Inspection findLatestLinkedProductInspection( Product product ) ;
	public Long findCountLinkedProductInspection( Product product ) ;
	
}
