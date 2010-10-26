package com.n4systems.model.product;

import java.io.File;

import javax.persistence.EntityManager;


import com.n4systems.exceptions.InvalidArgumentException;
import com.n4systems.model.Asset;
import com.n4systems.model.user.User;
import com.n4systems.persistence.savers.ModifiedBySaver;
import com.n4systems.reporting.PathHandler;

public class AssetAttachmentSaver extends ModifiedBySaver<AssetAttachment> {
	private final Asset asset;
	
	public AssetAttachmentSaver(Asset asset) {
		super();
		this.asset = asset;
	}
	
	public AssetAttachmentSaver(Long modifiedBy, Asset asset) {
		super(modifiedBy);
		this.asset = asset;
	}

	public AssetAttachmentSaver(User modifiedBy, Asset asset) {
		super(modifiedBy);
		this.asset = asset;
	}

	@Override
	protected void save(EntityManager em, AssetAttachment entity) {
		fillInConnectionFields(entity);
		entity = saveAttachment(em, entity);
		moveAttachedFile(em, entity);
	}
	
	@Override
	protected AssetAttachment update(EntityManager em, AssetAttachment entity) {
		fillInConnectionFields(entity);
		return em.merge(entity);
	}

	@Override
	protected void remove(EntityManager em, AssetAttachment entity) {
		if (entity == null || entity.isNew()) {
			throw new InvalidArgumentException("you need an attachment that has been persisted.");
		}
		
		em.remove(entity);
		deleteFile(entity);
	}

	private AssetAttachment saveAttachment(EntityManager em, AssetAttachment entity) {
		entity = em.merge(entity);
		return entity;
	}

	private void moveAttachedFile(EntityManager em, AssetAttachment entity) {
		AssetAttachmentFileSaver fileSaver = new AssetAttachmentFileSaver();
		fileSaver.setAttachment(entity).save();
		entity = em.merge(entity);
	}

	private void fillInConnectionFields(AssetAttachment entity) {
		entity.setAsset(asset);
		entity.setTenant(asset.getTenant());
	}
	
	//TODO move to a file Deleter.
	private void deleteFile(AssetAttachment attachment) {
		File attachedFile = PathHandler.getProductAttachmentFile(attachment);
		
		if (attachedFile.exists()) {
			attachedFile.delete();
		}
		
	}
}
