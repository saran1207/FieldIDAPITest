${action.setPageType('my_account', 'saved_reports')!}
<#include "_secondaryNav.ftl"/>
<#if shareUserList?exists && !shareUserList.empty>
	<@s.form method="post" id="selectShareUsers" action="savedReportShareCreate" theme="fieldid" cssClass="crudForm bigForm pageSection">
		<h2><@s.text name="label.share_report"/></h2>
		<@s.hidden name="uniqueID"/>
		
		<#assign checkBoxListName="shareUsers" />
		<#assign viewTree=shareUserList />
		<#include '../common/_viewTreeCheckBoxList.ftl' />
		
		<div class="formAction">
			<@s.url id="cancelUrl" action="savedReports"/>
			<@s.reset key="label.cancel" onclick="return redirect('${cancelUrl}');" />
			<@s.submit key="label.save" />
		</div>
	</@s.form>
	
	
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.noresults" /></h2>
		<p><@s.text name="message.emptyreportshareuserlist" /></p>
		<p><a href="<@s.url action="savedReports"/>"><@s.text name="label.back"/></a></p>
	</div>
</#if>