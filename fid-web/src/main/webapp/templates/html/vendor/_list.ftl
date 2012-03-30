<#if page.hasResults() && page.validPage() >
    <head>
        <script type="text/javascript">

            jQuery(document).ready(function(){
                jQuery('.registerAssetLightbox').colorbox({iframe: true, width: '770px', height: '550px'});
            });

        </script>
    </head>
	<#include '../common/_pagination.ftl' />
	<table class="list" id="assetTable">
		<tr>
			<th><@s.text name="label.id_number" /></th>
			<th><@s.text name="label.rfidnumber" /></th>
			<th><@s.text name="label.assettype" /></th>
			<th><@s.text name="label.desc" /></th>
			<th></th>
		</tr>
		
		<#list page.list as asset>
		<tr>
			<td>
				<a href='<@s.url value="showNetworkAsset.action" uniqueID="${asset.id}"/>' >${asset.identifier}</a>
			</td>
			<#if asset.rfidNumber??>
				<td>${asset.rfidNumber}</td>
			<#else>
				<td>&nbsp;</td>
			</#if>			
			<td>${asset.type.name}</td>
			<td>${asset.description!}&nbsp;</td>
			<td>
			    <#if action.isAssetAlreadyRegistered(asset)>
                    <@s.text name="label.already_registered"/>
                <#else>
                    <a href='<@s.url action="regNetworkAsset.action" namespace="/aHtml/iframe" uniqueID="${asset.id}"/>' class="registerAssetLightbox" ><@s.text name="label.registerasset"/></a>
                </#if>
			</td>
		</tr>
		</#list>			
	</table>
	<#include '../common/_pagination.ftl' />
</div>
<#elseif !page.hasResults() >
	<div class="emptyList" >
		<h2><@s.text name="label.noresults"/></h2>
		<p>
			<#if isSearch>
				<@s.text name="label.empty_network_asset" />				
			<#else>
				<@s.text name="label.empty_pre_assigned_asset_list" />
			</#if>
		</p>
	</div>
<#else>
	<#if isSearch>
		<@s.url value="searchNetworkAsset.action" uniqueID="${uniqueID}" id="url"/>				
	<#else>
		<@s.url value="preAssignedAssets.action" uniqueID="${uniqueID}" id="url"/>				
	</#if>
	<script type="text/javascript">
		var lastPage = ${page.lastPage};
		window.location.href = '${url}' + '\&currentPage=' + lastPage;
	</script>
</#if>