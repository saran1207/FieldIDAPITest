<%@ taglib prefix="s" uri="/struts-tags" %>
<div class="pagination">
	<s:if test="%{ page.getTotalPages() > 1 }" >
		
		
		<s:if test="%{page.isHasPreviousPage()}"> <a href="<s:url includeParams="get"><s:param name="pageNumber" value="1"/></s:url>">first</a> </s:if>
		<s:if test="%{page.isHasPreviousPage()}"> <a href="<s:url includeParams="get"><s:param name="pageNumber" value="%{page.previousPage}"/></s:url>">&lt; previous</a></s:if>
		
			
		<s:if test="%{page.isHasNextPage()}"> <a href="<s:url includeParams="get"><s:param name="pageNumber" value="%{page.nextPage}"/></s:url>">next &gt;</a> </s:if>
		
		<s:if test="%{page.isHasNextPage()}"> <a href="<s:url includeParams="get"><s:param name="pageNumber" value="%{page.totalPages}"/></s:url>">last</a> </s:if>
	</s:if>
</div>
