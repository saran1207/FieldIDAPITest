
<head>
	<@n4.includeStyle type="page" href="publishedCatalog"/>
	<@n4.includeStyle href="steps"/>
</head>

<#include '../safetyNetwork/_safetyNetworkLayout.ftl'>
<div class="safetyNetworkSplash">
	<h1 id="manage_catalog_heading" class="safetyNetworkHeadings"><@s.text name="label.your_catalog"/></h2>
	<p class="safetyNetworkFont"><@s.text name="label.publish_description"/></p>
	<div id="steps" class="safetyCatalog">
		<div class="step">
			<h2><@s.text name="label.what_do_you_want_to_publish"/></h2>
			<div class="stepContent"  id="step1">
				<@s.form action="publishCatalog" theme="fieldid" id="publishForm">
					<div class="selectOptions">
						<@s.text name="label.select"/>: <a href="javascript:void(0);" onclick="selectAll('publishForm');"><@s.text name="label.all"/></a>, <a href="javascript:void(0);" onclick="selectNone('publishForm')"><@s.text name="label.none"/></a>
					</div>	
					<div id="catalogContents" class="selectOptions">
						<div class="customSelection">
							<h3><@s.text name="label.asset_types"/></h3>
					
							<#list productTypes as productType>
								<div class="customSelectionType">
									<@s.checkbox name="publishedProductTypeIds['${productType.id}']" />
									<label>${productType.name?html}</label>
								</div>
							</#list>
								
						</div>
						
						<div class="customSelection">
							<h3><@s.text name="label.event_types"/></h3>
							<#list inspectionTypes as inspectionType>
								<div class="customSelectionType">
									<@s.checkbox name="publishedInspectionTypeIds['${inspectionType.id}']" />
									<label>${inspectionType.name?html}</label>
								</div>
							</#list>
						</div>	
					</div>
					<div class="stepAction">
						<@s.submit key="label.publish" id="publish"/>
						<@s.url id="cancelUrl" action="safetyNetwork"/>
						<@s.text name="label.or"/> 
						<a href="#" onclick="redirect('${cancelUrl}'); return false;"/><@s.text name="label.cancel"/></a>
					</div>
				</@s.form>
			</div>
		</div>
	</div>
</div>
