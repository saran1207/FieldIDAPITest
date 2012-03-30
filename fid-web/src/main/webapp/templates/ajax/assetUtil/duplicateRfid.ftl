<#assign html>
	<#escape y as y?html>
		<div>
			<p>
				<@s.text name="warning.changerfid"/>
			</p>	
			
			<table class="list">
				<tr>
					<th>${identifierLabel}</th>
					<th><@s.text name="label.rfidnumber"/></th>
					<th><@s.text name="label.customername"/></th>
					<th><@s.text name="label.description"/></th>
				</tr>
				<#list assets as asset >
					<tr>
						<td>${asset.identifier}</td>
						<td>${asset.rfidNumber!}</td>
						<td>${(asset.owner.name)!}</td>
						<td>${asset.description}</td>
					</tr>
				</#list>
			</table>
			<p>
				<div class="formAction" style="text-align: center;">
					<button onclick="cancel();" id="cancelRfidChange" ><@s.text name="label.cancel"/></button>
					<button onclick="closeAndSubmit();" id="confirmRfidChange"><@s.text name="label.continue"/></button>
				</div>
			</p>
		</div>
	</#escape>
	
</#assign>
<#escape x as x?js_string >	
	var modalBox = $( 'modalBox' );
	if( modalBox == null ) {
		document.body.insert( 
			new Element( 'div', {id: 'modalBoxHolder'} ).insert( 
				new Element('div',{ id:'modalBox' }) 
				) 
			);
		modalBox = $('modalBox');
	}
	
	modalBox.update("${html}");

    jQuery().colorbox({inline: true, href:"#modalBox", title: "<@s.text name="label.checkrfid"/>", maxWidth: '450px'});
	
</#escape>