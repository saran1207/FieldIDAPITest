package com.n4systems.model.builders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.n4systems.model.UnitOfMeasure;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

public class InfoFieldBuilder extends BaseBuilder<InfoFieldBean> {

	private final String name;
	private final Long weight;
	private final String infoFieldType;
    private final Set<InfoOptionBean> options;
    private final UnitOfMeasure unitOfMeasure;
    private final boolean usingUnitOfMeasure;

	public static InfoFieldBuilder anInfoField() {
		return new InfoFieldBuilder("info_field_1", 1L, InfoFieldBean.TEXTFIELD_FIELD_TYPE, new HashSet<InfoOptionBean>(), null, false);
	}
	
	public InfoFieldBuilder(String name, Long weight, String infoFieldType, Set<InfoOptionBean> options, UnitOfMeasure unitOfMeasure, boolean usingUnitOfMeasure) {
		this.name = name;
		this.weight = weight;
		this.infoFieldType = infoFieldType;
        this.options = options;
        this.unitOfMeasure = unitOfMeasure;
        this.usingUnitOfMeasure = usingUnitOfMeasure;
    }

	public InfoFieldBuilder withName(String name) {
		return makeBuilder(new InfoFieldBuilder(name, weight, infoFieldType, options, unitOfMeasure, usingUnitOfMeasure));
	}

    public InfoFieldBuilder type(String infoFieldType) {
        return makeBuilder(new InfoFieldBuilder(name, weight, infoFieldType, options, unitOfMeasure, usingUnitOfMeasure));
    }

    public InfoFieldBuilder unitOfMeasure(UnitOfMeasure unitOfMeasure) {
        return makeBuilder(new InfoFieldBuilder(name, weight, infoFieldType, options, unitOfMeasure, usingUnitOfMeasure));
    }

    public InfoFieldBuilder usingUnitOfMeasure(boolean usingUnitOfMeasure) {
        return makeBuilder(new InfoFieldBuilder(name, weight, infoFieldType, options, unitOfMeasure, usingUnitOfMeasure));
    }

    public InfoFieldBuilder withOptions(InfoOptionBean... opts) {
        HashSet<InfoOptionBean> options = new HashSet<InfoOptionBean>();
        options.addAll(Arrays.asList(opts));
        return makeBuilder(new InfoFieldBuilder(name, weight, infoFieldType, options, unitOfMeasure, usingUnitOfMeasure));
    }

    @Override
    public InfoFieldBean createObject() {
        InfoFieldBean infoField = new InfoFieldBean();
		infoField.setUniqueID(getId());
		infoField.setName(name);
		infoField.setFieldType(infoFieldType);
		infoField.setWeight(weight);
        infoField.setUnitOfMeasure(unitOfMeasure);
        infoField.setUsingUnitOfMeasure(usingUnitOfMeasure);

        for (InfoOptionBean option : options) {
            option.setInfoField(infoField);
        }

		infoField.setUnfilteredInfoOptions(options);
		return infoField;
    }

}
