${action.setPageType('event_type_group', 'show')!}

<head>
    <@n4.includeStyle href="/style/legacy/newCss/component/buttons.css" type="page"/>
    <style type="text/css">
        .sectionContent, .crudForm .infoSet {
            overflow: visible;
        }
    </style>
</head>

<div class="crudForm bigForm pageSection">
	<h2><@s.text name="label.groupdetails"/> <a href="<@s.url action="eventTypeGroupEdit" uniqueID="${group.id}"/>"><@s.text name="label.littleedit"/></a></h2>
	<div class="sectionContent">
		<div class="infoSet">
			<label for="name"><@s.text name="label.name"/></label>
			<span>${(group.name?html) !}</span>
		</div>
		<div class="infoSet">
			<label for="reporttitle"><@s.text name="label.reporttitle"/></label>
			<span>${(group.reportTitle?html) !}</span>
		</div>
		
		<div class="infoSet">
			<label for="reportstyle"><@s.text name="label.reportstyle"/></label>
			<span>${(group.printOut.name?html)!action.getText("label.none")}</span>
		</div>
		<div class="infoSet">
			<label for="observationreportstyle"><@s.text name="label.observationreportstyle"/></label>
			<span>${(group.observationPrintOut.name?html)!action.getText("label.none")}</span>
		</div>
	</div>
</div>

<div class="pageSection">
	<h2 class="decoratedHeader"><@s.text name="label.eventtypes"/></h2>
	<div class="crudForm bigForm sectionContent">
		
		<#list eventTypes as eventType>
			<div class="infoSet">
				<label class="line" ><a href="<@s.url action="eventType" uniqueID="${eventType.id}"/>">${(eventType.name?html) !}</a></label>
			</div>
		</#list>
		<#if eventTypes.empty >
			<div class="infoSet">
				<label class="line"><@s.text name="label.noeventtypesundergroup"/></label>
			</div>
		</#if>	
		<div class="infoSet">
            <div class="dropdown-btn">
                <a class="btn btn-secondary btn-sml" href="#">Add Event Type<img src="/fieldid/images/menu-down.png"></a>
                <ul class="pull-right">
                    <li><a href="/fieldid/eventTypeAdd.action?newEventType=Asset&group=${group.id}">Asset Event</a></li>
                    <li><a href="/fieldid/eventTypeAdd.action?newEventType=Place&group=${group.id}">Place Event</a></li>
                    <li><a href="/fieldid/eventTypeAdd.action?newEventType=Action&group=${group.id}">Action</a></li>
                </ul>
            </div>
		</div>
		 
	</div>
</div>

