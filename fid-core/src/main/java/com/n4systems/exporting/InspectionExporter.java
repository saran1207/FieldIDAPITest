package com.n4systems.exporting;

import com.n4systems.api.conversion.ModelToViewConverter;
import com.n4systems.api.conversion.inspection.InspectionToViewConverter;
import com.n4systems.api.model.InspectionView;
import com.n4systems.exporting.beanutils.ExportMapMarshaler;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.model.Inspection;
import com.n4systems.model.inspectionschedule.NextInspectionDateByInspectionLoader;
import com.n4systems.persistence.loaders.ListLoader;

public class InspectionExporter implements Exporter {
	private final ExportMapMarshaler<InspectionView> marshaler;
	private final ListLoader<Inspection> inspectionLoader;
	private final ModelToViewConverter<Inspection, InspectionView> converter;

	public InspectionExporter(ListLoader<Inspection> inspectionLoader, ExportMapMarshaler<InspectionView> marshaler, ModelToViewConverter<Inspection, InspectionView> converter) {
		this.inspectionLoader = inspectionLoader;
		this.marshaler = marshaler;
		this.converter = converter;
	}
	
	public InspectionExporter(ListLoader<Inspection> inspectionLoader, NextInspectionDateByInspectionLoader nextDateLoader) {
		this(inspectionLoader, new ExportMapMarshaler<InspectionView>(InspectionView.class), new InspectionToViewConverter(nextDateLoader));
	}

	@Override
	public void export(MapWriter mapWriter) throws ExportException {
		InspectionView view;
		
		for (Inspection inspection: inspectionLoader.load()) {
			try {
				view = converter.toView(inspection);
				mapWriter.write(marshaler.toBeanMap(view));
			} catch (Exception e) {
				throw new ExportException(String.format("Unable to export inspection [%s]", inspection), e);
			}
		}
	}

}
