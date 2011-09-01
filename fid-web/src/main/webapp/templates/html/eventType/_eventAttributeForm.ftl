<p class="eventAttribute" id="attribute_${infoField_index}">
	<@s.textfield name="infoFields[${infoField_index}]" theme="simple"/> 
	
	<a href="javascript:void(0); " onclick=" removeEventAttribute( ${infoField_index} ); return false;"><@s.text name="label.remove"/></a>

</p>