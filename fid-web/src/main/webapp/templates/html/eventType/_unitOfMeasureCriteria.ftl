<@s.text name="label.primaryunit"/>: ${criteria.primaryUnit.name}

<#if criteria.secondaryUnit?exists>
    <br/>
    <@s.text name="label.secondaryunit"/>: ${criteria.secondaryUnit.name}
</#if>