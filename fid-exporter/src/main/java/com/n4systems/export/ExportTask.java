package com.n4systems.export;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.n4systems.export.converters.AssetConverter;
import com.n4systems.export.converters.AssetStatusConverter;
import com.n4systems.export.converters.AssetTypeConverter;
import com.n4systems.export.converters.AssignedToUpdateConverter;
import com.n4systems.export.converters.BaseOrgConverter;
import com.n4systems.export.converters.CriteriaResultConverter;
import com.n4systems.export.converters.DeficiencyConverter;
import com.n4systems.export.converters.EntityStateConverter;
import com.n4systems.export.converters.EventBookConverter;
import com.n4systems.export.converters.EventConverter;
import com.n4systems.export.converters.EventScheduleConverter;
import com.n4systems.export.converters.EventTypeConverter;
import com.n4systems.export.converters.ISODateConverter;
import com.n4systems.export.converters.InfoOptionConverter;
import com.n4systems.export.converters.LineItemConverter;
import com.n4systems.export.converters.LocationConverter;
import com.n4systems.export.converters.OrderConverter;
import com.n4systems.export.converters.ProjectConverter;
import com.n4systems.export.converters.ProofTestInfoConverter;
import com.n4systems.export.converters.RecommendationConverter;
import com.n4systems.export.converters.StringTrimmingConverter;
import com.n4systems.export.converters.SubAssetConverter;
import com.n4systems.export.converters.SubEventConverter;
import com.n4systems.export.converters.UserConverter;
import com.n4systems.model.Asset;
import com.n4systems.model.SubAsset;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.Dom4JDriver;

public class ExportTask implements PersistenceTask {
	private final long tenantId;
	private final OutputStream out;
	
	public ExportTask(long tenantId, OutputStream out) {
		this.tenantId = tenantId;
		this.out = out;
	}
	
	private XStream createXStream(EntityManager em) {
		XStream xs = new XStream(new Dom4JDriver());
		xs.setMode(XStream.NO_REFERENCES);
		xs.alias("Assets", ExportCollectionWrapper.class);
		xs.addImplicitCollection(ExportCollectionWrapper.class, "collection");
		xs.alias("Asset", Asset.class);
		xs.registerConverter(new StringTrimmingConverter(), XStream.PRIORITY_VERY_HIGH);
		xs.registerConverter(new ISODateConverter(), XStream.PRIORITY_VERY_HIGH);
		xs.registerConverter(new AssetConverter(new SubAssetsForMasterLoader(em), new EventsForAssetLoader(em)));
		xs.registerConverter(new EventConverter());
		xs.registerConverter(new SubEventConverter());
		xs.registerConverter(new CriteriaResultConverter());
		xs.registerConverter(new EventTypeConverter());
		xs.registerConverter(new EventBookConverter());
		xs.registerConverter(new ProofTestInfoConverter());
		xs.registerConverter(new EventScheduleConverter());
		xs.registerConverter(new UserConverter());
		xs.registerConverter(new AssignedToUpdateConverter());
		xs.registerConverter(new BaseOrgConverter());
		xs.registerConverter(new LocationConverter());
		xs.registerConverter(new EntityStateConverter());
		xs.registerConverter(new AssetStatusConverter());
		xs.registerConverter(new AssetTypeConverter());
		xs.registerConverter(new LineItemConverter());
		xs.registerConverter(new OrderConverter());
		xs.registerConverter(new InfoOptionConverter());
		xs.registerConverter(new SubAssetConverter());
		xs.registerConverter(new ProjectConverter());
		xs.registerConverter(new RecommendationConverter());
		xs.registerConverter(new DeficiencyConverter());
		return xs;
	}
	
	@Override
	public void runTask(EntityManager em) throws Exception {
		List<Asset> assets = loadMasterAssets(em, tenantId);
		
		Writer writer = new OutputStreamWriter(out, "UTF-8");
		
		System.out.println("Exporting to xml ...");
		createXStream(em).toXML(new ExportCollectionWrapper<Asset>(assets), writer);
		writer.flush();
	}

	private List<Asset> loadMasterAssets(EntityManager em, long tenantId) {
		String jpql = String.format("FROM %s a WHERE a.tenant.id = :tenantId AND a.id NOT IN (SELECT s.asset.id FROM %s s)", Asset.class.getName(), SubAsset.class.getName());
		TypedQuery<Asset> query = em.createQuery(jpql, Asset.class);
		query.setParameter("tenantId", tenantId);

		System.out.print("Loading assets ... ");
		List<Asset> assets = query.getResultList();
		System.out.println("Done");
		
		System.out.println(String.format("Found: %d Master Assets", assets.size()));
		return assets;
	}



}
