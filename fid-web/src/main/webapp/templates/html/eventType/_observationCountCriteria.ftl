<@s.text name="label.observation_group"/>: ${criteria.observationCountGroup.name}

<#list criteria.observationCountGroup.observationCounts as count>
  <div>
        ${count.name} (<#if count.counted><@s.text name="label.included_in_total"/><#else><@s.text name="label.not_included_in_total"/></#if>)
  </div>
</#list>