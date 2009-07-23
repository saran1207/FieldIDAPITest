<title><@s.text name="title.massupdateinspections" /></title>
<head>
	<script type="text/javascript" src="<@s.url value="javascript/customerUpdate.js" includeParams="none" />"></script>
	<script type="text/javascript">
		customerChangeUrl = "<@s.url action="divisionList" namespace="/ajax" includeParams="none"/>";
		
		function selectField( fieldType ) {
			var field = $('check_' + fieldType );
			
			field.checked = true;
		}
	</script>
</head>

<h4 >Instructions </h4>
<div class="help">
	<p>
		<@s.text name="instruction.massupdate" /> 
	</p>
	
</div>

<@s.form action="massUpdateInspectionsSave" theme="simple" cssClass="listForm">
	<@s.hidden name="searchId" />
	<@s.hidden name="currentPage" />
	<table class="list">
		<tr>
			<th class="checkboxRow"><@s.text name="label.select"/></th>
			<th><@s.text name="label.fieldstoupdate"/></th>
		</tr>
		<#if securityGuard.jobSitesEnabled>
			<tr>
				<td><@s.checkbox name="select['jobSite']" id="check_jobSite"/></td>
				<td>
					<p>
						<label class="label" ><@s.text name="label.jobsite"/>:</label> 
						<span class="field">
							<@s.select  name="jobSite" list="jobSites" listKey="id" listValue="name"  labelposition="left" onchange="selectField('jobSite');" >
								<#if (action.fieldErrors['jobSite'])?exists> 
									<@s.param name="cssClass">inputError</@s.param>
									<@s.param name="title">${action.fieldErrors['jobSite']}</@s.param>
								</#if> 
							</@s.select>
						</span>
					</p>
				</td>
			</tr>
		<#else>
			<tr>
				<td><@s.checkbox name="select['customer']" id="check_customer" /></td>
				<td>
					<p>
						<label class="label" ><@s.text name="label.eusername"/>:</label> 
						<span class="field"><@s.select  name="customer" list="customers" listKey="id" listValue="name"  onchange="customerChanged(this); selectField('customer');" /></span>
					</p>
					<p>
						<label class="label"><@s.text name="label.division"/>:</label> 
						<span class="field"><@s.select id="division" name="division" list="divisions" listKey="id" listValue="name" onchange="selectField('customer');" >
							<#if !Session.sessionUser.inDivision >
								<@s.param name="headerKey"></@s.param>
								<@s.param name="headerValue"></@s.param>
							</#if>					
						</@s.select></span>
					</p>
				</td>
				
			</tr>
		</#if>
			
		<tr>
			<td><@s.checkbox name="select['inspectionBook']" id="check_inspectionBook"/></td>
			<td>
				<p>
					<label class="label" ><@s.text name="label.inspectionbook"/>:</label> 
					<span class="field"><@s.select  name="inspectionBook" list="inspectionBooks" listKey="id" listValue="name" emptyOption="true" labelposition="left" onchange="selectField('inspectionBook');" /></span>
				</p>
			</td>
		</tr>
		
		<tr>
			<td><@s.checkbox name="select['location']" id="check_location"/></td>
			<td>
				<p>
					<label class="label"><@s.text name="label.location"/>:</label> 
					<span class="field"><@s.textfield name="location" onchange="selectField('location');"/></span>
				</p>
			</td>
		</tr>
		
		<tr>
			<td><@s.checkbox name="select['printable']" id="check_printable"/></td>
			<td>
				<p>
					<label class="label"><@s.text name="label.printable"/>:</label> 
					<span class="field"><@s.checkbox name="printable" onchange="selectField('printable');"/></span>
				</p>
			</td>
		</tr>
	</table>	
		
	<div class="formAction">
		<a href="<@s.url action="reportResults" includeParams="none" searchId="${searchId!1}"currentPager="${currentPage!1}"/>"><@s.text name="label.returntoreport"/></a>
		<@s.submit key="hbutton.save" onclick="if( !confirm( '${action.getText( 'warning.massupdate' )}' ) ) { return false; }"/>
	</div>

</@s.form>
