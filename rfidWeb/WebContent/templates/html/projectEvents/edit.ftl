<head>
	<script type="text/javascript">
		function lockOutMassUpdate() {
			$$('.massUpdateHolder').each(function (element) { element.hide(); });
			$$('.massUpdateIndicator').each(function (element) { element.show(); });
		}
	</script>
</head>
${action.setPageType('job', 'events')!}
<#include "_secondaryNav.ftl"/>
<#assign listPage=true/>
<#include '_form.ftl' >
<#assign pageActions>
	<ul class="listOfLinks">
		<li class="first massUpdateHolder"><a class="addAllToJob massUpdate" href="javascript:void(0);" ><@s.text name="label.assignalltojob" /></a></li>
		<li class="massUpdateHolder"><a class="removeAllFromJob massUpdate" href="javascript:void(0);" ><@s.text name="label.removeallfromjob" /></a></li>
		<li class="massUpdateIndicator" style="display:none"><img src="<@s.url value="/images/indicator_mozilla_blu.gif"/>" alt="<@s.text name="label.loading"/>"/></li>
	</ul>
</#assign>
${pageActions}
<#if validPage >
	<#if hasResults >
		<#assign postRowHeaderTemplate="../projectEvents/_postRowHeader.ftl" />
		<#assign postRowTemplate="../projectEvents/_postRow.ftl" />
		<#include '../customizableSearch/table.ftl'>

		<div class="adminLink">	
			<span class="total"><@s.text name="label.totalschedules"/> ${totalResults}</span>
		</div>
		<div class="adminLink">	
			${pageActions}
		</div>
	<#else>
		<div class="emptyList" >
			<h2><@s.text name="label.noresults" /></h2>
			<p><@s.text name="message.emptysearch" /></p>
		</div>
	</#if>
<#else>
	<div class="emptyList" >
		<h2><@s.text name="label.invalidpage" /></h2>
		<p>
			<@s.text name="message.invalidpage" />
			<a href="<@s.url action="assignSchedulesToJob" searchId="${searchId!0}"/>" ><@s.text name="message.backtopageone"/></a>
		</p>
	</div>
</#if>

<#include "../customizableSearch/_massActionRestriction.ftl"/>
<#if maxSizeForMassUpdate gt totalResults >
<script type="text/javascript">
	$$(".addAllToJob").each( function(element, index) { element.observe('click', function(event){ Event.stop(event); lockOutMassUpdate(); getResponse('<@s.url namespace="/ajax" action="assignToJob" searchId="${searchId}" currentPage="${currentPage!}" job="${job.id}" escapeAmp="false"/>'); }) } );
	$$(".removeAllFromJob").each( function(element, index) { element.observe('click', function(event){ Event.stop(event); lockOutMassUpdate(); getResponse('<@s.url namespace="/ajax" action="assignToJob" searchId="${searchId}" currentPage="${currentPage!}" job="" escapeAmp="false"/>'); })} );
</script>
</#if>
