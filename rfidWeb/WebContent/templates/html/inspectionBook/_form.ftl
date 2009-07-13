
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
	<label><@s.text name="label.customer"/></label>
	<span>
		<@s.select name="customerId" list="customers" listKey="id" listValue="name" headerKey="" headerValue="">
			<#if (action.fieldErrors['customerId'])?exists> 
				<@s.param name="cssClass">inputError</@s.param>
				<@s.param name="title">${action.fieldErrors['customerId']}</@s.param>
			</#if>  
			
		</@s.select>
	</span>
</p>

<p>
	<label><@s.text name="label.open"/></label>
	<span>
		<@s.checkbox name="open" />
	</span>
</p>
<div class="formAction">
	<button onclick="return redirect( '<@s.url action="inspectionBooks" currentPage="${currentPage}" />' );" ><@s.text name="label.cancel"/></button>
	<@s.submit key="hbutton.save" />
</div>