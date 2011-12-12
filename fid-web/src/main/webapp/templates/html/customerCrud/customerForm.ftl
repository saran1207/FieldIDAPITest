<head>
	<title>
		<#if uniqueID?exists  >
			${action.setPageType('customer','edit')!}
		<#else>
			${action.setPageType('customer','add')!}
		</#if>
	</title>
	<@n4.includeStyle href="user" type="page"/>
    <script type="text/javascript" src="<@s.url value="/javascript/lockSubmitButtons.js"/>"></script>

	<#if customer.linkedOrg?exists>
		<@n4.includeScript>
			document.observe("dom:loaded", function() {
					$$(".linkedCustomerControlled").invoke("disable");
				});
		</@n4.includeScript>
	</#if>

	<script type="text/javascript" >
		var buttons = new Array( 'saveButton');
		var buttonLockMessages = new Array( '<@s.text name="hbutton.pleasewait" />');
		
		document.observe("dom:loaded", function() {
		    $('saveButton').observe('click', function(event) {
		        event.stop();
		        lockSubmitButtons();
		        $('customerForm').submit();
		    });
	    });
	    
	    function imageFileUploaded( fileName, directory ){
			$("imageUpload").remove();
			$("imageUploaded").show();
			$("removeImage").value = "false";
			$("previewImage").hide();
			$("uploadInfo").hide();
			$("newImage").value = "true";
			$("imageDirectory").value = directory;
		}
		
		function removeUploadImage() { 
			$( "imageUploaded" ).hide();
			$("removeImage").value = "true";
			var iframe = '<iframe id="imageUpload" src="<@s.url action="uploadImageForm" namespace="/aHtml/fileUploads" />" scrolling="no" scrollbar="no" style="overflow:hidden;" frameborder="0" width="300" height="25" ></iframe> <span id="uploadInfo"><@s.text name="label.org_image_looks_best"/></span>';
			$( "imageUploadField" ).insert( { top: iframe } );
			$("imageUploaded").removeClassName( "inputError" );
			$("imageUploaded").title = "";
			$("uploadInfo").show();
		}
	</script>
</head>

<@s.form action="customerEdit!save" cssClass="fullForm fluidSets" theme="fieldid" id="customerForm">
	<#include "../common/_formErrors.ftl"/>
	<@s.hidden name="uniqueID" />
	<@s.hidden name="currentPage" />
	<#if customer.linkedOrg?exists>
		<p class="instructions">
			<@s.text name="instructions.linked_customer_edit"/>
		</p>
	</#if>
	<div class="multiColumn">
		<div class="fieldGroup fieldGroupGap">
			<h2><@s.text name="label.details"/></h2>
			<div class="infoSet">
				<label class="label" for="customerId"><@s.text name="label.id"/> <#include "../common/_requiredMarker.ftl"/></label>
				<@s.textfield  name="customerId" size="30" />
			</div>
			<div class="infoSet">
				<label class="label" for="parentOrgId"><@s.text name="label.organizationalunit"/> <#include "../common/_requiredMarker.ftl"/></label>
				<@s.select  name="parentOrgId" list="parentOrgs" listKey="id" listValue="name" />
			</div>
			<div class="infoSet">
				<label class="label" for="customerName"><@s.text name="label.name"/> <#include "../common/_requiredMarker.ftl"/></label>
				<@s.textfield name="customerName" size="50" cssClass="linkedCustomerControlled" />
			</div>
			<div class="infoSet">
				<label class="label" for="customerNotes"><@s.text name="label.notes"/></label>
				<@s.textarea  name="customerNotes" />
			</div>
			<div class="infoSet">
				<label class="label" for="customerNotes"><@s.text name="label.logo_image"/></label>
				
				<span id="imageUploadField" class="fieldHolder">
				
					<#if !logoImageDirectory?exists || logoImageDirectory.length() == 0  || removeImage >
						<iframe id="imageUpload" src="<@s.url action="uploadImageForm" namespace="/aHtml/fileUploads" />"  scrolling="no" scrollbar="no" style="overflow:hidden;" frameborder="0" width="300" height="25" ></iframe>
						<span id="uploadInfo"><@s.text name="label.org_image_looks_best"/></span>
					</#if>
					<span id="imageUploaded" <#if (action.fieldErrors['logoImageContentType'])?exists>class="inputError" title="${action.fieldErrors['uploadedImageContentType']}"</#if> <#if  !logoImageDirectory?exists || logoImageDirectory.length()  == 0  || removeImage >style="display:none;"</#if> >
						<@s.url id="previewImage" uniqueID="${uniqueID!}" action="downloadOrgLogo" namespace="/file" />
						<img src="${previewImage}" width="150px" <#if newImage > style="display:none"</#if> id="previewImage"/>
						
						<@s.hidden name="removeImage" id="removeImage"/>  <a href="removeImage" id="removeImageLink" onclick="removeUploadImage(); return false;"><@s.text name="label.remove"/></a>
						<@s.hidden name="newImage" id="newImage"/>
						<@s.hidden name="logoImageDirectory" id="imageDirectory"/>
						
						
					</span>
				</span>
			</div>
		</div>
		<div class="fieldGroup">
			<h2><@s.text name="label.contact_information"/></h2>
			<div class="infoSet">
				<label class="label" for="contactName"><@s.text name="label.contactname"/></label>
				<@s.textfield  name="contactName" />
			</div>
			<div class="infoSet">
				<label class="label" for="accountManagerEmail"><@s.text name="label.email_address"/></label>
				<@s.textfield  name="accountManagerEmail" />
			</div>
			<div class="infoSet">
				<label class="label" for="addressInfo.streetAddress"><@s.text name="label.streetaddress"/></label>
				<@s.textfield  name="addressInfo.streetAddress" cssClass="linkedCustomerControlled"/>
			</div>
			<div class="infoSet">
				<label class="label" for="addressInfo.city"><@s.text name="label.city"/></label>
				<@s.textfield  name="addressInfo.city" cssClass="linkedCustomerControlled" />
			</div>
			<div class="infoSet">
				<label class="label" for="addressInfo.state"><@s.text name="label.state"/></label>
				<@s.textfield  name="addressInfo.state" cssClass="linkedCustomerControlled"/>
			</div>
			<div class="infoSet">
				<label class="label" for="addressInfo.zip"><@s.text name="label.zip"/></label>
				<@s.textfield  name="addressInfo.zip" cssClass="linkedCustomerControlled" />
			</div>
			<div class="infoSet">
				<label class="label" for="addressInfo.country"><@s.text name="label.country"/></label>
				<@s.textfield  name="addressInfo.country" cssClass="linkedCustomerControlled" />
			</div>
			<div class="infoSet">
				<label class="label" for="addressInfo.phone1"><@s.text name="label.phone1"/></label>
				<@s.textfield  name="addressInfo.phone1" cssClass="linkedCustomerControlled"/>
			</div>
			<div class="infoSet">
				<label class="label" for="addressInfo.phone2"><@s.text name="label.phone2"/></label>
				<@s.textfield  name="addressInfo.phone2" cssClass="linkedCustomerControlled" />
			</div>
			<div class="infoSet">
				<label class="label" for="addressInfo.fax1"><@s.text name="label.fax"/></label>
				<@s.textfield  name="addressInfo.fax1" cssClass="linkedCustomerControlled"/>
			</div>
		</div>
	</div>
	<div class="formAction" >
		<@s.submit  key="label.save" id="saveButton"/>
		
		<@s.text name="label.or"/>  
		
		<#if uniqueID?exists>  
			<a href="<@s.url action="customerShow" uniqueID="${uniqueID!}" currentPage="${currentPage!}" listFilter="${listFilter!}"/>" ><@s.text name="hbutton.cancel" /></a>
		<#else>
			<a href="<@s.url action="customerList" currentPage="${currentPage}" listFilter="${listFilter!}"/>" ><@s.text name="label.cancel"/></a>
		</#if>
		 
		
	</div>
</@s.form>