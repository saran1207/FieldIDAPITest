<#if usingPackage && publishedProductTypes.isEmpty()>
	<p class="stepAction">
		<@s.text name="label.no_asset_types_in_catalog"/> <button onclick="$('usingPackage').value = 'false'; $('step1').hide(); $('step2').hide(); $('step2Loading').show(); $('step1Form').request(getStandardCallbacks()); return false;" ><@s.text name="label.import_event_types"/></button>
	</p>
<#else>
	<@s.form action="importCatalogConfirm" namespace="/ajax" cssClass="" id="step2Form" theme="fieldid">
		<div class="selectOptions">
			<@s.text name="label.select"/>: <a href="javascript:void(0);" onclick="selectAll('step2Form');"><@s.text name="label.all"/></a>, <a href="javascript:void(0);" onclick="selectNone('step2Form')"><@s.text name="label.none"/></a>
		</div>
		
		<@s.hidden name="uniqueID"/>
		<@s.hidden name="usingPackage"/>	
		<div class="importSelection">
			<#if usingPackage>
				<#include "_importByPackage.ftl"/>
			<#else>
				<#include "_importCustom.ftl"/>
			</#if>
		</div>
	
		<div class="stepAction">
			<@s.submit key="label.continue" id="continue" onclick="$('step2Form').request(getStandardCallbacks()); toStep(3, 'step3Loading'); return false;"/>
			<@s.text name="label.or"/> <a href="javascript:void(0);" onclick="scroll(0,0); backToStep(1)"><@s.text name="label.back_to_step"/> 1</a>
		</div>	
	</@s.form>
</#if>