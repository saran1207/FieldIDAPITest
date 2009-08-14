<head>
	<script type="text/javascript" src="<@s.url value="/javascript/inspectionType.js"/>"></script>
	<script type="text/javascript">
		inspectionAttributeIndex = ${(infoFields?size)!0};
		addInspectionAttributeUrl = '<@s.url action="inspectionAttributeAdd" namespace="/ajax"/>';
	</script>
	
</head>

<#include "/templates/html/common/_formErrors.ftl" />
<@s.hidden name="uniqueID" />
<h2><@s.text name="label.details"/></h2>
<p>
	<@s.label value="${ action.getText( 'label.name' ) }:" />
	<span>   
		<@s.textfield name="name" >
			<#if (action.fieldErrors['name'])?exists> 
				<@s.param name="cssClass">inputError</@s.param>
			</#if>
			<#if (action.fieldErrors['name'])?exists> 
				<@s.param name="title">${action.fieldErrors['name']}</@s.param>
			</#if>  
		</@s.textfield>
	</span>
</p>

<p>
	<@s.label value="${action.getText('label.group')}:" />
	<span>
		<@s.select name="group" list="inspectionTypeGroups" listKey="id" listValue="name" />
	</span>
</p>
<p>
	<@s.label value="${action.getText('label.printable')}:" />
	<span>
		<@s.checkbox name="printable"/>
	</span>
</p>
<p>
	<@s.label value="${action.getText('label.masterinspection')}:" />
	<span>
		<@s.checkbox name="master"/>
	</span>
</p>

<table class="list">
	<tr>
		<th><@s.text name="label.supportedprooftesttype"/></th>
		<th class="setting"><@s.text name="label.on"/></th>
		<th class="setting"><@s.text name="label.off"/></th>
	</tr>
	<#list proofTestTypes as proofTestType >
		<tr>
			<td><@s.text name="${proofTestType.displayName!}" /></td>
			<td><@s.radio name="supportedProofTestTypes['${proofTestType.name()}']" list="on" /></td>
			<td><@s.radio name="supportedProofTestTypes['${proofTestType.name()}']" list="off"  /></td>
		</tr>
	</#list>
</table>

<div >
	<h2><@s.text name="label.inspectionattributes"/></h2>
	<div id="infoFields">
		<#list infoFields as infoField >
			<#if infoField?exists>
				<#include "_inspectionAttributeForm.ftl"/>
			</#if>
		</#list>
	</div>
	<div class="formAction">
		<button onclick="addInspectionAttribute(); return false;" ><@s.text name="label.addattribute"/></button>
	</div>
</div>


<div class="formAction actions">
	<@s.submit key="hbutton.save" name="save"/> 
	<#if !uniqueID?exists >
		<@s.submit key="hbutton.saveandaddinspectionform" name="saveAndAdd"/> 
	<#else >
		<@s.url id="deleteConfirmUrl" action="inspectionTypeDeleteConfirm" uniqueID="${uniqueID}"/>
		<@s.submit key="label.delete" name="delete" cssClass="delete" onclick="return redirect('${deleteConfirmUrl}');"/> 
	</#if>
	<@s.text name="label.or"/> <a href="${cancelUrl}"><@s.text name="label.cancel"/></a>
	
	
</div>


