// dynamically validates any form elements with class 'validation-required' 
// error messages are placed into a div with id ELEMENT_NAME+'ErrorMsg'
// by Jesse Miller, N4 Systems
function dynamicValidation(theForm) {

	
	formValidates = true;
	
	for (i=0; i < theForm.elements.length; i++) {
		
		if (theForm.elements[i].className == 'validation-required') {
			if (theForm.elements[i].type == 'text') {
				if (theForm.elements[i].value.length <= 0) {
					document.getElementById(theForm.elements[i].name+'ErrorMsg').innerHTML = " * "+theForm.elements[i].id + " is required.\n";
					//alert(theForm.elements[i].name + " is required!");
					formValidates = false;					
										
				} else {
					document.getElementById(theForm.elements[i].name+'ErrorMsg').innerHTML = " ";
				}
			}
		}
		
	}
	
	return formValidates;

}
