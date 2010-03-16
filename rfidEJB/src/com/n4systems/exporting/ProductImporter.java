package com.n4systems.exporting;

import com.n4systems.api.conversion.ConversionException;
import com.n4systems.api.conversion.product.ProductToModelConverter;
import com.n4systems.api.model.ExternalModelView;
import com.n4systems.api.model.ProductView;
import com.n4systems.api.validation.Validator;
import com.n4systems.api.validation.validators.ProductViewValidator;
import com.n4systems.exporting.io.MapReader;
import com.n4systems.model.Product;
import com.n4systems.persistence.Transaction;
import com.n4systems.services.product.ProductSaveService;

public class ProductImporter extends AbstractImporter<ProductView> {
	private final ProductSaveService saver;
	private final ProductToModelConverter converter;
	
	public ProductImporter(MapReader mapReader, Validator<ExternalModelView> validator, ProductSaveService saver, ProductToModelConverter converter) {
		super(ProductView.class, mapReader, validator);
		this.saver = saver;
		this.converter = converter;
		
		validator.getValidationContext().put(ProductViewValidator.PRODUCT_TYPE_KEY, converter.getType());
	}

	@Override
	protected void importView(Transaction transaction, ProductView view) throws ConversionException {
		Product product = converter.toModel(view, transaction);
		saver.setProduct(product);
		saver.createWithoutHistory();
	}

}
