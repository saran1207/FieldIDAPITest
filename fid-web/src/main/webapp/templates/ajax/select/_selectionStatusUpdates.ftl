<#assign defaultSelectionNotice>
    <#include '/templates/html/customizableSearch/_selectionStatus.ftl'>
</#assign>

displaySelectionNotice('${selectionNotice?default(defaultSelectionNotice)?js_string}');
displayNumSelected(${numSelectedItems});
checkHeaderBoxIfAllItemsAreSelected();