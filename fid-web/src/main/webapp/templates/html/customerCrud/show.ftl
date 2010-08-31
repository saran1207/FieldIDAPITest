${action.setPageType('customer','show')!}

<div class="view oneColumn" >
	<div >
		<span class="label" ><@s.text name="label.customerid"/>:</span><span class="field">${customerId!}</span>
	</div>
	<div >
		<span class="label" ><@s.text name="label.customername"/>:</span><span class="field">${customerName!}</span>
	</div>
	<div class="formRowHolder">
		<span class="label" ><@s.text name="label.organizationalunit"/>:</span><span class="field">${customer.parent.name!}</span>
	</div>
	<div >
		<span class="label" ><@s.text name="label.contactname"/>:</span><span class="field">${contactName!}</span>
	</div>
	<div >
		<span class="label" ><@s.text name="label.contactemail"/>:</span><span class="field">${accountManagerEmail!}</span>
	</div>
	<div >
		<span class="label" ><@s.text name="label.streetaddress"/>:</span><span class="field">${addressInfo.streetAddress!}</span>
	</div>
	<div >
		<span class="label" ><@s.text name="label.city"/>:</span><span class="field">${addressInfo.city!}</span>
	</div>
	<div >
		<span class="label" ><@s.text name="label.state"/>:</span><span class="field">${addressInfo.state!}</span>
	</div>
	<div >
		<span class="label" ><@s.text name="label.zip"/>:</span><span class="field">${addressInfo.zip!}</span>
	</div>
	<div >
		<span class="label" ><@s.text name="label.country"/>:</span><span class="field">${addressInfo.country!}</span>
	</div>
	<div >
		<span class="label" ><@s.text name="label.phone1"/>:</span><span class="field">${addressInfo.phone1!}</span>
	</div>
	<div >
		<span class="label" ><@s.text name="label.phone2"/>:</span><span class="field">${addressInfo.phone2!}</span>
	</div>
	<div >
		<span class="label" ><@s.text name="label.fax"/>:</span><span class="field">${addressInfo.fax1!}</span>
	</div>

	
</div>


