<head>
	<#include "/templates/html/common/_orgPicker.ftl"/>
</head>
<#include "/templates/html/common/_formErrors.ftl"/>
<@s.hidden name="currentPage"/>
<@s.hidden name="uniqueID"/>
<div class="infoSet">
	<label><#include "/templates/html/common/_requiredMarker.ftl"/><@s.text name="label.title"/></label>
	<span>
		<@s.textfield name="name" >
			<#if (action.fieldErrors['name'])?exists> 
				<@s.param name="cssClass">inputError</@s.param>
				<@s.param name="title">${action.fieldErrors['name']}</@s.param>
			</#if>  
		</@s.textfield>
	</span>
</div>
<div class="infoSet">
	<label for="owner"><#include "/templates/html/common/_requiredMarker.ftl"/><@s.text name="label.owner"/></label>
	<@n4.orgPicker name="owner" theme="fieldid"/>
</div>

<div class="infoSet">
	<label><@s.text name="label.open"/></label>
	<span>
		<@s.checkbox name="open" />
	</span>
</div>
<div class="formAction borderLessFormAction">
	<@s.submit key="hbutton.save" />
	<@s.text name="label.or"/>
	<a href="#" onclick="return redirect( '<@s.url action="eventBooks" currentPage="${currentPage}" />' );" ><@s.text name="label.cancel"/></a>
</div>