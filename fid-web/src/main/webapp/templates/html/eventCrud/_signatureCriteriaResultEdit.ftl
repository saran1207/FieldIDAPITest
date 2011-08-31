<div id="signatureCriteria${criteriaId}" class="criteriaEditContainer">
    <#if signatureFileId?exists && signatureFileId != "">
        <img src="<@s.url action="downloadTemporarySignature" namespace="/file" signatureFileId="${signatureFileId}"/>" width="150" height="50"/><br/>
        <input type="hidden" name="criteriaResults[${currentCriteriaIndex}].signed" value="true"/>
        <input type="hidden" name="criteriaResults[${currentCriteriaIndex}].signatureFileId" value="${signatureFileId}"/>
        <a class="clearSignatureLink" onclick="clearSignature(${criteriaId}, ${currentCriteriaIndex})"><@s.text name="label.clear_signature"/></a>
    <#elseif criteriaResult?exists && criteriaResult.signed>
        <img src="<@s.url action="downloadSignature" namespace="/file" eventId="${event.id}" criteriaId="${criteriaResult.criteriaId}"/>" width="150" height="50"/><br/>
        <input type="hidden" name="criteriaResults[${currentCriteriaIndex}].signed" value="true"/>
        <a class="clearSignatureLink" onclick="clearSignature(${criteriaId}, ${currentCriteriaIndex})"><@s.text name="label.clear_signature"/></a>
    <#else>
        <input type="hidden" name="criteriaResults[${currentCriteriaIndex}].signed" value="false"/>
        <a href="/fieldid/aHtml/sign.action" criteriaid="${criteriaId}" currentcriteriaindex="${currentCriteriaIndex}" class="lightview signLink" rel="iframe" title="Signature :: :: scrolling:false, width: 790, height: 300"><@s.text name="label.sign"/></a>
    </#if>
</div>