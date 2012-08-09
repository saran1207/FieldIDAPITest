Work Assigned: ${event.type.name}

${event.asset.type.name} / ${event.asset.identifier} <#if event.asset.assetStatus?exists>/ ${event.asset.assetStatus.displayName}</#if>
${event.owner.displayName}

Due Date ${dueDateStringMap.get(event.id)}
Assignee ${event.assignee.fullName}