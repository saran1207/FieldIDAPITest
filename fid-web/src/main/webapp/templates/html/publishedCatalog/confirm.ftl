<@s.form action="importCatalog" namespace="/ajax" id="step3Form" theme="fieldid">
	<@s.hidden name="uniqueID"/>
	<@s.hidden name="usingPackage"/>
	<#list publishedAssetTypes as assetType>
		<@s.hidden name="importAssetTypeIds['${assetType.id}']" />
	</#list>
	<#list publishedInspectionTypes as inspectionType>
		<@s.hidden name="importInspectionTypeIds['${inspectionType.id}']" />
	</#list>
	
	<p>
		<@s.text name="label.import_summary"><@s.param>${summary.productTypeImportSummary.importMapping.size()}</@s.param><@s.param>${summary.inspectionTypeImportSummary.importMapping.size()}</@s.param></@s.text>
	</p>
	<#if summary.productTypeImportSummary.anyRenamed || summary.inspectionTypeImportSummary.anyRenamed>
		<p>
			<span class="attention"><@s.text name="label.important_warning"/>:</span> <@s.text name="warning.import_renaming"><@s.param>${summary.productTypeImportSummary.numberRenamed}</@s.param><@s.param>${summary.inspectionTypeImportSummary.numberRenamed}</@s.param></@s.text>
			<a href="javascript:void(0);" onclick="$('renamedElements').toggle()" ><@s.text name="label.view_details"/></a>
		</p>
		<table id="renamedElements" style="display:none">
			<#if summary.productTypeImportSummary.anyRenamed >
				<tr><th colspan="3"><@s.text name="label.asset_types"/></td></th>
				<#list publishedAssetTypes as assetType>
					<#if summary.productTypeImportSummary.importMapping.get(assetType.id)?exists && summary.productTypeImportSummary.isRenamed(assetType.id, assetType.name)>
						<tr>
							<td>${assetType.name?html}</td>
							<td><@s.text name="label.will_be_named"/></td>
							<td>${summary.productTypeImportSummary.importMapping.get(assetType.id).name?html}</td>
						</tr>
					</#if> 
				</#list>
			</#if>
			<#if summary.inspectionTypeImportSummary.anyRenamed >
				<tr><th colspan="3"><@s.text name="label.event_types"/></th></tr>
				<#list publishedInspectionTypes as inpsectionType>
					<#if summary.inspectionTypeImportSummary.importMapping.get(inpsectionType.id)?exists && summary.inspectionTypeImportSummary.isRenamed(inpsectionType.id, inpsectionType.name)>
						<tr >
							<td>${inpsectionType.name?html}</td>
							<td><@s.text name="label.will_be_named"/></td>
							<td>${summary.inspectionTypeImportSummary.importMapping.get(inpsectionType.id).name?html}</td>
						</tr>
					</#if> 
				</#list>
			</#if>
		</table>
	</#if>
	
	
		
	<div class="stepAction">
		<@s.submit key="label.start_import_now" id="import" onclick="$('step3Form').request(getStandardCallbacks());  toStep(4, 'step4Loading'); return false;"/>
		<@s.text name="label.or"/> <a href="javascript:void(0);" onclick="scroll(0,0); backToStep(2)"><@s.text name="label.back_to_step"/> 2</a>
	</div>
</@s.form>