package com.n4systems.fieldid.selenium.persistence;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.n4systems.model.AddressInfo;
import com.n4systems.model.Configuration;
import com.n4systems.model.SubAsset;
import com.n4systems.model.ui.seenit.SeenItStorageItem;
import rfid.ejb.entity.AddAssetHistory;

import com.n4systems.model.Asset;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
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
import com.n4systems.model.Tenant;
import com.n4systems.model.UserRequest;
import com.n4systems.model.activesession.ActiveSession;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.model.catalog.Catalog;
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
import com.n4systems.model.user.User;
import com.n4systems.notifiers.notifications.Notification;

import rfid.ejb.entity.AssetCodeMapping;

public class TenantCleaner {

    @SuppressWarnings("unchecked")
	public void cleanTenants(EntityManager em, String[] tenants) {
        Query tenantQuery = em.createQuery("select id from "+ Tenant.class.getName()+" where name in (:tenantNames)");
        tenantQuery.setParameter("tenantNames", Arrays.asList(tenants));
        List tenantIds = tenantQuery.getResultList();

        if (tenantIds.isEmpty())
            return;

        Query assetsQuery = em.createQuery("from " + Asset.class.getName() + " where tenant.id in (:tenantIds)").setParameter("tenantIds", tenantIds);
        Query networkRegisteredAssetsQuery = em.createQuery("select p1 from " + Asset.class.getName() + " p1, " + Asset.class.getName() + " p2 where p1.linkedAsset.id = p2.id and p2.tenant.id in (:tenantIds)").setParameter("tenantIds", tenantIds);

        removeAllConfigsForTenants(em, tenantIds);
        removeAllForTenants(em, AssetCodeMapping.class, tenantIds);
        removeAllForTenants(em, Catalog.class, tenantIds);
        removeAllForTenants(em, Event.class, tenantIds);
        removeAllForTenants(em, AssociatedEventType.class, tenantIds);
        removeAllForTenants(em, EventSchedule.class, tenantIds);
        removeAllForTenants(em, AssetTypeSchedule.class, tenantIds);
        removeAllForTenants(em, EventType.class, tenantIds);
        removeAllForTenants(em, EventForm.class, tenantIds);
        removeAllForTenants(em, EventTypeGroup.class, tenantIds);
        removeAllForTenants(em, CriteriaSection.class, tenantIds);
        removeAllForTenants(em, OneClickCriteria.class, tenantIds);
        
//        removeAllForTenants(em, TextFieldCriteria.class, tenantIds);
//        removeAllForTenants(em, SelectCriteria.class, tenantIds);

        List<Asset> networkRegisteredAssets = networkRegisteredAssetsQuery.getResultList();
        for (Asset asset : networkRegisteredAssets) {
            safeRemoveAsset(em, asset);
        }

        List<Asset> assets = assetsQuery.getResultList();
        for (Asset asset : assets) {
            safeRemoveAsset(em, asset);
        }

        removeAllForTenants(em, AddAssetHistory.class, tenantIds, new Callback<AddAssetHistory>() {
            @Override
            public void callback(AddAssetHistory entity) {
                entity.getInfoOptions().clear();
            }
        });

        cleanNullTenantEntities(em, AddAssetHistory.class);

        removeAllActiveSessionsForTenant(em, tenantIds);
        cleanUpOrgConnections(em, tenantIds);
        cleanUpSignUpReferrals(em, tenantIds);
        removeAllForTenants(em, EventGroup.class, tenantIds);
        removeAllForTenants(em, EventBook.class, tenantIds);
        removeAllForTenants(em, AutoAttributeCriteria.class, tenantIds);
        removeAllForTenants(em, AutoAttributeDefinition.class, tenantIds);
        removeAllForTenants(em, AssetType.class, tenantIds, new Callback<AssetType>() {
            @Override
            public void callback(AssetType entity) {
                entity.getSchedules().clear();
            }
        });
        removeAllForTenants(em, AssetTypeGroup.class, tenantIds);
        removeAllForTenants(em, AssetStatus.class, tenantIds);
        removeAllForTenants(em, FileAttachment.class, tenantIds);
        removeAllForTenants(em, UserRequest.class, tenantIds);
        removeAllForTenants(em, AutoAttributeCriteria.class, tenantIds);
        removeAllForTenants(em, Message.class, tenantIds);
        removeAllForTenants(em, EulaAcceptance.class, tenantIds);
        removeAllForTenants(em, Project.class, tenantIds);
        removeAllForTenants(em, LineItem.class, tenantIds);
        removeAllForTenants(em, Order.class, tenantIds);

        removeAllForTenants(em, StateSet.class, tenantIds);
        removeAllForTenants(em, NotificationSetting.class, tenantIds);
        
        removeAllExternalOrgsPointingToTenants(em, tenantIds);

//        cleanUpModifiedOrCreatedReferencesForTenant(em, tenantIds, CustomerOrg.class, DivisionOrg.class, SecondaryOrg.class, PrimaryOrg.class);

        cleanUpAddressInfo(em);

        removeAllWhereModifiedOrCreatedByUsersFromTenant(em, tenantIds, EventGroup.class, EventTypeGroup.class, SeenItStorageItem.class);

        cleanUpOwnerForUsers(em, tenantIds);

        removeAllForTenants(em, CustomerOrg.class, tenantIds);
        removeAllForTenants(em, DivisionOrg.class, tenantIds);
        removeAllForTenants(em, SecondaryOrg.class, tenantIds);
        removeAllForTenants(em, PrimaryOrg.class, tenantIds);

        removeAllForTenants(em, User.class, tenantIds);
    }

    private void cleanUpOwnerForUsers(EntityManager em, List<Long> tenantIds) {
        long startTime = System.currentTimeMillis();
        for (Long tenantId : tenantIds) {
            Query q = em.createQuery("update " + User.class.getName() + " set owner = null where tenant.id = :tenantId").setParameter("tenantId", tenantId);
            q.executeUpdate();
        }
        System.out.println("Finished nulling out owners in: " + (System.currentTimeMillis() - startTime));
    }

    private void removeAllConfigsForTenants(EntityManager em, List<Long> tenantIds) {
        for (Long tenantId : tenantIds) {
            Query query = em.createQuery("from " + Configuration.class.getName() + " where tenantId = " + tenantId);
            removeAllFromQuery(em, query);
        }
    }

    private void cleanUpAddressInfo(EntityManager em) {
        Query query = em.createQuery("update " + AddressInfo.class.getName() + " set createdby = null ");
        query.executeUpdate();
        query = em.createQuery("update " + AddressInfo.class.getName() + " set modifiedby = null ");
        query.executeUpdate();
    }

    private void cleanUpModifiedOrCreatedReferencesForTenant(EntityManager em, List<Long> tenantIds, Class<? extends BaseOrg>... entityClasses) {
        long startTime = System.currentTimeMillis();
        for (Long tenantId : tenantIds) {
            for (Class entityClass : entityClasses) {
//                METHOD1: (verrry slow)
                Query query1 = em.createQuery("update " + entityClass.getName() + " set modifiedBy = null where tenant.id = :tenantId").setParameter("tenantId", tenantId);
                Query query2 = em.createQuery("update " + entityClass.getName() + " set createdBy = null where tenant.id = :tenantId").setParameter("tenantId", tenantId);
                query1.executeUpdate();
                query2.executeUpdate();
//                METHOD2: (fast, maybe doesn't work..)
//                Query query = em.createQuery("from " + entityClass.getName() + " where tenant.id = :tenantId").setParameter("tenantId", tenantId);
//                List<BaseOrg> orgs = query.getResultList();
//                for (BaseOrg org : orgs) {
//                    org.setCreatedBy(null);
//                    org.setModifiedBy(null);
//                    em.merge(org);
//                }
            }
        }
        System.out.println("Time cleaning modified/created references: " + (System.currentTimeMillis() - startTime));
    }

    private void removeAllWhereModifiedOrCreatedByUsersFromTenant(EntityManager em, List tenantIds, Class... entityClasses) {
        for (Class c : entityClasses) {
            Query query = em.createQuery("from " + c.getName() + " where createdBy in (from "+User.class.getName()+" where tenant.id in (:tenantIds1)) "+
                    " or modifiedBy in (from "+User.class.getName()+" where tenant.id in (:tenantIds2))");
            query.setParameter("tenantIds1", tenantIds);
            query.setParameter("tenantIds2", tenantIds);
            removeAllFromQuery(em, query);
        }
    }

    private void removeAllActiveSessionsForTenant(EntityManager em, List tenantIds) {
        Query query = em.createQuery("from " + ActiveSession.class.getName() + " where user.tenant.id in (:tenantIds)");
        query.setParameter("tenantIds", tenantIds);
        removeAllFromQuery(em, query);
    }

    private void cleanNullTenantEntities(EntityManager em, Class c) {
        Query query = em.createQuery("from " + c.getName() + " where tenant is null ");
        List<Object> results = query.getResultList();
        for (Object o : results) {
            clearInfoOptionsIfNecessary(o);
        }
        removeAllFromQuery(em, query);
    }

    private void removeAllExternalOrgsPointingToTenants(EntityManager em, List tenantIds) {
        Query query = em.createQuery("from " + ExternalOrg.class.getName() + " where linkedOrg.tenant.id in (:tenantIds)").setParameter("tenantIds", tenantIds);
        List<ExternalOrg> orgs = query.getResultList();
        for (ExternalOrg org : orgs) {
            cleanOwnedEntities(em, Event.class, org);
            cleanOwnedEntities(em, EventSchedule.class, org);
            cleanOwnedEntities(em, AddAssetHistory.class, org);
            Query assetQuery = em.createQuery("from " + Asset.class.getName() + " where owner.id = " + org.getId());
            List<Asset> assets = assetQuery.getResultList();
            for (Asset asset : assets) {
                safeRemoveAsset(em, asset);
            }
        }

        removeAllFromQuery(em, query);
    }

    private void cleanUpSignUpReferrals(EntityManager em, List tenantIds) {
        Query query = em.createQuery("from " + SignupReferral.class.getName() + " where referredTenant.id in (:tenantIds) or referralTenant.id in (:tenantIds)").setParameter("tenantIds", tenantIds);
        removeAllFromQuery(em, query);
    }

    private void cleanUpOrgConnections(EntityManager em, List tenantIds) {
        Query typedQuery = em.createQuery("from " + TypedOrgConnection.class.getName() + " where tenant.id in (:tenantIds1) or connectedOrg.tenant.id in (:tenantIds2)").setParameter("tenantIds1", tenantIds).setParameter("tenantIds2", tenantIds);
        Query query = em.createQuery("from " + OrgConnection.class.getName() + " where vendor.tenant.id in (:tenantIds1)  or customer.tenant.id in (:tenantIds2)").setParameter("tenantIds1", tenantIds).setParameter("tenantIds2", tenantIds);

        removeAllFromQuery(em, typedQuery);
        removeAllFromQuery(em, query);
    }

    private <T> void removeAllForTenants(EntityManager em, Class<T> entityToRemove, List tenantIds) {
        removeAllForTenants(em, entityToRemove, tenantIds, new Callback<T>() {
            @Override
            public void callback(T entity) {
            }
        });
    }

    private <T> void removeAllForTenants(EntityManager em, Class<T> entityToRemove, List<Long> tenantIds, Callback<T> callback) {
        System.out.println("Removing all: " + entityToRemove);
        for (Long tenantId : tenantIds) {
            removeAllForTenant(em, entityToRemove, tenantId, callback);
        }
    }

    private <T> void removeAllForTenant(EntityManager em, Class<T> entityToRemove, Long tenantId, Callback<T> callback) {
        Query query = em.createQuery("from " + entityToRemove.getName() + " where tenant.id = :tenantId ").setParameter("tenantId", tenantId);
        List<T> results = query.getResultList();
        for (T entity : results) {
            callback.callback(entity);
            em.remove(entity);
        }
    }

    private <T> void removeAll(EntityManager em, Class<T> entityToRemove) {
        Query query = em.createQuery("from " + entityToRemove.getName());
        removeAllFromQuery(em, query);
    }

    private void removeAllFromQuery(EntityManager em, Query q) {
        for (Object o : q.getResultList()) {
            em.remove(o);
        }
    }

    private void cleanOwnedEntities(EntityManager em, Class c, BaseOrg owner) {
        Query query = em.createQuery("from " + c.getName() + " where owner.id = " + owner.getId());
        removeAllFromQuery(em, query);
    }

    private void safeRemoveAsset(EntityManager em, Asset asset) {
        System.out.println("Removing asset id: " + asset.getId());
        Query scheduleQuery =  em.createQuery("from " + EventSchedule.class.getName() + " where asset.id = " + asset.getId());
        Query attachmentQuery = em.createQuery("from " + AssetAttachment.class.getName() + " where asset.id = " + asset.getId());
        Query inspQuery = em.createQuery("from " + Event.class.getName() + " where asset.id = " + asset.getId());
        Query subAssetQuery = em.createQuery("from " + SubAsset.class.getName() + " where masterAsset.id = " + asset.getId());

        removeAllFromQuery(em, scheduleQuery);
        removeAllFromQuery(em, attachmentQuery);
        removeAllFromQuery(em, inspQuery);
        removeAllFromQuery(em, subAssetQuery);

        asset.getInfoOptions().clear();
        em.merge(asset);
        em.remove(asset);
    }

    private static interface Callback<T> {
        public void callback(T entity);
    }

    private void clearInfoOptionsIfNecessary(Object o) {
        try {
            Class c = o.getClass();
            Method method = c.getMethod("getInfoOptions");
            Collection coll = (Collection) method.invoke(o);
            coll.clear();
        } catch (Exception e) {
            System.out.println("Error executing method");
        }
    }

}
