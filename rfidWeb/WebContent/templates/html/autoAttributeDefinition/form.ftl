${action.setPageType('auto_attribute', 'definitions')!}
<#include "_secondaryNav.ftl"/>
<@s.form action="autoAttributeDefinitionSave" cssClass="inputForm" theme="css_xhtml" >
	
	
	<@s.hidden name="uniqueID" />
	<@s.hidden name="criteriaId"/>
		
	<div id="inputlist"  style="width:750px;   padding:10px;  border:1px solid #cfdddd;">
		<h3><@s.text name="label.inputfields" /></h3>
		<#assign noblanks='true' />
		<#assign noComboBox=true/>
		<#assign fieldPrefix='input' />
		<#assign prefix='input'/>
		<#include "/templates/html/common/_dynamicOptions.ftl" />
				
	</div>
	<div style="margin:20px;" >
		<h3><@s.text name="label.mapto" /></h3>
	</div>
	<div id="outputlist"  style="width:750px;   padding:10px;  border:1px solid #cfdddd;">
		<h3><@s.text name="label.outputfields" /></h3>
		<#assign noblanks='false' />
		<#assign noComboBox=true />
		<#assign fieldPrefix= 'output' />
		<#assign prefix= 'output' />
		<#include "/templates/html/common/_dynamicOptions.ftl" />
	</div>
	
	<div class="formAction">
		<input type="button" value="<@s.text name="label.cancel" />"  onclick="return redirect('<@s.url action="autoAttributeDefinitionList" criteriaId="${criteriaId}"/>');" />
		<@s.submit name="save" key="hbutton.save"/>
		<#if !autoAttributeDefinition.id?exists  >
			<@s.submit name="saveandadd" key="hbutton.saveandadd" />
		</#if>
	</div>
</@s.form >

