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
<a href="#" class="searchOwner" orgFilter="${orgType}" orgId="${parameters.id?html}"><@s.text name="label.choose"/></a>
&nbsp;<@s.text name="label.or"/>&nbsp;
<a href="#" class="clearSearchOwner"><@s.text name="label.clear"/></a>

	
</span>