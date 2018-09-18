package com.n4systems.fieldid.api.pub.mapping.model.marshal;

import com.n4systems.fieldid.api.pub.mapping.SetterReference;
import com.n4systems.fieldid.api.pub.mapping.TypeMapper;
import com.n4systems.fieldid.api.pub.mapping.TypeMapperBuilder;
import com.n4systems.fieldid.api.pub.model.Messages;
import com.n4systems.model.Asset;

/**
 * Created by rrana on 2018-09-13.
 */
public class AssetToMessage<T> extends TypeMapper<Asset, T> {

public AssetToMessage(SetterReference<T, String> setAssetId, SetterReference<T, String> setIdentifier, SetterReference<T, String> setRfidNumber, SetterReference<T, String> setCustomerRefNumber) {
        super(TypeMapperBuilder.<Asset, T>newBuilder()
        .add(Asset::getPublicId, setAssetId)
        .add(Asset::getIdentifier, setIdentifier)
        .add(Asset::getRfidNumber, setRfidNumber)
        .add(Asset::getCustomerRefNumber, setCustomerRefNumber)
        .build());
        }
}