<td <#if action.getExtendedFeature(feature)> class='onIcon'<#else> class='offIcon'</#if>></td>
<td>
	<@s.form id="extendedFeature_${feature}" action="updateExtendedFeature" namespace="/adminAjax" theme="fieldidSimple">
		<@s.hidden name="id"/>
		<@s.hidden name="featureName" value="${feature}" />
		<#if action.getExtendedFeature(feature)> 
			<@s.hidden name="featureOn" value="false" />
		<#else> 
			<@s.hidden name="featureOn" value="true" />
		</#if>

		<a href="javascript:void(0);" onClick="saveExtendedFeature('${feature}');">
			<#if action.getExtendedFeature(feature)>on<#else>off</#if>
		</a>
	</@s.form>
</td>
<td class="featureLabel"><@s.text name="${feature.label}"/></td>
<td> <img id="loading_${feature}" class="loading" src="<@s.url value="../images/loading-small.gif"/>" /><td>
