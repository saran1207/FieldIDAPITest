<p id="attribute_${infoField_index}">
	<@s.textfield name="infoFields[${infoField_index}]" theme="simple"/> 
	
	<a href="javascript:void(0); " onclick=" removeInspectionAttribute( ${infoField_index} ); return false;"><@s.text name="label.remove"/></a>

</p>