<div class="pagination">
	<#if page.getTotalPages() gt 1 >
		
		<#if page.hasPreviousPage> <a href="<@s.url includeParams="get" currentPage="1"/>">first</a> </#if>
		<#if page.hasPreviousPage> <a href="<@s.url includeParams="get" currentPage="${page.previousPage}"/>">&lt; previous</a></#if>
		
			
		<#if page.hasNextPage> <a href="<@s.url includeParams="get" currentPage="${page.nextPage}"/>">next &gt;</a> </#if>
		
		<#if page.hasNextPage> <a href="<@s.url includeParams="get" currentPage="${page.totalPages}"/>">last</a> </#if>
	</#if>
</div>
