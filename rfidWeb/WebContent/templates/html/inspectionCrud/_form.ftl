	<head>
		<script type="text/javascript" src="<@s.url value="/javascript/commentTemplates.js" />" ></script>
		<script type="text/javascript" src="<@s.url value="/javascript/inspection.js" />" ></script>
		<script type="text/javascript" src="<@s.url value="/javascript/inspectionBook.js"/>" ></script>
		<script type="text/javascript" src="<@s.url value="javascript/customerUpdate.js"  />"></script>	
		<script type="text/javascript" src="<@s.url value="javascript/changeJobSite.js" />"></script>
		<script type="text/javascript" src="<@s.url value="javascript/calcNextDate.js" />"></script>
		<script type="text/javascript">
			customerChangeUrl = "<@s.url action="divisionList" namespace="/ajax" />";
			changeCommentUrl = '<@s.url action="commentTemplateShow" namespace="ajax"   />';
			updateInspectionBooksUrl = '<@s.url action="inspectionBooks" namespace="ajax"   />';
			jobSiteChangeUrl = '<@s.url action="jobSite" namespace="/ajax" />';
			updateNextDateUrl = '<@s.url action="calcNextDate" namespace="/ajax" />';
			productTypeId = ${product.type.id}
			var proofTestTypes = ${json.toJSON( proofTestTypesUpload )}
		</script>
		<script type="text/javascript">
			function imageFileUploaded( fileName, directory ){
				$("singleFileUpload").remove();
				$("proofTestUploadFile").show();
				$("newFile").value = "true";
				$("proofTestDirectory").value = directory;
			}
			
			function uploadAnotherFile() { 
				var iframe = '<iframe id="singleFileUpload" src="<@s.url action="uploadImageForm" namespace="/ajax" />" scrollbar="no" style="overflow:hidden;" frameborder="0" width="500" height="20" ></iframe>';

				$( "proofTestUploadFile" ).hide();
				$( "proofTestUpload" ).insert( { top: iframe } );
				$("newFile").value = "true";
				$("proofTestDirectory").value = "";
			}
			
		</script>
		
		<@n4.includeStyle href="pageStyles/inspection" />
		
		
		<#include "/templates/html/common/_calendar.ftl"/>
	</head>
	<title>${(inspectionType.name)!} <@s.text name="label.on"/> ${product.serialNumber}</title>
	
	<#include "/templates/html/common/_formErrors.ftl" />
	
	<#include "_inspectionSummaryForm.ftl"/>
	
	<#setting url_escaping_charset='UTF-8'>	
	<#list inspectionType.infoFieldNames as infoField >
		<p>	
			<label>${infoField}:</label>
			<span><@s.textfield name="infoOptionMap['${infoField?url}']"/></span>
		</p>
	 	
	</#list>
	<#if inspection.id?exists && action.isParentProduct() >
		<p>
			<label><@s.text name="label.result"/>:</label>
			<span><@s.select name="result" list="results" listKey="name()" listValue="%{getText( label )}" /></span>
		</p>
	</#if>
	
	<#assign formInspection=inspection>
	<#assign identifier="inspectionForm">
	<#include "_inspection.ftl" />
	
	<#if action.isParentProduct() && !inspectionType.supportedProofTests.isEmpty() >
		<h2><@s.text name="label.prooftest"/></h2>
		<p>
		
			<label><@s.select id="proofTestType" name="proofTestType" list="inspectionType.supportedProofTests" listKey="name()" listValue="%{ getText( label ) }" onchange="checkProofTestType( 'proofTestType' )"/></label>
			<span id="proofTestUpload" <#if proofTestTypeEnum?exists && !proofTestTypeEnum.uploadable >style="display:none"</#if> >
				<#if !proofTestDirectory?exists || proofTestDirectory.length() == 0  >
					<iframe id="singleFileUpload" src="<@s.url action="uploadImageForm" namespace="/ajax" />" scrollbar="no" style="overflow:hidden;" frameborder="0" width="500" height="20" ></iframe>
				</#if>
				<span id="proofTestUploadFile" <#if !proofTestDirectory?exists || proofTestDirectory.length()  == 0  >style="display:none;"</#if> >
					<a href="uploadAgain"  onclick="uploadAnotherFile(); return false;"><@s.text name="label.uploaddifferentfile"/></a>
					<@s.hidden name="newFile" id="newFile"/>
					<@s.hidden name="proofTestDirectory" id="proofTestDirectory"/>
				</span>
			</span>
			
			<span id="proofTestManual" <#if proofTestTypeEnum?exists && proofTestTypeEnum.uploadable >style="display:none"</#if> >
				<label><@s.text name="label.peakload"/></label>
				<span><@s.textfield name="peakLoad"/></span>
				<label><@s.text name="label.duration"/></label>
				<span><@s.textfield name="testDuration"/></span>
				<label><@s.text name="label.peakloadduration"/></label>
				<span><@s.textfield name="peakLoadDuration"/></span>
			</span>
		</p>
		
		
	</#if>
	
	<#include "_postInspectionForm.ftl" />
		
	
	<#include "../common/_attachedFilesForm.ftl" />
