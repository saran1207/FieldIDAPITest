require "user"
class PutForeignKeysAndIndexesIntoMysql < ActiveRecord::Migration
  def self.up
    
    add_index "catalogs_inspectiontypes", ["publishedinspectiontypes_id"], :unique => true
    add_index "catalogs_inspectiontypes", ["catalogs_id", "publishedinspectiontypes_id"],  :unique => :true, :name => "index_unique_inspectiontypes_in_catalog"
    add_index "catalogs_producttypes", ["catalogs_id", "publishedproducttypes_id"],  :unique => :true, :name => "index_unique_producttypes_in_catalog"
    add_index "catalogs_producttypes", ["publishedproducttypes_id"],  :unique => :true
    add_index "criteriaresults_deficiencies", ["criteriaresults_id", "deficiencies_id"], :name => "index_results_deficiencies"
    add_index "criteriaresults_recommendations", ["criteriaresults_id", "recommendations_id"], :name => "index_results_recommendations"
    add_index "criteriasections_criteria", ["criteria_id"],  :unique => :true
    # ------- add_index "divisions", ["customer_id", "name"],  :unique => :true
    add_index "divisions", ["divisionid", "r_tenant"],  :unique => :true
    add_index "findproductoption", ["key"],  :unique => :true
    add_index "findproductoption_manufacture", ["r_findproductoption", "r_tenant"],  :unique => :true, :name => "index_find_product_options"
    add_index "inspections_fileattachments", ["attachments_id"],  :unique => :true
   add_index "inspectionschedules", ["inspection_inspection_id"],  :unique => :true
    add_index "inspectionsmaster", ["state"]
    add_index "inspectionsmaster_inspectionssub", ["subinspections_inspection_id"],  :unique => :true, :name => "index_imaster_inspectionssub_on_subs_inspection_id"
    add_index "inspectiontypes_criteriasections", ["sections_id"],  :unique => :true
    add_index "lineitems", ["index"]
    add_index "lineitems", ["productcode"]
    add_index "notificationsettings_owner", ["customer_id", "division_id", "notificationsettings_id"],  :unique => :true, :name => "index_notifictaion_customer_ownership"
    add_index "notificationsettings_owner", ["jobsite_id", "notificationsettings_id"],  :unique => :true, :name => "index_notification_jobsite_ownership"
    add_index "ordermapping", ["externalsourceid", "orderkey", "organizationid"],  :unique => :true, :name => "index_ordermapping_constraint" 
    add_index "organization", ["fidac"],  :unique => :true
    add_index "organization", ["name"],  :unique => :true
    add_index "printouts", ["type"]
    add_index "products", ["linkeduuid"]
    add_index "products", ["mobileguid"]
    add_index "products", ["purchaseorder"]
    
    add_index :products, [:r_tenant, :rfidnumber]
    add_index :products, [:r_tenant, :serialnumber]
    add_index :products, [:r_tenant, :customerrefnumber]
    
    add_index "products", ["state"]
    add_index "products", ["uuid"],  :unique => :true
    add_index "productserialextension", ["extensionkey", "r_tenant"],  :unique => :true, :name => "index_product_extension_constraint"
    add_index "productserialextensionvalue", ["r_productserial", "r_productserialextension"],  :unique => :true, :name => "index_product_extension_value_constraint"
    add_index "producttypegroups", ["name", "r_tenant"],  :unique => :true
    add_index "producttypes", ["name", "r_tenant"],  :unique => :true
    add_index "producttypes", ["state"]
    add_index "producttypes_fileattachments", ["attachments_id"],  :unique => :true
    add_index "projects", ["name"]
    add_index "projects", ["projectid", "r_tenant"],  :unique => :true
    add_index "projects", ["status"]
    add_index "projects_fileattachments", ["notes_id"],  :unique => :true
    add_index "projects_products", ["products_id", "projects_id"]
    add_index "projects_users", ["projects_id", "resources_uniqueid"],  :unique => :true
    add_index "requesttransactions", ["name"],  :unique => :true
    add_index "setupdatalastmoddates", ["r_tenant"],  :unique => :true
    add_index "statesets_states", ["states_id"],  :unique => :true
    add_index "tenantlink", ["r_linkedtenant", "r_manufacturer"],  :unique => :true
    
    fix_user_ids()
    
    
    add_index "users", ["r_tenant", "userid"],  :unique => :true
  
  
    add_foreign_key(:users, :customers, :source_column => :r_enduser, :foreign_column => :id)
    add_foreign_key(:divisions, :customers, :source_column => :customer_id, :foreign_column => :id)
    add_foreign_key(:infofield , :producttypes, :source_column => :r_productinfo, :foreign_column => :id)
    add_foreign_key(:products , :producttypes, :source_column => :type_id, :foreign_column => :id)
    add_foreign_key(:addproducthistory, :users, :source_column => :assigneduser_id, :foreign_column => :uniqueid)
    add_foreign_key(:addproducthistory  , :divisions, :source_column => :r_division, :foreign_column => :id)
    add_foreign_key(:addproducthistory_infooption , :addproducthistory, :source_column => :r_addproducthistory, :foreign_column => :uniqueid)
    add_foreign_key(:addproducthistory_infooption , :infooption, :source_column => :r_infooption, :foreign_column => :uniqueid)
    add_foreign_key(:addproducthistory , :jobsites, :source_column => :r_jobsite, :foreign_column => :id)
    add_foreign_key(:addproducthistory , :customers, :source_column => :r_owner, :foreign_column => :id)
    add_foreign_key(:addproducthistory , :productstatus, :source_column => :r_productstatus, :foreign_column => :uniqueid)
    add_foreign_key(:addproducthistory , :producttypes, :source_column => :r_producttype, :foreign_column => :id)
    add_foreign_key(:organization , :addressinfo, :source_column => :r_addressinfo, :foreign_column => :id)
    add_foreign_key(:alertstatus , :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:autoattributecriteria_inputinfofield , :autoattributecriteria, :source_column => :r_autoattributecriteria, :foreign_column => :id)
    add_foreign_key(:autoattributecriteria_inputinfofield , :infofield, :source_column => :r_infofield, :foreign_column => :uniqueid)
    add_foreign_key(:autoattributecriteria , :users, :source_column => :modifiedby, :foreign_column => :uniqueid)
    add_foreign_key(:autoattributecriteria_outputinfofield , :autoattributecriteria, :source_column => :r_autoattributecriteria, :foreign_column => :id)
    add_foreign_key(:autoattributecriteria_outputinfofield , :infofield, :source_column => :r_infofield, :foreign_column => :uniqueid)
    add_foreign_key(:autoattributecriteria , :producttypes, :source_column => :r_producttype, :foreign_column => :id)
    add_foreign_key(:autoattributecriteria , :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:autoattributedefinition_inputinfooption , :autoattributedefinition, :source_column => :r_autoattributedefinition, :foreign_column => :id)
    add_foreign_key(:autoattributedefinition_inputinfooption , :infooption, :source_column => :r_infooption, :foreign_column => :uniqueid)
    add_foreign_key(:autoattributedefinition , :users, :source_column => :modifiedby, :foreign_column => :uniqueid)
    add_foreign_key(:autoattributedefinition_outputinfooption , :autoattributedefinition, :source_column => :r_autoattributedefinition, :foreign_column => :id)
    add_foreign_key(:autoattributedefinition_outputinfooption , :infooption, :source_column => :r_infooption, :foreign_column => :uniqueid)
    add_foreign_key(:autoattributedefinition , :autoattributecriteria, :source_column => :r_autoattributecriteria, :foreign_column => :id)
    add_foreign_key(:autoattributedefinition , :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:catalogs_inspectiontypes , :catalogs, :source_column => :catalogs_id, :foreign_column => :id)
    add_foreign_key(:catalogs_inspectiontypes , :inspectiontypes, :source_column => :publishedinspectiontypes_id, :foreign_column => :id)
    add_foreign_key(:catalogs_producttypes , :catalogs, :source_column => :catalogs_id, :foreign_column => :id)
    add_foreign_key(:catalogs_producttypes, :producttypes, :source_column => :publishedproducttypes_id, :foreign_column => :id)
    add_foreign_key(:catalogs , :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:configurations , :users, :source_column => :modifiedby, :foreign_column => :uniqueid)
    add_foreign_key(:configurations, :organization, :source_column => :tenantid, :foreign_column => :id)
    add_foreign_key(:criteria_deficiencies , :criteria, :source_column => :criteria_id, :foreign_column => :id)
    add_foreign_key(:criteria , :users, :source_column => :modifiedby, :foreign_column => :uniqueid)
    add_foreign_key(:criteria , :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:criteria_recommendations , :criteria, :source_column => :criteria_id, :foreign_column => :id)
    add_foreign_key(:criteria , :statesets, :source_column => :states_id, :foreign_column => :id)
    add_foreign_key(:criteriaresults , :criteria, :source_column => :criteria_id, :foreign_column => :id)
    add_foreign_key(:criteriaresults_deficiencies , :criteriaresults, :source_column => :criteriaresults_id, :foreign_column => :id)
    add_foreign_key(:criteriaresults_deficiencies  , :observations, :source_column => :deficiencies_id, :foreign_column => :id)
    add_foreign_key(:criteriaresults, :inspections, :source_column => :inspection_id, :foreign_column => :id)
    add_foreign_key(:criteriaresults , :users, :source_column => :modifiedby, :foreign_column => :uniqueid)
    add_foreign_key(:criteriaresults , :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:criteriaresults_recommendations , :criteriaresults, :source_column => :criteriaresults_id, :foreign_column => :id)
    add_foreign_key(:criteriaresults_recommendations , :observations, :source_column => :recommendations_id, :foreign_column => :id)
    add_foreign_key(:criteriaresults , :states, :source_column => :state_id, :foreign_column => :id)
    add_foreign_key(:criteriasections_criteria , :criteria, :source_column => :criteria_id, :foreign_column => :id)
    add_foreign_key(:criteriasections_criteria , :criteriasections, :source_column => :criteriasections_id, :foreign_column => :id)
    add_foreign_key(:criteriasections , :users, :source_column => :modifiedby, :foreign_column => :uniqueid)
    add_foreign_key(:criteriasections , :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:customers , :addressinfo, :source_column => :addressinfo_id, :foreign_column => :id)
    add_foreign_key(:customers , :users, :source_column => :modifiedby, :foreign_column => :uniqueid)
    add_foreign_key(:divisions , :users, :source_column => :modifiedby, :foreign_column => :uniqueid)
    add_foreign_key(:divisions , :addressinfo, :source_column => :addressinfo_id, :foreign_column => :id)
    add_foreign_key(:eulaacceptances , :users, :source_column => :acceptor_uniqueid, :foreign_column => :uniqueid)
    add_foreign_key(:eulaacceptances , :eulas, :source_column => :eula_id, :foreign_column => :id)
    add_foreign_key(:eulaacceptances , :users, :source_column => :modifiedby, :foreign_column => :uniqueid, :name => "fk_eulaacceptances_modfied_user")
    add_foreign_key(:eulaacceptances , :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:fileattachments , :users, :source_column => :modifiedby, :foreign_column => :uniqueid)
    add_foreign_key(:fileattachments , :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:infofield , :unitofmeasures, :source_column => :r_unitofmeasure, :foreign_column => :id)
    add_foreign_key(:infooption , :infofield, :source_column => :r_infofield, :foreign_column => :uniqueid)
    add_foreign_key(:inspectionbooks , :customers, :source_column => :customer_id, :foreign_column => :id)
    add_foreign_key(:inspectionbooks , :users, :source_column => :modifiedby, :foreign_column => :uniqueid)
    add_foreign_key(:inspectionbooks , :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:inspectiongroups , :users, :source_column => :modifiedby, :foreign_column => :uniqueid)
    add_foreign_key(:inspectiongroups , :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:inspections_fileattachments , :fileattachments, :source_column => :attachments_id, :foreign_column => :id)
    add_foreign_key(:inspections_fileattachments , :inspections, :source_column => :inspections_id, :foreign_column => :id)
    add_foreign_key(:inspections_infooptionmap , :inspections, :source_column => :inspections_id, :foreign_column => :id)
    add_foreign_key(:inspections , :users, :source_column => :modifiedby, :foreign_column => :uniqueid)
    add_foreign_key(:inspections , :products, :source_column => :product_id, :foreign_column => :id)
    add_foreign_key(:inspections , :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:inspections , :inspectiontypes, :source_column => :type_id, :foreign_column => :id)
    add_foreign_key(:inspectionschedules , :inspectiontypes, :source_column => :inspectiontype_id, :foreign_column => :id)
    add_foreign_key(:inspectionschedules , :users, :source_column => :modifiedby, :foreign_column => :uniqueid)
    add_foreign_key(:inspectionschedules , :products, :source_column => :product_id, :foreign_column => :id)
    add_foreign_key(:inspectionschedules , :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:inspectionschedules , :customers, :source_column => :customer_id, :foreign_column => :id)
    add_foreign_key(:inspectionschedules , :divisions, :source_column => :division_id, :foreign_column => :id)
    add_foreign_key(:inspectionschedules , :jobsites, :source_column => :jobsite_id, :foreign_column => :id)
    add_foreign_key(:inspectionschedules , :projects, :source_column => :project_id, :foreign_column => :id)
    add_foreign_key(:inspectionsmaster , :inspectionbooks, :source_column => :book_id, :foreign_column => :id)
    add_foreign_key(:inspectionsmaster , :customers, :source_column => :customer_id, :foreign_column => :id)
    add_foreign_key(:inspectionsmaster , :divisions, :source_column => :division_id, :foreign_column => :id)
    add_foreign_key(:inspectionsmaster , :inspectiongroups, :source_column => :group_id, :foreign_column => :id)
    add_foreign_key(:inspectionsmaster , :inspections, :source_column => :inspection_id, :foreign_column => :id)
    add_foreign_key(:inspectionsmaster_inspectionssub , :inspections, :source_column => :inspectionsmaster_inspection_id, :foreign_column => :id, :name => "fk_masterinspections_inspection")
    add_foreign_key(:inspectionsmaster_inspectionssub , :inspections, :source_column => :subinspections_inspection_id, :foreign_column => :id, :name => "fk_subinspections_inspection")
    add_foreign_key(:inspectionsmaster , :users, :source_column => :inspector_uniqueid, :foreign_column => :uniqueid)
    add_foreign_key(:inspectionsmaster , :jobsites, :source_column => :jobsite_id, :foreign_column => :id)
    add_foreign_key(:inspectionsmaster , :organization, :source_column => :organization_id, :foreign_column => :id)
    add_foreign_key(:inspectionssub , :inspections, :source_column => :inspection_id, :foreign_column => :id)
    add_foreign_key(:inspectiontypegroups , :users, :source_column => :modifiedby, :foreign_column => :uniqueid)
    add_foreign_key(:inspectiontypegroups , :printouts, :source_column => :observationprintout_id, :foreign_column => :id, :name => "fk_inspectiontypegroups_observation_printouts")
    add_foreign_key(:inspectiontypegroups , :printouts, :source_column => :printout_id, :foreign_column => :id, :name => "fk_inspectiontypegroups_cert_printouts")
    add_foreign_key(:inspectiontypegroups , :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:inspectiontypes_criteriasections , :inspectiontypes, :source_column => :inspectiontypes_id, :foreign_column => :id)
    add_foreign_key(:inspectiontypes_criteriasections , :criteriasections, :source_column => :sections_id, :foreign_column => :id)
    add_foreign_key(:inspectiontypes , :inspectiontypegroups, :source_column => :group_id, :foreign_column => :id)
    add_foreign_key(:inspectiontypes_infofieldnames , :inspectiontypes, :source_column => :inspectiontypes_id, :foreign_column => :id)
    add_foreign_key(:inspectiontypes , :users, :source_column => :modifiedby, :foreign_column => :uniqueid)
    add_foreign_key(:inspectiontypes , :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:inspectiontypes_supportedprooftests , :inspectiontypes, :source_column => :inspectiontypes_id, :foreign_column => :id)
    
    add_foreign_key(:jobsites , :users, :source_column => :modifiedby, :foreign_column => :uniqueid)
    add_foreign_key(:jobsites , :customers, :source_column => :r_customer, :foreign_column => :id)
    add_foreign_key(:jobsites , :divisions, :source_column => :r_division, :foreign_column => :id)
    add_foreign_key(:jobsites , :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:legacybuttonstatemappings , :criteria, :source_column => :criteria_id, :foreign_column => :id)
    add_foreign_key(:legacybuttonstatemappings , :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:legacybuttonstatemappings , :states, :source_column => :state_id, :foreign_column => :id)
    add_foreign_key(:lineitems , :users, :source_column => :modifiedby, :foreign_column => :uniqueid)
    add_foreign_key(:lineitems , :orders, :source_column => :order_id, :foreign_column => :id)
    add_foreign_key(:addressinfo , :users, :source_column => :modifiedby, :foreign_column => :uniqueid)
    add_foreign_key(:organization , :users, :source_column => :modifiedby, :foreign_column => :uniqueid)
    add_foreign_key(:notificationsettings_addresses, :notificationsettings, :source_column => :notificationsettings_id, :foreign_column => :id)
    add_foreign_key(:notificationsettings_inspectiontypes, :notificationsettings, :source_column => :notificationsettings_id, :foreign_column => :id)
    add_foreign_key(:notificationsettings, :users, :source_column => :modifiedby, :foreign_column => :uniqueid)
    add_foreign_key(:notificationsettings_owner, :customers, :source_column => :customer_id, :foreign_column => :id)
    add_foreign_key(:notificationsettings_owner, :divisions, :source_column => :division_id, :foreign_column => :id)
    add_foreign_key(:notificationsettings_owner, :jobsites, :source_column => :jobsite_id, :foreign_column => :id)
    add_foreign_key(:notificationsettings_owner, :notificationsettings, :source_column => :notificationsettings_id, :foreign_column => :id)
    add_foreign_key(:notificationsettings_producttypes, :notificationsettings, :source_column => :notificationsettings_id, :foreign_column => :id)
    add_foreign_key(:notificationsettings, :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:notificationsettings, :users, :source_column => :user_id, :foreign_column => :uniqueid, :name => "fk_notificationsettings_users_owner")
    add_foreign_key(:observations, :users, :source_column => :modifiedby, :foreign_column => :uniqueid)
    add_foreign_key(:observations, :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:orders, :customers, :source_column => :customer_id, :foreign_column => :id)
    add_foreign_key(:orders, :divisions, :source_column => :division_id, :foreign_column => :id)
    add_foreign_key(:orders, :users, :source_column => :modifiedby, :foreign_column => :uniqueid)
    add_foreign_key(:orders, :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:organization , :organization, :source_column => :parent_id, :foreign_column => :id, :name => "fk_organization_parent")
    add_foreign_key(:organization , :organization, :source_column => :r_tenant, :foreign_column => :id, :name => "fk_organization_tenant")
    add_foreign_key(:organization_extendedfeatures, :organization, :source_column => :organization_id, :foreign_column => :id)
    add_foreign_key(:products , :customers, :source_column => :owner_id, :foreign_column => :id)
    
    add_foreign_key(:productserial_infooption , :infooption, :source_column => :r_infooption, :foreign_column => :uniqueid)
    add_foreign_key(:productserial_infooption , :products, :source_column => :r_productserial, :foreign_column => :id)
    add_foreign_key(:printouts, :users, :source_column => :modifiedby, :foreign_column => :uniqueid)
    add_foreign_key(:printouts, :organization, :source_column => :tenant_id, :foreign_column => :id)
    add_foreign_key(:productattachments, :products, :source_column => :product_id, :foreign_column => :id)
    add_foreign_key(:productcodemapping_infooption, :infooption, :source_column => :r_infooption, :foreign_column => :uniqueid)
    add_foreign_key(:productcodemapping_infooption, :productcodemapping, :source_column => :r_productcodemapping, :foreign_column => :uniqueid)
    add_foreign_key(:products, :users, :source_column => :assigneduser_id, :foreign_column => :uniqueid)
    add_foreign_key(:products, :orders, :source_column => :customerorder_id, :foreign_column => :id)
    add_foreign_key(:products, :lineitems, :source_column => :shoporder_id, :foreign_column => :id)
    add_foreign_key(:products, :divisions, :source_column => :division_id, :foreign_column => :id)
    add_foreign_key(:products, :users, :source_column => :identifiedby_uniqueid, :foreign_column => :uniqueid, :name => "fk_proudcts_identifiedby_user")
    add_foreign_key(:products, :users, :source_column => :modifiedby, :foreign_column => :uniqueid, :name => "fk_products_modified_by_user")
    add_foreign_key(:products, :productstatus, :source_column => :productstatus_uniqueid, :foreign_column => :uniqueid)
    add_foreign_key(:products, :jobsites, :source_column => :jobsite_id, :foreign_column => :id)
    add_foreign_key(:producttypes_inspectiontypes, :inspectiontypes, :source_column => :inspectiontypes_id, :foreign_column => :id)
    add_foreign_key(:producttypes_inspectiontypes, :producttypes, :source_column => :producttypes_id, :foreign_column => :id)
    add_foreign_key(:producttypes, :users, :source_column => :modifiedby, :foreign_column => :uniqueid)
    add_foreign_key(:producttypes_fileattachments, :fileattachments, :source_column => :attachments_id, :foreign_column => :id)
    add_foreign_key(:producttypes_fileattachments, :producttypes, :source_column => :producttypes_id, :foreign_column => :id)
    add_foreign_key(:producttypes, :producttypegroups, :source_column => :group_id, :foreign_column => :id)
    add_foreign_key(:producttypes_producttypes, :producttypes, :source_column => :producttypes_id, :foreign_column => :id, :name => "fk_producttypes_producttypes_master_type")
    add_foreign_key(:producttypes_producttypes, :producttypes, :source_column => :subtypes_id, :foreign_column => :id, :name => "fk_producttypes_producttypes_sub_type")
    add_foreign_key(:producttypeschedules, :inspectiontypes, :source_column => :inspectiontype_id, :foreign_column => :id)
    add_foreign_key(:producttypeschedules, :users, :source_column => :modifiedby, :foreign_column => :uniqueid)
    add_foreign_key(:producttypeschedules, :customers, :source_column => :customer_id, :foreign_column => :id)
    add_foreign_key(:producttypeschedules, :producttypes, :source_column => :producttype_id, :foreign_column => :id)
    add_foreign_key(:producttypeschedules, :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:projects, :customers, :source_column => :customer_id, :foreign_column => :id)
    add_foreign_key(:projects, :divisions, :source_column => :division_id, :foreign_column => :id)
    add_foreign_key(:projects_fileattachments, :fileattachments, :source_column => :notes_id, :foreign_column => :id)
    add_foreign_key(:projects_fileattachments, :projects, :source_column => :projects_id, :foreign_column => :id)
    add_foreign_key(:projects, :jobsites, :source_column => :jobsite_id, :foreign_column => :id)
    add_foreign_key(:projects_products, :products, :source_column => :products_id, :foreign_column => :id)
    add_foreign_key(:projects_products, :projects, :source_column => :projects_id, :foreign_column => :id)
    add_foreign_key(:projects , :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:projects_users, :projects, :source_column => :projects_id, :foreign_column => :id)
    add_foreign_key(:projects_users, :users, :source_column => :resources_uniqueid, :foreign_column => :uniqueid)
    add_foreign_key(:requesttransactions, :users, :source_column => :modifiedby, :foreign_column => :uniqueid)
    add_foreign_key(:requesttransactions, :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:savedreports_columns, :savedreports, :source_column => :savedreports_id, :foreign_column => :id)
    add_foreign_key(:savedreports_criteria, :savedreports, :source_column => :savedreports_id, :foreign_column => :id)
    add_foreign_key(:setupdatalastmoddates, :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:states, :users, :source_column => :modifiedby, :foreign_column => :uniqueid)
    add_foreign_key(:states, :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:statesets, :users, :source_column => :modifiedby, :foreign_column => :uniqueid)
    add_foreign_key(:statesets, :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:statesets_states, :states, :source_column => :states_id, :foreign_column => :id)
    add_foreign_key(:statesets_states, :statesets, :source_column => :statesets_id, :foreign_column => :id)
    add_foreign_key(:subproducts, :products, :source_column => :masterproduct_id, :foreign_column => :id, :name => "fk_subproducts_masterproduct")
    add_foreign_key(:subproducts, :products, :source_column => :product_id, :foreign_column => :id, :name => "fk_subproducts_subproduct")
    add_foreign_key(:tagoptions, :users, :source_column => :modifiedby, :foreign_column => :uniqueid)
    add_foreign_key(:tagoptions, :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:commenttemplate, :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:customers, :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:users, :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:populatorlog, :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:products, :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:productstatus, :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:producttypes, :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:findproductoption_manufacture, :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:productcodemapping, :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:serialnumbercounter, :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:tenantlink, :organization, :source_column => :r_linkedtenant, :foreign_column => :id, :name => "fk_tenantlink_linkedteant")
    add_foreign_key(:tenantlink, :organization, :source_column => :r_manufacturer, :foreign_column => :id, :name => "fk_tenantlink_manufacturer")
    add_foreign_key(:unitofmeasures, :unitofmeasures, :source_column => :child_unitofmeasure_id, :foreign_column => :id)
    add_foreign_key(:users, :divisions, :source_column => :r_division, :foreign_column => :id)
    add_foreign_key(:userrequest, :users, :source_column => :modifiedby, :foreign_column => :uniqueid, :name => "fk_userrequest_modifiedusers")
    add_foreign_key(:userrequest, :organization, :source_column => :r_tenant, :foreign_column => :id)
    add_foreign_key(:userrequest, :users, :source_column => :r_useraccount, :foreign_column => :uniqueid)
    add_foreign_key(:findproductoption_manufacture , :findproductoption, :source_column => :r_findproductoption, :foreign_column => :uniqueid)
    add_foreign_key(:productcodemapping , :producttypes, :source_column => :r_productinfo, :foreign_column => :id)
    add_foreign_key(:productserialextensionvalue , :productserialextension, :source_column => :r_productserialextension, :foreign_column => :uniqueid)
    add_foreign_key(:productserialextensionvalue , :products, :source_column => :r_productserial, :foreign_column => :id)
    add_foreign_key(:users, :organization, :source_column => :r_organization, :foreign_column => :id, :name => "fk_the_users_organization")

  end
  
  def self.down
  end


  private
    def self.fix_user_ids
      simple_user_map = { 'Endeavor Mine' =>15511453,
                   'peter' =>15511453,
                   'TIM.REDLAND'=>15511490,
                   'Quyen'=>216044,
                   'GOODRICH' =>216044,
                   'Michelle'=>15511480,
                   'Walter'=>15511453,
                   'BEAU'=>10802351,
                   'steve' =>15511453,
                   'Angela' => 216044,
                   'KIM.LONEY' =>15511490,
        }
        
        simple_user_map.each do |key, value|
          users = User.find(:all, :conditions => { :userid => key, :r_tenant => value })
          count = 1;
          users.each do |user|
            user.userid = user.userid.downcase
            if user.deleted 
              user.userid += count.to_s
            end
            puts user.userid
            user.save
            count += 1
          end
        end
        
        
        complex_user_map = { 'TEMP'=>4, 'henry goodrich'=>216044 }
        complex_user_map.each do |key, value|
          users = User.find(:all, :conditions => { :userid => key, :r_tenant => value })
          users.each do |user|
            if (user.userid == 'TEMP' || user.userid == 'Henry Goodrich')
              user.userid = user.userid + '1' 
            end
            user.userid = user.userid.downcase
            user.save
          end
      end
      
    end
end