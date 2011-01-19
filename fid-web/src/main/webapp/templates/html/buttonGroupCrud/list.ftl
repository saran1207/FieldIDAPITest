${action.setPageType('event_type', 'button_groups')!}
<title><@s.text name="title.buttonsetup"/></title>
<head>
	<@n4.includeStyle href="buttonGroups" type="page" />
	<style>
		.rowName{ width:220px; }
		.formErrors { width:190px; }
		.message ul li, .error ul li { padding-left: 5px; }
	</style>
	<script type="text/javascript" src="<@s.url value="/javascript/buttonGroup.js"/>" ></script>
	<script type="text/javascript">
		undoButtonGroupUrl = '<@s.url action="buttonGroupEdit" namespace="/ajax"/>';
		changesWarning = "<@s.text name="warning.buttongroupsnotsave"/>"; 
		maxNumberOfImages = ${ maxNumberOfImages!0};
		addButtonGroupUrl = '<@s.url action="buttonGroupAdd" namespace="/ajax"/>';
		addButtonUrl = '<@s.url action="buttonAdd" namespace="/ajax"/>';
	</script>
</head>
<p class="instructionsBox">
	<@s.text name="instruction.buttongroup"/>
</p>

<div class="addNewGroup">
	<button id="addGroup" class="submitButton" onclick="addButtonGroup()" ><@s.text name="label.addbuttongroup"/></button>
</div>
<div class="viewSection" />
	<table class="simpleTable" id="buttonGroups" >
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
<div class="addNewGroup">
	<button id="addGroup" class="submitButton" onclick="addButtonGroup()" ><@s.text name="label.addbuttongroup"/></button>
</div>
<div class="buttonActions">
	<button id="save" class="submitButton" onclick="if( confirmNoChanges() == true ) { redirect( '<@s.url action="eventTypes" />' ); }" ><@s.text name="label.save"/></button>
	<@s.text name="label.or"/>
	<@s.url id="cancelUrl" action="eventTypes"/>
	<a href="#" onclick="return redirect('${cancelUrl}');"><@s.text name="label.cancel"/></a>
</div>




