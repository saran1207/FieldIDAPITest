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
			<label><@s.text name="label.totalevents"/></label>
			<span>${report.totalEvents}</span>
		</p>
		
		<p>
			<label><@s.text name="label.numberofassettypes"/></label>
			<span>${report.assetTypes?size}</span>
		</p>
		
		<p>
			<label><@s.text name="label.numberofeventtypegroup"/></label>
			<span>${report.eventTypeGroups?size}</span>
		</p>
	</div>
	<h2><@s.text name="label.details"/></h2>
	<table class="list">
		<tr>
			<th><@s.text name="label.assettype"/></th>
			<th><@s.text name="label.totalevents"/></th>
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
								${record.count!0} ${record.eventTypeGroupName?html}
							</div> 
						</#list>
					</div>
				</td>
				<td>${report.getEventsForAssetType( key ) }</td>
				<td>${report.getDisinctAssetsForAssetType( key ) }</td>
			</tr>
			
			
		</#list>
	</table>
	
	<table class="list">
		<tr>
			<th><@s.text name="label.eventtypegroup"/></th>
			<th><@s.text name="label.totalevents"/></th>
		</tr>
		
		<#list report.eventTypeGroups as key >
			<tr>
				<td class="nameRow">
					<a id="detailRowEventOpen_${key}" href="javascript:void(0);" onclick="return openSection( 'detailRowEvent_${key}', 'detailRowEventOpen_${key}','detailRowEventClose_${key}');"><img src="<@s.url value="/images/expandLarge.gif"/>" alt"+"></a>
					<a id="detailRowEventClose_${key}" style="display:none" href="javascript:void(0);" onclick="return closeSection( 'detailRowEvent_${key}', 'detailRowEventClose_${key}','detailRowEventOpen_${key}');"><img src="<@s.url value="/images/collapseLarge.gif"/>" alt="-"></a>
					${key?html}
					<div id="detailRowEvent_${key}" style="display:none" class="subList">
						<#list report.countsByEventTypeGroup[ key ] as record >
							<div class="count">
								${record.count!0} ${record.assetTypeName?html}
							</div> 
						</#list>
					</div>
				</td>
				<td>${report.getEventsForEventTypeGroup( key ) }</td>
			</tr>
			
			
		</#list>
	</table>
	
	
	<div class="adminLink">	
		<a href="<@s.url action="reportResults" searchId="${searchId!1}" currentPage="${currentPage!1}"/>"><@s.text name="label.returntoreport"/></a>
	</div>
</div>

