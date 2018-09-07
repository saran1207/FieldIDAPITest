package com.n4systems.sso.dao;

import com.n4systems.model.Tenant;
import com.n4systems.model.sso.SsoEntity;
import com.n4systems.model.sso.SsoIdpMetadata;
import com.n4systems.model.sso.SsoSpMetadata;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Data Access Object to interact with the SSO tables.
 */
@Component
public class SsoMetadataDaoImpl implements SsoMetadataDao {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public SsoIdpMetadata getIdp(String entityId) {
        SsoEntity ssoEntity = entityManager.find(SsoEntity.class, entityId);
        if (ssoEntity == null)
            return null;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SsoIdpMetadata> query = builder.createQuery(SsoIdpMetadata.class);
        Root<SsoIdpMetadata> root = query.from(SsoIdpMetadata.class);
        query.select(root);
        Predicate predicate = builder.equal(root.get("ssoEntity"), ssoEntity);
        query.where(predicate);
        Query q = entityManager.createQuery(query);
        try {
            SsoIdpMetadata result = (SsoIdpMetadata) q.getSingleResult();
            return result;
        }
        catch(NoResultException ex) {
            return null;
        }
    }

    @Override
    public SsoIdpMetadata getIdpByTenant(long tenantId) {
        Tenant tenant = entityManager.find(Tenant.class, tenantId);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SsoIdpMetadata> query = builder.createQuery(SsoIdpMetadata.class);
        Root<SsoIdpMetadata> root = query.from(SsoIdpMetadata.class);
        query.select(root);
        Predicate predicate = builder.equal(root.get("tenant"), tenant);
        query.where(predicate);
        Query q = entityManager.createQuery(query);
        try {
            SsoIdpMetadata result = (SsoIdpMetadata) q.getSingleResult();
            return result;
        }
        catch(NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<SsoIdpMetadata> getIdp() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SsoIdpMetadata> query = builder.createQuery(SsoIdpMetadata.class);
        Root<SsoIdpMetadata> root = query.from(SsoIdpMetadata.class);
        query.select(root);
        Query q = entityManager.createQuery(query);
        List<SsoIdpMetadata> list = q.getResultList();
        return list;
    }

    @Override
    @Transactional
    public SsoIdpMetadata addIdp(SsoIdpMetadata idpMetadata) {
        SsoEntity ssoEntity = idpMetadata.getSsoEntity();
        entityManager.persist(ssoEntity);
        idpMetadata.setSsoEntity(ssoEntity);
        entityManager.persist(idpMetadata);
        return idpMetadata;
    }

    @Override
    @Transactional
    public SsoIdpMetadata updateIdp(SsoIdpMetadata idpMetadata) {
        return entityManager.merge(idpMetadata);
    }

    @Override
    @Transactional
    public void deleteIdp(String entityId) {
        SsoIdpMetadata idp = getIdp(entityId);
        SsoEntity ssoEntity = entityManager.find(SsoEntity.class, idp.getSsoEntity().getEntityId());
        entityManager.remove(idp);
        entityManager.remove(ssoEntity);
    }

    @Override
    public SsoSpMetadata getSp(String entityId) {
        SsoEntity ssoEntity = entityManager.find(SsoEntity.class, entityId);
        if (ssoEntity == null)
            return null;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SsoSpMetadata> query = builder.createQuery(SsoSpMetadata.class);
        Root<SsoSpMetadata> root = query.from(SsoSpMetadata.class);
        query.select(root);
        Predicate predicate = builder.equal(root.get("ssoEntity"), ssoEntity);
        query.where(predicate);
        Query q = entityManager.createQuery(query);
        q.setHint("javax.persistence.fetchgraph", entityManager.getEntityGraph("lazyCollections"));
        try {
            SsoSpMetadata result = (SsoSpMetadata) q.getSingleResult();
            return result;
        }
        catch(NoResultException ex) {
            return null;
        }
    }

    @Override
    public SsoSpMetadata getSpByTenant(long tenantId) {
        Tenant tenant = entityManager.find(Tenant.class, tenantId);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SsoSpMetadata> query = builder.createQuery(SsoSpMetadata.class);
        Root<SsoSpMetadata> root = query.from(SsoSpMetadata.class);
        query.select(root);
        Predicate predicate = builder.equal(root.get("tenant"), tenant);
        query.where(predicate);
        Query q = entityManager.createQuery(query);
        q.setHint("javax.persistence.fetchgraph", entityManager.getEntityGraph("lazyCollections"));
        try {
            SsoSpMetadata result = (SsoSpMetadata) q.getSingleResult();
            return result;
        }
        catch(NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<SsoSpMetadata> getSp() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SsoSpMetadata> query = builder.createQuery(SsoSpMetadata.class);
        Root<SsoSpMetadata> root = query.from(SsoSpMetadata.class);
        query.select(root);
        Query q = entityManager.createQuery(query);
        q.setHint("javax.persistence.fetchgraph", entityManager.getEntityGraph("lazyCollections"));
        List<SsoSpMetadata> list = q.getResultList();
        return list;
    }

    @Override
    @Transactional
    public SsoSpMetadata addSp(SsoSpMetadata spMetadata) {
        spMetadata.setId(null);
        SsoEntity ssoEntity = spMetadata.getSsoEntity();
        entityManager.persist(ssoEntity);
        spMetadata.setSsoEntity(ssoEntity);
        entityManager.persist(spMetadata);
        return spMetadata;
    }

    @Override
    @Transactional
    public SsoSpMetadata updateSp(SsoSpMetadata spMetadata) {
        return entityManager.merge(spMetadata);
    }

    @Override
    @Transactional
    public void deleteSp(String entityId) {
        SsoSpMetadata sp = getSp(entityId);
        SsoEntity ssoEntity = entityManager.find(SsoEntity.class, sp.getSsoEntity().getEntityId());
        entityManager.remove(sp);
        entityManager.remove(ssoEntity);
    }
}
