<ul class="secondaryNav" >
	<#if !secondaryNavAction?exists || secondaryNavAction != "list">
		<li><a href="<@s.url action="jobAssets" projectId="${project.id}"/>">&#171; <@s.text name="label.View_all_assets"/></a></li>
	<#elseif sessionUser.hasAccess("managejobs")>
		<li class="add button"><a id="addNewAsset" href="<@s.url action="jobAssetAdd" projectId="${project.id}"/>"><@s.text name="label.add_asset"/></a></li>
	</#if>
</ul>