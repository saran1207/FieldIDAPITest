<head>
	<script type="text/javascript" src="<@s.url value="/javascript/eventType.js"/>"></script>
	<script type="text/javascript">
		eventAttributeIndex = ${(infoFields?size)!0};
		addEventAttributeUrl = '<@s.url action="eventAttributeAdd" namespace="/ajax"/>';
	</script>
	
	<style>
		.crudForm .label {
 		   font-weight: normal;
		}
		
		.eventAttribute {
			padding: 5px 0;
		}
	</style>
	
</head>

<#include "/templates/html/common/_formErrors.ftl" />
<@s.hidden name="uniqueID" />
<h2><@s.text name="label.details"/></h2>
<div class="infoSet">
	<@s.label cssClass="label" cssClass="label" value="${ action.getText( 'label.name' ) }:" />
	<span class="fieldHolder">   
		<@s.textfield name="name" >
			<#if (action.fieldErrors['name'])?exists> 
				<@s.param name="cssClass">inputError</@s.param>
			</#if>
			<#if (action.fieldErrors['name'])?exists> 
				<@s.param name="title">${action.fieldErrors['name']}</@s.param>
			</#if>  
		</@s.textfield>
	</span>
</div>

<div class="infoSet">
	<@s.label cssClass="label" value="${action.getText('label.group')}:" />
	<span class="fieldHolder">
		<@s.select name="group" list="eventTypeGroups" listKey="id" listValue="name" />
	</span>
</div>
<div class="infoSet">
	<@s.label cssClass="label" value="${action.getText('label.printable')}:" />
	<span class="fieldHolder">
		<@s.checkbox name="printable"/>
	</span>
</div>

<#if !action.isAction()>
    <div class="infoSet">
        <@s.label cssClass="label" value="${action.getText('label.masterevent')}:" />
        <span class="fieldHolder">
            <@s.checkbox name="master"/>
        </span>
    </div>

    <#if securityGuard.assignedToEnabled>
        <div class="infoSet">
            <@s.label cssClass="label" value="${action.getText('label.assigned_to_can_be_updated')}:" />
            <span class="fieldHolder">
                <@s.checkbox name="assignedToAvailable"/>
            </span>
        </div>
    </#if>
</#if>
<#if securityGuard.proofTestIntegrationEnabled>
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
</#if>
<div >
	<h2><@s.text name="label.eventattributes"/></h2>
	<div id="infoFields">
		<#list infoFields as infoField >
			<#if infoField?exists>
				<#include "_eventAttributeForm.ftl"/>
			</#if>
		</#list>
	</div>
	<div class="infoSet">
		<button onclick="addEventAttribute(); return false;" ><@s.text name="label.addattribute"/></button>
	</div>
</div>


<div class="formAction">
	<@s.submit key="hbutton.save" name="save"/> 

	<#if !uniqueID?exists >
		<@s.text name="label.or"/>
		<@s.submit key="hbutton.saveandaddeventform" name="saveAndAdd"/> 
	<#else >
		<@s.text name="label.or"/>
		<@s.url id="deleteConfirmUrl" action="eventTypeDeleteConfirm" uniqueID="${uniqueID}"/>
		<a href="#" onclick="return redirect('${deleteConfirmUrl}');"><@s.text name="label.delete"/></a>
	</#if>
		<@s.text name="label.or"/> <a href="${cancelUrl}"><@s.text name="label.cancel"/></a>
</div>


