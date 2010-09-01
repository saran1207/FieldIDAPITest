<div class="pagination">
	<#if page.getTotalPages() gt 1 >
		
		
		<#if page.hasPreviousPage> <a href="<@s.url includeParams="get" pageNumber="1"/>">first</a> </#if>
		<#if page.hasPreviousPage> <a href="<@s.url includeParams="get" pageNumber="${page.previousPage}"/>&lt; previous</a></#if>
		
			
		<#if page.hasNextPage> <a href="<@s.url includeParams="get" pageNumber="${page.nextPage}"/>">next &gt;</a> </#if>
		
		<#if page.hasNextPage> <a href="<@s.url includeParams="get" pageNumber="${page.totalPages}"/>">last</a> </#if>
	</#if>
</div>
