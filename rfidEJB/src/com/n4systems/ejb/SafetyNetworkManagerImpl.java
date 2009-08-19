package com.n4systems.ejb;

import java.util.List;

import javax.ejb.Stateless;

import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.Inspection;
import com.n4systems.model.Product;
import com.n4systems.model.Tenant;
import com.n4systems.model.orgs.BaseOrg;

@Stateless
public class SafetyNetworkManagerImpl implements SafetyNetworkManager {
	
	public Tenant findByFidAC(String fidAC) {
		return null;
	}

	public Long findCountLinkedProductInspection(Product product) {
		return 0L;
	}

	public Inspection findLatestLinkedProductInspection(Product product) {
		return null;
	}

	public Inspection findLinkedProductInspection(Product product, Long inspectionDocId) throws InvalidQueryException {
		return null;
	}

	public List<Inspection> findLinkedProductInspections(Product product) throws InvalidQueryException {
		return null;
	}

	public List<Product> findLinkedProducts(Product product) {
		return null;
	}

	public String findNewFidAC() {
		return null;
	}

	public String findProductLink(String rfidNumber, BaseOrg tenant) {
		return null;
	}

//	private static final int fidACSize = 8;
//	
//	
//	
//	@PersistenceContext (unitName="rfidEM")
//	private EntityManager em;
//	
//	@EJB private PersistenceManager persistenceManager;
//	
//	@SuppressWarnings("unchecked")
//	public String findNewFidAC() {
//		List<String> currentSNACs = em.createQuery( "select fidAC from Organization where fidAC IS NOT NULL" ).getResultList();
//		String fidAC = generatNewFidAC();
//		while( currentSNACs.contains( fidAC ) ) {
//			fidAC = generatNewFidAC();
//		}
//		return fidAC;
//	}
//	
//	protected String generatNewFidAC() {
//		String fidAC = "";
//		for( int i = 0; i < fidACSize; i++ ) {
//			int random = new Random().nextInt( 36 );
//			if( random < 10 ) {
//				fidAC += String.valueOf( random );
//			} else {
//				// capital letters.
//				fidAC += (char)(random + 55);
//			} 
//		}
//		return fidAC;
//	}
//	
//	public InspectorOrganization findByFidAC( String fidAC ) {
//		
//		Query query = em.createQuery( "From InspectorOrganization WHERE UPPER(fidAC) = UPPER(:fidAC)");
//		query.setParameter( "fidAC", fidAC );
//		InspectorOrganization tenant;
//		try { 
//			tenant = (InspectorOrganization)query.getSingleResult();
//		} catch ( NoResultException ne ) {
//			tenant = null;
//		}
//		
//		return tenant;
//	}
//
//	/**
//	 * finds a product that can be linked to this one.
//	 * returns the uuid of the product to link this one to.
//	 */
//	public String findProductLink( String rfidNumber, Organization tenant ) {
//		
//		if( rfidNumber != null && rfidNumber.length() != 0 && tenant.isInspector() ) {
//			// re attach the object.
//			InspectorOrganization inspector = em.find( InspectorOrganization.class, tenant.getId() );
//			if( inspector.getLinkedManufacturers() != null && inspector.getLinkedManufacturers().size() > 0 ) {
//			
//				Query query = em.createQuery( "FROM " + Product.class.getName() + " ps where UPPER(ps.rfidNumber) = :rfidNumber " +
//						" AND ps.tenant IN (:tenants) AND state = :activeState ");
//				query.setParameter( "rfidNumber", rfidNumber.toUpperCase() );
//				query.setParameter( "tenants", inspector.getLinkedManufacturers() );
//				query.setParameter( "activeState", EntityState.ACTIVE );
//			
//				Product product = null; 
//				try {
//					product = (Product)query.getSingleResult();
//				} catch ( NoResultException nre ) {
//					product = null;
//				}
//				
//				if( product != null ) {
//					if( product.getUuid() == null ) {
//						product.setUuid( UUID.randomUUID().toString() );
//						em.merge( product );
//					}
//					return product.getUuid();
//				}
//			}
//		}
//		return null;
//	}
//	
//	/**
//	 * finds all products linked to the given bean.
//	 */
//	@SuppressWarnings("unchecked")
//	public List<Product> findLinkedProducts( Product product ) {
//		
//		Organization tenant = em.find( Organization.class, product.getTenant().getId() );
//		List<Product> linkedProducts = new ArrayList<Product>();
//		if( tenant.isManufacturer() && product.getUuid() != null  ) {
//			ManufacturerOrganization manufacturer = (ManufacturerOrganization) tenant;
//			if( manufacturer.getLinkedTenants() != null && ! manufacturer.getLinkedTenants().isEmpty() ) {
//				//lookup all inspections.
//				Query query = em.createQuery( "FROM " + Product.class.getName() + " ps where ps.linkedUuid = :uuid AND " +
//						"ps.tenant IN (:tenants) AND state = :activeState ");
//				query.setParameter( "uuid", product.getUuid() );
//				query.setParameter( "tenants", manufacturer.getLinkedTenants() );
//				query.setParameter( "activeState", EntityState.ACTIVE );
//			
//				linkedProducts = (List<Product>)query.getResultList(); 
//			}
//		} else if( tenant.isInspector() && product.getLinkedUuid() != null ) {
//			InspectorOrganization inspector = (InspectorOrganization) tenant;
//			if( inspector.getLinkedManufacturers() != null && ! inspector.getLinkedManufacturers().isEmpty() ) {
//				//lookup all inspections.
//				Query query = em.createQuery( "FROM " + Product.class.getName() + " ps where ps.uuid = :linkedUuid AND ps.tenant IN (:tenants)");
//				query.setParameter( "linkedUuid", product.getLinkedUuid() );
//				query.setParameter( "tenants", inspector.getLinkedManufacturers() );
//				
//				try {
//					linkedProducts.add( (Product)query.getSingleResult() );
//				} catch ( NoResultException nre ) {
//					
//				} 
//			}
//		}
//		
//		return linkedProducts;
//	}
//	
//	
//	/**
//	 * finds all available inspections that are on linked products to the given product.
//	 * deleted inspections will not be returned.
//	 */
//	public List<Inspection> findLinkedProductInspections( Product product ) throws InvalidQueryException {
//		
//		List<Inspection> inspections = new ArrayList<Inspection>();
//		List<Product> linkedProducts = findLinkedProducts( product ); 
//		
//		QueryBuilder<Inspection> builder = new QueryBuilder<Inspection>(Inspection.class);
//		builder.setSimpleSelect();
//		
//		for (Product linkedProduct : linkedProducts) {
//			builder.getWhereParameters().clear();
//			builder.addSimpleWhere("state", EntityState.ACTIVE );
//			builder.addSimpleWhere("product", linkedProduct);
//
//			inspections.addAll(persistenceManager.findAll(builder));
//		}
//		
//		return inspections;
//	}
//	
//	
//	/**
//	 * finds the single 
//	 * deleted inspections will not be returned.
//	 */
//	public Inspection findLinkedProductInspection( Product product, Long inspectionId ) throws InvalidQueryException {
//		
//		QueryBuilder<Inspection> queryBuilder = new QueryBuilder<Inspection>(Inspection.class);
//		queryBuilder.setSimpleSelect().addSimpleWhere("id", inspectionId).addSimpleWhere("state", EntityState.ACTIVE );
//		queryBuilder.addPostFetchPaths("modifiedBy.userID", "type.sections", "type.supportedProofTests");
//		queryBuilder.addPostFetchPaths("type.infoFieldNames",  "attachments", "results", "product", "product.infoOptions", "infoOptionMap");
//		queryBuilder.addPostFetchPaths("subInspections");
//		
//		Inspection inspection = persistenceManager.find(queryBuilder);
//		
//		// Ensure this inspection is from a linked product to the owned product
//		List<Product> linkedProducts = findLinkedProducts( product );
//		for (Product linkedProduct : linkedProducts) {
//			if (linkedProduct.getId().equals(inspection.getProduct().getId())) {
//				return inspection;
//			}
//		}
//		
//		return null; 
//	}
//	
//	/** 
//	 * finds the newest  available inspection that are on linked products to the given product.
//	 * deleted inspections will not be returned.
//	 */
//	public Inspection findLatestLinkedProductInspection( Product product ) {
//		
//		Inspection inspection = null;
//		List<Product> linkedProducts = findLinkedProducts( product ); 
//		
//		if( !linkedProducts.isEmpty() ) {
//			
//			String query = "SELECT inspection from Inspection inspection left join inspection.product " +
//				 "WHERE inspection.product IN ( :products ) AND inspection.state=:activeState ORDER BY inspection.date, inspection.created DESC";
//							
//			Query inspectionQuery = em.createQuery( query );
//				
//			inspectionQuery.setMaxResults( 1 );
//			inspectionQuery.setParameter("products", linkedProducts );
//			inspectionQuery.setParameter( "activeState", EntityState.ACTIVE );
//			
//				
//			try{
//				inspection = (Inspection)inspectionQuery.getSingleResult();
//			} catch ( NoResultException e) {}
//			catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return inspection;
//	}
//	
//	
//	public Long findCountLinkedProductInspection( Product product ) {
//		
//		List<Product> linkedProducts = findLinkedProducts( product ); 
//		if( !linkedProducts.isEmpty() ) {
//			String query = "SELECT count(inspection.id) from Inspection inspection left join inspection.product " +
//				 "WHERE inspection.product IN ( :products ) AND inspection.state= :activeState";
//							
//			Query inspectionQuery = em.createQuery( query );
//			inspectionQuery.setParameter("products", linkedProducts );
//			inspectionQuery.setParameter("activeState", EntityState.ACTIVE );
//				
//			try{
//				return (Long)inspectionQuery.getSingleResult();
//			} catch ( NoResultException e) {}
//		}
//		return 0L;
//	}
	

}
