<!-- static html included as help text when editing asset types -->	
	
	<h2 style="padding:5px 0px;">Asset Description Template Formatting</h2>
	<p>
	Asset Description template is used throughout Field ID Web &amp; Mobile to provide a description of each asset.&nbsp; You can customize how this looks for each Asset Type.&nbsp; Sentences are created with a combination of preconfigured and custom notations.&nbsp; <br>

	<br>
	There is no limit to the number of notations you can use and each notation can be separated by any character or word. <br>
	<br>
	</p>
	
	<span style="font-weight: bold;"> </span>
	<div style="line-height:150%;">
		<table class="Default" style="width: 400px;">
			<tbody>
				<tr>
					<td><span style="font-weight: bold;">Notation</span><br></td>
					<td><span style="font-weight: bold;">Displays</span><br></td>
				</tr>
				<tr>
					<td >{Identifier}</td>		
					<td>The Identifier of the asset</td>
				</tr>
				<tr>
					<td >{RFID}</td>
					<td >The RFID Number on the asset</td>
				</tr>
				<tr>
					<td >{RefNumber}</td>
					<td >The Reference Number on the asset</td>
				</tr>
		
				<tr>
					<td >{OrderNumber}</td>
					<td >The Order Number on the asset</td>
				</tr>
				<tr>
					<td >{PONumber}</td>
					<td class="alt">The Purchase Order on the asset</td>
				</tr>
				<tr>
					<td >{<i>CustomAttributeName</i>}</td>			
					<td >The value of the attribute specified<br></td>
				</tr>
			</tbody>
		</table>
	
		<p><br><b>Examples</b></p>
	
		<div style="border-style:solid;border-color:#DDD;border-width:1px;padding:3px;margin:3px 0px;">			
		{Identifier} ({RFID}), {Size} X {Length}, {Type}, {WorkingLoadLimit}
		</div>
	
		<p style="color:#999">	will display:</p>
		<p>SN421 (E00f01000089EE5B8), 1/4" X 42ft, BD2, 24,000lbs</p>
		
		<br>
		
		<div style="border-style:solid;border-color:#DDD;border-width:1px;padding:3px;margin:3px 0px;">			
			Manufactured by {Manufacturer} on {Birthdate} with a 2 year warranty
		</div>
		<p style="color:#999">will display :</p>			
		<p>Manufactured by ACME on 12/12/11 with a 2 year warranty</p>		
	
		<br>
	</div>
