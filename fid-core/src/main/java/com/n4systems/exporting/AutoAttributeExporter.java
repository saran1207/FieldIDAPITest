package com.n4systems.exporting;

import com.n4systems.api.conversion.ModelToViewConverter;
import com.n4systems.api.conversion.autoattribute.AutoAttributeToViewConverter;
import com.n4systems.api.model.AutoAttributeView;
import com.n4systems.exporting.beanutils.ExportMapMarshaller;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.persistence.loaders.ListLoader;

public class AutoAttributeExporter implements Exporter {
	private final ExportMapMarshaller<AutoAttributeView> marshaler;
	private final ListLoader<AutoAttributeDefinition> autoAttribLoader;
	private final ModelToViewConverter<AutoAttributeDefinition, AutoAttributeView> converter;

	public AutoAttributeExporter(ListLoader<AutoAttributeDefinition> autoAttribLoader, ExportMapMarshaller<AutoAttributeView> marshaler, ModelToViewConverter<AutoAttributeDefinition, AutoAttributeView> converter) {
		this.autoAttribLoader = autoAttribLoader;
		this.marshaler = marshaler;
		this.converter = converter;
	}
	
	public AutoAttributeExporter(ListLoader<AutoAttributeDefinition> autoAttribLoader) {
		this(autoAttribLoader, new ExportMapMarshaller<AutoAttributeView>(AutoAttributeView.class), new AutoAttributeToViewConverter());
	}

	@Override
	public void export(MapWriter mapWriter) throws ExportException {
		AutoAttributeView view;
		
		for (AutoAttributeDefinition attrib: autoAttribLoader.load()) {
			try {
				view = converter.toView(attrib);
				mapWriter.write(marshaler.toBeanMap(view));
			} catch (Exception e) {
				throw new ExportException(String.format("Unable to export Auto Attribute [%d]", attrib.getId()), e);
			}
		}
	}
	
}
