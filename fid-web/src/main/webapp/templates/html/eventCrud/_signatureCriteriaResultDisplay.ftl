    <#if criteriaResult.signed>
        <span class="criteriaButton">
            <img src="<@s.url action="downloadSignature" namespace="/file" eventId="${event.id}" criteriaId="${criteriaResult.criteria.id}"/>" width="150" height="39"/><br/>
        </span>
    <#else>
        <span class="criteriaText">Not signed</span>
    </#if>