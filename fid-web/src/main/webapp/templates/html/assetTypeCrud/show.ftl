<#escape x as action.replaceCR(x?html) >
${action.setPageType('asset_type', 'show')!}
		
<div class="viewSection" >
	<h2><@s.text name="label.assetinformation"/></h2>
	<p>
		<label><@s.text name="label.group"/></label>
		<span class="fieldValue"><#if assetType.group?exists><a href="<@s.url action="assetTypeGroup" uniqueID="${assetType.group.id}"/>"></#if>${(assetType.group.name)!}<#if assetType.group?exists></a></#if></span>
	</p>
	<p>
		<label><@s.text name="label.name"/></label>
		<span class="fieldValue">${assetType.name!}</span>
	</p>
    <p>
        <label><@s.text name="label.linkable"/></label>
        <span class="fieldValue">${assetType.linkable?string("Yes", "No")}</span>
    </p>

	<p>
		<label><@s.text name="label.warnings"/></label>
		<span class="fieldValue">${assetType.warnings!}</span>
	</p>
	
	<p>
		<label><@s.text name="label.more_information"/></label>
		<span class="fieldValue">${assetType.instructions!}</span>
	</p>
	<#if securityGuard.manufacturerCertificateEnabled>
		<p>
			<label><@s.text name="label.hasmanufacturercertificate"/></label>
			<span class="fieldValue">${assetType.hasManufactureCertificate?string( action.getText("value.yes"), action.getText("value.no") )}</span>
		</p>
		<p>
			<label><@s.text name="label.manufacturercertificatetext"/></label>
			<span class="fieldValue">${assetType.manufactureCertificateText!}</span>
		</p>
	</#if>	
	<p>
		<label><@s.text name="label.assetdescription"/></label>
		<span class="fieldValue">${assetType.descriptionTemplate!}</span>
	</p>
</div>

<#if !assetType.infoFields.isEmpty() >
	<div class="viewSection setViewSection" id="assetAttributes">
		<h2>
			<span><@s.text name="label.attributes"/></span>
			<label><@s.text name="label.type"/></label>
			<label><@s.text name="label.required"/></label>
			<label><@s.text name="label.expandvalues"/></label>
			
		</h2>
		
		<#list assetType.infoFields as infoField >
			<p>
				<span>${infoField.name} ${infoField.retired?string( "( "+ action.getText("label.retired") + " )", "" ) }</span>
				<label>
					<#switch infoField.fieldType >
						<#case 'textfield'>
							<#if infoField.usingUnitOfMeasure >
								<@s.text name="label.unitofmeasure"/>
							<#else>
								<@s.text name="label.textfield"/>
							</#if>
						<#break >
						<#case 'combobox'>
							<@s.text name="label.combobox"/>
						<#break >
						<#case 'selectbox'>
							<@s.text name="label.selectbox"/>
						<#break >
						<#case 'datefield'>
							<@s.text name="label.datefield"/>
						<#break >
					</#switch>
					
				</label>
				<label>${infoField.required?string( action.getText("value.yes"), action.getText("value.no") )}</label>
				<label>
					<#if infoField.hasStaticInfoOption() >
						<a id="optionExpand_${infoField.uniqueID}" href="javascript:void(0);" onclick="expandOptions( ${infoField.uniqueID} ); return false;"><img src="<@s.url value="/images/expand.gif" includeParams="none"/>" alt="[+]" /></a>
						<a style="display:none" id="optionCollapse_${infoField.uniqueID}" href="javascript:void(0);" onclick="collapseOptions( ${infoField.uniqueID} ); return false;"><img src="<@s.url value="/images/collapse.gif" includeParams="none"/>" alt="[+]" /></a>
					</#if>
				</label>
				
				
			</p>
			<div class="viewSection smallViewSection dropDownOption" id="infoOptions_${infoField.uniqueID}" >
				<h2><@s.text name="label.dropdownoptions"/></h2>
				<#list infoField.infoOptions as infoOption >
					
					<p >
						<span>${infoOption.name}</span>
					</p>
				</#list>
			</div>
		</#list>
	</div>
</#if>
<div class="viewSection setViewSection">
	<#assign downloadAction="downloadAssetTypeAttachedFile"/>
	<#assign attachments=assetType.attachments/>
	<#include "/templates/html/common/_attachedFilesShow.ftl"/>
</div>
<#if assetType.imageName?exists >
	
	<div class="viewSection" >
		<h2><@s.text name="label.assetimage" /></h2>
		<p>
			<img src="<@s.url action="downloadAssetTypeImage" namespace="/file" uniqueID="${assetType.uniqueID}" includeParams="none" />" alt="<@s.text name="label.assetimage"/>" width="300"/>
		</p>
	</div>

</#if>

<#if !assetType.subTypes.isEmpty() >
	
	<div class="viewSection smallViewSection" >
		<h2><@s.text name="label.componenttypes" /></h2>
		<#list subTypes?sort_by( "name" ) as type >
			<p>
				${type.name}
			</p>
		</#list>
	</div>

</#if>


</#escape>
<head>
	<script type="text/javascript">
		function expandOptions( infoFieldId ) {
			$('infoOptions_'+ infoFieldId).style.display="block";
			$('optionExpand_'+ infoFieldId).style.display="none";
			$('optionCollapse_'+ infoFieldId).style.display="inline";
			
		}
		
		function collapseOptions( infoFieldId ) {
			$('infoOptions_'+ infoFieldId).style.display="none";
			$('optionExpand_'+ infoFieldId).style.display="inline";
			$('optionCollapse_'+ infoFieldId).style.display="none";
			
		}  
	</script>
</head>