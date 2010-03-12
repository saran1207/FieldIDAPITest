package com.n4systems.exporting;

import com.n4systems.api.conversion.ModelToViewConverter;
import com.n4systems.api.conversion.product.ProductToViewConverter;
import com.n4systems.api.model.ProductView;
import com.n4systems.exporting.beanutils.ExportMapMarshaler;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.model.Product;
import com.n4systems.persistence.loaders.ListLoader;

public class ProductExporter implements Exporter {
	private final ExportMapMarshaler<ProductView> marshaler;
	private final ListLoader<Product> productLoader;
	private final ModelToViewConverter<Product, ProductView> converter;

	public ProductExporter(ListLoader<Product> productLoader, ExportMapMarshaler<ProductView> marshaler, ModelToViewConverter<Product, ProductView> converter) {
		this.productLoader = productLoader;
		this.marshaler = marshaler;
		this.converter = converter;
	}
	
	public ProductExporter(ListLoader<Product> productLoader) {
		this(productLoader, new ExportMapMarshaler<ProductView>(ProductView.class), new ProductToViewConverter());
	}

	@Override
	public void export(MapWriter mapWriter) throws ExportException {
		ProductView view;
		
		for (Product product: productLoader.load()) {
			try {
				view = converter.toView(product);
				mapWriter.write(marshaler.toBeanMap(view));
			} catch (Exception e) {
				throw new ExportException(String.format("Unable to export Product [%s]", product), e);
			}
		}
	}

}
