	<script type="text/javascript">
		function onDrop() {
			var rows = Sortable.sequence("groupList");
			var params = new Object();
			for (var i = 0; i < rows.size(); i++) {
				params['indexes[' + i + ']'] = rows[i];
			}
			
			getResponse('<@s.url action="productTypeGroupsUpdateOrder" namespace="/ajax"/>', 'post', params);
		}
		
	</script>
	<style>
		.drag {
			cursor: move;
		}
		#groupList li {
			padding: 10px 0;
			overflow:hidden;
			border-bottom: 1px solid #D0DAFD;
		}
		
		
		#groupList li div {
			float:left;
			padding: 0 15px;
		}
		
		
		div.formAction {
			text-align:left;
		}
	</style>
</head>
${action.setPageType('product_type_group', 'list')!}
<title><@s.text name="title.reorderproducttypegroups"/></title>
<div class="formAction">
	<button onclick="redirect('<@s.url action="productTypeGroups"/>');"><@s.text name="label.donereordering"/></button>
</div>
<div class="pageSection">
	<h2><@s.text name="label.producttypegroups"/></h2>
	<ul id="groupList" class="contentSection">
		<#list groups as group > 
			<li id="sort_group_${group.id}" groupId="${group.id}" >
				<div class="drag" ><img src="<@s.url value="/images/drag.gif"/>" alt="<@s.text name="label.drag"/>"/></div>
				<div >${group.name?html}</div>
			</li>
		</#list>
	</ul>	
</div>

<script type="text/javascript">
	Sortable.create("groupList", {handle: 'drag', onUpdate: onDrop});
</script>