<title><@s.text name="title.merge_customers"/> - ${losingCustomer.name?html} <@s.text name="label.into"/> ${winningCustomer.name?html}</title>
<head>
	<link rel="stylesheet" type="text/css" href="<@s.url value="/style/steps.css"/>" />
</head>

<div class="step">
	<h2><@s.text name="label.you_are_done"/></h2>
	<div class="stepContent">
		<p>
			<@s.text name="message.customers_being_merged"/>
		</p>
		<p>
			<@s.text name="label.go_to_winning_customer_page"/> <a href="<@s.url action="customerShow" uniqueID="${winningCustomer.id}"/>">${winningCustomer.name?html}</a>
		</p>	
	</div>
</div>