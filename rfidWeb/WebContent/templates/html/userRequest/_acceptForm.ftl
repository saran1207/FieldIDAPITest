<head>
	<script type="text/javascript" src="<@s.url value="javascript/timezone.js" />"></script>
	<script type="text/javascript">
		countryChangeUrl = "<@s.url action="getRegions" namespace="/ajax" />";
	</script>
</head>
<div id="acceptForm" style="width:652px">
	<@s.form  action="userRequestAccept" cssClass="inputForm oneColumn" theme="css_xhtml" >
		<div class="formRowHolder">
			<div class="wwgrp">
				<span class="label wwlbl"><@s.text name="label.companyname"/> </span> <span class="field wwctrl">${userRequest.companyName?html}</span>
			</div>
		</div>
		<@s.hidden name="uniqueID" />
		<div class="formRowHolder">
			<@s.select key="label.organizationalunit" name="organizationalUnit" list="organizationalUnits" listKey="id" listValue="name" labelposition="left" required="true"/>
		</div>
		<div class="formRowHolder">
			<@s.select id="customerList" key="label.customer" name="customer" list="customers" listKey="id" listValue="name" labelposition="left" onchange="customerChanged(this);"  required="true"/>
		</div>
		<div class="formRowHolder">
			<@s.select key="label.division" id="division" name="division" list="divisions" listKey="id" listValue="name" labelposition="left" emptyOption="true"/>
		</div>
		
		<div>
			<@s.fielderror>
					<@s.param>userPermissions</@s.param>				
			</@s.fielderror>
			<table class="list" style="width:650px">
				
				<tr class="titleRow">
					<th class="permissionName"><@s.text name="label.permissions"/></th>
					<th class="radio"><@s.text name="label.on"/></th>
					<th class="radio"><@s.text name="label.off"/></th>
				</tr>
			
				<#list permissions as permission >
					<tr >
						<td class="permissionName">
							<@s.text name="${permission.name}"/>
						</td>
						<td class="radio">
							<@s.radio name="userPermissions['${permission.id}']" list="on"  theme="simple" cssClass="permissionOn"/>
						</td>
						<td class="radio">
							<@s.radio name="userPermissions['${permission.id}']" list="off"  theme="simple" cssClass="permissionOff"/>
						</td>
					</tr >
				</#list>
				<tr>
					<td class="permissionName"></td>
					<td class="radio"><input type="reset" name="allOn" onclick="allPermissionsOn();return false;" value="<@s.text name="label.allon"/>"/></td>
					<td class="radio"><input type="reset" name="allOff" onclick="allPermissionsOff();return false;" value="<@s.text name="label.alloff"/>"/></td>
				</tr>
				
				
			</table>
		
		</div>
		
		<div class="formAction">
			<@s.submit type="button" key="hbutton.cancel" onclick="formCancel(); return false"/>							
			<@s.submit key="hbutton.accept" onclick="return formSubmit();"/>
		</div>
	</@s.form>
	
	<script type="text/javascript" >

		function allPermissionsOn() {
			var onElements = $$('.permissionOn');
			for( var i = 0; i < onElements.length; i++ ) {
				onElements[i].checked=true;
			}
		
		}
		
		function allPermissionsOff() {
			var offElements = $$('.permissionOff');
			for( var i = 0; i < offElements.length; i++ ) {
				offElements[i].checked=true;
					
			} 
		
		}
		
		customerChanged($('customerList'));
	</script>
</div>