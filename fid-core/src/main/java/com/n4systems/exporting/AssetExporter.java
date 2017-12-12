package com.n4systems.exporting;

import com.n4systems.api.conversion.ModelToViewConverter;
import com.n4systems.api.conversion.asset.AssetToViewConverter;
import com.n4systems.api.model.AssetView;
import com.n4systems.exporting.beanutils.ExportMapMarshaller;
import com.n4systems.exporting.beanutils.OwnerSerializationHandler;
import com.n4systems.exporting.beanutils.SerializableField;
import com.n4systems.exporting.io.MapWriter;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.infooption.InfoOptionMapConverter;
import com.n4systems.persistence.loaders.ListLoader;
import org.bouncycastle.util.Arrays;
import rfid.ejb.entity.InfoFieldBean;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AssetExporter implements Exporter {
	private final ExportMapMarshaller<AssetView> marshaler;
	private final ListLoader<Asset> assetLoader;
	private final ModelToViewConverter<Asset, AssetView> converter;
	private final AssetType assetType;

	public AssetExporter(ListLoader<Asset> assetLoader, ExportMapMarshaller<AssetView> marshaler, ModelToViewConverter<Asset, AssetView> converter, AssetType assetType) {
		this.assetLoader = assetLoader;
		this.marshaler = marshaler;
		this.converter = converter;
		this.assetType = assetType;
	}
	
	public AssetExporter(ListLoader<Asset> assetLoader) {
		this(assetLoader, new ExportMapMarshaller<AssetView>(AssetView.class), new AssetToViewConverter(), null);
	}

	public AssetExporter(ListLoader<Asset> assetLoader, AssetType assetType) {
		this(assetLoader, new ExportMapMarshaller<AssetView>(AssetView.class), new AssetToViewConverter(), assetType);
	}

	@Override
	public void export(MapWriter mapWriter) throws ExportException {
		AssetView view;

		List<Asset> assets = assetLoader.load();
		if (!assets.isEmpty()) {
			for (Asset asset : assets) {
				try {
					view = converter.toView(asset);
					mapWriter.write(marshaler.toBeanMap(view));
				} catch (Exception e) {
					throw new ExportException(String.format("Unable to export Asset [%s]", asset), e);
				}
			}
		}
		else {
			try {
				mapWriter.writeTitlesOnly(getTitleFields(assetType));
			} catch (Exception e) {
				throw new ExportException(String.format("Unable to export Asset [%s]", "?"), e);
			}
		}
	}

	private List<String> getTitleFields(AssetType assetType) {

		Field[] fields = AssetView.class.getDeclaredFields();
		List<Field> serializableFields = new ArrayList<Field>();
		for (Field field: fields) {
			if (field.isAnnotationPresent(SerializableField.class)) {
				serializableFields.add(field);
			}
		}
		serializableFields.sort(new Comparator<Field>() {
			@Override
			public int compare(Field f1, Field f2) {
				int order1 = f1.getAnnotation(SerializableField.class).order();
				int order2 = f2.getAnnotation(SerializableField.class).order();
				return (order1 == order2) ? 0 : ((order1 > order2) ? 1 : -1);
			}
		});
		List<String> titleFields = new ArrayList<String>();
		for (Field field: serializableFields) {
			String title = field.getAnnotation(SerializableField.class).title();
			if (title.startsWith("A:")) {
				/* Custom attributes */
				for (InfoFieldBean infoField: assetType.getInfoFields()) {
					titleFields.add(title + infoField.getName());
				}
			}
			else
			if (title.isEmpty()) {
				/* This is the owner attribute which consists of multiple fields */
				titleFields.add(OwnerSerializationHandler.ORGANIZATION_MAP_KEY);
				titleFields.add(OwnerSerializationHandler.CUSTOMER_MAP_KEY);
				titleFields.add(OwnerSerializationHandler.DIVISION_MAP_KEY);
			}
			else {
				titleFields.add(title);
			}
		}
		return titleFields;
	}
}
