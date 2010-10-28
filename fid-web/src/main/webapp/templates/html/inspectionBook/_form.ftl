<head>
	<#include "/templates/html/common/_orgPicker.ftl"/>
</head>
<#include "/templates/html/common/_formErrors.ftl"/>
<@s.hidden name="currentPage"/>
<@s.hidden name="uniqueID"/>
<p>
	<label><@s.text name="label.title"/></label>
	<span>
		<@s.textfield name="name" >
			<#if (action.fieldErrors['name'])?exists> 
				<@s.param name="cssClass">inputError</@s.param>
				<@s.param name="title">${action.fieldErrors['name']}</@s.param>
			</#if>  
		</@s.textfield>
	</span>
</p>
<p>
	<label for="owner"><@s.text name="label.owner"/></label>
	<@n4.orgPicker name="owner" theme="fieldid" required="true"/>
</p>

<p>
	<label><@s.text name="label.open"/></label>
	<span>
		<@s.checkbox name="open" />
	</span>
</p>
<div class="formAction">
	<button onclick="return redirect( '<@s.url action="eventBooks" currentPage="${currentPage}" />' );" ><@s.text name="label.cancel"/></button>
	<@s.submit key="hbutton.save" />
</div>