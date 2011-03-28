package com.n4systems.export.converters;

import javax.persistence.EntityManager;

import com.n4systems.model.SubAsset;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class SubAssetConverter extends ExportConverter {
	private final EntityManager em;
	
	public SubAssetConverter(EntityManager em) {
		this.em = em;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(SubAsset.class);
	}
	
	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		SubAsset sub = (SubAsset) source;
		
		writer.startNode("Component");
		writeNode(writer, context, "Label", sub.getLabel());
		
		context.convertAnother(sub.getAsset(), new AssetConverter(em));
		writer.endNode();
	}

}
