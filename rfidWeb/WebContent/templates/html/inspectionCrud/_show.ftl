<@s.bean id="moneyUtil" name="com.n4systems.tools.MoneyUtils"/>
<div id="inspection" >
	<div id="productSummary">
		<h2>
			<span><@s.text name="label.productsummary"/></span>
			
			<@s.url id="inspectionCertUrl" action="downloadInspectionCert" namespace="/file" reportType="INSPECTION_CERT" uniqueID="${uniqueID}" />
			<@s.url id="observationCertUrl" action="downloadInspectionCert" namespace="/file" reportType="OBSERVATION_CERT" uniqueID="${uniqueID}" />
				
			<#if inspection.hasAnyPrintOuts()>
				<div id="cert_links" class="print" onmouseover="repositionCertLinks('cert_list', 'cert_links');" >
					<ul id="cert_list">
						<#if inspection.printable && inspection.type.group.hasPrintOut()>
							<li><a href="${inspectionCertUrl}" target="_blank" >${inspection.type.group.reportTitle?html} (<@s.text name="label.pdfreport"/>)</a></li>
						</#if>
						<#if inspection.type.group.hasObservationPrintOut()>
							<li><a href="${observationCertUrl}" target="_blank" ><@s.text name="label.printobservationcertificate"/></a></li>
						</#if>
					</ul>
					<img src="<@s.url value="/images/pdf_small.gif"/>" /> 
					<a href="javascript:void(0);" >
						<@s.text name="label.print"/>					
					</a>
				</div>
			</#if>
			
		</h2>
		<p>
			<label><@s.text name="${Session.sessionUser.serialNumberLabel}"/></label>
			<span>
				${product.serialNumber?html}
			</span>
		</p>
		<p>
			<label><@s.text name="label.rfidnumber"/></label>
			<span>
				${product.rfidNumber!""?html}
			</span>
		</p>
		
		<p>
			<label><@s.text name="label.producttype"/></label>
			<span>
				${product.type.name!""?html}
			</span>
		</p>
		
		<p>
			<label><@s.text name="label.desc"/></label>
			<span>
				${product.description?html}
			</span>
		</p>
	</div>
	
	<h2><@s.text name="label.customerinformation"/></h2>
	
	
	<p>
		<label><@s.text name="label.customer"/></label>
		<span>${(inspection.owner.customerOrg.name)!?html}</span>
	</p>
	
	<p>
		<label><@s.text name="label.division"/></label>
		<span>${(inspection.owner.divisionOrg.name)!?html}</span>
	</p>
	
	<p>
		<label><@s.text name="label.location"/></label>
		<span>${inspection.location!?html}</span>
	</p>
	
	<h2><@s.text name="label.inspectiondetails"/></h2>
	<p> 
		<label><@s.text name="label.inspectiontype"/></label>
		<span>${inspectionType.name?html}</span>
	</p>
	
	<p>
		<label><@s.text name="label.inspector"/></label>
		<span>
			<#include "_userName.ftl"/>
		</span>
	</p>
	<p>
		<label><@s.text name="label.inspectiondate"/></label>
		<span>${action.formatDateTime(inspection.date)}</span>
	</p>

	<p> 
		<label><@s.text name="label.scheduledon"/></label>
		<span>
			<#if inspection.schedule?exists>																																																					
				${action.formatDate(inspection.schedule.nextDate, false)}
			<#else>
				<@s.text name="label.notscheduled"/>
			</#if>
		</span>
	</p>
	
	<p>
		<label><@s.text name="label.inspectionbook"/></label>
		<span>${(inspection.book.name)!?html}</span>
	</p>
	
	
	<#list inspection.infoOptionMap.keySet() as key >
		<p>	
			<label>${key}:</label>
			<span> ${inspection.infoOptionMap[key]?html}</span>
		</p>
	</#list>
	
	<p>
		<label><@s.text name="label.result"/></label>
		<span><@s.text name="${(inspection.status.label?html)!}"/></span>
	</p>

	<#assign formInspection=inspection/>
	<#assign identifier="inspectionForm"/>
	<#include "_inspection.ftl" />
	
	<#if inspection.proofTestInfo?exists >
		<h2><@s.text name="label.prooftest"/></h2>
		<p>
			<label><@s.text name="label.prooftesttype"/></label>
			<span><@s.text name="${(inspection.proofTestInfo.proofTestType.label)! action.getText( 'label.unknown') }"/></span>
		</p>
		<p>
			<label><@s.text name="label.peakload"/></label>
			<span><@s.text name="${(inspection.proofTestInfo.peakLoad)!?html}"/></span>
		</p>
		<p>
			<label><@s.text name="label.duration"/></label>
			<span><@s.text name="${(inspection.proofTestInfo.duration)!?html}"/></span>
		</p>
		<p>
			<label><@s.text name="label.peakloadduration"/></label>
			<span><@s.text name="${(inspection.proofTestInfo.peakLoadDuration)!?html}"/></span>
		</p>
		
		<#if inspection.proofTestInfo.proofTestType?exists && inspection.proofTestInfo.proofTestType.uploadable >
			<p>
				<label id="chartImage">
					<img alt="<@s.text name="label.nochart"/>" src="<@s.url action="downloadProofTestChart" namespace="/file" includeParams="none" uniqueID="${uniqueID}"/>" width="450"/>
				</label>
			</p>
		</#if>
		
	</#if>
	
	
	
	
	<h2><@s.text name="label.postinspectioninformation"/></h2>
	
	<p>
		<label><@s.text name="label.comments"/></label>
		<#escape x as action.replaceCR(x?html) >
			<span>${inspection.comments!}</span>
		</#escape>
	</p>
	
	<p>
		<label><@s.text name="label.modifiedby"/></label>
		<span>
			<#if inspection.modifiedBy?exists >
				<#assign user=inspection.modifiedBy >
			<#else>
				<#assign user=inspection >
			</#if>
			<#include "_userName.ftl"/> 
			<@s.text name="label.on"/> ${action.formatDateTime(inspection.modified)} 
		</span>
	</p>
	
	<#if !linkedInspection >
		<#assign downloadAction="downloadAttachedFile"/>
		<#assign attachments=inspection.attachments/>
		
		<#include "/templates/html/common/_attachedFilesShow.ftl"/>
	</#if>
	
	
	<#if subInspections?exists >
		<#list subInspections as subInspection >
			<h2>
				<a href="javascript:void(0);" id="expand_${subInspection.id}" onclick=" openSection( 'subInspection_${subInspection.id}', 'expand_${subInspection.id}', 'collapse_${subInspection.id}' ); return false;"><img src="<@s.url value="/images/expandLarge.gif" />" ></a>
				<a href="javascript:void(0);" id="collapse_${subInspection.id}" onclick="closeSection( 'subInspection_${subInspection.id}', 'collapse_${subInspection.id}', 'expand_${subInspection.id}' ); return false;" style="display:none"><img src="<@s.url value="/images/collapseLarge.gif" />" ></a>
				${subInspection.type.name?html} - ${(subInspection.name?html)!}
			</h2>
			<div id="subInspection_${subInspection.id}" style="display:none">
				<#if !subInspection.infoOptionMap.keySet().isEmpty() >
					<h2><@s.text name="label.productsummary"/></h2>
					<#list subInspection.infoOptionMap.keySet() as key >
						<p>	
							<label>${key?html}:</label>
							<span> ${subInspection.infoOptionMap[key]?html}</span>
						</p>
					</#list>
				</#if>
			
				<#assign formInspection=subInspection>
				<#assign identifier=subInspection.id/>
				
				<#include "_inspection.ftl" />
				<h2><@s.text name="label.postinspectioninformation"/></h2>
		
				<p>
					<label><@s.text name="label.comments"/></label>
					<#escape x as action.replaceCR(x?html) >
						<span>${subInspection.comments!?html}</span>
					</#escape>
				</p>
				<#assign downloadAction="downloadSubAttachedFile"/>
				<#assign attachments=subInspection.attachments/>
				<#assign attachmentID=subInspection.id>
				<#include "/templates/html/common/_attachedFilesShow.ftl"/>
			</div>
			
		</#list>
	</#if>
	
</div>