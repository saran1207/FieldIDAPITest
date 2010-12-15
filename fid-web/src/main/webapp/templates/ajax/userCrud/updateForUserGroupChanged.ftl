<#escape x as x?js_string >
<#assign html>
<@s.select cssClass="userTypeSelect" id="userType" name="userType" list="userTypes" listKey="id" listValue="name" key="label.usertype" labelposition="left" theme="fieldidSimple"/>
</#assign>
	$$('.userTypeSelect').each(function(element) {
		element.replace('${html}');
    });
</#escape>
