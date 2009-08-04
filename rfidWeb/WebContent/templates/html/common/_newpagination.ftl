<head>
	<script type="text/javascript" src="<@s.url value="/javascript/pagination.js"/>"></script>
	<@n4.includeStyle href="featureStyles/pagination.css"/>
	<script type="text/javascript">
		goToPageURL = '<@s.url action="${currentAction}" currentPage="${currentPage}" includeParams="get"/>';
	</script>
</head>
<div class="paginationWrapper">
	<ul class="pagination">
		<#if (totalPages > 1 ) >
			<@s.url id="paginatedAction" action="${currentAction}" escapeAmp="false" includeContext="false" searchId="${searchId!}" />
				
			<#if hasPreviousPage>
				<li><a href="<@s.url value="${paginatedAction}" currentPage="${firstPage}"    includeParams="get" />"><@s.text name="label.pagenavigation.first"/></a></li>
				<li><a href="<@s.url value="${paginatedAction}" currentPage="${previousPage}" includeParams="get" />">&lt; <@s.text name="label.pagenavigation.previous"/></a></li> 
			</#if>
			
			<#list pageBlockList as pageIdx >	
				<li class="<#if pageIdx == currentPage >currentPage</#if>" >
					<#if pageIdx != currentPage >
						<a href="<@s.url value="${paginatedAction}" currentPage="${pageIdx}" includeParams="get" />">
					<#else>
						<span>
					</#if>
					${pageIdx}
					<#if pageIdx != currentPage >
						</a>
					<#else>
						</span> 
					</#if>
				</li>
				
			</#list>
				
			<#if hasNextPage> 
				<li><a href="<@s.url value="${paginatedAction}" currentPage="${nextPage}" includeParams="get"  />"><@s.text name="label.pagenavigation.next"/> &gt;</a></li>
				<li><a href="<@s.url value="${paginatedAction}" currentPage="${lastPage}" includeParams="get"  />"><@s.text name="label.pagenavigation.last"/></a></li>
			</#if>
			
			<li>
				<span class="gotoPage">
					<label for="goto"><@s.text name="label.goto_page"/></label>
					
					<@s.textfield name="currentPage" size="3" theme="simple" cssClass="toPage" id="toPage"/>
					<label for="lastPage"><@s.text name="label.of_pages"><@s.param>${lastPage}</@s.param></@s.text></label>
				</span>
			</li>
		</#if>
	</ul>
</div>