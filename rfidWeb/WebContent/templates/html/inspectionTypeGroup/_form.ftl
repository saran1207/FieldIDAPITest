<head>
	<@n4.includeStyle href="pageStyles/eventTypeGroup"/>
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
	<label for="printOutId" ><@s.text name="label.reportstyle"/></label>
	<ul class="printOutSelection">
		<li class="none">
			<div class="printOutThumbnail" ></div>
			<div class="printOutDescription">
				<@s.text name="label.none"/>
			</div>
			<div>
				<@s.radio name="certPrintOutId" list="%{getSingleMapElement(0)}" theme="simple" />
			</div>
		</li>
		<#list certPrintOuts as printOut >
			<li>
				<div class="printOutThumbnail" >
				<a href="<@s.url action="downloadPrintOutPreview" namespace="/file" uniqueID="${printOut.id}"/>" class="lightview" title="<@s.text name="label.preview"/>">
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
				<@s.radio name="certPrintOutId" list="%{getSingleMapElement(${printOut.id})}" theme="simple" />
			</div>
			</li>
		</#list>
		<li>
			<div class="printOutThumbnail">
				<img src="<@s.url value="/images/newCustomPrintOut.jpg" />" alt="<@s.text name="label.newcustomprintout"/>"/>
			</div>
			<div class="printOutDescription">
				<@s.text name="label.newcustomprintout"/><br/>
				<a href="javascript:void(0);" id="contactUs_button"><@s.text name="label.contactus"/></a>
				<div id="contactUs" class="hidden" style="border :1px solid black"><@s.text name="label.contactusinformation"/></div>
				<script type="text/javascript">
					$("contactUs_button").observe( 'click', function(event) { showQuickView('contactUs', event); } );
				</script>
			</div>
		</li>
	</ul>
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
				<a href="<@s.url action="downloadPrintOutPreview" namespace="/file" uniqueID="${printOut.id}"/>" class="lightview" title="<@s.text name="label.preview"/>">
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

