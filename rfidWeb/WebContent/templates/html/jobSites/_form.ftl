<head>
	<script type="text/javascript" src="<@s.url value="javascript/customerUpdate.js" />"></script>
	<script type="text/javascript">
		customerChangeUrl = "<@s.url action="divisionList" namespace="/ajax" />";
	</script>
</head>
<#include "/templates/html/common/_formErrors.ftl"/>
<@s.hidden name="currentPage"/>
<@s.hidden name="uniqueID"/>
<p>
	<label><@s.text name="label.name"/></label>
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
		<@s.select name="customer" list="customers" listKey="id" listValue="name" headerKey="" headerValue="" onchange="customerChanged(this);">
			<#if (action.fieldErrors['customer'])?exists> 
				<@s.param name="cssClass">inputError</@s.param>
				<@s.param name="title">${action.fieldErrors['customer']}</@s.param>
			</#if>  
			
		</@s.select>
	</span>
</p>

<p>
	<label><@s.text name="label.division"/></label>
	<span>
		<@s.select name="division" id="division" list="divisions" listKey="id" listValue="name" headerKey="" headerValue="">
			<#if (action.fieldErrors['division'])?exists> 
				<@s.param name="cssClass">inputError</@s.param>
				<@s.param name="title">${action.fieldErrors['division']}</@s.param>
			</#if>  
			
		</@s.select>
	</span>
</p>


<div class="formAction">
	<button onclick="return redirect( '<@s.url action="jobSites"  currentPage="${currentPage!}" />' );" ><@s.text name="label.cancel"/></button>
	<@s.submit key="hbutton.save" />
</div>