    <#if criteriaResult.signed>
        <span class="criteriaButton">
            <img src="<@s.url action="downloadSignature" namespace="/file" eventId="${formEvent.id}" criteriaId="${criteriaResult.criteria.id}"/>" width="150" height="39"/><br/>
        </span>
    <#else>
        <span class="criteriaText"><@s.text name="label.not_signed"/></span>
    </#if>