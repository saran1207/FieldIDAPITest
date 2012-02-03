package com.n4systems.fieldid.selenium.persistence;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.n4systems.model.columns.ActiveColumnMapping;
import com.n4systems.model.columns.ColumnLayout;
import com.n4systems.model.commenttemplate.CommentTemplate;
import com.n4systems.model.saveditem.SavedItem;
import com.n4systems.model.tenant.SetupDataLastModDates;
import org.apache.log4j.Logger;

import rfid.ejb.entity.AddAssetHistory;
import rfid.ejb.entity.AssetCodeMapping;

import com.n4systems.fieldid.selenium.util.TimeLogger;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.Configuration;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.Event;
import com.n4systems.model.EventBook;
import com.n4systems.model.EventForm;
import com.n4systems.model.EventGroup;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.LineItem;
import com.n4systems.model.OneClickCriteria;
import com.n4systems.model.Order;
import com.n4systems.model.Project;
import com.n4systems.model.StateSet;
import com.n4systems.model.SubAsset;
import com.n4systems.model.Tenant;
import com.n4systems.model.UserRequest;
import com.n4systems.model.activesession.ActiveSession;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.model.catalog.Catalog;
import com.n4systems.model.downloadlink.DownloadLink;
import com.n4systems.model.eula.EulaAcceptance;
import com.n4systems.model.messages.Message;
import com.n4systems.model.notificationsettings.NotificationSetting;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.ExternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.SecondaryOrg;
import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.model.safetynetwork.TypedOrgConnection;
import com.n4systems.model.signup.SignupReferral;
import com.n4systems.model.ui.seenit.SeenItStorageItem;
import com.n4systems.model.user.User;
import rfid.ejb.entity.IdentifierCounterBean;

public class TenantCleaner {
	private static final Logger logger = Logger.getLogger(TenantCleaner.class);
	
	private final EntityManager em;
	
	public TenantCleaner(EntityManager em) {
		this.em = em;
	}
	
	private void remove(Object entity) {
		em.remove(entity);
		em.flush();
	}
	
    @SuppressWarnings("unchecked")
	public void cleanTenants(String[] tenants) {
    	TimeLogger timeLogger = new TimeLogger(logger, "cleanTenants(%s)", (Object)tenants);
    	
        Query tenantQuery = em.createQuery("select id from "+ Tenant.class.getName()+" where name in (:tenantNames)");
        tenantQuery.setParameter("tenantNames", Arrays.asList(tenants));
        List<Long> tenantIds = tenantQuery.getResultList();

        if (tenantIds.isEmpty())
            return;

        clearUsersSavedItems(tenantIds);

        removeAllConfigsForTenants(tenantIds);
        removeAddAssetHistory(tenantIds);
        removeAllForTenants(DownloadLink.class, tenantIds);
        removeAllForTenants(AssetCodeMapping.class, tenantIds);
        removeAllForTenants(Catalog.class, tenantIds);
        removeAllForTenants(EventSchedule.class, tenantIds);
        removeAllForTenants(Event.class, tenantIds);
        removeAllForTenants(AssociatedEventType.class, tenantIds);
        removeAllForTenants(AssetTypeSchedule.class, tenantIds);
        removeAllForTenants(EventType.class, tenantIds);
        removeAllForTenants(EventForm.class, tenantIds);
        removeAllForTenants(EventTypeGroup.class, tenantIds);
        removeAllForTenants(CriteriaSection.class, tenantIds);
        removeAllForTenants(OneClickCriteria.class, tenantIds);
        removeAllForTenants(ActiveColumnMapping.class, tenantIds);
        removeAllForTenants(ColumnLayout.class, tenantIds);
        removeAllForTenants(SetupDataLastModDates.class, tenantIds);
        removeAllForTenants(IdentifierCounterBean.class, tenantIds);

//        removeAllForTenants(TextFieldCriteria.class, tenantIds);
//        removeAllForTenants(SelectCriteria.class, tenantIds);

        Query networkRegisteredAssetsQuery = em.createQuery("select p1 from " + Asset.class.getName() + " p1, " + Asset.class.getName() + " p2 where p1.linkedAsset.id = p2.id and p2.tenant.id in (:tenantIds)").setParameter("tenantIds", tenantIds);
        List<Asset> networkRegisteredAssets = networkRegisteredAssetsQuery.getResultList();
        for (Asset asset : networkRegisteredAssets) {
            safeRemoveAsset(asset);
        }

        Query assetsQuery = em.createQuery("from " + Asset.class.getName() + " where tenant.id in (:tenantIds)").setParameter("tenantIds", tenantIds);
        List<Asset> assets = assetsQuery.getResultList();
        for (Asset asset : assets) {
            safeRemoveAsset(asset);
        }

        removeAllActiveSessionsForTenant(tenantIds);
        cleanUpOrgConnections(tenantIds);
        cleanUpSignUpReferrals(tenantIds);
        removeAllForTenants(EventGroup.class, tenantIds);
        removeAllForTenants(EventBook.class, tenantIds);
        removeAllForTenants(AutoAttributeCriteria.class, tenantIds);
        removeAllForTenants(AutoAttributeDefinition.class, tenantIds);
        clearAllSubAssetTypes(tenantIds);
        removeAllForTenants(AssetType.class, tenantIds);
        removeAllForTenants(AssetTypeGroup.class, tenantIds);
        removeAllForTenants(AssetStatus.class, tenantIds);
        removeAllForTenants(FileAttachment.class, tenantIds);
        removeAllForTenants(UserRequest.class, tenantIds);
        removeAllForTenants(AutoAttributeCriteria.class, tenantIds);
        removeAllForTenants(Message.class, tenantIds);
        removeAllForTenants(EulaAcceptance.class, tenantIds);
        removeAllForTenants(Project.class, tenantIds);
        removeAllForTenants(LineItem.class, tenantIds);
        removeAllForTenants(Order.class, tenantIds);
        removeAllForTenants(StateSet.class, tenantIds);
        removeAllForTenants(NotificationSetting.class, tenantIds);
        removeAllExternalOrgsPointingToTenants(tenantIds);
        removeAllForTenants(EventGroup.class, tenantIds);
        removeAllForTenants(EventTypeGroup.class, tenantIds);
        removeAllSeenItStorageItems(tenantIds);
        removeAllForTenants(CommentTemplate.class, tenantIds);
        cleanUpForeignFieldsOnUsers(tenantIds);

        removeAllForTenants(DivisionOrg.class, tenantIds);
        removeAllForTenants(CustomerOrg.class, tenantIds);
        removeAllForTenants(SecondaryOrg.class, tenantIds);
        removeAllForTenants(PrimaryOrg.class, tenantIds);

        clearLastLoggedUsersFor(tenantIds);

        removeAllForTenants(User.class, tenantIds);
        
        timeLogger.stop();
    }

    private void clearUsersSavedItems(List<Long> tenantIds) {
        Query usersQuery = em.createQuery("from " + User.class.getName() + " where tenant.id in (:tenantIds)").setParameter("tenantIds", tenantIds);
        List<User> users = usersQuery.getResultList();
        for (User user : users) {
            user.setLastRunSearch(null);
            user.setLastRunReport(null);
            user.setSavedItems(new ArrayList<SavedItem>());
            em.merge(user);
        }
    }

    private void clearLastLoggedUsersFor(List<Long> tenantIds) {
        Query tenantsQuery = em.createQuery("from " + Tenant.class.getName() + " where id in (:tenantIds)").setParameter("tenantIds", tenantIds);
        final List<Tenant> resultList = tenantsQuery.getResultList();
        for (Tenant tenant : resultList) {
            tenant.setLastLoginTime(null);
            tenant.setLastLoginUser(null);
            em.merge(tenant);
        }
    }

    private void clearAllSubAssetTypes(List<Long> tenantIds) {
        for (Long tenantId: tenantIds) {
            Query query = em.createQuery("from " + AssetType.class.getName() + " where tenant.id = ?");
            List resultList = query.setParameter(1, tenantId).getResultList();
            for (Object o : resultList) {
                ((AssetType)o).getSubTypes().clear();
                em.merge(o);
            }
        }
    }

    private void removeAddAssetHistory(List<Long> tenantIds) {
    	TimeLogger timeLogger = new TimeLogger(logger, "removeAddAssetHistory(%s)", tenantIds);
    	
    	for (Long tenantId: tenantIds) {
    		Query query = em.createNativeQuery("DELETE FROM addassethistory_infooption WHERE r_addproducthistory in (SELECT uniqueid FROM addassethistory WHERE tenant_id = ?)");
        	logger.error("Delete addassethistory_infooption: " + query.setParameter(1, tenantId).executeUpdate());
        	
        	query = em.createNativeQuery("DELETE FROM addassethistory WHERE tenant_id = ?");
        	logger.error("Delete addassethistory: " + query.setParameter(1, tenantId).executeUpdate());
    	}
    	em.flush();
    	
        timeLogger.stop();
    }
    
    private void removeAllSeenItStorageItems(List<Long> tenantIds) {
    	TimeLogger timeLogger = new TimeLogger(logger, "removeAllSeenItStorageItems(%s)", tenantIds);
    	
    	String jpql = String.format("from %s where userId in (select id from %s where tenant.id in (:tenantIds))", SeenItStorageItem.class.getName(), User.class.getName());
        Query query = em.createQuery(jpql).setParameter("tenantIds", tenantIds);
        removeAllFromQuery(query);

        timeLogger.stop();
    }

    private void cleanUpForeignFieldsOnUsers(List<Long> tenantIds) {
    	TimeLogger timeLogger = new TimeLogger(logger, "cleanUpOwnerForUsers(%s)", tenantIds);
    	
        for (Long tenantId : tenantIds) {
            Query q = em.createQuery("update " + User.class.getName() + " set owner = null, createdBy = null, modifiedBy = null where tenant.id = :tenantId").setParameter("tenantId", tenantId);
            q.executeUpdate();
        }
        
        timeLogger.stop();
    }

    private void removeAllConfigsForTenants(List<Long> tenantIds) {
    	TimeLogger timeLogger = new TimeLogger(logger, "removeAllConfigsForTenants(%s)", tenantIds.toArray());
        
    	for (Long tenantId : tenantIds) {
            Query query = em.createQuery("from " + Configuration.class.getName() + " where tenantId = " + tenantId);
            removeAllFromQuery(query);
        }
        
        timeLogger.stop();
    }

    private void cleanUpAddressInfo() {
    	TimeLogger timeLogger = new TimeLogger(logger, "cleanUpAddressInfo(em)");
    	
        Query query = em.createQuery("update " + AddressInfo.class.getName() + " set createdby = null, modifiedby = null");
        query.executeUpdate();
        
        timeLogger.stop();
    }

    private void removeAllWhereModifiedOrCreatedByUsersFromTenant(List<Long> tenantIds, Class<?> ... entityClasses) {
        for (Class c : entityClasses) {
        	TimeLogger timeLogger = new TimeLogger(logger, "removeAllWhereModifiedOrCreatedByUsersFromTenant(%s, %s)", tenantIds.toArray(), c.getSimpleName());
            
        	Query query = em.createQuery("from " + c.getName() + " where createdBy in (from "+User.class.getName()+" where tenant.id in (:tenantIds1)) "+
                    " or modifiedBy in (from "+User.class.getName()+" where tenant.id in (:tenantIds2))");
            query.setParameter("tenantIds1", tenantIds);
            query.setParameter("tenantIds2", tenantIds);
            removeAllFromQuery(query);
            
            timeLogger.stop();
        }
    }

    private void removeAllActiveSessionsForTenant(List<Long> tenantIds) {
    	TimeLogger timeLogger = new TimeLogger(logger, "removeAllActiveSessionsForTenant(%s)", tenantIds.toArray());
    	
        Query query = em.createQuery("from " + ActiveSession.class.getName() + " where user.tenant.id in (:tenantIds)");
        query.setParameter("tenantIds", tenantIds);
        removeAllFromQuery(query);
        
        timeLogger.stop();
    }

    private void cleanNullTenantEntities(Class<?> c) {
        Query query = em.createQuery("from " + c.getName() + " where tenant is null ");
        List<Object> results = query.getResultList();
        for (Object o : results) {
            clearInfoOptionsIfNecessary(o);
        }
        removeAllFromQuery(query);
    }

    private void removeAllExternalOrgsPointingToTenants(List<Long> tenantIds) {
    	TimeLogger timeLogger = new TimeLogger(logger, "removeAllExternalOrgsPointingToTenants(%s)", tenantIds.toArray());
    	
        Query query = em.createQuery("from " + ExternalOrg.class.getName() + " where linkedOrg.tenant.id in (:tenantIds)").setParameter("tenantIds", tenantIds);
        List<ExternalOrg> orgs = query.getResultList();
        for (ExternalOrg org : orgs) {
            cleanOwnedEntities(Event.class, org);
            cleanOwnedEntities(EventSchedule.class, org);
            cleanOwnedEntities(AddAssetHistory.class, org);
            cleanOwnedEntities(User.class, org);
            Query divisionQuery = em.createQuery("from " + DivisionOrg.class.getName() + " where parent.id = " + org.getId());
            removeAllFromQuery(divisionQuery);
            Query assetQuery = em.createQuery("from " + Asset.class.getName() + " where owner.id = " + org.getId());
            List<Asset> assets = assetQuery.getResultList();
            for (Asset asset : assets) {
                safeRemoveAsset(asset);
            }
        }

        removeAllFromQuery(query);
        timeLogger.stop();
    }

    private void cleanUpSignUpReferrals(List<Long> tenantIds) {
    	TimeLogger timeLogger = new TimeLogger(logger, "cleanUpSignUpReferrals(%s)", tenantIds.toArray());
    	
        Query query = em.createQuery("from " + SignupReferral.class.getName() + " where referredTenant.id in (:tenantIds) or referralTenant.id in (:tenantIds)").setParameter("tenantIds", tenantIds);
        removeAllFromQuery(query);
        
        timeLogger.stop();
    }

    private void cleanUpOrgConnections(List<Long> tenantIds) {
    	TimeLogger timeLogger = new TimeLogger(logger, "cleanUpOrgConnections(%s)", tenantIds.toArray());
    	
        Query typedQuery = em.createQuery("from " + TypedOrgConnection.class.getName() + " where tenant.id in (:tenantIds1) or connectedOrg.tenant.id in (:tenantIds2) or owner.tenant.id in (:tenantIds3)").setParameter("tenantIds1", tenantIds).setParameter("tenantIds2", tenantIds).setParameter("tenantIds3", tenantIds);
        Query query = em.createQuery("from " + OrgConnection.class.getName() + " where vendor.tenant.id in (:tenantIds1)  or customer.tenant.id in (:tenantIds2)").setParameter("tenantIds1", tenantIds).setParameter("tenantIds2", tenantIds);

        removeAllFromQuery(typedQuery);
        removeAllFromQuery(query);
        
        timeLogger.stop();
    }

    private <T> void removeAllForTenants(Class<T> entityToRemove, List<Long> tenantIds) {
        removeAllForTenants(entityToRemove, tenantIds, new Callback<T>() {
            @Override
            public void callback(T entity) {}
        });
    }

    private <T> void removeAllForTenants(Class<T> entityToRemove, List<Long> tenantIds, Callback<T> callback) {
    	TimeLogger timeLogger = new TimeLogger(logger, "removeAllForTenants(%s, %s, callback)", entityToRemove.getSimpleName(), tenantIds);
        
    	for (Long tenantId : tenantIds) {
            removeAllForTenant(entityToRemove, tenantId, callback);
        }
    	
        timeLogger.stop();
    }

    private <T> void removeAllForTenant(Class<T> entityToRemove, Long tenantId, Callback<T> callback) {
        Query query = em.createQuery("from " + entityToRemove.getName() + " where tenant.id = :tenantId ").setParameter("tenantId", tenantId);
        List<T> results = query.getResultList();
        for (T entity : results) {
            callback.callback(entity);
            remove(entity);
        }
    }

    private <T> void removeAll(Class<T> entityToRemove) {
        Query query = em.createQuery("from " + entityToRemove.getName());
        removeAllFromQuery(query);
    }

    private void removeAllFromQuery(Query q) {
        for (Object o : q.getResultList()) {
        	remove(o);
        }
    }

    private void cleanOwnedEntities(Class<?> c, BaseOrg owner) {
    	TimeLogger timeLogger = new TimeLogger(logger, "cleanOwnedEntities(%s, %s)", c.getSimpleName(), owner.getName());
    	
        Query query = em.createQuery("from " + c.getName() + " where owner.id = " + owner.getId());
        removeAllFromQuery(query);
        
        timeLogger.stop();
    }

    private void safeRemoveAsset(Asset asset) {
    	TimeLogger timeLogger = new TimeLogger(logger, "safeRemoveAsset(%d)", asset.getId());
    	
        Query scheduleQuery =  em.createQuery("from " + EventSchedule.class.getName() + " where asset.id = " + asset.getId());
        Query attachmentQuery = em.createQuery("from " + AssetAttachment.class.getName() + " where asset.id = " + asset.getId());
        Query inspQuery = em.createQuery("from " + Event.class.getName() + " where asset.id = " + asset.getId());
        Query subAssetQuery = em.createQuery("from " + SubAsset.class.getName() + " where masterAsset.id = " + asset.getId());

        removeAllFromQuery(scheduleQuery);
        removeAllFromQuery(attachmentQuery);
        removeAllFromQuery(inspQuery);
        removeAllFromQuery(subAssetQuery);

        asset.getInfoOptions().clear();
        em.merge(asset);
        remove(asset);
        
        timeLogger.stop();
    }

    private static interface Callback<T> {
        public void callback(T entity);
    }

    private void clearInfoOptionsIfNecessary(Object o) {
        try {
            Class<?> c = o.getClass();
            Method method = c.getMethod("getInfoOptions");
            Collection<?> coll = (Collection<?>) method.invoke(o);
            coll.clear();
        } catch (Exception e) {
        	logger.info("Error executing method");
        }
    }

}
