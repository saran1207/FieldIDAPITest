<title><@s.text name="title.buttonsetup"/></title>
<head>
	<style>
		.rowName{ width:220px; }
		.formErrors { width:190px; }
		.message ul li, .error ul li { padding-left: 5px; }
	</style>
	<script type="text/javascript" src="<@s.url value="/javascript/buttonGroup.js"/>" ></script>
	<script type="text/javascript">
		undoButtonGroupUrl = '<@s.url action="buttonGroupEdit" namespace="/ajax"/>';
		changesWarning = "<@s.text name="warning.buttongroupsnotsave"/>"; 
	</script>
	<script type="text/javascript">
		maxNumberOfImages = ${ maxNumberOfImages!0};
		addButtonGroupUrl = '<@s.url action="buttonGroupAdd" namespace="/ajax"/>';
		addButtonUrl = '<@s.url action="buttonAdd" namespace="/ajax"/>';
	</script>
</head>
<p class="instructions">
	<@s.text name="instruction.buttongroup"/>
</p>

<#assign actions>
	<div>
		<button onclick="addButtonGroup()" ><@s.text name="label.addbuttongroup"/></button>
		<button onclick="if( confirmNoChanges() == true ) { redirect( '<@s.url action="inspectionTypeForm" uniqueID="${inspectionTypeId!}"/>' ); }" ><@s.text name="label.imdone"/></button>
	</div>
</#assign>
${actions}
<div class="viewSection" />
	<h2><@s.text name="label.yourbuttongroups"/></h2>
	<table class="simpleTable" id="buttonGroups" >
		<tr>
			<th class="" ><@s.text name="label.name"/></th>
			
			<th><@s.text name="label.buttonsandlabels"/></th>
		</tr>
		<#list stateSets as stateSet >
			<tr id="stateRow_${stateSet_index}">
				<td colspan="2"> 
					<#assign states=stateSet.states />
					<#assign buttonGroupIndex = stateSet_index />
					<#include "_form.ftl"/>
				</td>
			</tr>
		</#list>
		
	</table>
	
		
</div>
${actions}




