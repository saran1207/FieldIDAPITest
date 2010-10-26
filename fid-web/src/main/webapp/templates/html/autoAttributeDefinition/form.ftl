${action.setPageType('auto_attribute', 'definitions')!}
<head>
	<script language="javascript" src="<@s.url value="/javascript/unitOfMeasure.js"/>"> </script>
	<script language="javascript" src="<@s.url value="/javascript/combobox.js"/>"> </script>
	<script type="text/javascript">
		unitOfMeasureUrl = '<@s.url action="unitOfMeasure" namespace="/ajax" />';
	</script>
</head>
<#include "_secondaryNav.ftl"/>
<@s.form action="autoAttributeDefinitionSave" cssClass="fullForm " theme="fieldid" >
	
	
	<@s.hidden name="uniqueID" />
	<@s.hidden name="criteriaId"/>
		
	<div id="inputlist"  class="fluidSets" style="width:750px;   padding:10px;  border:1px solid #cfdddd;">
		<h3><@s.text name="label.inputfields" /></h3>
		<#assign noblanks=true />
		<#assign noComboBox=true/>
		<#assign fieldPrefix='input' />
		<#assign prefix='input'/>
		<#assign useAutoAttributes=false/>
		<#include "/templates/html/assetCrud/_attributes.ftl" />
				
	</div>
	<div style="margin:20px;" >
		<h3><@s.text name="label.mapto" /></h3>
	</div>
	<div id="outputlist"  class="fluidSets" style="width:750px;   padding:10px;  border:1px solid #cfdddd;">
		<h3><@s.text name="label.outputfields" /></h3>
		<#assign noblanks=false />
		<#assign noComboBox=false />
		<#assign fieldPrefix= 'output' />
		<#assign prefix= 'output' />
		<#assign useAutoAttributes=false/>
		<#include "/templates/html/assetCrud/_attributes.ftl" />
	</div>
	
	<div class="actions">
		
		<@s.submit name="save" key="hbutton.save"/>
		<#if !autoAttributeDefinition.id?exists  >
			<@s.submit name="saveandadd" key="hbutton.saveandadd" />
		</#if>
		<@s.text name="label.or"/>
		<a href="<@s.url action="autoAttributeDefinitionList" criteriaId="${criteriaId}"/>" ><@s.text name="label.cancel" /></a>
	</div>
</@s.form >

