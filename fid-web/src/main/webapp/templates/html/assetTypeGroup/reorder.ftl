<head>
    <script type="text/javascript">
        document.observe("dom:loaded", function() {
            jQuery("#groupList").sortable({
                update: function(event, ui) {
                    var rows = jQuery(this).sortable('toArray');
                    var params = new Object();
                    for (var i = 0; i < rows.size(); i++) {
                        params['indexes[' + i + ']'] = jQuery("#" + rows[i]).attr('groupid');
                    }
                    getResponse('<@s.url action="assetTypeGroupsUpdateOrder" namespace="/ajax"/>', 'post', params);
                }
            });
        });
    </script>

	<style>
		.drag {
			cursor: move;
		}
		#groupList li {
			padding: 10px 0;
			overflow:hidden;
			border-bottom: 1px solid #CCCCCC;
		}
		
		
		#groupList li div {
			float:left;
			padding: 0px 5px;
			margin-top: 2px;
		}
		
		#groupList li div.drag {
			margin: 0px;
		}
		
		div.formAction {
			text-align:left;
		}
	</style>
</head>
${action.setPageType('asset_type_group', 'list')!}
<title><@s.text name="title.reorderassettypegroups"/></title>
<div class="formAction">
	<button onclick="redirect('<@s.url action="assetTypeGroups"/>');"><@s.text name="label.donereordering"/></button>
</div>
<div class="pageSection">
	<h2><@s.text name="label.assettypegroups"/></h2>
	<ul id="groupList" class="contentSection">
		<#list groups as group > 
			<li id="sort_group_${group.id}" groupId="${group.id}" >
				<div class="drag" ><img src="<@s.url value="/images/reorder.png"/>" alt="<@s.text name="label.drag"/>"/></div>
				<div >${group.name?html}</div>
			</li>
		</#list>
	</ul>	
</div>
