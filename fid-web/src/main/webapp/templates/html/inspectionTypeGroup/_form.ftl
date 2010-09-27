<head>
	<@n4.includeStyle type="page" href="eventTypeGroup"/>
	
	<script type"text/javascript">
	Event.observe(window, 'load', function() {
		$$('input[type="radio"]').each( function(elt) { 
			elt.observe('click', function() { 
				clearOtherBorders();
				elt.up('li').setStyle({ 'border' : '2px solid #D0DAFD'});
			 });
		 });
	});
	
	function clearOtherBorders(){
		$$('li').each(function(elt) { 
			elt.setStyle({'border' : '1px solid #D0DAFD'});
		});
	}
	</script>
</head>
<#include "/templates/html/common/_formErrors.ftl" />

<div class="certFormTitle">
	<h2><@s.text name="label.event_group_name"/></h2>
	<span><@s.text name="label.event_group_name_desc"/></span>
</div>
<div class="infoSet">
	<label for="name"><@s.text name="indicator.required"/> <@s.text name="label.name"/></label>
	<span><@s.textfield name="name" required="true"/></span>
</div>

<div class="certFormTitle">
	<h2><@s.text name="label.report_title"/></h2>
	<span><@s.text name="label.report_title_desc"/></span>
</div>
<div class="infoSet">
	<label for="reportTitle" >
		<@s.text name="indicator.required"/> <@s.text name="label.reporttitle"/>
	</label>
	<@s.textfield name="reportTitle" required="true"/>
</div>

<ul class="printOutSelection">
	<#list certPrintOuts as printOut >
		<li>
			<#if printOut.thumbPreviewFileThere>
				<div class="printOutThumbnail" >
					<img src="<@s.url action="downloadPrintOutThumb" namespace="/file" uniqueID="${printOut.id}" />" alt="<@s.text name="label.thumbnail"/>"/>
				</div>
				<div class="printOutDetails">
					<@s.radio name="certPrintOutId" list="%{getSingleMapElement(${printOut.id})}" theme="simple" />
					${printOut.name?html}
					<br/>
					<#if printOut.printOutFileThere>
						<a href="<@s.url action="downloadPrintOutPreview" namespace="/file" uniqueID="${printOut.id}"/>"  title="<@s.text name="label.preview"/>">
							<@s.text name="label.preview"/>
						</a>
					</#if>
				</div>
			<#else>
				<div class="noImageContainer">
					<div class="noImage">
					</div>
				</div>
				<div class="printOutDetails">
					<@s.radio name="certPrintOutId" list="%{getSingleMapElement(${printOut.id})}" theme="simple" />
					${printOut.name?html}
					<br/>
					<#if printOut.printOutFileThere>
						<a href="<@s.url action="downloadPrintOutPreview" namespace="/file" uniqueID="${printOut.id}"/>"  title="<@s.text name="label.preview"/>">
							<@s.text name="label.preview"/>
						</a>
					</#if>
				</div>
			</#if>
		</li>
	</#list>
</ul>
<div class="noneSelected">
	<@s.radio name="certPrintOutId" list="%{getSingleMapElement(0)}" />
	<@s.text name="label.no_certificate"/> 
</div>

<ul class="printOutSelection">
	<#list observationPrintOuts as printOut >
		<li>
			<#if printOut.thumbPreviewFileThere>
				<div class="printOutThumbnail" >
					<img src="<@s.url action="downloadPrintOutThumb" namespace="/file" uniqueID="${printOut.id}" />" alt="<@s.text name="label.thumbnail"/>"/>
				</div>
				<div class="printOutDetails">
					<@s.radio name="observationPrintOutId" list="%{getSingleMapElement(${printOut.id})}" theme="simple" />
					${printOut.name?html}
					<br/>
					<#if printOut.printOutFileThere>
						<a href="<@s.url action="downloadPrintOutPreview" namespace="/file" uniqueID="${printOut.id}"/>"  title="<@s.text name="label.preview"/>">
							<@s.text name="label.preview"/>
						</a>
					</#if>
				</div>
			<#else>
				<div class="noImageContainer">
					<div class="noImage">
					</div>
				</div>
				<div class="printOutDetails">
					<@s.radio name="observationPrintOutId" list="%{getSingleMapElement(${printOut.id})}" theme="simple" />
					${printOut.name?html}
					<br/>
					<#if printOut.printOutFileThere>
						<a href="<@s.url action="downloadPrintOutPreview" namespace="/file" uniqueID="${printOut.id}"/>"  title="<@s.text name="label.preview"/>">
							<@s.text name="label.preview"/>
						</a>
					</#if>
				</div>
			</#if>
		</li>
	</#list>
</ul>
<div class="noneSelected">
	<@s.radio name="observationPrintOutId" list="%{getSingleMapElement(0)}" />
	<@s.text name="label.no_observation_certificate"/>
</div>
