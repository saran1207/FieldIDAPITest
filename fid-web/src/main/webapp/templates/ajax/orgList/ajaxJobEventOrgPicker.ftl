<#assign locationPickerMarkup>
    <@n4.location name="criteria.location" id="location" nodesList=predefinedLocationTree fullName="${helper.getFullNameOfLocation(criteria.location)}" theme="fieldid"/>
</#assign>

<#escape x as x?js_string >
    updateLocation('${locationPickerMarkup}');
</#escape>

