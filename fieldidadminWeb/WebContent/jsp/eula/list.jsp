<%@ taglib prefix="s" uri="/struts-tags" %>
<h1>EULAS</h1>

<a href='<s:url action="eulaAdd" />'>add eula</a>

<s:if  test="%{page.hasResults() && page.validPage()}" >
	
	
	<jsp:include page="/jsp/common/_pagination.jsp" />
	<table class="list">
		<tr>
			<th>version</th>
			<th>effective date</th>
			<th></th>
		</tr>
		<s:iterator id="video" value="page.getList()">
			<tr id="eula_<s:property value="id"/>" >
				<td><s:property value="version"/></td>
				<td><s:property value="effectiveDate"/></td>
				<td><a href="eulaEdit.action?id=<s:property value="id"/>">edit</a></td>
				<td><a href="eulaDelete.action?id=<s:property value="id"/>">delete</a></td>
			</tr>	
		</s:iterator>
	</table>
</s:if>	
<s:else >
	<div class="emptyList" >
		<h2>no results</h2>
		<p>
			there are no EULAS
		</p>
	</div>
</s:else>

	


