<#if uniqueId?exists> 
	<#if uniqueId lt 0 >
		<#assign id=-1 * uniqueId />
	<#else>
		<#assign id=uniqueId />
	</#if>
</#if>
<#assign html>
	<span class="warning"><@s.text name="label.identifier_in_use"/></span>
</#assign>
$$('#identifierRow_${id!} .identifierStatus').each( function(element) { element.update('${html?js_string}')});
$('identifierRow_${id!}').addClassName('warning');