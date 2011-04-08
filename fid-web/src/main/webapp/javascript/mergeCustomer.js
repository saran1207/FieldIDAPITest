function selectWinner(customerID, name, code, createDate) {
	
	backToStep(3);

	var headerCol = new Element('td').update('Winner');
	var nameCol = new Element('td').update(name);	
	var codeCol = new Element('td').update(code);	
	var dateCol = new Element('td').update(createDate);

	$('winningCustomer').appendChild(headerCol);
	$('winningCustomer').appendChild(nameCol);
	$('winningCustomer').appendChild(codeCol);
	$('winningCustomer').appendChild(dateCol);
	
	$('winningCustomerId').writeAttribute('value', customerID);
}

function clearWinner() {
	
	$('winningCustomerId').value='';
		
	$('winningCustomer').childElements().each( function (element) {		
		element.remove();
	});
	
	backToStep(2);
	
}