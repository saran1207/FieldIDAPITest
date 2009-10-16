class ChangeCollation < ActiveRecord::Migration
  def self.up
    
    execute  "ALTER TABLE addproducthistory CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE addproducthistory_infooption CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE addressinfo CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE alertstatus CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE autoattributecriteria CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE autoattributecriteria_inputinfofield CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE autoattributecriteria_outputinfofield CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE autoattributedefinition CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE autoattributedefinition_inputinfooption CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE autoattributedefinition_outputinfooption CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE catalogs CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE catalogs_inspectiontypes CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE catalogs_producttypes CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE commenttemplate CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE configurations CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE criteria CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE criteria_deficiencies CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE criteria_recommendations CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE criteriaresults CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE criteriaresults_deficiencies CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE criteriaresults_recommendations CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE criteriasections CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE criteriasections_criteria CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE customers CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    remove_index(:divisions, :name => 'division_name_enduser')
    execute  "ALTER TABLE divisions CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE eulaacceptances CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE eulas CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE fileattachments CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE findproductoption CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE findproductoption_manufacture CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE infofield CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE infooption CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    remove_index(:inspectionbooks, :name=>"index_inspectionbooks_on_r_tenant_and_customer_uniqueid_and_nam")
    execute  "ALTER TABLE inspectionbooks CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE inspectiongroups CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE inspections CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE inspections_fileattachments CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE inspections_infooptionmap CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE inspectionschedules CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE inspectionsmaster CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE inspectionsmaster_inspectionssub CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE inspectionssub CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE inspectiontypegroups CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE inspectiontypes CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE inspectiontypes_criteriasections CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE inspectiontypes_infofieldnames CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE inspectiontypes_supportedprooftests CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE instructionalvideos CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE jobsites CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE legacybuttonstatemappings CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE lineitems CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE notificationsettings CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE notificationsettings_addresses CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE notificationsettings_inspectiontypes CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE notificationsettings_owner CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE notificationsettings_producttypes CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE observations CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE ordermapping CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE orders CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE organization CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE organization_extendedfeatures CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE populatorlog CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE printouts CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE productattachments CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE productcodemapping CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE productcodemapping_infooption CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE products CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE productserial_infooption CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE productserialextension CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE productserialextensionvalue CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE productstatus CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE producttypegroups CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE producttypes CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE producttypes_fileattachments CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE producttypes_inspectiontypes CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE producttypes_producttypes CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE producttypeschedules CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE projects CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE projects_fileattachments CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE projects_products CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE projects_users CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE requesttransactions CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE savedreports CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE savedreports_columns CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE savedreports_criteria CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE serialnumbercounter CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE setupdatalastmoddates CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE states CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE statesets CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE statesets_states CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE subproducts CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE tagoptions CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE tasks CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE tenantlink CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE unitofmeasures CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    execute  "ALTER TABLE userrequest CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    remove_index(:users, :name => "uniqueuseridrtenant")
    execute  "ALTER TABLE users CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci"
    
  end
  
  def self.down
  end
end
