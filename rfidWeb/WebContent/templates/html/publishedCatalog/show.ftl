${action.setPageType('safety_network_connections', 'import')!}
<head>
	<@n4.includeScript>
		var yourDoneUrl = '<@s.url action="connections" />';
		var yourCancelUrl = yourDoneUrl;
	</@n4.includeScript>
</head>

<#include "_show.ftl"/>


<div class="helpfulHints">
	<h3><@s.text name="label.where_do_i_start"/></h3>
	<p><@s.text name="label.where_do_i_start.full"/></p>

	<h3><@s.text name="label.what_will_be_imported"/></h3>
	<p><@s.text name="label.what_will_be_imported.full"/></p>

	<h3><@s.text name="label.will_this_overwrite_my_existing_setup"/></h3>
	<p><@s.text name="label.will_this_overwrite_my_existing_setup.full"/></p>

	<h3><@s.text name="label.how_do_i_know_if_the_import_was_successful"/></h3>
	<p><@s.text name="label.how_do_i_know_if_the_import_was_successful.full"/></p>
</div>


