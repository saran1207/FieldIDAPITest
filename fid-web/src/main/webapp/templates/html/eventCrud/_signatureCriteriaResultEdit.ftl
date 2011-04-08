<div id="signatureCriteria${criteriaId}" class="criteriaEditContainer">
    <#if signatureFileId?exists>
        <img src="<@s.url action="downloadTemporarySignature" namespace="/file" signatureFileId="${signatureFileId}"/>" width="150" height="50"/><br/>
        <input type="hidden" name="criteriaResults[${criteriaCount}].signed" value="true"/>
        <input type="hidden" name="criteriaResults[${criteriaCount}].signatureFileId" value="${signatureFileId}"/>
        <a onclick="clearSignature(${criteriaId}, ${criteriaCount})">Clear signature</a>
    <#elseif criteriaResult?exists && criteriaResult.signed>
        <img src="<@s.url action="downloadSignature" namespace="/file" eventId="${event.id}" criteriaId="${criteriaResult.criteriaId}"/>" width="150" height="50"/><br/>
        <input type="hidden" name="criteriaResults[${criteriaCount}].signed" value="false"/>
        <a onclick="clearSignature(${criteriaId}, ${criteriaCount})">Clear signature</a>
    <#else>
        <a href="/fieldid/aHtml/sign.action" criteriaid="${criteriaId}" criteriacount="${criteriaCount}" class="lightview" rel="iframe" title="Signature :: :: scrolling:false, width: 790, height: 300">Sign</a>
    </#if>
</div>