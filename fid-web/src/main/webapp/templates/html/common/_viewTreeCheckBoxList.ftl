<@n4.includeStyle href="viewTree" />
<#macro drawTreeNode node firstNode foundNonEmptyNode>
	<ul class="viewTreeNode">
		<#assign nodeId=node.nodeId />
		
		<#if foundNonEmptyNode>
			<#assign foundNonEmptyNodePassthrough=true />
			<#assign openStyle="" />
			<#assign closeStyle="display:none;" />
		<#else>
			<#if (node.elements.size() > 0)>
				<#assign foundNonEmptyNodePassthrough=true />
				<#assign openStyle="display:none;" />
				<#assign closeStyle="" />
			<#else>
				<#assign foundNonEmptyNodePassthrough=false />
				<#assign openStyle="display:none;" />
				<#assign closeStyle="" />
			</#if>
		</#if>
		
		<#if !firstNode>
			<h2 class="viewTreeNodeTitle">
				<a href="javascript:void(0);" id="exp_${nodeId}" onclick="openSection( 'node_${nodeId}', 'exp_${nodeId}', 'col_${nodeId}');return false" style="${openStyle}"><img src="<@s.url value="/images/expand.gif" includeParams="none"/>" /></a>
				<a href="javascript:void(0);" id="col_${nodeId}" onclick="closeSection('node_${nodeId}', 'col_${nodeId}', 'exp_${nodeId}');return false" style="${closeStyle}"><img src="<@s.url value="/images/collapse.gif" includeParams="none"/>" /></a>
				${node.nodeName}
			</h2>
			<div id="node_${nodeId}" style="${closeStyle}">
		<#else>
			<h2 class="viewTreeNodeTitle">${node.nodeName}</h2>
			<div id="node_${nodeId}">
		</#if>
			<#list node.elements as element> 
				<li class="viewTreeNodeItem">
					<@s.checkbox id="${checkBoxListName}_${element.id}" name="${checkBoxListName}" fieldValue="${element.id}" theme="simple"/>
					<label id="lbl_${checkBoxListName}_${element.id}" for="${checkBoxListName}_${element.id}"><@s.text name="${element.displayName}"/></label>
				</li>
			</#list>
			<#list node.childNodes as child>
	  			<li class="viewTreeNodeItem"><@drawTreeNode node=child firstNode=false foundNonEmptyNode=foundNonEmptyNodePassthrough /></li>
			</#list>
		</div>
	</ul>
</#macro>

<div class="viewTreeContainer">
	<@drawTreeNode node=viewTree firstNode=true foundNonEmptyNode=false />
</div>