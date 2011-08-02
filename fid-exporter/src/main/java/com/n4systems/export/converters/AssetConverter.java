package com.n4systems.export.converters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class AssetConverter extends EntityWithOwnerConverter<Asset> {
	protected final EntityManager em;
	
	public AssetConverter(EntityManager em) {
		this.em = em;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return Asset.class.isAssignableFrom(type);
	}
	
	@Override
	protected void marshalEntity(Asset asset, HierarchicalStreamWriter writer, MarshallingContext context) {
		writeNode(writer, context, "AssetId",				asset.getMobileGUID());
		writeNode(writer, context, "ActiveState",			asset.getEntityState());
		writeNode(writer, context, "AssetType",				asset.getType());
		writeNode(writer, context, "SerialNumber",			asset.isActive() ? asset.getIdentifier() : asset.getArchivedIdentifier());
		writeNode(writer, context, "RFID",					asset.getRfidNumber());
		writeNode(writer, context, "ReferenceNumber",		asset.getCustomerRefNumber());
		writeNode(writer, context, "Location",				asset.getAdvancedLocation());
		
		super.marshalEntity(asset, writer, context);
		
		writeNode(writer, context,		"IdentifiedDate",		asset.getIdentified());
		writeUserNode(writer, context,	"IdentifiedBy",			asset.getIdentifiedBy());
		writeUserNode(writer, context,	"AssignedTo",			asset.getAssignedUser());
		writeNode(writer, context, 		"PurchaseOrder",		asset.getPurchaseOrder());
		writeNode(writer, context,		"OrderNumber",			asset.getShopOrder());
		writeNode(writer, context,		"CustomerOrderNumber",	asset.getCustomerOrder());
		writeNode(writer, context,		"CurrentAssetStatus",	asset.getAssetStatus());
		writeNode(writer, context,		"Comments",				asset.getComments());
		
		List<InfoOptionBean> infoOptions = new ArrayList<InfoOptionBean>(asset.getInfoOptions());
		Collections.sort(infoOptions);
		writeIterable(writer, context, "Attributes", infoOptions, new InfoOptionConverter());
		
		List<Event> events = loadEvents(asset);
		System.out.println("\tFound " + events.size() + " events");
		writeIterable(writer, context, "Events", events, new EventConverter());
	}

	protected List<Event> loadEvents(Asset asset) {
		String jpql = String.format("FROM %s e WHERE e.asset.id = :assetId ORDER BY e.date", Event.class.getName());
		TypedQuery<Event> query = em.createQuery(jpql, Event.class);
		query.setParameter("assetId", asset.getId());

		List<Event> events = query.getResultList();
		return events;
	}
}
