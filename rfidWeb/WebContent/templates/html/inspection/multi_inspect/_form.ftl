<@s.form action="inspectionCreate" namespace="/multiInspect/ajax" id="createInspection">
	<@s.hidden name="type" value="${eventTypeId}"/>
	<@s.hidden name="scheduleId" value="0"/>

	<@s.hidden name="productId" id="productId"/>
	<@s.hidden name="location" id="location"/>
	<@s.hidden name="ownerId" id="ownerId"/>
	<@s.hidden name="productStatus" id="productStatusId"/>
<h2><@s.text name="label.inspectiondetails"/></h2>
	<p>
		<label><@s.text name="label.inspector"/></label>
		<span>
		
			<@s.select name="inspector" list="inspectors" listKey="id" listValue="name"  />
		</span>
	</p>
	<p>
		<label><@s.text name="label.inspectiondate"/></label>
		<span >
				<@s.datetimepicker id="inspectionDate" name="inspectionDate" theme="fieldidSimple"  type="dateTime"/>
		</span>	
	</p>
	
	
	<#include "/templates/html/inspectionCrud/_attributes.ftl"/>
	
	<h2><@s.text name="label.postinspectioninformation"/></h2>
	<p>
		<label><@s.text name="label.printable"/></label>
		<span>
			<@s.checkbox name="printable" /> <@s.text name="label.printableexplination"/>
		</span> 
	</p>
</@s.form>	