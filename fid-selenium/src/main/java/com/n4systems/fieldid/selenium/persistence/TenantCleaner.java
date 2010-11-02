package com.n4systems.fieldid.selenium.persistence;

import com.n4systems.model.Asset;
import com.n4systems.model.AssociatedInspectionType;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.Event;
import com.n4systems.model.EventGroup;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.EventBook;
import com.n4systems.model.InspectionType;
import com.n4systems.model.AssetType;
import com.n4systems.model.UserRequest;
import com.n4systems.model.activesession.ActiveSession;
import com.n4systems.model.catalog.Catalog;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.ExternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.orgs.SecondaryOrg;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.model.safetynetwork.TypedOrgConnection;
import com.n4systems.model.signup.SignupReferral;
import com.n4systems.model.user.User;
import rfid.ejb.entity.AddAssetHistory;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class TenantCleaner {

    public void cleanTenant(EntityManager em, long tenantId) {
        Query assetsQuery = em.createQuery("from " + Asset.class.getName() + " where tenant.id = " + tenantId);
        Query networkRegisteredAssetsQuery = em.createQuery("select p1 from " + Asset.class.getName() + " p1, " + Asset.class.getName() + " p2 where p1.linkedAsset.id = p2.id and p2.tenant.id = " + tenantId);
        List<Asset> assets = assetsQuery.getResultList();

        removeAllForTenant(em, Catalog.class, tenantId);
        removeAllForTenant(em, Event.class, tenantId);
        removeAllForTenant(em, AssociatedInspectionType.class, tenantId);
        removeAllForTenant(em, InspectionType.class, tenantId);

        List<Asset> networkRegisteredAssets = networkRegisteredAssetsQuery.getResultList();
        for (Asset asset : networkRegisteredAssets) {
            safeRemoveAsset(em, asset);
        }

        for (Asset asset : assets) {
            safeRemoveAsset(em, asset);
        }

        removeAllForTenant(em, AddAssetHistory.class, tenantId, new Callback<AddAssetHistory>(){
            @Override
            public void callback(AddAssetHistory entity) {
                entity.getInfoOptions().clear();
            }
        });

        cleanNullTenantEntities(em, AddAssetHistory.class);

        removeAll(em, ActiveSession.class);
        cleanUpOrgConnections(em, tenantId);
        cleanUpSignUpReferrals(em, tenantId);
        removeAllForTenant(em, EventGroup.class, tenantId);
        removeAllForTenant(em, EventBook.class, tenantId);
        removeAllForTenant(em, AutoAttributeCriteria.class, tenantId);
        removeAllForTenant(em, AutoAttributeDefinition.class, tenantId);
        removeAllForTenant(em, AssetType.class, tenantId);
        removeAllForTenant(em, FileAttachment.class, tenantId);
        removeAllForTenant(em, UserRequest.class, tenantId);
        removeAllForTenant(em, AutoAttributeCriteria.class, tenantId);
        removeAllForTenant(em, User.class, tenantId);

        removeAllExternalOrgsPointingToThisTenant(em, tenantId);

        removeAllForTenant(em, CustomerOrg.class, tenantId);
        removeAllForTenant(em, DivisionOrg.class, tenantId);
        removeAllForTenant(em, SecondaryOrg.class, tenantId);
        removeAllForTenant(em, PrimaryOrg.class, tenantId);
    }

    private void cleanNullTenantEntities(EntityManager em, Class c) {
        Query query = em.createQuery("from " + c.getName() + " where tenant is null ");
        List<Object> results = query.getResultList();
        for (Object o : results) {
            clearInfoOptionsIfNecessary(o);
        }
        removeAllFromQuery(em, query);
    }

    private void removeAllExternalOrgsPointingToThisTenant(EntityManager em, long tenantId) {
        Query query = em.createQuery("from " + ExternalOrg.class.getName() + " where linkedOrg.tenant.id = " + tenantId);
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

    private void cleanUpSignUpReferrals(EntityManager em, long tenantId) {
        Query query = em.createQuery("from " + SignupReferral.class.getName() + " where referredTenant.id = " + tenantId + " or referralTenant.id = " + tenantId);
        removeAllFromQuery(em, query);
    }

    private void cleanUpOrgConnections(EntityManager em, long tenantId) {
        Query typedQuery = em.createQuery("from " + TypedOrgConnection.class.getName() + " where tenant.id = " + tenantId + " or connectedOrg.tenant.id = " +tenantId );
        Query query = em.createQuery("from " + OrgConnection.class.getName() + " where vendor.tenant.id = " + tenantId + " or customer.tenant.id = " +tenantId );

        removeAllFromQuery(em, typedQuery);
        removeAllFromQuery(em, query);
    }

    private <T> void removeAllForTenant(EntityManager em, Class<T> entityToRemove, long tenantId) {
        removeAllForTenant(em, entityToRemove, tenantId, new Callback<T> () {
            @Override
            public void callback(T entity) { }
        });
    }

    private <T> void removeAllForTenant(EntityManager em, Class<T> entityToRemove, long tenantId, Callback<T> callback) {
        Query query = em.createQuery("from " + entityToRemove.getName() + " where tenant.id = " + tenantId);
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
        Query scheduleQuery =  em.createQuery("from " + EventSchedule.class.getName() + " where asset.id = " + asset.getId());
        Query attachmentQuery = em.createQuery("from " + AssetAttachment.class.getName() + " where asset.id = " + asset.getId());
        Query inspQuery = em.createQuery("from " + Event.class.getName() + " where asset.id = " + asset.getId());

        removeAllFromQuery(em, scheduleQuery);
        removeAllFromQuery(em, attachmentQuery);
        removeAllFromQuery(em, inspQuery);

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
