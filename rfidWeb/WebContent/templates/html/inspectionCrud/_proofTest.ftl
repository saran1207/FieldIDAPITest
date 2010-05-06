	<h2><@s.text name="label.prooftest"/></h2>
		<p>
		
			<label><@s.select id="proofTestType" name="proofTestType" list="inspectionType.supportedProofTests" listKey="name()" listValue="%{ getText( label ) }" onchange="checkProofTestType( 'proofTestType' )"/></label>
			<span id="proofTestUpload" <#if proofTestTypeEnum?exists && !proofTestTypeEnum.uploadable >style="display:none"</#if> >
				<#if !proofTestDirectory?exists || proofTestDirectory.length() == 0  >
					<iframe id="singleFileUpload" src="<@s.url action="uploadImageForm" namespace="/aHtml/fileUploads" />" scrolling="no" scrollbar="no" style="overflow:hidden;" frameborder="0" width="500" height="20" ></iframe>
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
		