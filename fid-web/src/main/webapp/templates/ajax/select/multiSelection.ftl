<#assign selectionNotice>
    <@s.text name="notice.current_page_selected"><@s.param>${numItemsJustSelected}</@s.param></@s.text>
    <a href="#" onClick="addEntireResultToSelection('${searchContainerKey}', '${searchId}')"><@s.text name="notice.select_all_results"><@s.param>${totalResults}</@s.param></@s.text></a>
</#assign>

<#include '_selectionStatusUpdates.ftl'/>