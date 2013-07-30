package com.n4systems.fieldid.wicket.pages.setup.assettype;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.n4systems.model.AssetType;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import java.util.List;
import java.util.Set;

public class CopyAssetTypePage extends AddAssetTypePage {

    public CopyAssetTypePage(PageParameters params) {
        super(params);
    }

    @Override
    public boolean isCopy() {
        return true;
    }

    @Override
    protected AssetType getAssetType(PageParameters params) {
        AssetType copy = new AssetType();
        AssetType original = assetTypeService.getAssetType(params.get("uniqueID").toLong());
        copy.setTenant(original.getTenant());
        copy.setGroup(original.getGroup());
        copy.setDescriptionTemplate(original.getDescriptionTemplate());
        copy.setLinkable(original.isLinkable());
        copy.setHasManufactureCertificate(original.isHasManufactureCertificate());
        copy.setManufactureCertificateText(original.getManufactureCertificateText());
        copy.setWarnings(original.getWarnings());
        copy.setInstructions(original.getInstructions());
        copy.setCautionUrl(original.getCautionUrl());
        copy.setInfoFields(copyInfoFields(original, copy));
        return copy;
    }

    private List<InfoFieldBean> copyInfoFields(AssetType original, AssetType copy) {
        List<InfoFieldBean> copyFields = Lists.newArrayList();
        for(InfoFieldBean field: original.getAvailableInfoFields()) {
            InfoFieldBean copyField = new InfoFieldBean();
            copyField.setName(field.getName());
            copyField.setFieldType(field.getFieldType());
            copyField.setRequired(field.isRequired());
            copyField.setUsingUnitOfMeasure(field.isUsingUnitOfMeasure());
            copyField.setRetired(field.isRetired());
            copyField.setIncludeTime(field.isIncludeTime());
            copyField.setAssetInfo(copy);
            copyField.setUnitOfMeasure(field.getUnitOfMeasure());
            copyField.setUnfilteredInfoOptions(copyInfoOptions(field, copyField));
            copyFields.add(copyField);
        }
        return copyFields;
    }

    private Set<InfoOptionBean> copyInfoOptions(InfoFieldBean original, InfoFieldBean copy) {
        Set<InfoOptionBean> copyOptions = Sets.newHashSet();
        for(InfoOptionBean option: original.getInfoOptions()) {
            InfoOptionBean copyOption = new InfoOptionBean();
            copyOption.setName(option.getName());
            copyOption.setWeight(option.getWeight());
            copyOption.setStaticData(option.isStaticData());
            copyOption.setInfoField(copy);
            copyOptions.add(copyOption);
        }
        return copyOptions;
    }
}
