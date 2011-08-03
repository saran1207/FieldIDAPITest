<title><@s.text name="title.massupdateevents" /></title>

<div class="crudForm largeForm bigForm pageSection">
	<h2><@s.text name="label.removaldetails"/></h2>
	
		<div class="infoSet">
			<label for="">${masterEventsToDelete}</label>
			<span><@s.text name="label.master_events_being_deleted"/></span>
		</div>
		
			<div class="infoSet">
			<label for="">${standardEventsToDelete}</label>
			<span><@s.text name="label.standard_events_being_deleted"/></span>
		</div>
		
		<div class="infoSet">
			<label for="">${eventSchedulesToDelete}</label>
			<span><@s.text name="label.schedulesbeingdeleted"/></span>
		</div>
	
		<div class="formAction">
		<@s.url id="cancelUrl" action="massUpdateEvents" searchId="${searchId}"/>
		<@s.url id="deleteUrl" action="massUpdateEventsDelete" searchId="${searchId}"/>
		
		<@s.submit key="label.delete" onclick="return redirect('${deleteUrl}');" theme="fieldid" />
		<@s.text name="label.or"/>
		<a href="#" onclick="return redirect('${cancelUrl}');"><@s.text name="label.cancel"/></a>
	</div>
</div>