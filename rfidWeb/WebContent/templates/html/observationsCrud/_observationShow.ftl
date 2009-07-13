<div class="observationsShow" id="${prefix}_${criteriaResult.id}" style="display: none;">

	<#list observationList as observation >
	
		<#switch observation.state>
			<#case "OUTSTANDING">
				<#assign selectClass=prefix + "Selected">
				<#break>
			<#case "REPAIREDONSITE">
				<#assign selectClass=prefix + "RepairedOnSite">
				<#break>
			<#case "REPAIRED">
				<#assign selectClass=prefix + "Repaired">
				<#break>
			<#case "COMMENT">
				<#assign selectClass="obsCommentShow">
		</#switch>

		<p class="observationTextShow ${selectClass}">
			
			<span>${observation.text} </span>
		</p>
	</#list>
</div>