<div id="eventSchedules" class="infoSet reducedPaddingInfoSet">
	<ul id="eventSchedulesList">
		<@s.iterator value="webEventSchedules" id="eventSchedule" status="stat" >
			<li id="eventSchedule_${stat.index}">
				<@s.text name="message.asset_event_schedule">
					<@s.param>${eventSchedule.typeName!}</@s.param>
					<@s.param>${eventSchedule.date!}</@s.param>
				</@s.text>		
				|
				<a onclick="removeSchedule(this);"><@s.text name="label.remove"/></a>		
				<@s.hidden id="webEventSchedule_${stat.index}_type" name="webEventSchedules[${stat.index}].type" value="${eventSchedule.type!}" />
				<@s.hidden id="webEventSchedule_${stat.index}_typeName" name="webEventSchedules[${stat.index}].typeName" value="${eventSchedule.typeName!}" />
				<@s.hidden id="webEventSchedule_${stat.index}_date" name="webEventSchedules[${stat.index}].date" value="${eventSchedule.date!}" />
				<@s.hidden id="webEventSchedule_${stat.index}_job" name="webEventSchedules[${stat.index}].job" value="${eventSchedule.job!}" />
				<@s.hidden id="webEventSchedule_${stat.index}_jobName" name="webEventSchedules[${stat.index}].jobName" value="${eventSchedule.jobName!}" />
			</li>
		</@s.iterator>
	</ul>
</div>

<script type="text/javascript">

	function removeSchedule(target) {
		var listItem = $(target.parentNode.id);
		var list = $(target.parentNode.parentNode.id);	
		var reindex = false;
		var index = 0;
		list.childElements().each(function(s){
			if(reindex) {
				reIndexEvent(s, index, index-1);
			} 				
			if(s == listItem) {
				s.remove();
				reindex = true;
			}
			index++;
		});
	}
	
	function reIndexEvent(node, oldIndex, newIndex) {
		renameElement( $("webEventSchedule_" +  oldIndex + "_type"), newIndex, "type");
		renameElement( $("webEventSchedule_" +  oldIndex + "_typeName"), newIndex, "typeName");
		renameElement( $("webEventSchedule_" +  oldIndex + "_date"), newIndex, "date");
		renameElement( $("webEventSchedule_" +  oldIndex + "_job"), newIndex, "job");
		renameElement( $("webEventSchedule_" +  oldIndex + "_jobName"), newIndex, "jobName");
		node.id = "eventSchedule_" + newIndex;
	}
	
	function renameElement(node, index, name) {
		node.id = "webEventSchedule_" +  index + "_" + name;
		node.name = "webEventSchedules[" + index + "]." + name;
	}

</script>