<h2>2. <@s.text name="label.conf_other_identifiers"/></h2>
<div class="stepContent" id="step2" style="display:none;">

    <@s.form id="step2form" cssClass="inputForm" theme="fieldidSimple">
        <#include '../multiAssetCrud/_identifiersList.ftl'/>
    </@s.form>

    <@s.form action="bulkRegFinalize" namespace="/" id="masterForm" theme="fieldid">
        <input type="hidden" name="vendorId" value="${vendorId}"/>
        <div class="stepAction">
            <@s.submit id="step2next" key="label.perform_bulk_register" onclick="return mergeAndSubmit('step1form', 'step2form', 'masterForm');"/>
            <@s.text name="label.or"/> <a href="#step1" onclick="backToStep1(); return false;"><@s.text name="label.back_to_step"/> 1</a>
        </div>
    </@s.form>

</div>
