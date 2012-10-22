package com.n4systems.export;

import com.n4systems.export.converters.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.Dom4JDriver;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

public class ExportTask {
	private final long tenantId;
	private final OutputStream out;
	
	public ExportTask(long tenantId, OutputStream out) {
		this.tenantId = tenantId;
		this.out = out;
	}
	
	private XStream createXStream(int masterAssets) {
		XStream xs = new XStream(new Dom4JDriver());
		xs.setMode(XStream.NO_REFERENCES);
		
		xs.alias("Assets", ExportCollectionWrapper.class);
		xs.alias("Asset", IdWrapper.class);
		xs.addImplicitCollection(ExportCollectionWrapper.class, "collection");
		
		xs.registerConverter(new StringTrimmingConverter(), XStream.PRIORITY_VERY_HIGH);
		xs.registerConverter(new ISODateConverter(), XStream.PRIORITY_VERY_HIGH);
		xs.registerConverter(new IdWrapperConverter(tenantId, masterAssets));

		xs.registerConverter(new CriteriaResultConverter());
		xs.registerConverter(new EventTypeConverter());
		xs.registerConverter(new EventBookConverter());
		xs.registerConverter(new ProofTestInfoConverter());
		xs.registerConverter(new UserConverter());
		xs.registerConverter(new AssignedToUpdateConverter());
		xs.registerConverter(new BaseOrgConverter());
		xs.registerConverter(new LocationConverter());
		xs.registerConverter(new EntityStateConverter());
		xs.registerConverter(new AssetStatusConverter());
		xs.registerConverter(new AssetTypeConverter());
		xs.registerConverter(new LineItemConverter());
		xs.registerConverter(new OrderConverter());
		xs.registerConverter(new ProjectConverter());
		return xs;
	}
	
	public void doExport() throws Exception {
		List<IdWrapper> assetIds = ReadonlyTransactionExecutor.load(new AssetIdListLoader(tenantId));
		
		Writer writer = new OutputStreamWriter(out, "UTF-8");
		System.out.println("Exporting to xml ...");
		
		try {
			XStream xstream = createXStream(assetIds.size());
			xstream.toXML(new ExportCollectionWrapper<IdWrapper>(assetIds), writer);
		} finally {
			writer.flush();
		}
	}



}
