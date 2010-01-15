<title><@s.text name="label.setup" /></title>
<head>
	<@n4.includeStyle type="page" href="setup" />
 </head>  
   <div class="viewSection setup">
		<h2 id="systemAccess"><@s.text name="label.systemaccessandsetup" /></h2>
		
		<#if sessionUser.hasAccess("managesystemconfig") == true >
			<p>
				<label><a href="<@s.url action="organizations"/>" ><@s.text name="title.manage_organizational_units.plural" /></a></label>
				<span >Create and manage departments and divsions within your company.</span>
			</p>
		</#if>
		<#if sessionUser.hasAccess("manageendusers") == true >
			<p>
				<label><a href="<@s.url action="customerList"/>" ><@s.text name="title.manage_customers.plural" /></a></label>
				<span >View and setup all of your <@s.text name='label.customers'/> in Field ID.</span>
			</p>
		</#if>
		
			
		<#if sessionUser.hasAccess("managesystemusers") == true >
			<p>
				<label><a href="userList.action" ><@s.text name="title.manage_users.plural" /></a></label>
				<span >View and setup all of your users in Field ID.</span>
			</p>
			<#if securityGuard.partnerCenterEnabled>
				<p>
					<label><a href="userRequestList.action" ><@s.text name="title.manage_user_registrations.plural" /></a></label>
					<span >Accept or deny requests for <@s.text name='label.customer'/> user accounts.</span>
				</p>
			</#if>
		</#if>
		
		<#if sessionUser.hasAccess("managesystemconfig") == true >
			<p>
				<label><a href="<@s.url action="systemSettingsEdit"/>" ><@s.text name="title.manage_system_settings.plural" /></a></label>
				<span>Find information about your system setup and adjust your company's branding.</span>
			</p>
		</#if>	
		
	</div>
	
	<#if sessionUser.hasAccess("managesystemconfig") == true >
		<div class="viewSection setup">
			<h2 id="yourProductSetup"><@s.text name="label.yourproductsetup" /></h2>
				<p>
					<label><a href="productTypes.action" ><@s.text name="title.manage_product_types.plural" /></a></label>
					<span>Manage all of your products and their attributes.</span>
				</p>
				<p>
					<label><a href="productTypeGroups.action" ><@s.text name="title.manage_product_type_groups.plural" /></a></label>
					<span>Manage all of your product type groups.</span>
				</p>
				<#if securityGuard.integrationEnabled>
					<p>
						<label><a href="productCodeMappingList.action" ><@s.text name="title.manage_product_code_mappings.plural" /></a></label>
						<span>Manage and map product information from your accounting or ERP system into Field ID.</span>
					</p>
				</#if>
			
			<p>
				<label><a href="productStatusList.action" ><@s.text name="title.manage_product_statuses.plural" /></a></label>
				<span>Create product statuses that let you identify the state of a product.</span>
			</p>
			<p>
				<label><a href="inspectionTypes.action" ><@s.text name="title.manage_inspection_types.plural" /></a></label>
				<span>Create inspection types to allow you to do different types of inspections.</span>
			</p>
			<#if sessionUser.hasAccess("managesystemconfig") == true >
				<p>
					<label><a href="<@s.url action="eventTypeGroups"/>" ><@s.text name="title.manage_event_type_groups.plural"/></a></label>
					<span >Create and manage event type groups to allow you to group inspection types and pick the certificate for those inspection types.</span>
				</p>
			</#if>
			
			<p>
				<label><a href="inspectionBooks.action" ><@s.text name="title.manage_inspection_books.plural" /></a></label>
				<span>Create inspection books to organize your inspections.</span>
			</p>
		</div>
	</#if>
	
	<#if sessionUser.hasAccess("managesystemconfig") == true >
		<div class="viewSection setup">
			<h2 id="notificationAndTemplates"><@s.text name="label.autocomplete_templates" /></h2>
			<p>
				<label><a href="autoAttributeCriteriaList.action" ><@s.text name="title.auto_attribute_wizard.plural" /></a></label>
				<span>Easily map variables to pre-defined values for products.</span>
			</p>
			<p>
				<label><a href="commentTemplateList.action" ><@s.text name="title.manage_comment_templates.plural" /></a></label>
				<span>Speed up the entry of comments by creating frequently used templates.</span>
			</p>
			
		</div>
	</#if>
	
	<#if sessionUser.hasAccess("managesystemconfig") == true >
		<div class="viewSection setup">
			<h2 id="dataSync"><@s.text name="label.datasync" /></h2>
			<p>
				<label><a href="<@s.url action="dataLog"/>" class="blue"><@s.text name="title.data_log" /></a></label>
				<span>
					Information about data from FieldID Mobile and proof test imports.
				</span>
			</p>
		</div>
			
	</#if>
	
	
