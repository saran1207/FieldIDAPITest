<#escape x as action.replaceCR(x?html) >
${action.setPageType('product_type', 'show')!}
		
<div class="viewSection" >
	<h2><@s.text name="label.productinformation"/></h2>
	<p>
		<label><@s.text name="label.group"/></label>
		<span class="fieldValue"><#if productType.group?exists><a href="<@s.url action="productTypeGroup" uniqueID="${productType.group.id}"/>"></#if>${(productType.group.name)!}<#if productType.group?exists></a></#if></span>
	</p>
	<p>
		<label><@s.text name="label.name"/></label>
		<span class="fieldValue">${productType.name!}</span>
	</p>
	
	<p>
		<label><@s.text name="label.warnings"/></label>
		<span class="fieldValue">${productType.warnings!}</span>
	</p>
	
	<p>
		<label><@s.text name="label.instructions"/></label>
		<span class="fieldValue">${productType.instructions!}</span>
	</p>
	
	<p>
		<label><@s.text name="label.hasmanufacturercertificate"/></label>
		<span class="fieldValue">${productType.hasManufactureCertificate?string( action.getText("value.yes"), action.getText("value.no") )}</span>
	</p>
	<p>
		<label><@s.text name="label.manufacturercertificatetext"/></label>
		<span class="fieldValue">${productType.manufactureCertificateText!}</span>
	</p>
	
	<p>
		<label><@s.text name="label.productdescription"/></label>
		<span class="fieldValue">${productType.descriptionTemplate!}</span>
	</p>
</div>

<#if !productType.infoFields.isEmpty() >
	<div class="viewSection setViewSection" id="productAttributes">
		<h2>
			<span><@s.text name="label.attributes"/></span>
			<label><@s.text name="label.type"/></label>
			<label><@s.text name="label.required"/></label>
			<label><@s.text name="label.expandvalues"/></label>
			
		</h2>
		
		<#list productType.infoFields as infoField >
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
	<#assign downloadAction="downloadProductTypeAttachedFile"/>
	<#assign attachments=productType.attachments/>
	<#include "/templates/html/common/_attachedFilesShow.ftl"/>
</div>
<#if productType.imageName?exists >
	
	<div class="viewSection smallViewSection" >
		<h2><@s.text name="label.productimage" /></h2>
		<p>
			<img src="<@s.url action="downloadProductTypeImage" namespace="/file" uniqueID="${productType.uniqueID}" includeParams="none" />" alt="<@s.text name="label.productimage"/>" width="300"/>
		</p>
	</div>

</#if>

<#if !productType.subTypes.isEmpty() >
	
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