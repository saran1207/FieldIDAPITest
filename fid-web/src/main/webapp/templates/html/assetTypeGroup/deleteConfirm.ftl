<title><@s.text name="title.confirmassettypegroupdelete" /> - ${group.name?html}</title>
${action.setPageType('asset_type_group', 'edit')!}
<div class="instructions">
	<@s.text name="instruction.deleteassettypegroup"><@s.param >${group.name?html}</@s.param></@s.text>
</div>

<div class="crudForm largeForm bigForm pageSection">
	<h2><@s.text name="label.removaldetails"/></h2>
	<div class="sectionContent">
		<div class="infoSet">
			<label for="">${removalSummary.assetTypesConnected}</label>
			<span><@s.text name="label.assettypesbeingdetached"/></span>
		</div>
        <div class="infoSet">
            <label for="">${removalSummary.savedReportsConnected}</label>
            <span><@s.text name="label.saved_reports_being_deleted"/></span>
        </div>
	</div>
	<div class="formAction">
		<@s.url id="cancelUrl" action="assetTypeGroup" uniqueID="${uniqueID}"/>
		<@s.submit key="label.cancel" onclick="return redirect('${cancelUrl}');" theme="fieldid"/>
		
		<@s.url id="deleteUrl" action="assetTypeGroupDelete" uniqueID="${uniqueID}"/>
		<@s.submit key="label.delete" onclick="return redirect('${deleteUrl}');" theme="fieldid" />
	</div>
</div>

