package com.n4systems.sso.dao;

import com.n4systems.model.Tenant;
import com.n4systems.model.sso.SsoEntity;
import com.n4systems.model.sso.SsoGlobalSettings;
import com.n4systems.model.sso.SsoIdpMetadata;
import com.n4systems.model.sso.SsoSpMetadata;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
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
    public SsoIdpMetadata getIdpByEntityId(String entityId) {
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
    public List<SsoIdpMetadata> getAllIdp() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SsoIdpMetadata> query = builder.createQuery(SsoIdpMetadata.class);
        Root<SsoIdpMetadata> root = query.from(SsoIdpMetadata.class);
        query.select(root);
        Query q = entityManager.createQuery(query);
        List<SsoIdpMetadata> list = q.getResultList();
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SsoIdpMetadata addIdp(SsoIdpMetadata idpMetadata) throws SsoDuplicateEntityIdException {
        try {
            SsoEntity ssoEntity = idpMetadata.getSsoEntity();
            entityManager.persist(ssoEntity);
            idpMetadata.setSsoEntity(ssoEntity);
            entityManager.persist(idpMetadata);
            return idpMetadata;
        }
        catch(EntityExistsException ex) {
            throw new SsoDuplicateEntityIdException(ex);
        }
        catch(PersistenceException ex) {
            if (ex.getCause() instanceof ConstraintViolationException)
                throw new SsoDuplicateEntityIdException(ex);
            else
                throw ex;
        }
}

    @Override
    @Transactional
    public void deleteIdpByEntityId(String entityId) {
        SsoIdpMetadata idp = getIdpByEntityId(entityId);
        SsoEntity ssoEntity = entityManager.find(SsoEntity.class, idp.getSsoEntity().getEntityId());
        entityManager.remove(idp);
        entityManager.remove(ssoEntity);
    }

    @Override
    @Transactional
    public SsoSpMetadata getSpByEntityId(String entityId) {
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
        return getPopulatedSingleResult(q);
    }

    @Override
    @Transactional
    public SsoSpMetadata getSpByTenant(long tenantId) {
        Tenant tenant = entityManager.find(Tenant.class, tenantId);
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SsoSpMetadata> query = builder.createQuery(SsoSpMetadata.class);
        Root<SsoSpMetadata> root = query.from(SsoSpMetadata.class);
        query.select(root);
        Predicate predicate = builder.equal(root.get("tenant"), tenant);
        query.where(predicate);
        Query q = entityManager.createQuery(query);
        return getPopulatedSingleResult(q);
    }

    private SsoSpMetadata getPopulatedSingleResult(Query q) {
        try {
            SsoSpMetadata result = (SsoSpMetadata) q.getSingleResult();
            if (result != null) {
            /* Load the collections */
                result.getNameID().size();
                result.getBindingsHoKSSO().size();
                result.getBindingsSLO().size();
                result.getBindingsSSO().size();
            }
            return result;
        }
        catch(NoResultException ex) {
            return null;
        }
    }

    @Override
    public SsoSpMetadata getSpById(Long id) {
        return entityManager.find(SsoSpMetadata.class, id);
    }

    @Override
    public List<SsoSpMetadata> getAllSp() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SsoSpMetadata> query = builder.createQuery(SsoSpMetadata.class);
        Root<SsoSpMetadata> root = query.from(SsoSpMetadata.class);
        query.select(root);
        Query q = entityManager.createQuery(query);
        List<SsoSpMetadata> list = q.getResultList();
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SsoSpMetadata addSp(SsoSpMetadata spMetadata) throws SsoDuplicateEntityIdException {
        try {
            spMetadata.setId(null);
            SsoEntity ssoEntity = spMetadata.getSsoEntity();
            entityManager.persist(ssoEntity);
            spMetadata.setSsoEntity(ssoEntity);
            entityManager.persist(spMetadata);
            return spMetadata;
        }
        catch(EntityExistsException ex) {
            throw new SsoDuplicateEntityIdException(ex);
        }
        catch(PersistenceException ex) {
            if (ex.getCause() instanceof ConstraintViolationException)
                throw new SsoDuplicateEntityIdException(ex);
            else
                throw ex;
        }
    }

    @Override
    @Transactional
    public void deleteSpById(Long id) {
        SsoSpMetadata sp = entityManager.find(SsoSpMetadata.class, id);
        if (sp != null)
            entityManager.remove(sp);
    }

    @Override
    @Transactional
    public void deleteSpByEntityId(String entityId) {
        SsoSpMetadata sp = getSpByEntityId(entityId);
        SsoEntity ssoEntity = entityManager.find(SsoEntity.class, sp.getSsoEntity().getEntityId());
        entityManager.remove(sp);
        entityManager.remove(ssoEntity);
    }

    @Override
    public SsoGlobalSettings getSsoGlobalSettings() {
        return entityManager.find(SsoGlobalSettings.class, new Long(1));
    }

    @Override
    @Transactional
    public SsoGlobalSettings updateSsoGlobalSettings(SsoGlobalSettings ssoGlobalSettings) {
        return entityManager.merge(ssoGlobalSettings);
    }
}
