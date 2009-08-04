<title><@s.text name="title.summaryreport"/></title>

<head>
	<@n4.includeStyle href="pageStyles/aggregateReport"/>
</head>
<div class="adminLink">	
	<a href="<@s.url action="reportResults"  searchId="${searchId!1}" currentPage="${currentPage!1}"/>"><@s.text name="label.returntoreport"/></a>
</div>

<div class="crudForm largeForm">
	<div class=" multiRow">
		<h2><@s.text name="label.summary"/></h2>
		<p>
			<label><@s.text name="label.totalproducts"/></label>
			<span>${report.totalProducts}</span>
		</p>
		
		<p>
			<label><@s.text name="label.totalinspections"/></label>
			<span>${report.totalInspections}</span>
		</p>
		
		<p>
			<label><@s.text name="label.numberofproducttypes"/></label>
			<span>${report.productTypes?size}</span>
		</p>
		
		<p>
			<label><@s.text name="label.numberofeventtypegroup"/></label>
			<span>${report.inspectionTypeGroups?size}</span>
		</p>
	</div>
	<h2><@s.text name="label.details"/></h2>
	<table class="list">
		<tr>
			<th><@s.text name="label.producttype"/></th>
			<th><@s.text name="label.totalinspections"/></th>
			<th><@s.text name="label.totalproducts"/></th>
		</tr>
		
		<#list report.productTypes as key >
			
			
			<tr>
				<td class="nameRow">
					<a id="detailRowProductOpen_${key}" href="javascript:void(0);" onclick="return openSection( 'detailRowProduct_${key}', 'detailRowProductOpen_${key}','detailRowProductClose_${key}');"><img src="<@s.url value="/images/expandLarge.gif"/>" alt"+"></a>  
					<a id="detailRowProductClose_${key}" style="display:none" href="javascript:void(0);" onclick="return closeSection( 'detailRowProduct_${key}', 'detailRowProductClose_${key}','detailRowProductOpen_${key}');"><img src="<@s.url value="/images/collapseLarge.gif"/>" alt="-"></a> 
					${key?html}
					<div id="detailRowProduct_${key}" style="display:none" class="subList">
						<#list report.countsByProductType[ key ] as record >
							<div class="count">
								${record.count!0} ${record.inspectionTypeGroupName?html}
							</div> 
						</#list>
					</div>
				</td>
				<td>${report.getInspectionsForProductType( key ) }</td>
				<td>${report.getDisinctProductsForProductType( key ) }</td>
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
								${record.count!0} ${record.productTypeName?html}
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

