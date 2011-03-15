<span class="orgPicker" id="${parameters.id?html}_container">
<#-- hidden id value. -->
<input type="hidden"<#rt/>
 name="${parameters.idName?default("")?html}"<#rt/>
<#if parameters.nameValue?exists>
 value="<@s.property value="parameters.idNameValue"/>"<#rt/>
</#if>
<#if parameters.id?exists>
 id="${parameters.id?html}"<#rt/>
</#if>
 class="orgSelected"/><#rt/>

<#-- Text input area. -->
<input type="text" name="${parameters.name?default('')?html}_orgName"<#rt/>
readonly="readonly"<#rt/>
value="${(parameters.nameValue.displayName)?default('')?html}"<#rt/>
<#if parameters.id?exists>
 id="${parameters.id?html}_orgName"<#rt/>
</#if>
<#if parameters.title?exists>
 title="${parameters.title?html}"<#rt/>
</#if>
<#if parameters.cssClass?exists>
 class="${parameters.cssClass?html}"<#rt/>
</#if>
<#if parameters.title?exists>
 title="${parameters.title?html}"<#rt/>
</#if>
/>
<#if !(parameters.required?exists) || !parameters.required>
<a href="#" class="clearSearchOwner" <#if !parameters.idNameValue?exists || parameters.nameValue.displayName?exists >style="display:none"</#if> ><@s.text name="label.clear"/></a>
</#if>	

<a href="#" class="searchOwner" orgFilter="${orgType}" orgId="${parameters.id?html}"><@s.text name="label.choose"/></a>
	
</span>