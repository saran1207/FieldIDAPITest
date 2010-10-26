package com.n4systems.exporting;

import com.n4systems.api.conversion.ModelToViewConverter;
import com.n4systems.api.conversion.product.AssetToViewConverter;
import com.n4systems.api.model.AssetView;
import com.n4systems.exporting.beanutils.ExportMapMarshaler;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.model.Asset;
import com.n4systems.persistence.loaders.ListLoader;

public class ProductExporter implements Exporter {
	private final ExportMapMarshaler<AssetView> marshaler;
	private final ListLoader<Asset> productLoader;
	private final ModelToViewConverter<Asset, AssetView> converter;

	public ProductExporter(ListLoader<Asset> productLoader, ExportMapMarshaler<AssetView> marshaler, ModelToViewConverter<Asset, AssetView> converter) {
		this.productLoader = productLoader;
		this.marshaler = marshaler;
		this.converter = converter;
	}
	
	public ProductExporter(ListLoader<Asset> productLoader) {
		this(productLoader, new ExportMapMarshaler<AssetView>(AssetView.class), new AssetToViewConverter());
	}

	@Override
	public void export(MapWriter mapWriter) throws ExportException {
		AssetView view;
		
		for (Asset product: productLoader.load()) {
			try {
				view = converter.toView(product);
				mapWriter.write(marshaler.toBeanMap(view));
			} catch (Exception e) {
				throw new ExportException(String.format("Unable to export Asset [%s]", product), e);
			}
		}
	}

}
