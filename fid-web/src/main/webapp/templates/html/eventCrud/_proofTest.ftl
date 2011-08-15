	<h2><@s.text name="label.prooftest"/></h2>
		<div class="infoSet">
		
		
			<div class="fieldHolder">
			<label class="label"><@s.select cssClass="proofTestType" id="proofTestType" theme="fieldidSimple" name="proofTestType" list="eventType.supportedProofTests" listKey="name()" listValue="%{ getText( label ) }" onchange="checkProofTestType( 'proofTestType' )"/></label>
				<div class="proofTestUpload" id="proofTestUpload" <#if proofTestTypeEnum?exists && !proofTestTypeEnum.uploadable >style="display:none"</#if> >
					<#if !proofTestDirectory?exists || proofTestDirectory.length() == 0  >
						<iframe id="singleFileUpload" src="<@s.url action="uploadImageForm" namespace="/aHtml/fileUploads" />" scrolling="no" scrollbar="no" style="overflow:hidden;" frameborder="0" width="240" height="21" ></iframe>
					</#if>
					<span class="proofTestUploadFile" id="proofTestUploadFile" <#if !proofTestDirectory?exists || proofTestDirectory.length()  == 0  >style="display:none;"</#if> >
						<a href="uploadAgain"  onclick="uploadAnotherFile(); return false;"><@s.text name="label.uploaddifferentfile"/></a>
						<@s.hidden name="newFile" id="newFile"/>
						<@s.hidden name="proofTestDirectory" id="proofTestDirectory"/>
					</span>
				</div>
				
				<span class="proofTestManual" id="proofTestManual" <#if proofTestTypeEnum?exists && proofTestTypeEnum.uploadable > style="display:none"</#if> >
                    <label class="label"><@s.text name="label.peakload"/></label>
                    <@s.textfield name="peakLoad" theme="fieldidSimple"/>
                    <label class="label"><@s.text name="label.duration"/></label>
                    <@s.textfield name="testDuration" theme="fieldidSimple"/>
                    <label class="label"><@s.text name="label.peakloadduration"/></label>
                    <@s.textfield name="peakLoadDuration" theme="fieldidSimple"/>
				</span>
			</div>
		</div>
		