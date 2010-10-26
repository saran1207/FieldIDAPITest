<title><@s.text name="title.summaryreport"/></title>

<head>
	<@n4.includeStyle type="page" href="aggregateReport"/>
</head>
<div class="adminLink">	
	<a href="<@s.url action="reportResults"  searchId="${searchId!1}" currentPage="${currentPage!1}"/>"><@s.text name="label.returntoreport"/></a>
</div>

<div class="crudForm largeForm">
	<div class=" multiRow">
		<h2><@s.text name="label.summary"/></h2>
		<p>
			<label><@s.text name="label.totalassets"/></label>
			<span>${report.totalAssets}</span>
		</p>
		
		<p>
			<label><@s.text name="label.totalinspections"/></label>
			<span>${report.totalInspections}</span>
		</p>
		
		<p>
			<label><@s.text name="label.numberofassettypes"/></label>
			<span>${report.assetTypes?size}</span>
		</p>
		
		<p>
			<label><@s.text name="label.numberofeventtypegroup"/></label>
			<span>${report.inspectionTypeGroups?size}</span>
		</p>
	</div>
	<h2><@s.text name="label.details"/></h2>
	<table class="list">
		<tr>
			<th><@s.text name="label.assettype"/></th>
			<th><@s.text name="label.totalinspections"/></th>
			<th><@s.text name="label.totalassets"/></th>
		</tr>
		
		<#list report.assetTypes as key >
			
			
			<tr>
				<td class="nameRow">
					<a id="detailRowAssetOpen_${key}" href="javascript:void(0);" onclick="return openSection( 'detailRowAsset_${key}', 'detailRowPAssetOpen_${key}','detailRowAssetClose_${key}');"><img src="<@s.url value="/images/expandLarge.gif"/>" alt"+"></a>
					<a id="detailRowAssetClose_${key}" style="display:none" href="javascript:void(0);" onclick="return closeSection( 'detailRowAsset_${key}', 'detailRowAssetClose_${key}','detailRowAssetOpen_${key}');"><img src="<@s.url value="/images/collapseLarge.gif"/>" alt="-"></a>
					${key?html}
					<div id="detailRowAsset_${key}" style="display:none" class="subList">
						<#list report.countsByAssetType[ key ] as record >
							<div class="count">
								${record.count!0} ${record.inspectionTypeGroupName?html}
							</div> 
						</#list>
					</div>
				</td>
				<td>${report.getInspectionsForAssetType( key ) }</td>
				<td>${report.getDisinctAssetsForAssetType( key ) }</td>
			</tr>
			
			
		</#list>
	</table>
	
	<table class="list">
		<tr>
			<th><@s.text name="label.eventtypegroup"/></th>
			<th><@s.text name="label.totalinspections"/></th>
		</tr>
		
		<#list report.inspectionTypeGroups as key >
			<tr>
				<td class="nameRow">
					<a id="detailRowInspectionOpen_${key}" href="javascript:void(0);" onclick="return openSection( 'detailRowInspection_${key}', 'detailRowInspectionOpen_${key}','detailRowInspectionClose_${key}');"><img src="<@s.url value="/images/expandLarge.gif"/>" alt"+"></a>  
					<a id="detailRowInspectionClose_${key}" style="display:none" href="javascript:void(0);" onclick="return closeSection( 'detailRowInspection_${key}', 'detailRowInspectionClose_${key}','detailRowInspectionOpen_${key}');"><img src="<@s.url value="/images/collapseLarge.gif"/>" alt="-"></a> 
					${key?html}
					<div id="detailRowInspection_${key}" style="display:none" class="subList">
						<#list report.countsByInspectionTypeGroup[ key ] as record >
							<div class="count">
								${record.count!0} ${record.assetTypeName?html}
							</div> 
						</#list>
					</div>
				</td>
				<td>${report.getInspectionsForInspectionTypeGroup( key ) }</td>
			</tr>
			
			
		</#list>
	</table>
	
	
	<div class="adminLink">	
		<a href="<@s.url action="reportResults" searchId="${searchId!1}" currentPage="${currentPage!1}"/>"><@s.text name="label.returntoreport"/></a>
	</div>
</div>

