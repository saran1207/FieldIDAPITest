  add_index "addproducthistory", ["assigneduser_id"], :name => "index_addproducthistory_on_assigneduser_id"
  add_index "addproducthistory", ["r_division"], :name => "index_addproducthistory_on_r_division"
  add_index "addproducthistory", ["r_fieldiduser"], :name => "addproducthistory_oneuser", :unique => true
  add_index "addproducthistory", ["r_jobsite"], :name => "index_addproducthistory_on_r_jobsite"
  add_index "addproducthistory", ["r_owner"], :name => "index_addproducthistory_on_r_owner"
  add_index "addproducthistory", ["r_productstatus"], :name => "index_addproducthistory_on_r_productstatus"
  add_index "addproducthistory", ["r_producttype"], :name => "index_addproducthistory_on_r_producttype"
  add_index "addproducthistory_infooption", ["r_addproducthistory"], :name => "index_addproducthistory_infooption_on_r_addproducthistory"
  add_index "addproducthistory_infooption", ["r_infooption"], :name => "index_addproducthistory_infooption_on_r_infooption"
  add_index "addressinfo", ["modifiedby"], :name => "index_addressinfo_on_modifiedby"
  add_index "autoattributecriteria", ["modifiedby"], :name => "index_autoattributecriteria_on_modifiedby"
  add_index "autoattributecriteria", ["r_producttype"], :name => "index_autoattributecriteria_on_r_producttype"
  add_index "autoattributecriteria", ["r_tenant"], :name => "index_autoattributecriteria_on_r_tenant"
  add_index "autoattributecriteria_inputinfofield", ["r_autoattributecriteria"], :name => "index_autoattributecriteria_inputinfofield_on_r_autoattributecr"
  add_index "autoattributecriteria_inputinfofield", ["r_infofield"], :name => "index_autoattributecriteria_inputinfofield_on_r_infofield"
  add_index "autoattributecriteria_outputinfofield", ["r_autoattributecriteria"], :name => "index_autoattributecriteria_outputinfofield_on_r_autoattributec"
  add_index "autoattributecriteria_outputinfofield", ["r_infofield"], :name => "index_autoattributecriteria_outputinfofield_on_r_infofield"
  add_index "autoattributedefinition", ["modifiedby"], :name => "index_autoattributedefinition_on_modifiedby"
  add_index "autoattributedefinition", ["r_autoattributecriteria"], :name => "index_autoattributedefinition_on_r_autoattributecriteria"
  add_index "autoattributedefinition", ["r_tenant"], :name => "index_autoattributedefinition_on_r_tenant"
  add_index "autoattributedefinition_inputinfooption", ["r_autoattributedefinition"], :name => "index_autoattributedefinition_inputinfooption_on_r_autoattribut"
  add_index "autoattributedefinition_inputinfooption", ["r_infooption"], :name => "index_autoattributedefinition_inputinfooption_on_r_infooption"
  add_index "autoattributedefinition_outputinfooption", ["r_autoattributedefinition"], :name => "index_autoattributedefinition_outputinfooption_on_r_autoattribu"
  add_index "autoattributedefinition_outputinfooption", ["r_infooption"], :name => "index_autoattributedefinition_outputinfooption_on_r_infooption"
  add_index "catalogs", ["r_tenant"], :name => "index_catalogs_on_r_tenant", :unique => true
  add_index "catalogs_inspectiontypes", ["catalogs_id", "publishedinspectiontypes_id"], :name => "index_catalogs_inspectiontypes_on_catalogs_id_and_publishedinsp", :unique => true
  add_index "catalogs_inspectiontypes", ["catalogs_id"], :name => "index_catalogs_inspectiontypes_on_catalogs_id"
  add_index "catalogs_inspectiontypes", ["publishedinspectiontypes_id"], :name => "index_catalogs_inspectiontypes_on_publishedinspectiontypes_id", :unique => true
  add_index "catalogs_producttypes", ["catalogs_id", "publishedproducttypes_id"], :name => "index_catalogs_producttypes_on_catalogs_id_and_publishedtypes_i", :unique => true
  add_index "catalogs_producttypes", ["catalogs_id"], :name => "index_catalogs_producttypes_on_catalogs_id"
  add_index "catalogs_producttypes", ["publishedproducttypes_id"], :name => "index_catalogs_producttypes_on_publishedproducttypes_id"
  add_index "catalogs_producttypes", ["publishedproducttypes_id"], :name => "index_catalogs_producttypes_on_publishedtypes_id", :unique => true
  add_index "commenttemplate", ["r_tenant"], :name => "index_commenttemplate_on_r_tenant"
  add_index "commenttemplate", ["templateid"], :name => "commenttemplate_idx"
  add_index "configurations", ["modifiedby"], :name => "index_configurations_on_modifiedby"
  add_index "configurations", ["tenantid"], :name => "index_configurations_on_tenantid"
  add_index "criteria", ["modifiedby"], :name => "index_criteria_on_modifiedby"
  add_index "criteria", ["r_tenant"], :name => "index_criteria_on_r_tenant"
  add_index "criteria", ["states_id"], :name => "index_criteria_on_states_id"
  add_index "criteriaresults", ["criteria_id"], :name => "index_criteriaresults_on_criteria_id"
  add_index "criteriaresults", ["inspection_id"], :name => "index_criteriaresults_on_inspection_id"
  add_index "criteriaresults", ["modifiedby"], :name => "index_criteriaresults_on_modifiedby"
  add_index "criteriaresults", ["r_tenant"], :name => "index_criteriaresults_on_r_tenant"
  add_index "criteriaresults", ["state_id"], :name => "index_criteriaresults_on_state_id"
  add_index "criteriaresults_deficiencies", ["criteriaresults_id", "deficiencies_id"], :name => "index_criteriaresults_deficiencies_on_criteriaresults_id_and_de"
  add_index "criteriaresults_deficiencies", ["criteriaresults_id"], :name => "index_criteriaresults_deficiencies_on_criteriaresults_id"
  add_index "criteriaresults_deficiencies", ["deficiencies_id"], :name => "index_criteriaresults_deficiencies_on_deficiencies_id"
  add_index "criteriaresults_recommendations", ["criteriaresults_id", "recommendations_id"], :name => "index_criteriaresults_recommendations_on_criteriaresults_id_and"
  add_index "criteriaresults_recommendations", ["criteriaresults_id"], :name => "index_criteriaresults_recommendations_on_criteriaresults_id"
  add_index "criteriaresults_recommendations", ["recommendations_id"], :name => "index_criteriaresults_recommendations_on_recommendations_id"
  add_index "criteriasections", ["modifiedby"], :name => "index_criteriasections_on_modifiedby"
  add_index "criteriasections", ["r_tenant"], :name => "index_criteriasections_on_r_tenant"
  add_index "criteriasections_criteria", ["criteria_id"], :name => "criteriasections_criteria_criteria_id_key", :unique => true
  add_index "criteriasections_criteria", ["criteria_id"], :name => "index_criteriasections_criteria_on_criteria_id"
  add_index "customers", ["addressinfo_id"], :name => "index_customers_on_addressinfo_id"
  add_index "customers", ["modifiedby"], :name => "index_customers_on_modifiedby"
  add_index "customers", ["r_tenant"], :name => "index_customers_on_r_tenant"
  add_index "divisions", ["customer_id", "name"], :name => "division_name_enduser", :unique => true
  add_index "divisions", ["customer_id"], :name => "index_divisions_on_customer_id"
  add_index "divisions", ["modifiedby"], :name => "index_divisions_on_modifiedby"
  add_index "fileattachments", ["modifiedby"], :name => "index_fileattachments_on_modifiedby"
  add_index "fileattachments", ["r_tenant"], :name => "index_fileattachments_on_r_tenant"
  add_index "findproductoption", ["key"], :name => "findproductoption_uniquekey_type", :unique => true
  add_index "findproductoption_manufacture", ["r_findproductoption", "r_tenant"], :name => "fpm_unique", :unique => true
  add_index "infofield", ["r_productinfo"], :name => "index_infofield_on_r_productinfo"
  add_index "infofield", ["r_unitofmeasure"], :name => "index_infofield_on_r_unitofmeasure"
  add_index "infooption", ["r_infofield"], :name => "index_infooption_on_r_infofield"
  add_index "inspectionbooks", ["customer_id", "name", "r_tenant"], :name => "index_inspectionbooks_on_r_tenant_and_customer_uniqueid_and_nam", :unique => true
  add_index "inspectionbooks", ["customer_id"], :name => "index_inspectionbooks_on_customer_id"
  add_index "inspectionbooks", ["modifiedby"], :name => "index_inspectionbooks_on_modifiedby"
  add_index "inspectionbooks", ["r_tenant"], :name => "index_inspectionbooks_on_r_tenant"
  add_index "inspectiongroups", ["modifiedby"], :name => "index_inspectiongroups_on_modifiedby"
  add_index "inspectiongroups", ["r_tenant"], :name => "index_inspectiongroups_on_r_tenant"
  add_index "inspections", ["modifiedby"], :name => "index_inspections_on_modifiedby"
  add_index "inspections", ["product_id"], :name => "index_inspections_on_product_id"
  add_index "inspections", ["r_tenant"], :name => "index_inspections_on_r_tenant"
  add_index "inspections", ["type_id"], :name => "index_inspections_on_type_id"
  add_index "inspections_fileattachments", ["attachments_id"], :name => "index_inspections_fileattachments_on_attachments_id"
  add_index "inspections_fileattachments", ["attachments_id"], :name => "inspections_fileattachments_attachments_id_key", :unique => true
  add_index "inspections_fileattachments", ["inspections_id"], :name => "index_inspections_fileattachments_on_inspections_id"
  add_index "inspectionschedules", ["customer_id"], :name => "index_inspectionschedules_on_customer_id"
  add_index "inspectionschedules", ["division_id"], :name => "index_inspectionschedules_on_division_id"
  add_index "inspectionschedules", ["inspection_inspection_id"], :name => "index_inspectionschedules_on_inspection_inspection_id", :unique => true
  add_index "inspectionschedules", ["inspectiontype_id"], :name => "index_inspectionschedules_on_inspectiontype_id"
  add_index "inspectionschedules", ["jobsite_id"], :name => "index_inspectionschedules_on_jobsite_id"
  add_index "inspectionschedules", ["modifiedby"], :name => "index_inspectionschedules_on_modifiedby"
  add_index "inspectionschedules", ["product_id"], :name => "index_inspectionschedules_on_product_id"
  add_index "inspectionschedules", ["project_id"], :name => "index_inspectionschedules_on_project_id"
  add_index "inspectionschedules", ["r_tenant"], :name => "index_inspectionschedules_on_r_tenant"
  add_index "inspectionsmaster", ["customer_id"], :name => "index_inspectionsmaster_on_customer_id"
  add_index "inspectionsmaster", ["division_id"], :name => "index_inspectionsmaster_on_division_id"
  add_index "inspectionsmaster", ["group_id"], :name => "index_inspectionsmaster_on_group_id"
  add_index "inspectionsmaster", ["inspection_id"], :name => "index_inspectionsmaster_on_inspection_id"
  add_index "inspectionsmaster", ["inspector_uniqueid"], :name => "index_inspectionsmaster_on_inspector_uniqueid"
  add_index "inspectionsmaster", ["jobsite_id"], :name => "index_inspectionsmaster_on_jobsite_id"
  add_index "inspectionsmaster", ["organization_id"], :name => "index_inspectionsmaster_on_organization_id"
  add_index "inspectionsmaster", ["state"], :name => "index_inspectionsmaster_on_state"
  add_index "inspectionsmaster_inspectionssub", ["subinspections_inspection_id"], :name => "index_inspectionsmaster_inspectionssub_on_subinspections_inspec", :unique => true
  add_index "inspectionssub", ["inspection_id"], :name => "index_inspectionssub_on_inspection_id"
  add_index "inspectiontypegroups", ["modifiedby"], :name => "index_inspectiontypegroups_on_modifiedby"
  add_index "inspectiontypegroups", ["observationprintout_id"], :name => "index_inspectiontypegroups_on_observationprintout_id"
  add_index "inspectiontypegroups", ["printout_id"], :name => "index_inspectiontypegroups_on_printout_id"
  add_index "inspectiontypegroups", ["r_tenant"], :name => "index_inspectiontypegroups_on_r_tenant"
  add_index "inspectiontypes", ["group_id"], :name => "index_inspectiontypes_on_group_id"
  add_index "inspectiontypes", ["modifiedby"], :name => "index_inspectiontypes_on_modifiedby"
  add_index "inspectiontypes", ["r_tenant"], :name => "index_inspectiontypes_on_r_tenant"
  add_index "inspectiontypes_criteriasections", ["sections_id"], :name => "index_inspectiontypes_criteriasections_on_sections_id"
  add_index "inspectiontypes_criteriasections", ["sections_id"], :name => "inspectiontypes_criteriasections_sections_id_key", :unique => true
  add_index "inspectiontypes_supportedprooftests", ["inspectiontypes_id"], :name => "index_inspectiontypes_supportedprooftests_on_inspectiontypes_id"
  add_index "jobsites", ["modifiedby"], :name => "index_jobsites_on_modifiedby"
  add_index "jobsites", ["r_customer"], :name => "index_jobsites_on_r_customer"
  add_index "jobsites", ["r_division"], :name => "index_jobsites_on_r_division"
  add_index "jobsites", ["r_tenant"], :name => "index_jobsites_on_r_tenant"
  add_index "lineitems", ["index"], :name => "index_lineitems_on_index"
  add_index "lineitems", ["lineid"], :name => "index_lineitems_on_lineid"
  add_index "lineitems", ["modified"], :name => "index_lineitems_on_modified"
  add_index "lineitems", ["order_id"], :name => "index_lineitems_on_order_id"
  add_index "lineitems", ["productcode"], :name => "index_lineitems_on_productcode"
  add_index "notificationsettings", ["modifiedby"], :name => "index_notificationsettings_on_modifiedby"
  add_index "notificationsettings", ["r_tenant"], :name => "index_notificationsettings_on_r_tenant"
  add_index "notificationsettings", ["user_id"], :name => "index_notificationsettings_on_user_id"
  add_index "notificationsettings_addresses", ["notificationsettings_id"], :name => "index_notificationsettings_addresses_on_notificationsettings_id"
  add_index "notificationsettings_inspectiontypes", ["notificationsettings_id"], :name => "index_notificationsettings_inspectiontypes_on_notificationsetti"
  add_index "notificationsettings_owner", ["customer_id", "division_id", "notificationsettings_id"], :name => "unique_notificationsettings_owner_customer_division", :unique => true
  add_index "notificationsettings_owner", ["customer_id"], :name => "index_notificationsettings_owner_on_customer_id"
  add_index "notificationsettings_owner", ["division_id"], :name => "index_notificationsettings_owner_on_division_id"
  add_index "notificationsettings_owner", ["jobsite_id", "notificationsettings_id"], :name => "unique_notificationsettings_owner_jobsite", :unique => true
  add_index "notificationsettings_owner", ["jobsite_id"], :name => "index_notificationsettings_owner_on_jobsite_id"
  add_index "notificationsettings_owner", ["notificationsettings_id"], :name => "index_notificationsettings_owner_on_notificationsettings_id"
  add_index "notificationsettings_producttypes", ["notificationsettings_id"], :name => "index_notificationsettings_producttypes_on_notificationsettings"
  add_index "observations", ["modifiedby"], :name => "index_observations_on_modifiedby"
  add_index "observations", ["r_tenant"], :name => "index_observations_on_r_tenant"
  add_index "ordermapping", ["externalsourceid", "orderkey", "organizationid"], :name => "ordermapping_uniquekeys_idx", :unique => true
  add_index "ordermapping", ["externalsourceid", "organizationid"], :name => "ordermapping_quicklookup"
  add_index "orders", ["customer_id"], :name => "index_orders_on_customer_id"
  add_index "orders", ["division_id"], :name => "index_orders_on_division_id"
  add_index "orders", ["modifiedby"], :name => "index_orders_on_modifiedby"
  add_index "orders", ["ordernumber"], :name => "index_orders_on_ordernumber"
  add_index "orders", ["ordertype"], :name => "index_orders_on_ordertype"
  add_index "orders", ["r_tenant"], :name => "index_orders_on_r_tenant"
  add_index "organization", ["fidac"], :name => "index_organization_on_snac", :unique => true
  add_index "organization", ["modifiedby"], :name => "index_organization_on_modifiedby"
  add_index "organization", ["name"], :name => "index_organization_on_name", :unique => true
  add_index "organization", ["parent_id"], :name => "index_organization_on_parent_id"
  add_index "organization", ["r_addressinfo"], :name => "index_organization_on_r_addressinfo"
  add_index "organization", ["r_tenant"], :name => "index_organization_on_r_tenant"
  add_index "organization_extendedfeatures", ["organization_id"], :name => "index_organization_extendedfeatures_on_organization_id"
  add_index "populatorlog", ["r_tenant"], :name => "index_populatorlog_on_r_tenant"
  add_index "printouts", ["custom"], :name => "index_printouts_on_custom"
  add_index "printouts", ["modifiedby"], :name => "index_printouts_on_modifiedby"
  add_index "printouts", ["tenant_id"], :name => "index_printouts_on_tenant_id"
  add_index "printouts", ["type"], :name => "index_printouts_on_type"
  add_index "productcodemapping", ["r_productinfo"], :name => "index_productcodemapping_on_r_productinfo"
  add_index "productcodemapping_infooption", ["r_infooption"], :name => "index_productcodemapping_infooption_on_r_infooption"
  add_index "productcodemapping_infooption", ["r_productcodemapping"], :name => "index_productcodemapping_infooption_on_r_productcodemapping"
  add_index "products", ["assigneduser_id"], :name => "index_products_on_assigneduser_id"
  add_index "products", ["customerorder_id"], :name => "index_products_on_customerorder_id"
  add_index "products", ["division_id"], :name => "index_products_on_division_id"
  add_index "products", ["id"], :name => "productserial_uniqueid_idx", :unique => true
  add_index "products", ["identifiedby_uniqueid"], :name => "index_products_on_identifiedby_uniqueid"
  add_index "products", ["jobsite_id"], :name => "index_products_on_jobsite_id"
  add_index "products", ["linkeduuid"], :name => "index_productserial_on_linkeduuid"
  add_index "products", ["mobileguid"], :name => "index_products_on_mobileguid"
  add_index "products", ["modifiedby"], :name => "index_products_on_modifiedby"
  add_index "products", ["organization_id"], :name => "index_products_on_organization_id"
  add_index "products", ["owner_id"], :name => "index_products_on_owner_id"
  add_index "products", ["productstatus_uniqueid"], :name => "index_products_on_productstatus_uniqueid"
  add_index "products", ["purchaseorder"], :name => "index_productserial_on_purchaseorder"
  add_index "products", ["rfidnumber"], :name => "index_products_on_rfidnumber"
  add_index "products", ["serialnumber"], :name => "index_products_on_serialnumber"
  add_index "products", ["shoporder_id"], :name => "index_products_on_shoporder_id"
  add_index "products", ["shoporder_id"], :name => "productserial_rordermaster_idx"
  add_index "products", ["state"], :name => "index_products_on_state"
  add_index "products", ["type_id"], :name => "index_products_on_type_id"
  add_index "products", ["uuid"], :name => "index_productserial_on_uuid", :unique => true
  add_index "productserial_infooption", ["r_infooption"], :name => "index_productserial_infooption_on_r_infooption"
  add_index "productserial_infooption", ["r_productserial"], :name => "index_productserial_infooption_on_r_productserial"
  add_index "productserialextension", ["extensionkey", "r_tenant"], :name => "productserialextension_keypermanufacture", :unique => true
  add_index "productserialextension", ["r_tenant"], :name => "index_productserialextension_on_r_tenant"
  add_index "productserialextensionvalue", ["r_productserial", "r_productserialextension"], :name => "productserialextensionvalue_oneperproduct", :unique => true
  add_index "productserialextensionvalue", ["r_productserial"], :name => "index_productserialextensionvalue_on_r_productserial"
  add_index "productserialextensionvalue", ["r_productserialextension"], :name => "index_productserialextensionvalue_on_r_productserialextension"
  add_index "productstatus", ["r_tenant"], :name => "index_productstatus_on_r_tenant"
  add_index "producttypegroups", ["name", "r_tenant"], :name => "index_producttypegroups_on_r_tenant_and_name", :unique => true
  add_index "producttypes", ["group_id"], :name => "index_producttypes_on_group_id"
  add_index "producttypes", ["id"], :name => "productinfo_uniqueid_idx"
  add_index "producttypes", ["modifiedby"], :name => "index_producttypes_on_modifiedby"
  add_index "producttypes", ["name", "r_tenant"], :name => "productinfo_itemnumber_key", :unique => true
  add_index "producttypes", ["r_tenant"], :name => "index_producttypes_on_r_tenant"
  add_index "producttypes", ["state"], :name => "index_producttypes_on_state"
  add_index "producttypes_fileattachments", ["attachments_id"], :name => "index_producttypes_fileattachments_on_attachments_id"
  add_index "producttypes_fileattachments", ["attachments_id"], :name => "producttypes_fileattachments_attachments_id_key", :unique => true
  add_index "producttypes_fileattachments", ["producttypes_id"], :name => "index_producttypes_fileattachments_on_producttypes_id"
  add_index "producttypes_producttypes", ["subtypes_id"], :name => "index_producttypes_producttypes_on_subtypes_id"
  add_index "producttypeschedules", ["customer_id"], :name => "index_producttypeschedules_on_customer_id"
  add_index "producttypeschedules", ["inspectiontype_id"], :name => "index_producttypeschedules_on_inspectiontype_id"
  add_index "producttypeschedules", ["modifiedby"], :name => "index_producttypeschedules_on_modifiedby"
  add_index "producttypeschedules", ["producttype_id"], :name => "index_producttypeschedules_on_producttype_id"
  add_index "producttypeschedules", ["r_tenant"], :name => "index_producttypeschedules_on_r_tenant"
  add_index "projects", ["customer_id"], :name => "index_projects_on_customer_id"
  add_index "projects", ["division_id"], :name => "index_projects_on_division_id"
  add_index "projects", ["jobsite_id"], :name => "index_projects_on_jobsite_id"
  add_index "projects", ["name"], :name => "index_projects_on_name"
  add_index "projects", ["projectid", "r_tenant"], :name => "index_projects_on_r_tenant_and_projectid", :unique => true
  add_index "projects", ["r_tenant"], :name => "index_projects_on_r_tenant"
  add_index "projects", ["status"], :name => "index_projects_on_status"
  add_index "projects_fileattachments", ["notes_id"], :name => "index_projects_fileattachments_on_notes_id"
  add_index "projects_fileattachments", ["notes_id"], :name => "projects_fileattachments_notes_id_key", :unique => true
  add_index "projects_products", ["products_id", "projects_id"], :name => "index_projects_products_on_projects_id_and_products_id"
  add_index "projects_products", ["products_id"], :name => "index_projects_products_on_products_id"
  add_index "projects_products", ["projects_id"], :name => "index_projects_products_on_projects_id"
  add_index "projects_users", ["projects_id", "resources_uniqueid"], :name => "index_projects_users_on_projects_id_and_resources_uniqueid", :unique => true
  add_index "projects_users", ["projects_id"], :name => "index_projects_users_on_projects_id"
  add_index "projects_users", ["resources_uniqueid"], :name => "index_projects_users_on_resources_uniqueid"
  add_index "requesttransactions", ["name"], :name => "index_requesttransactions_on_name", :unique => true
  add_index "savedreports", ["modifiedby"], :name => "index_savedreports_on_modifiedby"
  add_index "savedreports", ["owner_uniqueid"], :name => "index_savedreports_on_owner_uniqueid"
  add_index "savedreports", ["r_tenant"], :name => "index_savedreports_on_r_tenant"
  add_index "savedreports_columns", ["savedreports_id"], :name => "index_savedreports_columns_on_savedreports_id"
  add_index "serialnumbercounter", ["r_tenant"], :name => "one_counter_per_man", :unique => true
  add_index "states", ["modifiedby"], :name => "index_states_on_modifiedby"
  add_index "states", ["r_tenant"], :name => "index_states_on_r_tenant"
  add_index "statesets", ["modifiedby"], :name => "index_statesets_on_modifiedby"
  add_index "statesets", ["r_tenant"], :name => "index_statesets_on_r_tenant"
  add_index "statesets_states", ["states_id"], :name => "index_statesets_states_on_states_id"
  add_index "statesets_states", ["states_id"], :name => "statesets_states_states_id_key", :unique => true
  add_index "subproducts", ["masterproduct_id"], :name => "index_subproducts_on_masterproduct_id"
  add_index "tenantlink", ["r_linkedtenant", "r_manufacturer"], :name => "index_tenantlink_on_r_manufacturer_and_r_linkedtenant", :unique => true
  add_index "unitofmeasures", ["child_unitofmeasure_id"], :name => "index_unitofmeasures_on_child_unitofmeasure_id"
  add_index "userrequest", ["modifiedby"], :name => "index_userrequest_on_modifiedby"
  add_index "userrequest", ["r_tenant"], :name => "index_userrequest_on_r_tenant"
  add_index "userrequest", ["r_useraccount"], :name => "index_userrequest_on_r_useraccount"
  add_index "users", ["r_division"], :name => "index_users_on_r_division"
  add_index "users", ["r_enduser"], :name => "fki_f_enduser"
  add_index "users", ["r_enduser"], :name => "index_users_on_r_enduser"
  add_index "users", ["r_organization"], :name => "index_users_on_r_organization"
  add_index "users", ["r_tenant", "userid"], :name => "fieldiduser_idx"
  add_index "users", ["r_tenant", "userid"], :name => "uniqueuseridrtenant", :unique => true
  add_index "users", ["r_tenant"], :name => "index_users_on_r_tenant"
