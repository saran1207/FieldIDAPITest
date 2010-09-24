<head>
	<@n4.includeStyle type="page" href="eventTypeGroup"/>
</head>
<#include "/templates/html/common/_formErrors.ftl" />

<div class="infoSet">
	<label for="name"><@s.text name="indicator.required"/> <@s.text name="label.name"/></label>
	<@s.textfield name="name" required="true"/>
</div>

<div class="infoSet">
	<label for="reportTitle" >
		<@s.text name="indicator.required"/> <@s.text name="label.reporttitle"/>
		<a href="javascript:void(0);" id="whatsThis_reportTitle_button" >?</a>
		<div id="whatsThis_reportTitle" class="hidden" style="border :1px solid black"><@s.text name="whatsthis.reporttitle"/></div>
		<script type="text/javascript">
			$("whatsThis_reportTitle_button").observe( 'click', function(event) { showQuickView('whatsThis_reportTitle', event); } );
		</script>
	</label>
	<@s.textfield name="reportTitle" required="true"/>
</div>

<div class="infoSet">
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
	<div>
		<@s.radio name="certPrintOutId" list="%{getSingleMapElement(0)}" />
		<@s.text name="label.no_certificate"/>
	</div>
</div>

<div class="infoSet">
	<label for="printOutId" ><@s.text name="label.observationreportstyle"/></label>
	<ul class="printOutSelection">
		<li class="none">
			<div class="printOutThumbnail" ></div>
			<div class="printOutDescription">
				<@s.text name="label.none"/>
			</div>
			<div>
				<@s.radio name="observationPrintOutId" list="%{getSingleMapElement(0)}" theme="simple" />
			</div>
		</li>
		<#list observationPrintOuts as printOut >
			<li>
				<div class="printOutThumbnail" >
				<a href="<@s.url action="downloadPrintOutPreview" namespace="/file" uniqueID="${printOut.id}"/>" title="<@s.text name="label.preview"/>">
					<img src="<@s.url action="downloadPrintOutThumb" namespace="/file" uniqueID="${printOut.id}" />" alt="<@s.text name="label.thumbnail"/>"/>
				</a>
				</div>
			<div class="printOutDescription">
				${printOut.name?html} <br/>
				<a href="javascript:void(0);" id="printOut_${printOut.id}_button"><@s.text name="label.moreinfo"/></a>
				<div id="printOut_${printOut.id}" class="hidden" style="border :1px solid black">${action.replaceCR(printOut.description?html)}</div>
				<script type="text/javascript">
					$("printOut_${printOut.id}_button").observe( 'click', function(event) { showQuickView('printOut_${printOut.id}', event); } );
				</script>
			</div>
			<div>
				<@s.radio name="observationPrintOutId" list="%{getSingleMapElement(${printOut.id})}" theme="simple" />
			</div>
			</li>
		</#list>
		
	</ul>
</div>

