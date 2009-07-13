<head>
	<script type="text/javascript" src="<@s.url value="/javascript/pagination.js"/>"></script>
	<link rel="stylesheet" type="text/css" href="<@s.url value="/style/featureStyles/pagination.css"/>"/>
	<script type="text/javascript">
		goToPageURL = '<@s.url action="${currentAction}" currentPage="${currentPage}" includeParams="get"/>';
	</script>
</head>
<div class="paginationWrapper">
<ul class="pagination">
	<#if (page.totalPages > 1 ) >
		<#assign startingPage=page.readableCurrentPage - 4 />
		<#if startingPage < 1 > <#assign startingPage=1 /></#if>
		<#if (startingPage + 9 > page.totalPages) > <#assign startingPage=page.totalPages - 9 /></#if>
		<#if startingPage < 1 > <#assign startingPage=1 /></#if>
		<#assign endingPage=startingPage + 9 />
		<#if (endingPage > page.totalPages) > <#assign endingPage=page.totalPages /></#if>
		
		<#if page.hasPreviousPage> <li><a class="paginationLink" href="<@s.url action="${currentAction}" currentPage="1" includeParams="get" />"><@s.text name="label.pagenavigation.first"/></a></li> </#if>
		<#if page.hasPreviousPage> <li><a class="paginationLink" href="<@s.url action="${currentAction}" currentPage="${page.previousPage}" includeParams="get" />">&lt;<@s.text name="label.pagenavigation.previous"/></a></li> </#if>
		
		<#list startingPage..endingPage as pageIdx >
			
			<li class="<#if pageIdx == page.readableCurrentPage >currentPage</#if>" >
				
				<#if pageIdx != currentPage >
					<a href="<@s.url action="${currentAction}" currentPage="${pageIdx}" includeParams="get" />">
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
			
		<#if page.hasNextPage> <li><a class="paginationLink" href="<@s.url action="${currentAction}" currentPage="${page.nextPage}" includeParams="get"  />"><@s.text name="label.pagenavigation.next"/>&gt;</a></li> </#if>
		
		<#if page.hasNextPage> <li><a class="paginationLink" href="<@s.url action="${currentAction}" currentPage="${page.totalPages}" includeParams="get"  />"><@s.text name="label.pagenavigation.last"/></a></li> </#if>
		
		
		<li>
			<span class="gotoPage">
				<label for="goto"><@s.text name="label.goto_page"/></label>
				
				<@s.textfield name="currentPage" size="3" theme="simple" cssClass="toPage" id="toPage"/>
				<label for="lastPage"><@s.text name="label.of_pages"><@s.param>${page.totalPages}</@s.param></@s.text></label>
			</span>
		</li>
	</#if>
</ul>
</div>
