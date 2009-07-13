<div id="notifications" >
  <div class="message" id="message" <#if action.actionMessages.empty> style="display:none"</#if>>
      <@s.actionmessage />
  </div>
  <div id="error" class="error" <#if action.actionErrors.empty>style="display:none"</#if>>
      <@s.actionerror />
  </div>
</div>