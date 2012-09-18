<#assign locationPickerMarkup>
    <@n4.location name="modifiableEvent.location" id="location" nodesList=predefinedLocationTree fullName="${helper.getFullNameOfLocation(modifiableEvent.location)}"  theme="simple"/>
</#assign>

<#escape x as x?js_string >
    updateLocation('${locationPickerMarkup}');
</#escape>

