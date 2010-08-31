package com.n4systems.model.security;

import java.util.ArrayList;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;

import com.n4systems.model.api.HasOwner;
import com.n4systems.model.api.NetworkEntity;
import com.n4systems.model.api.SecurityEnhanced;

public class EntitySecurityEnhancer {
	
	public static <T> MethodSecurityInterceptor<T> createMethodInterceptor(T entity, SecurityLevel level) {
		return new MethodSecurityInterceptor<T>(entity, level);
	}
	
	/**
	 * Enhances an entity with the MethodSecurityInterceptor at the given SecurityLevel.
	 * If the entity HasOwner, the owner will also be enhanced
	 * within this method.
	 * @param entity	Entity to enhance
	 * @param level		SecurityLevel to enhance at
	 * @return			The proxied entity
	 */
	@SuppressWarnings("unchecked")
	public static <T> T enhanceEntity(T entity, SecurityLevel level) {
		if (entity instanceof RuntimeEnhanced) {
			// we don't want classes double enhanced
			return entity;
		}
		
		Enhancer e = new Enhancer();
		
		e.setSuperclass(entity.getClass());
		e.setCallback(createMethodInterceptor(entity, level));
		e.setInterfaces(new Class<?>[] {RuntimeEnhanced.class});
		
		T enhancedEntity = (T)e.create();
		
//		if (entity instanceof HasOwner) {
//			enhanceOwner((HasOwner)entity, (HasOwner)enhancedEntity, level);
//		}
		
		return enhancedEntity;
	}
	
	protected static void enhanceOwner(HasOwner from, HasOwner to, SecurityLevel level) {
		to.setOwner(enhance(from.getOwner(), level));
	}
	
	/**
	 * Calls the enhance method on an entity implementing SecurityEnhanced, making sure the entity is not null first.
	 */
	public static <T> T enhance(SecurityEnhanced<T> entity, SecurityLevel level) {
		return (entity != null) ? entity.enhance(level) : null;
	}
	
	/**
	 * Returns an enhanced entity.  Entity SecurityLevel is determined via {@link NetworkEntity#getSecurityLevel(com.n4systems.model.orgs.BaseOrg)} from the entity.
	 * Entities at {@link SecurityLevel#LOCAL} level will be returned directly, un-enhanced.
	 * @param entity	entity to enhance
	 * @param filter	A SecurityFilter with a BaseOrg owner
	 * @return			The enhanced entity
	 */
	public static <T extends NetworkEntity<T>> T enhance(T entity, SecurityFilter filter) {
		SecurityLevel shareLevel = entity.getSecurityLevel(filter.getOwner());
		
//		T enhancedEntity = (shareLevel.equals(SecurityLevel.LOCAL)) ? entity : entity.enhance(shareLevel);
		T enhancedEntity = entity.enhance(shareLevel);
		return enhancedEntity;
	}
	
	/**
	 * Returns an enhanced list of entities.  Entity SecurityLevel is determined via {@link NetworkEntity#getSecurityLevel(com.n4systems.model.orgs.BaseOrg)} from the entity.
	 * Entities at {@link SecurityLevel#LOCAL} level will be returned directly, un-enhanced.
	 * @param entities	List of entities implementing NetworkEntity
	 * @param filter	A SecurityFilter with a BaseOrg owner
	 * @return			The list of enhanced entities
	 */
	public static <T extends NetworkEntity<T>> List<T> enhanceList(List<T> entities, SecurityFilter filter) {
		List<T> enhancedEntities = new ArrayList<T>();
		
		for (T entity: entities) {
			enhancedEntities.add(enhance(entity, filter));
		}
		
		return enhancedEntities;
	}


}
