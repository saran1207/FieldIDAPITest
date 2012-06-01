${action.setPageType('event_book', 'list_archived')!}
<#assign page=archivedPage />

<#if  page.hasResults() && page.validPage() >
	<#assign currentAction="archivedEventBooks.action" />
	<#include '../common/_pagination.ftl' />
	<table class="list">
		<tr>
			<th><@s.text name="label.title" /></th>
			<th><@s.text name="label.customer" /></th>
			<th><@s.text name="label.datecreated" /></th>
			<th><@s.text name="label.status" /></th>
			<th></th>
		</tr>
		<#list page.getList() as book > 
			<tr id="book_${book.id}" >
				<td><a href="<@s.url action="eventBookEdit" uniqueID="${book.id}"/>" bookid="${book.id}">${book.name?html}</a></td>
				<td>${(book.owner.name?html)!}</td>
				<td>${action.formatDateTime(book.created)}</td>
				<td><span id="bookStatus_${book.id}">${book.open?string( action.getText( "label.open" ), action.getText( "label.closed" ) ) }</td>
				<td>
					<a href='<@s.url action="eventBookUnarchive" uniqueID="${book.id}"/>'><@s.text name="label.unarchive"/></a>
				</td>
			</tr>	
		</#list>
	</table>
	
	<#include '../common/_pagination.ftl' />
<#elseif !page.hasResults() >
	<div class="emptyList" >
		<h2><@s.text name="label.noresults" /></h2>
		<p>
			<@s.text name="label.emptyeventbooklist" />
		</p>
	</div>
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.invalidpage" /></h2>
		<p>
			<@s.text name="message.invalidpage" />
			<a href="<@s.url  action="eventBooks" currentPage="1" includeParams="get"/>" ><@s.text name="message.backtopageone"/></a>
		</p>
	</div>
</#if>
	


