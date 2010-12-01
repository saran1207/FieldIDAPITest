${action.setPageType('user','list')!}
<div class="quickForm">
	<h3><@s.text name="label.filter" /></h3>
	<@s.form method="GET" cssClass="simpleInputForm" theme="css_xhtml" > 
		<@s.hidden name="currentPage" value="1"/>
		<@s.textfield id="nameFilter" name="listFilter" key="label.name" labelposition="left"/>
		<#if securityGuard.readOnlyUserEnabled>
			<@s.select id="userType" name="userType" list="userTypes" listKey="id" listValue="name" key="label.usertype" labelposition="left"/>
		</#if>
		<div class="formAction">
			<@s.submit name="search" key="hbutton.search" />
			<@s.submit name="clear" key="hbutton.clear" onclick="$('nameFilter').value='';"/>
		</div>
	</@s.form>
</div>

<#if  page.hasResults() && page.validPage() >
	<#assign currentAction="userList.action" />
	<#include '../common/_pagination.ftl' />
	<#assign userList=page.list />
	<#include "_userList.ftl" />
	<#include '../common/_pagination.ftl' />
<#elseif !page.hasResults() >
	<div class="emptyList" >
		<h2><@s.text name="label.noresults" /></h2>
		<p>
			<@s.text name="label.emptylistusers" />
		</p>
	</div>
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.invalidpage" /></h2>
		<p>
			<@s.text name="message.invalidpage" />
			<a href="<@s.url  action="userList" currentPage="1"/>" ><@s.text name="message.backtopageone"/></a>
		</p>
	</div>
</#if>
