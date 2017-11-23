package com.n4systems.exporting;

import com.n4systems.api.conversion.ModelToViewConverter;
import com.n4systems.api.conversion.asset.AssetToViewConverter;
import com.n4systems.api.model.AssetView;
import com.n4systems.exporting.beanutils.ExportMapMarshaller;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.model.Asset;
import com.n4systems.persistence.loaders.ListLoader;

public class AssetExporter implements Exporter {
	private final ExportMapMarshaller<AssetView> marshaler;
	private final ListLoader<Asset> assetLoader;
	private final ModelToViewConverter<Asset, AssetView> converter;

	public AssetExporter(ListLoader<Asset> assetLoader, ExportMapMarshaller<AssetView> marshaler, ModelToViewConverter<Asset, AssetView> converter) {
		this.assetLoader = assetLoader;
		this.marshaler = marshaler;
		this.converter = converter;
	}
	
	public AssetExporter(ListLoader<Asset> assetLoader) {
		this(assetLoader, new ExportMapMarshaller<AssetView>(AssetView.class), new AssetToViewConverter());
	}

	@Override
	public void export(MapWriter mapWriter) throws ExportException {
		AssetView view;
		
		for (Asset asset: assetLoader.load()) {
			try {
				view = converter.toView(asset);
				mapWriter.write(marshaler.toBeanMap(view));
			} catch (Exception e) {
				throw new ExportException(String.format("Unable to export Asset [%s]", asset), e);
			}
		}
	}

}
