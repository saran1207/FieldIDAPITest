<#include "../../../../templates/html/common/_formErrors.ftl"/>
<@s.form id="nameForm" action="updateTenantName" namespace="/adminAjax" theme="fieldidSimple" >
	<@s.hidden name="id"/>
	<label class="bold">Company ID: </label>
	<span>
	<@s.textfield name="tenantName"/>
	</span>
	<div id="nameFormActions">
		<input id="updateNameButton" type="button" onClick="updateName();" value="<@s.text name='label.save'/>"/>
		<@s.text name="label.or"/>
		<a href="javascript:void(0);" onClick="cancelName(${id});"> 
			<@s.text name="label.cancel"/>
		</a>
	</div>
</@s.form>
