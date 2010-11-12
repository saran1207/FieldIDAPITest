<#if numSelectedItems gt 0>
    ${numSelectedItems} of ${totalResults} items in the results are selected.
    <a href="#" onClick="clearSelection('${searchContainerKey}', '${searchId}')">Clear selection</a>
</#if>