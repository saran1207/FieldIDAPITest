${action.setPageType('predefined_location', 'location_list')!}
<#assign currentAction="predefinedLocation.action" />
<head>
	<@n4.includeStyle type="page" href="locationCrud" />
	<script type="text/javascript" src="<@s.url value="/javascript/location.js"/>"></script>
	<script type="text/javascript">
		locationIndex = ${(nodes?size)!0};
		//addLocationUrl = '<@s.url action="locationAdd" namespace="/ajax"/>';
	</script>
</head>

<div class="levelContainer">
	<div class="levels">
		<ul>
			<li class="level">Level name 1</li>
			<li class="level">Level name 2</li>
			<li class="level">
				<input type="text" name="newLevel"/>
				 <input type="button" name="addLevel" value="<@s.text name="Add New Level"/>" id=""/>
			</li>
		</ul>
	</div>
</div>

<div class="nodeContainer">
	<div class="nodes">
		<h3>Level name 1</h3>
		<ul>
			<#list nodes as node >
				<#if nodes?exists>
					<#include "_locationForm.ftl"/>
				</#if>
			</#list>
			<li class="node">
				<input type="text" name="newLevel"/>
				 <input type="button" name="addNode" value="<@s.text name="Add New Node"/>" id=""/>
			</li>
		</ul>
	</div>
	<div class="nodes">
		<h3>Level name 2</h3>
		<ul>
			<li class="node">Node 3</li>
			<li class="node">Node 4</li>
			<li class="node">
				<input type="text" name="newLevel"/>
				 <input type="button" name="addNode" value="<@s.text name="Add New Node"/>" id=""/>
			</li>
		</ul>
	</div>
	
</div>