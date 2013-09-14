package com.n4systems.persistence.localization;

import com.google.common.collect.Lists;
import com.n4systems.persistence.listeners.LocalizationListener;
import com.n4systems.persistence.listeners.SetupDataUpdateEventListener;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.ejb.HibernatePersistence;
import org.hibernate.event.*;
import org.reflections.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.reflections.ReflectionUtils.withAnnotation;

public class HibernatePersistenceProvider extends HibernatePersistence {

    private List<PreUpdateEventListener> preUpdateEventListeners = Lists.newArrayList();
    private List<PreDeleteEventListener> preDeleteEventListeners = Lists.newArrayList();
    private List<PostLoadEventListener> postLoadEventListeners = Lists.newArrayList();
    private List<PostInsertEventListener> postInsertEventListeners = Lists.newArrayList();
    private List<PostUpdateEventListener> postUpdateEventListeners = Lists.newArrayList();
    private List<PostDeleteEventListener> postDeleteEventListeners = Lists.newArrayList();
    private List<PreLoadEventListener> preLoadEventListeners = Lists.newArrayList();
    private List<FlushEntityEventListener> flushEntityEventListeners = Lists.newArrayList();

    private @Autowired LocalizationListener localizationListener;
    private @Autowired SetupDataUpdateEventListener setupDataUpdateEventListener;

    public HibernatePersistenceProvider() {
        super();
    }

    private void addListeners() {
        Set<Field> fields = ReflectionUtils.getAllFields(this.getClass(), withAnnotation(Autowired.class));
        for (Field field:fields) {
            try {
                addListener(field.get(this));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void addListener(Object listener) {
        if (listener instanceof PreLoadEventListener) {
            preLoadEventListeners.add((PreLoadEventListener) listener);
        }
        if (listener instanceof PreUpdateEventListener) {
            preUpdateEventListeners.add((PreUpdateEventListener) listener);
        }
        if (listener instanceof PreDeleteEventListener) {
            preDeleteEventListeners.add((PreDeleteEventListener) listener);
        }
        if (listener instanceof PostInsertEventListener) {
            postInsertEventListeners.add((PostInsertEventListener) listener);
        }
        if (listener instanceof PostLoadEventListener) {
            postLoadEventListeners.add((PostLoadEventListener) listener);
        }
        if (listener instanceof PostUpdateEventListener) {
            postUpdateEventListeners.add((PostUpdateEventListener) listener);
        }
        if (listener instanceof PostDeleteEventListener) {
            postDeleteEventListeners.add((PostDeleteEventListener) listener);
        }
        if (listener instanceof FlushEntityEventListener) {
            flushEntityEventListeners.add((FlushEntityEventListener)listener);
        }

        // CAVEAT : not all types supported. add others as needed.
    }

    @SuppressWarnings("rawtypes")
    @Override
    public EntityManagerFactory createEntityManagerFactory(String persistenceUnitName, Map properties) {
        Ejb3Configuration cfg = new Ejb3Configuration();
        setupConfiguration(cfg);
        Ejb3Configuration configured = cfg.configure( persistenceUnitName, properties );
        return configured != null ? configured.buildEntityManagerFactory() : null;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public EntityManagerFactory createContainerEntityManagerFactory(PersistenceUnitInfo info, Map properties) {
        Ejb3Configuration cfg = new Ejb3Configuration();
        setupConfiguration(cfg);
        Ejb3Configuration configured = cfg.configure( info, properties );
        return configured != null ? configured.buildEntityManagerFactory() : null;
    }

    private void setupConfiguration(Ejb3Configuration cfg) {
        addListeners();

        cfg.getEventListeners().setPostInsertEventListeners(postInsertEventListeners.toArray(new PostInsertEventListener[]{}));
//        cfg.getEventListeners().setPreDeleteEventListeners(preDeleteEventListeners.toArray(new PreDeleteEventListener[]{}));
//        cfg.getEventListeners().setPreUpdateEventListeners(preUpdateEventListeners.toArray(new PreUpdateEventListener[]{}));
        cfg.getEventListeners().setPostLoadEventListeners(postLoadEventListeners.toArray(new PostLoadEventListener[]{}));
//        cfg.getEventListeners().setPreLoadEventListeners(preLoadEventListeners.toArray(new PreLoadEventListener[]{}));
        cfg.getEventListeners().setPostUpdateEventListeners(postUpdateEventListeners.toArray(new PostUpdateEventListener[]{}));
//        cfg.getEventListeners().setFlushEntityEventListeners(flushEntityEventListeners.toArray(new FlushEntityEventListener[]{}));
    }

}
