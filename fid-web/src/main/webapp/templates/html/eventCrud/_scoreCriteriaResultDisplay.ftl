<#if criteriaResult.score.na>
  <#assign scoreValue><@s.text name="label.na"/></#assign>
<#else>
  <#assign scoreValue>${criteriaResult.score.value}</#assign>
</#if>
<span class="criteriaText">${criteriaResult.score.name} (${scoreValue})</span>
