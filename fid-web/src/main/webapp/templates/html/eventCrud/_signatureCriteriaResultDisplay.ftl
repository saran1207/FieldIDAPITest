    <#if criteriaResult.signed>
        <img src="<@s.url action="downloadSignature" namespace="/file" eventId="${event.id}" criteriaId="${criteriaResult.criteria.id}"/>" width="150" height="39"/><br/>
    <#else>
        Not signed
    </#if>