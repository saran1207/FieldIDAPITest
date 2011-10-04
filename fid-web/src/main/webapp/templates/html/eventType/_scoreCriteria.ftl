<@s.text name="label.score_group"/>: ${criteria.scoreGroup.name}

<#list criteria.scoreGroup.scores as score>
  <div>
      <#if score.na>
        ${score.name} (<@s.text name="label.na"/>)
      <#else>
        ${score.name} (${score.value})
      </#if>
  </div>
</#list>