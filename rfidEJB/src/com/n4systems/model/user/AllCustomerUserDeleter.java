package com.n4systems.model.user;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import rfid.ejb.entity.UserBean;

import com.n4systems.persistence.deleters.BaseDeleter;

public class AllCustomerUserDeleter extends BaseDeleter<UserBean> {

	@Override
	protected void remove(EntityManager em, UserBean entity) {
		Query query = em.createQuery("UPDATE " + UserBean.class.getName() + " u SET dateModified = :now, deleted=true WHERE owner.customerOrg IS NOT NULL");
		query.setParameter("now", new Date());
		query.executeUpdate();
	}

}
