	<#include "../common/_formErrors.ftl"/>
	<@s.hidden name="uniqueID" />
	<@s.hidden name="customerId"/>
	
	<div class="multiColumn">
		<div class="infoBlock">
			<h2 class="clean"><@s.text name="label.identification_information"/></h2>
			<div class="infoSet">
				<label class="label" for="divsionID"><@s.text name="label.division_id"/> <@s.text name="indicator.required"/></label>
				<@s.textfield name="divisionID" />
			</div>
			<div class="infoSet">
				<label class="label" for="name"><@s.text name="label.name"/> <@s.text name="indicator.required"/></label>
				<@s.textfield  name="name" />
			</div>
			<div class="infoSet">
				<label class="label" for="contact.name"><@s.text name="label.contactname"/></label>
				<@s.textfield  name="contact.name" />
			</div>
			<div class="infoSet">
				<label class="label" for="contact.email"><@s.text name="label.contactemail"/></label>
				<@s.textfield  name="contact.email" />
			</div>
		</div>
		<div class="infoBlock">
			<h2 class="clean"><@s.text name="label.contact_details"/></h2>
			<div class="infoSet">
				<label class="label" for="streetAddress"><@s.text name="label.streetaddress"/></label>
				<@s.textfield  name="addressInfo.streetAddress" />
			</div>
			<div class="infoSet">
				<label class="label" for="city"><@s.text name="label.city"/></label>
				<@s.textfield  name="addressInfo.city" />
			</div>
			<div class="infoSet">
				<label class="label" for="state"><@s.text name="label.state"/></label>
				<@s.textfield  name="addressInfo.state" />
			</div>
			<div class="infoSet">
				<label class="label" for="zip"><@s.text name="label.zip"/></label>
				<@s.textfield  name="addressInfo.zip" />
			</div>
			<div class="infoSet">
				<label class="label" for="country"><@s.text name="label.country"/></label>
				<@s.textfield  name="addressInfo.country" />
			</div>
			<div class="infoSet">
				<label class="label" for="phone1"><@s.text name="label.phone1"/></label>
				<@s.textfield  name="addressInfo.phone1" />
			</div>
			<div class="infoSet">
				<label class="label" for="phone2"><@s.text name="label.phone2"/></label>
				<@s.textfield  name="addressInfo.phone2" />
			</div>
			<div class="infoSet">
				<label class="label" for="fax1"><@s.text name="label.fax"/></label>
				<@s.textfield  name="addressInfo.fax1" />
			</div>
		</div>
	</div>
	<div class="actions" >
		<@s.submit key="label.save" /> <@s.text name="label.or"/> <a href="<@s.url action="divisions" includeParams="get"/>"><@s.text name="label.cancel"/></a>
	</div>

	
	
