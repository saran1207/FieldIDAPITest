package com.n4systems.export.converters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.export.EventsForAssetLoader;
import com.n4systems.export.SubAssetsForMasterLoader;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.SubAsset;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class AssetConverter extends EntityWithOwnerConverter<Asset> {
	private final SubAssetsForMasterLoader subAssetLoader;
	private final EventsForAssetLoader eventLoader;
	
	public AssetConverter(SubAssetsForMasterLoader subAssetLoader, EventsForAssetLoader eventLoader) {
		this.subAssetLoader = subAssetLoader;
		this.eventLoader = eventLoader;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(Asset.class);
	}
	
	@Override
	protected void marshalEntity(Asset asset, HierarchicalStreamWriter writer, MarshallingContext context) {
		System.out.println("Converting: " + asset);	
		writeNode(writer, context, "AssetId",				asset.getMobileGUID());
		writeNode(writer, context, "ActiveState",			asset.getEntityState());
		writeNode(writer, context, "AssetType",				asset.getType());
		writeNode(writer, context, "SerialNumber",			asset.isActive() ? asset.getSerialNumber() : asset.getArchivedSerialNumber());
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
		writeIterable(writer, context, "Attributes", infoOptions);
		
		List<SubAsset> subAssets = subAssetLoader.loadSubAssets(asset);
		System.out.println("\tFound " + subAssets.size() + " sub assets");
		writeIterable(writer, context, "Components", subAssets);
		
		
		List<Event> events = eventLoader.loadEvents(asset);
		System.out.println("\tFound " + events.size() + " events");
		writeIterable(writer, context, "Events", events);
	}

}
