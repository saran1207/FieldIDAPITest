<head>
	<@n4.includeStyle type="page" href="inspection" />
	<script type="text/javascript" src="<@s.url value="/javascript/inspection.js"/>" ></script>
</head>

${action.setPageType('inspection', 'list')!}
<#include "_inspectionSearchForm.ftl" />


<div class="formAction container" style="text-align:right;">
	<#if Session.sessionUser.hasAccess("createinspection") >
		<span class="inspectionTypes inspectionTypesRelative" ><a href="javascript:void(0);" id="startNewInspection" onmouseover="positionDropdown(this);"><@s.text name="label.newinspection"/></a>
			<ul id="startNewInspection_list">
				<#include "_inspectionSelect.ftl" />
			</ul>
		</span>

	</#if>
	
</div>

<#if inspectionGroups?exists >
	<#if !inspectionGroups.isEmpty() >
		<table class="list" id="resultsTable" >
			<tr>
				<th><@s.text name="label.daterange"/></th>
				<th class="addInspectionColumn"></th>
			</tr>
			<#list inspectionGroups?sort_by("firstDate")?reverse as inspectionGroup >
				<tr>
					<td>
						<#if inspectionGroup_index == 0>
							<#assign openStyle>style="display:none"</#assign>
							<#assign closeStyle> </#assign>
						<#else>
							<#assign openStyle> </#assign>
							<#assign closeStyle>style="display:none"</#assign>
						</#if>
						<a href="javascript:void(0);" id="expand_${inspectionGroup.id}" onclick=" openSection( 'inspections_${inspectionGroup.id}', 'expand_${inspectionGroup.id}', 'collapse_${inspectionGroup.id}' ); return false;" ${openStyle}><img src="<@s.url value="/images/expandLarge.gif"/>" ></a>
						<a href="javascript:void(0);" id="collapse_${inspectionGroup.id}" onclick="closeSection( 'inspections_${inspectionGroup.id}', 'collapse_${inspectionGroup.id}', 'expand_${inspectionGroup.id}' ); return false;" ${closeStyle}><img src="<@s.url value="/images/collapseLarge.gif"/>" ></a>
						${action.formatDate(inspectionGroup.firstDate, true)!} - ${action.formatDate(inspectionGroup.firstDate, true)!}
						
						<div id="inspections_${inspectionGroup.id}" ${closeStyle} class="inspectionList">
						<#list inspectionGroup.availableInspections?sort_by( 'date' )?reverse as inspection >
							<div class="inspections" >	
								<span class="inspectionType">						
									<a id="showInspection_${inspection.id}" href="<@s.url action="inspection" uniqueID="${inspection.id}" />">
										${ ( inspection.type.name )!}
									</a>
								</span>
								<span class="inspectionDate">
									${action.formatDateTime(inspection.date)}
								</span>
								<span class="inspector">
									<@s.text name="label.inspectedby"/>: ${ (inspection.inspector.userLabel)! }
								</span>
								<span class="editInspection">
									<#if sessionUser.hasAccess("editinspection") >
										<a id="editInspection_${inspection.id}" href="<@s.url action="selectInspectionEdit" uniqueID="${inspection.id}" />">
											<@s.text name="label.edit"/>
										</a>
									</#if>
								</span>
							</div>
						</#list>
						</div>	
						
					</td>
					<td>
						<#if Session.sessionUser.hasAccess("createinspection") && !inVendorContext>
							<div class="inspectionTypes"><a href="javascript:void(0);" id="${inspectionGroup.id}" onmouseover="positionDropdown(this);"><@s.text name="label.addinspection"/></a><br/>
								<ul id="${inspectionGroup.id}_list">
									<#include "_inspectionSelect.ftl" />
								</ul>
							</div>
						</#if>
					</td>	
				</tr>
				
			</#list>
		</table>
	
	<#else>
		<div class="emptyList" >
			<h2><@s.text name="label.noresults"/></h2>
			<p>
				<@s.text name="message.emptyinspectionlist" />
			</p>
		</div>
	</#if>
</#if>

	
	
	
