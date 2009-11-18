
<h2><@s.text name="label.postinspectioninformation"/></h2>
<p>
	<label><@s.text name="label.comments"/></label>
	<span>
		<@s.select name="commentTemplate" list="commentTemplates" listKey="id" listValue="displayName" emptyOption="true" onchange="changeComments(this)"/> 
		
		<@s.textarea name="comments" id="comments"  cols="50" rows="3"/>
	</span>
	
</p>

<#if action.isParentProduct() >
	
	<p>
		<label><@s.text name="label.printable"/></label>
		<span>
			<@s.checkbox name="printable" /> <@s.text name="label.printableexplination"/>
		</span> 
	</p>
	
	<p>
		<label><@s.text name="label.productstatus"/></label>
		<span>
			<@s.select name="productStatus" list="productStatuses" listKey="uniqueID" listValue="name" headerKey="" headerValue="" />
		</span>
	</p>
		
	<#if !inspection.id?exists >
		<p>
			<label><@s.text name="label.nextinspectiondate"/>:</label>
			
			<span class="date">
				<@s.datetimepicker id="nextDate" name="nextInspectionDate" theme="fieldidSimple"/>
			</span>
		</p>
	</#if>
</#if>