<div class="columnGroup <#if group.isDynamic()>dynamic</#if>" id="${group.id}">
	<h4><@s.text name="${group.label}"/></h4>
	<#list group.mappings as mapping>
		<div>
			<@s.checkbox name="selectedColumns" class="columnSelector" id="chk_${mapping.id}" value="${action.isColumnSelected(mapping.id)}" fieldValue="${mapping.id}" theme="simple"/>
			<label id="lbl_${mapping.id}" for="chk_${mapping.id}"><@s.text name="${mapping.label}"/></label>
		</div>
	</#list>
	<#if group.mappings.empty>
		<div>
			<@s.text name="label.no_common_attributes"/>
		</div>
	</#if>
</div>