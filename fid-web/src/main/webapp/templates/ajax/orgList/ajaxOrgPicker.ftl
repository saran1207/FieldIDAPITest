<#assign locationPickerMarkup>
    <@n4.location name="assetWebModel.location" id="location" nodesList=predefinedLocationTree fullName="${helper.getFullNameOfLocation(assetWebModel.location)}"  theme="simple"/>
</#assign>

<#escape x as x?js_string >
    updateLocation('${locationPickerMarkup}');
</#escape>

