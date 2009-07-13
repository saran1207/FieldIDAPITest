# This file is auto-generated from the current state of the database. Instead of editing this file, 
# please use the migrations feature of Active Record to incrementally modify your database, and
# then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your database schema. If you need
# to create the application database on another system, you should be using db:schema:load, not running
# all the migrations from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended to check this file into your version control system.

ActiveRecord::Schema.define(:version => 200906251718) do

  create_table "addproducthistory", :primary_key => "uniqueid", :force => true do |t|
    t.integer "r_fieldiduser",   :limit => 8, :null => false
    t.integer "r_division",      :limit => 8
    t.integer "r_producttype",   :limit => 8
    t.integer "r_owner",         :limit => 8
    t.integer "r_productstatus", :limit => 8
    t.string  "purchaseorder"
    t.string  "location"
    t.integer "r_jobsite"
    t.integer "assigneduser_id"
  end

  add_index "addproducthistory", ["assigneduser_id"], :name => "index_addproducthistory_on_assigneduser_id"
  add_index "addproducthistory", ["r_division"], :name => "index_addproducthistory_on_r_division"
  add_index "addproducthistory", ["r_fieldiduser"], :name => "addproducthistory_oneuser", :unique => true
  add_index "addproducthistory", ["r_jobsite"], :name => "index_addproducthistory_on_r_jobsite"
  add_index "addproducthistory", ["r_owner"], :name => "index_addproducthistory_on_r_owner"
  add_index "addproducthistory", ["r_productstatus"], :name => "index_addproducthistory_on_r_productstatus"
  add_index "addproducthistory", ["r_producttype"], :name => "index_addproducthistory_on_r_producttype"

  create_table "addproducthistory_infooption", :force => true do |t|
    t.integer "r_addproducthistory", :limit => 8, :null => false
    t.integer "r_infooption",        :limit => 8, :null => false
  end

  add_index "addproducthistory_infooption", ["r_addproducthistory"], :name => "index_addproducthistory_infooption_on_r_addproducthistory"
  add_index "addproducthistory_infooption", ["r_infooption"], :name => "index_addproducthistory_infooption_on_r_infooption"

  create_table "addressinfo", :force => true do |t|
    t.timestamp "created"
    t.timestamp "modified"
    t.string   "streetaddress"
    t.string   "city"
    t.string   "state"
    t.string   "country"
    t.string   "zip"
    t.string   "phone1"
    t.string   "fax1"
    t.integer  "modifiedby",    :limit => 8
    t.string   "phone2"
  end

  add_index "addressinfo", ["modifiedby"], :name => "index_addressinfo_on_modifiedby"

  create_table "autoattributecriteria", :force => true do |t|
    t.integer  "r_producttype", :limit => 8, :null => false
    t.integer  "r_tenant",      :limit => 8, :null => false
    t.timestamp "created"
    t.timestamp "modified"
    t.integer  "modifiedby",    :limit => 8
  end

  add_index "autoattributecriteria", ["modifiedby"], :name => "index_autoattributecriteria_on_modifiedby"
  add_index "autoattributecriteria", ["r_producttype"], :name => "index_autoattributecriteria_on_r_producttype"
  add_index "autoattributecriteria", ["r_tenant"], :name => "index_autoattributecriteria_on_r_tenant"

  create_table "autoattributecriteria_inputinfofield", :force => true do |t|
    t.integer "r_infofield",             :limit => 8, :null => false
    t.integer "r_autoattributecriteria", :limit => 8, :null => false
  end

  add_index "autoattributecriteria_inputinfofield", ["r_autoattributecriteria"], :name => "index_autoattributecriteria_inputinfofield_on_r_autoattributecr"
  add_index "autoattributecriteria_inputinfofield", ["r_infofield"], :name => "index_autoattributecriteria_inputinfofield_on_r_infofield"

  create_table "autoattributecriteria_outputinfofield", :force => true do |t|
    t.integer "r_infofield",             :limit => 8, :null => false
    t.integer "r_autoattributecriteria", :limit => 8, :null => false
  end

  add_index "autoattributecriteria_outputinfofield", ["r_autoattributecriteria"], :name => "index_autoattributecriteria_outputinfofield_on_r_autoattributec"
  add_index "autoattributecriteria_outputinfofield", ["r_infofield"], :name => "index_autoattributecriteria_outputinfofield_on_r_infofield"

  create_table "autoattributedefinition", :force => true do |t|
    t.integer  "r_autoattributecriteria", :limit => 8, :null => false
    t.integer  "r_tenant",                :limit => 8, :null => false
    t.timestamp "created"
    t.timestamp "modified"
    t.integer  "modifiedby",              :limit => 8
  end

  add_index "autoattributedefinition", ["modifiedby"], :name => "index_autoattributedefinition_on_modifiedby"
  add_index "autoattributedefinition", ["r_autoattributecriteria"], :name => "index_autoattributedefinition_on_r_autoattributecriteria"
  add_index "autoattributedefinition", ["r_tenant"], :name => "index_autoattributedefinition_on_r_tenant"

  create_table "autoattributedefinition_inputinfooption", :force => true do |t|
    t.integer "r_infooption",              :limit => 8, :null => false
    t.integer "r_autoattributedefinition", :limit => 8, :null => false
  end

  add_index "autoattributedefinition_inputinfooption", ["r_autoattributedefinition"], :name => "index_autoattributedefinition_inputinfooption_on_r_autoattribut"
  add_index "autoattributedefinition_inputinfooption", ["r_infooption"], :name => "index_autoattributedefinition_inputinfooption_on_r_infooption"

  create_table "autoattributedefinition_outputinfooption", :force => true do |t|
    t.integer "r_infooption",              :limit => 8, :null => false
    t.integer "r_autoattributedefinition", :limit => 8, :null => false
  end

  add_index "autoattributedefinition_outputinfooption", ["r_autoattributedefinition"], :name => "index_autoattributedefinition_outputinfooption_on_r_autoattribu"
  add_index "autoattributedefinition_outputinfooption", ["r_infooption"], :name => "index_autoattributedefinition_outputinfooption_on_r_infooption"

  create_table "catalogs", :force => true do |t|
    t.timestamp "created",    :null => false
    t.timestamp "modified",   :null => false
    t.integer  "modifiedby"
    t.integer  "r_tenant",   :null => false
  end

  add_index "catalogs", ["r_tenant"], :name => "index_catalogs_on_r_tenant", :unique => true

  create_table "catalogs_inspectiontypes", :id => false, :force => true do |t|
    t.integer "catalogs_id",                 :null => false
    t.integer "publishedinspectiontypes_id", :null => false
  end

  add_index "catalogs_inspectiontypes", ["catalogs_id", "publishedinspectiontypes_id"], :name => "index_catalogs_inspectiontypes_on_catalogs_id_and_publishedinsp", :unique => true
  add_index "catalogs_inspectiontypes", ["catalogs_id"], :name => "index_catalogs_inspectiontypes_on_catalogs_id"
  add_index "catalogs_inspectiontypes", ["publishedinspectiontypes_id"], :name => "index_catalogs_inspectiontypes_on_publishedinspectiontypes_id", :unique => true

  create_table "catalogs_producttypes", :id => false, :force => true do |t|
    t.integer "catalogs_id",              :null => false
    t.integer "publishedproducttypes_id", :null => false
  end

  add_index "catalogs_producttypes", ["catalogs_id", "publishedproducttypes_id"], :name => "index_catalogs_producttypes_on_catalogs_id_and_publishedtypes_i", :unique => true
  add_index "catalogs_producttypes", ["catalogs_id"], :name => "index_catalogs_producttypes_on_catalogs_id"
  add_index "catalogs_producttypes", ["publishedproducttypes_id"], :name => "index_catalogs_producttypes_on_publishedproducttypes_id"
  add_index "catalogs_producttypes", ["publishedproducttypes_id"], :name => "index_catalogs_producttypes_on_publishedtypes_id", :unique => true

  create_table "commenttemplate", :primary_key => "uniqueid", :force => true do |t|
    t.timestamp "datecreated"  
    t.timestamp "datemodified" 
    t.string    "modifiedby",   :limit => 50
    t.string    "templateid",   :limit => 50
    t.string    "contents"
    t.integer   "r_tenant",     :limit => 8
  end

  add_index "commenttemplate", ["r_tenant"], :name => "index_commenttemplate_on_r_tenant"
  add_index "commenttemplate", ["templateid"], :name => "commenttemplate_idx"

  create_table "configurations", :force => true do |t|
    t.timestamp "created",                 :null => false
    t.timestamp "modified",                :null => false
    t.integer  "modifiedby", :limit => 8
    t.string   "key",                     :null => false
    t.string   "value",                   :null => false
    t.integer  "tenantid",   :limit => 8
  end

  add_index "configurations", ["modifiedby"], :name => "index_configurations_on_modifiedby"
  add_index "configurations", ["tenantid"], :name => "index_configurations_on_tenantid"

  create_table "criteria", :force => true do |t|
    t.integer  "r_tenant",    :limit => 8,                    :null => false
    t.timestamp "created",                                     :null => false
    t.timestamp "modified",                                    :null => false
    t.integer  "modifiedby",  :limit => 8
    t.string   "displaytext",                                 :null => false
    t.integer  "states_id",   :limit => 8,                    :null => false
    t.boolean  "principal",                :default => false, :null => false
    t.boolean  "retired",                  :default => false
  end

  add_index "criteria", ["modifiedby"], :name => "index_criteria_on_modifiedby"
  add_index "criteria", ["r_tenant"], :name => "index_criteria_on_r_tenant"
  add_index "criteria", ["states_id"], :name => "index_criteria_on_states_id"

  create_table "criteria_deficiencies", :id => false, :force => true do |t|
    t.integer "criteria_id", :limit => 8,   :null => false
    t.string  "text",        :limit => 511, :null => false
    t.integer "orderidx",                   :null => false
  end

  create_table "criteria_recommendations", :id => false, :force => true do |t|
    t.integer "criteria_id", :limit => 8,   :null => false
    t.string  "text",        :limit => 511, :null => false
    t.integer "orderidx",                   :null => false
  end

  create_table "criteriaresults", :force => true do |t|
    t.integer  "r_tenant",      :limit => 8, :null => false
    t.timestamp "created",                    :null => false
    t.timestamp "modified",                   :null => false
    t.integer  "modifiedby",    :limit => 8
    t.integer  "state_id",      :limit => 8, :null => false
    t.integer  "criteria_id",   :limit => 8, :null => false
    t.integer  "inspection_id", :limit => 8, :null => false
  end

  add_index "criteriaresults", ["criteria_id"], :name => "index_criteriaresults_on_criteria_id"
  add_index "criteriaresults", ["inspection_id"], :name => "index_criteriaresults_on_inspection_id"
  add_index "criteriaresults", ["modifiedby"], :name => "index_criteriaresults_on_modifiedby"
  add_index "criteriaresults", ["r_tenant"], :name => "index_criteriaresults_on_r_tenant"
  add_index "criteriaresults", ["state_id"], :name => "index_criteriaresults_on_state_id"

  create_table "criteriaresults_deficiencies", :id => false, :force => true do |t|
    t.integer "criteriaresults_id", :limit => 8, :null => false
    t.integer "deficiencies_id",    :limit => 8, :null => false
    t.integer "orderidx",                        :null => false
  end

  add_index "criteriaresults_deficiencies", ["criteriaresults_id", "deficiencies_id"], :name => "index_criteriaresults_deficiencies_on_criteriaresults_id_and_de"
  add_index "criteriaresults_deficiencies", ["criteriaresults_id"], :name => "index_criteriaresults_deficiencies_on_criteriaresults_id"
  add_index "criteriaresults_deficiencies", ["deficiencies_id"], :name => "index_criteriaresults_deficiencies_on_deficiencies_id"

  create_table "criteriaresults_recommendations", :id => false, :force => true do |t|
    t.integer "criteriaresults_id", :limit => 8, :null => false
    t.integer "recommendations_id", :limit => 8, :null => false
    t.integer "orderidx",                        :null => false
  end

  add_index "criteriaresults_recommendations", ["criteriaresults_id", "recommendations_id"], :name => "index_criteriaresults_recommendations_on_criteriaresults_id_and"
  add_index "criteriaresults_recommendations", ["criteriaresults_id"], :name => "index_criteriaresults_recommendations_on_criteriaresults_id"
  add_index "criteriaresults_recommendations", ["recommendations_id"], :name => "index_criteriaresults_recommendations_on_recommendations_id"

  create_table "criteriasections", :force => true do |t|
    t.integer  "r_tenant",   :limit => 8,                    :null => false
    t.timestamp "created",                                    :null => false
    t.timestamp "modified",                                   :null => false
    t.integer  "modifiedby", :limit => 8
    t.string   "title",                                      :null => false
    t.boolean  "retired",                 :default => false
  end

  add_index "criteriasections", ["modifiedby"], :name => "index_criteriasections_on_modifiedby"
  add_index "criteriasections", ["r_tenant"], :name => "index_criteriasections_on_r_tenant"

  create_table "criteriasections_criteria", :id => false, :force => true do |t|
    t.integer "criteriasections_id", :limit => 8, :null => false
    t.integer "criteria_id",         :limit => 8, :null => false
    t.integer "orderidx",                         :null => false
  end

  add_index "criteriasections_criteria", ["criteria_id"], :name => "criteriasections_criteria_criteria_id_key", :unique => true
  add_index "criteriasections_criteria", ["criteria_id"], :name => "index_criteriasections_criteria_on_criteria_id"

  create_table "customers", :force => true do |t|
    t.timestamp "created"       
    t.timestamp "modified"       
    t.string    "name",                        :null => false
    t.integer   "r_tenant",       :limit => 8
    t.string    "contactemail"
    t.string    "customerid",                  :null => false
    t.integer   "modifiedby"
    t.string    "contactname"
    t.integer   "addressinfo_id"
  end

  add_index "customers", ["addressinfo_id"], :name => "index_customers_on_addressinfo_id"
  add_index "customers", ["modifiedby"], :name => "index_customers_on_modifiedby"
  add_index "customers", ["r_tenant"], :name => "index_customers_on_r_tenant"

  create_table "divisions", :force => true do |t|
    t.string   "name"
    t.integer  "customer_id", :limit => 8, :null => false
    t.timestamp "created"
    t.timestamp "modified"
    t.integer  "modifiedby"
    t.integer  "r_tenant",                 :null => false
  end

  add_index "divisions", ["customer_id", "name"], :name => "division_name_enduser", :unique => true
  add_index "divisions", ["customer_id"], :name => "index_divisions_on_customer_id"
  add_index "divisions", ["modifiedby"], :name => "index_divisions_on_modifiedby"

  create_table "fileattachments", :force => true do |t|
    t.integer  "r_tenant",   :limit => 8, :null => false
    t.timestamp "created",                 :null => false
    t.timestamp "modified",                :null => false
    t.integer  "modifiedby", :limit => 8
    t.string   "filename"
    t.text     "comments"
  end

  add_index "fileattachments", ["modifiedby"], :name => "index_fileattachments_on_modifiedby"
  add_index "fileattachments", ["r_tenant"], :name => "index_fileattachments_on_r_tenant"

  create_table "findproductoption", :primary_key => "uniqueid", :force => true do |t|
    t.string "key"
    t.string "value"
    t.text   "description"
  end

  add_index "findproductoption", ["key"], :name => "findproductoption_uniquekey_type", :unique => true

  create_table "findproductoption_manufacture", :primary_key => "uniqueid", :force => true do |t|
    t.integer   "r_findproductoption", :limit => 8,                :null => false
    t.integer   "r_tenant",            :limit => 8,                :null => false
    t.integer   "weight",              :limit => 8,                :null => false
    t.integer   "mobileweight",                     :default => 0
    t.timestamp "datemodified"        
    t.timestamp  "datecreated"
  end

  add_index "findproductoption_manufacture", ["r_findproductoption", "r_tenant"], :name => "fpm_unique", :unique => true

  create_table "infofield", :primary_key => "uniqueid", :force => true do |t|
    t.string  "name",                             :null => false
    t.integer "r_productinfo",      :limit => 8,  :null => false
    t.boolean "required"
    t.integer "weight",             :limit => 8
    t.integer "r_unitofmeasure",    :limit => 8
    t.boolean "usingunitofmeasure"
    t.string  "fieldtype",          :limit => 50
    t.boolean "retired"
  end

  add_index "infofield", ["r_productinfo"], :name => "index_infofield_on_r_productinfo"
  add_index "infofield", ["r_unitofmeasure"], :name => "index_infofield_on_r_unitofmeasure"

  create_table "infooption", :primary_key => "uniqueid", :force => true do |t|
    t.string  "name",                     :null => false
    t.integer "r_infofield", :limit => 8, :null => false
    t.boolean "staticdata"
    t.integer "weight",      :limit => 8
  end

  add_index "infooption", ["r_infofield"], :name => "index_infooption_on_r_infofield"

  create_table "inspectionbooks", :force => true do |t|
    t.integer  "r_tenant",    :limit => 8, :null => false
    t.timestamp "created",                  :null => false
    t.timestamp "modified",                 :null => false
    t.integer  "legacyid",    :limit => 8
    t.integer  "modifiedby",  :limit => 8
    t.string   "name",                     :null => false
    t.integer  "customer_id", :limit => 8
    t.boolean  "open",                     :null => false
  end

  add_index "inspectionbooks", ["customer_id", "name", "r_tenant"], :name => "index_inspectionbooks_on_r_tenant_and_customer_uniqueid_and_nam", :unique => true
  add_index "inspectionbooks", ["customer_id"], :name => "index_inspectionbooks_on_customer_id"
  add_index "inspectionbooks", ["modifiedby"], :name => "index_inspectionbooks_on_modifiedby"
  add_index "inspectionbooks", ["r_tenant"], :name => "index_inspectionbooks_on_r_tenant"

  create_table "inspectiongroups", :force => true do |t|
    t.integer  "r_tenant",   :limit => 8, :null => false
    t.timestamp "created",                 :null => false
    t.timestamp "modified",                :null => false
    t.integer  "modifiedby", :limit => 8
    t.string   "mobileguid"
  end

  add_index "inspectiongroups", ["modifiedby"], :name => "index_inspectiongroups_on_modifiedby"
  add_index "inspectiongroups", ["r_tenant"], :name => "index_inspectiongroups_on_r_tenant"

  create_table "inspections", :force => true do |t|
    t.integer  "r_tenant",    :limit => 8,    :null => false
    t.timestamp "created",                     :null => false
    t.timestamp "modified",                    :null => false
    t.integer  "modifiedby",  :limit => 8
    t.string   "comments",    :limit => 2500
    t.integer  "type_id",     :limit => 8,    :null => false
    t.integer  "product_id",  :limit => 8,    :null => false
    t.integer  "formversion",                 :null => false
  end

  add_index "inspections", ["modifiedby"], :name => "index_inspections_on_modifiedby"
  add_index "inspections", ["product_id"], :name => "index_inspections_on_product_id"
  add_index "inspections", ["r_tenant"], :name => "index_inspections_on_r_tenant"
  add_index "inspections", ["type_id"], :name => "index_inspections_on_type_id"

  create_table "inspections_fileattachments", :id => false, :force => true do |t|
    t.integer "inspections_id", :limit => 8, :null => false
    t.integer "attachments_id", :limit => 8, :null => false
  end

  add_index "inspections_fileattachments", ["attachments_id"], :name => "index_inspections_fileattachments_on_attachments_id"
  add_index "inspections_fileattachments", ["attachments_id"], :name => "inspections_fileattachments_attachments_id_key", :unique => true
  add_index "inspections_fileattachments", ["inspections_id"], :name => "index_inspections_fileattachments_on_inspections_id"

  create_table "inspections_infooptionmap", :id => false, :force => true do |t|
    t.integer "inspections_id", :limit => 8, :null => false
    t.string  "element"
    t.string  "mapkey",                      :null => false
  end

  create_table "inspectionschedules", :force => true do |t|
    t.integer  "r_tenant",                 :limit => 8, :null => false
    t.timestamp "created",                               :null => false
    t.timestamp "modified",                              :null => false
    t.integer  "modifiedby",               :limit => 8
    t.integer  "product_id",               :limit => 8, :null => false
    t.timestamp "nextdate",                              :null => false
    t.integer  "inspectiontype_id"
    t.timestamp "completeddate"
    t.string   "status",                                :null => false
    t.integer  "inspection_inspection_id"
    t.string   "state",                                 :null => false
    t.integer  "project_id"
    t.integer  "division_id"
    t.integer  "customer_id"
    t.integer  "jobsite_id"
    t.string   "location"
  end

  add_index "inspectionschedules", ["customer_id"], :name => "index_inspectionschedules_on_customer_id"
  add_index "inspectionschedules", ["division_id"], :name => "index_inspectionschedules_on_division_id"
  add_index "inspectionschedules", ["inspection_inspection_id"], :name => "index_inspectionschedules_on_inspection_inspection_id", :unique => true
  add_index "inspectionschedules", ["inspectiontype_id"], :name => "index_inspectionschedules_on_inspectiontype_id"
  add_index "inspectionschedules", ["jobsite_id"], :name => "index_inspectionschedules_on_jobsite_id"
  add_index "inspectionschedules", ["modifiedby"], :name => "index_inspectionschedules_on_modifiedby"
  add_index "inspectionschedules", ["product_id"], :name => "index_inspectionschedules_on_product_id"
  add_index "inspectionschedules", ["project_id"], :name => "index_inspectionschedules_on_project_id"
  add_index "inspectionschedules", ["r_tenant"], :name => "index_inspectionschedules_on_r_tenant"

  create_table "inspectionsmaster", :id => false, :force => true do |t|
    t.integer  "inspection_id",      :limit => 8, :null => false
    t.string   "location"
    t.timestamp "date",                            :null => false
    t.boolean  "printable",                       :null => false
    t.string   "prooftesttype"
    t.string   "peakload"
    t.string   "duration"
    t.string   "status",                          :null => false
    t.integer  "division_id",        :limit => 8
    t.integer  "inspector_uniqueid", :limit => 8, :null => false
    t.integer  "group_id",           :limit => 8, :null => false
    t.integer  "book_id",            :limit => 8
    t.integer  "customer_id",        :limit => 8
    t.integer  "jobsite_id",         :limit => 8
    t.integer  "organization_id",    :limit => 8, :null => false
    t.string   "state",                           :null => false
    t.string   "peakloadduration"
  end

  add_index "inspectionsmaster", ["customer_id"], :name => "index_inspectionsmaster_on_customer_id"
  add_index "inspectionsmaster", ["division_id"], :name => "index_inspectionsmaster_on_division_id"
  add_index "inspectionsmaster", ["group_id"], :name => "index_inspectionsmaster_on_group_id"
  add_index "inspectionsmaster", ["inspection_id"], :name => "index_inspectionsmaster_on_inspection_id"
  add_index "inspectionsmaster", ["inspector_uniqueid"], :name => "index_inspectionsmaster_on_inspector_uniqueid"
  add_index "inspectionsmaster", ["jobsite_id"], :name => "index_inspectionsmaster_on_jobsite_id"
  add_index "inspectionsmaster", ["organization_id"], :name => "index_inspectionsmaster_on_organization_id"
  add_index "inspectionsmaster", ["state"], :name => "index_inspectionsmaster_on_state"

  create_table "inspectionsmaster_inspectionssub", :id => false, :force => true do |t|
    t.integer "inspectionsmaster_inspection_id", :limit => 8, :null => false
    t.integer "subinspections_inspection_id",    :limit => 8, :null => false
    t.integer "orderidx",                                     :null => false
  end

  add_index "inspectionsmaster_inspectionssub", ["subinspections_inspection_id"], :name => "index_inspectionsmaster_inspectionssub_on_subinspections_inspec", :unique => true

  create_table "inspectionssub", :id => false, :force => true do |t|
    t.integer "inspection_id", :limit => 8, :null => false
    t.string  "name"
  end

  add_index "inspectionssub", ["inspection_id"], :name => "index_inspectionssub_on_inspection_id"

  create_table "inspectiontypegroups", :force => true do |t|
    t.integer  "r_tenant",               :limit => 8, :null => false
    t.timestamp "created",                             :null => false
    t.timestamp "modified",                            :null => false
    t.integer  "modifiedby",             :limit => 8
    t.string   "name",                                :null => false
    t.string   "reporttitle"
    t.integer  "printout_id"
    t.integer  "observationprintout_id"
  end

  add_index "inspectiontypegroups", ["modifiedby"], :name => "index_inspectiontypegroups_on_modifiedby"
  add_index "inspectiontypegroups", ["observationprintout_id"], :name => "index_inspectiontypegroups_on_observationprintout_id"
  add_index "inspectiontypegroups", ["printout_id"], :name => "index_inspectiontypegroups_on_printout_id"
  add_index "inspectiontypegroups", ["r_tenant"], :name => "index_inspectiontypegroups_on_r_tenant"

  create_table "inspectiontypes", :force => true do |t|
    t.integer  "r_tenant",      :limit => 8,                    :null => false
    t.timestamp "created",                                       :null => false
    t.timestamp "modified",                                      :null => false
    t.integer  "modifiedby",    :limit => 8
    t.string   "name",                                          :null => false
    t.integer  "group_id",      :limit => 8,                    :null => false
    t.string   "description"
    t.boolean  "printable",                  :default => false, :null => false
    t.integer  "legacyeventid", :limit => 8
    t.boolean  "retired",                    :default => false
    t.boolean  "master",                                        :null => false
    t.integer  "formversion",                                   :null => false
  end

  add_index "inspectiontypes", ["group_id"], :name => "index_inspectiontypes_on_group_id"
  add_index "inspectiontypes", ["modifiedby"], :name => "index_inspectiontypes_on_modifiedby"
  add_index "inspectiontypes", ["r_tenant"], :name => "index_inspectiontypes_on_r_tenant"

  create_table "inspectiontypes_criteriasections", :id => false, :force => true do |t|
    t.integer "inspectiontypes_id", :limit => 8, :null => false
    t.integer "sections_id",        :limit => 8, :null => false
    t.integer "orderidx",                        :null => false
  end

  add_index "inspectiontypes_criteriasections", ["sections_id"], :name => "index_inspectiontypes_criteriasections_on_sections_id"
  add_index "inspectiontypes_criteriasections", ["sections_id"], :name => "inspectiontypes_criteriasections_sections_id_key", :unique => true

  create_table "inspectiontypes_infofieldnames", :id => false, :force => true do |t|
    t.integer "inspectiontypes_id", :limit => 8, :null => false
    t.string  "element"
    t.integer "orderidx",                        :null => false
  end

  create_table "inspectiontypes_supportedprooftests", :id => false, :force => true do |t|
    t.integer "inspectiontypes_id", :limit => 8, :null => false
    t.string  "element"
  end

  add_index "inspectiontypes_supportedprooftests", ["inspectiontypes_id"], :name => "index_inspectiontypes_supportedprooftests_on_inspectiontypes_id"

  create_table "instructionalvideos", :force => true do |t|
    t.timestamp "created",                 :null => false
    t.timestamp "modified",                :null => false
    t.integer  "modifiedby", :limit => 8
    t.string   "name",                    :null => false
    t.string   "url",                     :null => false
  end

  create_table "jobsites", :force => true do |t|
    t.integer  "r_tenant",   :limit => 8, :null => false
    t.timestamp "created",                 :null => false
    t.timestamp "modified",                :null => false
    t.integer  "modifiedby", :limit => 8
    t.string   "name",                    :null => false
    t.integer  "r_customer", :limit => 8
    t.integer  "r_division", :limit => 8
  end

  add_index "jobsites", ["modifiedby"], :name => "index_jobsites_on_modifiedby"
  add_index "jobsites", ["r_customer"], :name => "index_jobsites_on_r_customer"
  add_index "jobsites", ["r_division"], :name => "index_jobsites_on_r_division"
  add_index "jobsites", ["r_tenant"], :name => "index_jobsites_on_r_tenant"

  create_table "legacybuttonstatemappings", :id => false, :force => true do |t|
    t.integer "buttonstateid", :limit => 8, :null => false
    t.integer "r_tenant",      :limit => 8, :null => false
    t.integer "criteria_id",   :limit => 8, :null => false
    t.integer "state_id",      :limit => 8, :null => false
  end

  create_table "lineitems", :force => true do |t|
    t.integer  "r_tenant",    :limit => 8,   :null => false
    t.timestamp "created",                    :null => false
    t.timestamp "modified",                   :null => false
    t.integer  "modifiedby",  :limit => 8
    t.integer  "order_id",    :limit => 8,   :null => false
    t.integer  "index",       :limit => 8,   :null => false
    t.string   "description", :limit => 512
    t.integer  "quantity",                   :null => false
    t.string   "lineid"
    t.string   "productcode",                :null => false
  end

  add_index "lineitems", ["index"], :name => "index_lineitems_on_index"
  add_index "lineitems", ["lineid"], :name => "index_lineitems_on_lineid"
  add_index "lineitems", ["modified"], :name => "index_lineitems_on_modified"
  add_index "lineitems", ["order_id"], :name => "index_lineitems_on_order_id"
  add_index "lineitems", ["productcode"], :name => "index_lineitems_on_productcode"

  create_table "notificationsettings", :force => true do |t|
    t.timestamp "created",      :null => false
    t.timestamp "modified",     :null => false
    t.integer  "modifiedby"
    t.integer  "r_tenant",     :null => false
    t.string   "frequency",    :null => false
    t.string   "name",         :null => false
    t.string   "periodend",    :null => false
    t.string   "periodstart",  :null => false
    t.boolean  "usingjobsite", :null => false
    t.integer  "user_id",      :null => false
  end

  add_index "notificationsettings", ["modifiedby"], :name => "index_notificationsettings_on_modifiedby"
  add_index "notificationsettings", ["r_tenant"], :name => "index_notificationsettings_on_r_tenant"
  add_index "notificationsettings", ["user_id"], :name => "index_notificationsettings_on_user_id"

  create_table "notificationsettings_addresses", :id => false, :force => true do |t|
    t.integer "notificationsettings_id", :null => false
    t.string  "addr",                    :null => false
    t.integer "orderidx",                :null => false
  end

  add_index "notificationsettings_addresses", ["notificationsettings_id"], :name => "index_notificationsettings_addresses_on_notificationsettings_id"

  create_table "notificationsettings_inspectiontypes", :id => false, :force => true do |t|
    t.integer "notificationsettings_id", :null => false
    t.integer "inspectiontype_id",       :null => false
    t.integer "orderidx",                :null => false
  end

  add_index "notificationsettings_inspectiontypes", ["notificationsettings_id"], :name => "index_notificationsettings_inspectiontypes_on_notificationsetti"

  create_table "notificationsettings_owner", :force => true do |t|
    t.integer "notificationsettings_id", :null => false
    t.integer "customer_id"
    t.integer "division_id"
    t.integer "jobsite_id"
  end

  add_index "notificationsettings_owner", ["customer_id", "division_id", "notificationsettings_id"], :name => "unique_notificationsettings_owner_customer_division", :unique => true
  add_index "notificationsettings_owner", ["customer_id"], :name => "index_notificationsettings_owner_on_customer_id"
  add_index "notificationsettings_owner", ["division_id"], :name => "index_notificationsettings_owner_on_division_id"
  add_index "notificationsettings_owner", ["jobsite_id", "notificationsettings_id"], :name => "unique_notificationsettings_owner_jobsite", :unique => true
  add_index "notificationsettings_owner", ["jobsite_id"], :name => "index_notificationsettings_owner_on_jobsite_id"
  add_index "notificationsettings_owner", ["notificationsettings_id"], :name => "index_notificationsettings_owner_on_notificationsettings_id"

  create_table "notificationsettings_producttypes", :id => false, :force => true do |t|
    t.integer "notificationsettings_id", :null => false
    t.integer "producttype_id",          :null => false
    t.integer "orderidx",                :null => false
  end

  add_index "notificationsettings_producttypes", ["notificationsettings_id"], :name => "index_notificationsettings_producttypes_on_notificationsettings"

  create_table "observations", :force => true do |t|
    t.integer  "r_tenant",   :limit => 8,    :null => false
    t.timestamp "created",                    :null => false
    t.timestamp "modified",                   :null => false
    t.integer  "modifiedby", :limit => 8
    t.string   "type",                       :null => false
    t.string   "text",       :limit => 1000, :null => false
    t.string   "state",                      :null => false
  end

  add_index "observations", ["modifiedby"], :name => "index_observations_on_modifiedby"
  add_index "observations", ["r_tenant"], :name => "index_observations_on_r_tenant"

  create_table "ordermapping", :primary_key => "uniqueid", :force => true do |t|
    t.string "organizationid"
    t.string "externalsourceid"
    t.string "orderkey"
    t.string "sourceorderkey"
  end

  add_index "ordermapping", ["externalsourceid", "orderkey", "organizationid"], :name => "ordermapping_uniquekeys_idx", :unique => true
  add_index "ordermapping", ["externalsourceid", "organizationid"], :name => "ordermapping_quicklookup"

  create_table "orders", :force => true do |t|
    t.integer  "r_tenant",    :limit => 8,   :null => false
    t.timestamp "created",                    :null => false
    t.timestamp "modified",                   :null => false
    t.integer  "modifiedby",  :limit => 8
    t.string   "ordernumber",                :null => false
    t.string   "ordertype",                  :null => false
    t.timestamp "orderdate"
    t.integer  "customer_id", :limit => 8
    t.integer  "division_id", :limit => 8
    t.string   "description", :limit => 512
    t.string   "ponumber"
  end

  add_index "orders", ["customer_id"], :name => "index_orders_on_customer_id"
  add_index "orders", ["division_id"], :name => "index_orders_on_division_id"
  add_index "orders", ["modifiedby"], :name => "index_orders_on_modifiedby"
  add_index "orders", ["ordernumber"], :name => "index_orders_on_ordernumber"
  add_index "orders", ["ordertype"], :name => "index_orders_on_ordertype"
  add_index "orders", ["r_tenant"], :name => "index_orders_on_r_tenant"

  create_table "organization", :force => true do |t|
    t.string   "displayname"
    t.string   "certificatename"
    t.timestamp "created"
    t.timestamp "modified"
    t.string   "type"
    t.integer  "parent_id",            :limit => 8
    t.integer  "r_addressinfo",        :limit => 8
    t.string   "name"
    t.boolean  "usingserialnumber",                    :default => true, :null => false
    t.string   "adminemail"
    t.string   "serialnumberformat"
    t.integer  "modifiedby",           :limit => 8
    t.integer  "r_tenant",             :limit => 8
    t.string   "fidac",                :limit => 8
    t.string   "accountdiscriminator"
    t.string   "dateformat"
    t.string   "website",              :limit => 2056
  end

  add_index "organization", ["fidac"], :name => "index_organization_on_snac", :unique => true
  add_index "organization", ["modifiedby"], :name => "index_organization_on_modifiedby"
  add_index "organization", ["name"], :name => "index_organization_on_name", :unique => true
  add_index "organization", ["parent_id"], :name => "index_organization_on_parent_id"
  add_index "organization", ["r_addressinfo"], :name => "index_organization_on_r_addressinfo"
  add_index "organization", ["r_tenant"], :name => "index_organization_on_r_tenant"

  create_table "organization_extendedfeatures", :id => false, :force => true do |t|
    t.integer "organization_id", :limit => 8, :null => false
    t.string  "element"
  end

  add_index "organization_extendedfeatures", ["organization_id"], :name => "index_organization_extendedfeatures_on_organization_id"

  create_table "permissionaction", :primary_key => "uniqueid", :force => true do |t|
    t.string  "actionname"
    t.text    "description"
    t.string  "mapkey"
    t.boolean "customerallowed"
  end

  create_table "permissions", :id => false, :force => true do |t|
    t.integer "r_fieldiduser",      :limit => 8, :null => false
    t.integer "r_permissionaction", :limit => 8, :null => false
  end

  create_table "populatorlog", :primary_key => "uniqueid", :force => true do |t|
    t.timestamp "timelogged" 
    t.text      "logmessage"
    t.string    "logstatus",  :limit => 100
    t.string    "logtype",    :limit => 100
    t.integer   "r_tenant",   :limit => 8
  end

  add_index "populatorlog", ["r_tenant"], :name => "index_populatorlog_on_r_tenant"

  create_table "printouts", :force => true do |t|
    t.timestamp "created",                           :null => false
    t.timestamp "modified",                          :null => false
    t.text     "description",                       :null => false
    t.string   "name",               :limit => 100, :null => false
    t.string   "pdftemplate"
    t.boolean  "custom"
    t.string   "type"
    t.integer  "modifiedby"
    t.integer  "tenant_id"
    t.boolean  "withsubinspections"
  end

  add_index "printouts", ["custom"], :name => "index_printouts_on_custom"
  add_index "printouts", ["modifiedby"], :name => "index_printouts_on_modifiedby"
  add_index "printouts", ["tenant_id"], :name => "index_printouts_on_tenant_id"
  add_index "printouts", ["type"], :name => "index_printouts_on_type"

  create_table "productattachments", :force => true do |t|
    t.timestamp "created",    :null => false
    t.timestamp "modified",   :null => false
    t.integer  "modifiedby"
    t.integer  "r_tenant",   :null => false
    t.integer  "product_id", :null => false
    t.text     "comment"
    t.string   "filename"
  end

  add_index "productattachments", ["product_id"], :name => "index_productattachments_on_product_id"

  create_table "productcodemapping", :primary_key => "uniqueid", :force => true do |t|
    t.integer "r_tenant",          :limit => 8, :null => false
    t.string  "productcode"
    t.integer "r_productinfo",     :limit => 8, :null => false
    t.string  "customerrefnumber"
  end

  add_index "productcodemapping", ["r_productinfo"], :name => "index_productcodemapping_on_r_productinfo"

  create_table "productcodemapping_infooption", :primary_key => "uniqueid", :force => true do |t|
    t.integer "r_infooption",         :limit => 8, :null => false
    t.integer "r_productcodemapping", :limit => 8, :null => false
    t.integer "r_tenant",             :limit => 8
  end

  add_index "productcodemapping_infooption", ["r_infooption"], :name => "index_productcodemapping_infooption_on_r_infooption"
  add_index "productcodemapping_infooption", ["r_productcodemapping"], :name => "index_productcodemapping_infooption_on_r_productcodemapping"

  create_table "products", :force => true do |t|
    t.timestamp "created",                :null => false
    t.timestamp "modified"               
    t.string    "rfidnumber",             :limit => 46
    t.integer   "r_tenant",               :limit => 8
    t.integer   "shoporder_id",           :limit => 8
    t.string    "serialnumber",           :limit => 50
    t.string    "comments",               :limit => 2047
    t.integer   "owner_id",               :limit => 8
    t.integer   "type_id",                :limit => 8
    t.integer   "division_id",            :limit => 8
    t.integer   "productstatus_uniqueid", :limit => 8
    t.string    "mobileguid",             :limit => 36
    t.string    "customerrefnumber"
    t.integer   "organization_id",        :limit => 8
    t.integer   "identifiedby_uniqueid",  :limit => 8
    t.integer   "customerorder_id",       :limit => 8
    t.timestamp  "lastinspectiondate"
    t.string    "purchaseorder"
    t.string    "uuid"
    t.string    "linkeduuid"
    t.timestamp  "identified",                             :null => false
    t.string    "location"
    t.integer   "jobsite_id"
    t.integer   "modifiedby"
    t.integer   "assigneduser_id"
    t.string    "state",                                  :null => false
    t.string    "archivedserialnumber"
  end

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
  add_index "products", ["r_tenant"], :name => "smart_search_customer_ref_number"
  add_index "products", ["r_tenant"], :name => "smart_search_rfid"
  add_index "products", ["r_tenant"], :name => "smart_search_serial_number"
  add_index "products", ["rfidnumber"], :name => "index_products_on_rfidnumber"
  add_index "products", ["serialnumber"], :name => "index_products_on_serialnumber"
  add_index "products", ["shoporder_id"], :name => "index_products_on_shoporder_id"
  add_index "products", ["shoporder_id"], :name => "productserial_rordermaster_idx"
  add_index "products", ["state"], :name => "index_products_on_state"
  add_index "products", ["type_id"], :name => "index_products_on_type_id"
  add_index "products", ["uuid"], :name => "index_productserial_on_uuid", :unique => true

  create_table "productserial_infooption", :primary_key => "uniqueid", :force => true do |t|
    t.integer "r_productserial", :limit => 8, :null => false
    t.integer "r_infooption",    :limit => 8, :null => false
  end

  add_index "productserial_infooption", ["r_infooption"], :name => "index_productserial_infooption_on_r_infooption"
  add_index "productserial_infooption", ["r_productserial"], :name => "index_productserial_infooption_on_r_productserial"

  create_table "productserialextension", :primary_key => "uniqueid", :force => true do |t|
    t.integer "r_tenant",       :limit => 8, :null => false
    t.string  "extensionkey"
    t.string  "extensionlabel"
  end

  add_index "productserialextension", ["extensionkey", "r_tenant"], :name => "productserialextension_keypermanufacture", :unique => true
  add_index "productserialextension", ["r_tenant"], :name => "index_productserialextension_on_r_tenant"

  create_table "productserialextensionvalue", :primary_key => "uniqueid", :force => true do |t|
    t.integer "r_productserial",          :limit => 8,   :null => false
    t.integer "r_productserialextension", :limit => 8,   :null => false
    t.string  "extensionvalue",           :limit => 512
  end

  add_index "productserialextensionvalue", ["r_productserial", "r_productserialextension"], :name => "productserialextensionvalue_oneperproduct", :unique => true
  add_index "productserialextensionvalue", ["r_productserial"], :name => "index_productserialextensionvalue_on_r_productserial"
  add_index "productserialextensionvalue", ["r_productserialextension"], :name => "index_productserialextensionvalue_on_r_productserialextension"

  create_table "productstatus", :primary_key => "uniqueid", :force => true do |t|
    t.string    "name"
    t.integer   "r_tenant",     :limit => 8
    t.timestamp "datecreated"  
    t.timestamp "datemodified" 
    t.string    "modifiedby"
  end

  add_index "productstatus", ["r_tenant"], :name => "index_productstatus_on_r_tenant"

  create_table "producttypegroups", :force => true do |t|
    t.timestamp "created",                  :null => false
    t.timestamp "modified",                 :null => false
    t.integer  "modifiedby"
    t.integer  "r_tenant",                 :null => false
    t.string   "name",       :limit => 25, :null => false
    t.integer  "orderidx",                 :null => false
  end

  add_index "producttypegroups", ["name", "r_tenant"], :name => "index_producttypegroups_on_r_tenant_and_name", :unique => true

  create_table "producttypes", :force => true do |t|
    t.string    "cautionurl"
    t.string    "instructions",               :limit => 2047
    t.string    "name",                                       :null => false
    t.string    "warnings",                   :limit => 2047
    t.timestamp "created"                    
    t.timestamp "modified"                   
    t.integer   "r_tenant",                   :limit => 8
    t.boolean   "hasmanufacturecertificate"
    t.string    "manufacturecertificatetext", :limit => 2001
    t.string    "descriptiontemplate"
    t.string    "imagename"
    t.integer   "modifiedby"
    t.string    "state",                                      :null => false
    t.string    "archivedname"
    t.integer   "group_id"
  end

  add_index "producttypes", ["group_id"], :name => "index_producttypes_on_group_id"
  add_index "producttypes", ["id"], :name => "productinfo_uniqueid_idx"
  add_index "producttypes", ["modifiedby"], :name => "index_producttypes_on_modifiedby"
  add_index "producttypes", ["name", "r_tenant"], :name => "productinfo_itemnumber_key", :unique => true
  add_index "producttypes", ["r_tenant"], :name => "index_producttypes_on_r_tenant"
  add_index "producttypes", ["state"], :name => "index_producttypes_on_state"

  create_table "producttypes_fileattachments", :id => false, :force => true do |t|
    t.integer "producttypes_id", :limit => 8, :null => false
    t.integer "attachments_id",  :limit => 8, :null => false
  end

  add_index "producttypes_fileattachments", ["attachments_id"], :name => "index_producttypes_fileattachments_on_attachments_id"
  add_index "producttypes_fileattachments", ["attachments_id"], :name => "producttypes_fileattachments_attachments_id_key", :unique => true
  add_index "producttypes_fileattachments", ["producttypes_id"], :name => "index_producttypes_fileattachments_on_producttypes_id"

  create_table "producttypes_inspectiontypes", :id => false, :force => true do |t|
    t.integer "producttypes_id",    :limit => 8, :null => false
    t.integer "inspectiontypes_id", :limit => 8, :null => false
  end

  create_table "producttypes_producttypes", :id => false, :force => true do |t|
    t.integer "producttypes_id", :limit => 8, :null => false
    t.integer "subtypes_id",     :limit => 8, :null => false
  end

  add_index "producttypes_producttypes", ["subtypes_id"], :name => "index_producttypes_producttypes_on_subtypes_id"

  create_table "producttypeschedules", :force => true do |t|
    t.integer  "r_tenant",          :limit => 8, :null => false
    t.timestamp "created",                        :null => false
    t.timestamp "modified",                       :null => false
    t.integer  "modifiedby",        :limit => 8
    t.integer  "frequency",         :limit => 8
    t.integer  "producttype_id",    :limit => 8, :null => false
    t.integer  "customer_id",       :limit => 8
    t.boolean  "autoschedule"
    t.integer  "inspectiontype_id"
  end

  add_index "producttypeschedules", ["customer_id"], :name => "index_producttypeschedules_on_customer_id"
  add_index "producttypeschedules", ["inspectiontype_id"], :name => "index_producttypeschedules_on_inspectiontype_id"
  add_index "producttypeschedules", ["modifiedby"], :name => "index_producttypeschedules_on_modifiedby"
  add_index "producttypeschedules", ["producttype_id"], :name => "index_producttypeschedules_on_producttype_id"
  add_index "producttypeschedules", ["r_tenant"], :name => "index_producttypeschedules_on_r_tenant"

  create_table "projects", :force => true do |t|
    t.timestamp "created",                             :null => false
    t.timestamp "modified",                            :null => false
    t.integer  "modifiedby",          :limit => 8
    t.integer  "r_tenant",            :limit => 8,    :null => false
    t.string   "projectid",                           :null => false
    t.string   "name",                                :null => false
    t.integer  "customer_id",         :limit => 8
    t.integer  "division_id",         :limit => 8
    t.string   "status",                              :null => false
    t.timestamp "started"
    t.timestamp "estimatedcompletion"
    t.timestamp "actualcompletion"
    t.string   "duration"
    t.boolean  "retired"
    t.integer  "jobsite_id"
    t.string   "description",         :limit => 1001
    t.string   "workperformed",       :limit => 1001
    t.string   "ponumber"
    t.boolean  "open"
    t.boolean  "eventjob",                            :null => false
  end

  add_index "projects", ["customer_id"], :name => "index_projects_on_customer_id"
  add_index "projects", ["division_id"], :name => "index_projects_on_division_id"
  add_index "projects", ["jobsite_id"], :name => "index_projects_on_jobsite_id"
  add_index "projects", ["name"], :name => "index_projects_on_name"
  add_index "projects", ["projectid", "r_tenant"], :name => "index_projects_on_r_tenant_and_projectid", :unique => true
  add_index "projects", ["r_tenant"], :name => "index_projects_on_r_tenant"
  add_index "projects", ["status"], :name => "index_projects_on_status"

  create_table "projects_fileattachments", :id => false, :force => true do |t|
    t.integer "projects_id", :limit => 8, :null => false
    t.integer "notes_id",    :limit => 8, :null => false
  end

  add_index "projects_fileattachments", ["notes_id"], :name => "index_projects_fileattachments_on_notes_id"
  add_index "projects_fileattachments", ["notes_id"], :name => "projects_fileattachments_notes_id_key", :unique => true

  create_table "projects_products", :id => false, :force => true do |t|
    t.integer "projects_id", :limit => 8, :null => false
    t.integer "products_id", :limit => 8, :null => false
    t.integer "orderidx"
  end

  add_index "projects_products", ["products_id", "projects_id"], :name => "index_projects_products_on_projects_id_and_products_id"
  add_index "projects_products", ["products_id"], :name => "index_projects_products_on_products_id"
  add_index "projects_products", ["projects_id"], :name => "index_projects_products_on_projects_id"

  create_table "projects_users", :id => false, :force => true do |t|
    t.integer "projects_id",        :null => false
    t.integer "resources_uniqueid", :null => false
  end

  add_index "projects_users", ["projects_id", "resources_uniqueid"], :name => "index_projects_users_on_projects_id_and_resources_uniqueid", :unique => true
  add_index "projects_users", ["projects_id"], :name => "index_projects_users_on_projects_id"
  add_index "projects_users", ["resources_uniqueid"], :name => "index_projects_users_on_resources_uniqueid"

  create_table "requesttransactions", :force => true do |t|
    t.string   "name",                    :null => false
    t.string   "type",                    :null => false
    t.timestamp "created",                 :null => false
    t.timestamp "modified",                :null => false
    t.integer  "modifiedby", :limit => 8
    t.integer  "r_tenant",   :limit => 8, :null => false
  end

  add_index "requesttransactions", ["name"], :name => "index_requesttransactions_on_name", :unique => true

  create_table "savedreports", :force => true do |t|
    t.timestamp "created",        :null => false
    t.timestamp "modified",       :null => false
    t.integer  "modifiedby"
    t.integer  "r_tenant",       :null => false
    t.string   "name",           :null => false
    t.integer  "owner_uniqueid", :null => false
    t.string   "sortcolumn",     :null => false
    t.string   "sortdirection"
    t.string   "sharedbyname"
  end

  add_index "savedreports", ["modifiedby"], :name => "index_savedreports_on_modifiedby"
  add_index "savedreports", ["owner_uniqueid"], :name => "index_savedreports_on_owner_uniqueid"
  add_index "savedreports", ["r_tenant"], :name => "index_savedreports_on_r_tenant"

  create_table "savedreports_columns", :id => false, :force => true do |t|
    t.integer "savedreports_id", :null => false
    t.string  "element"
    t.integer "idx"
  end

  add_index "savedreports_columns", ["savedreports_id"], :name => "index_savedreports_columns_on_savedreports_id"

  create_table "savedreports_criteria", :id => false, :force => true do |t|
    t.integer "savedreports_id", :null => false
    t.string  "element"
    t.string  "mapkey",          :null => false
  end

  create_table "serialnumbercounter", :primary_key => "uniqueid", :force => true do |t|
    t.integer  "r_tenant",      :limit => 8, :null => false
    t.integer  "counter",       :limit => 8
    t.string   "decimalformat"
    t.integer  "daystoreset",   :limit => 8
    t.timestamp "lastreset"
  end

  add_index "serialnumbercounter", ["r_tenant"], :name => "one_counter_per_man", :unique => true

  create_table "setupdatalastmoddates", :id => false, :force => true do |t|
    t.integer  "r_tenant",        :null => false
    t.timestamp "producttypes",    :null => false
    t.timestamp "inspectiontypes", :null => false
    t.timestamp "autoattributes",  :null => false
    t.timestamp "owners",          :null => false
  end

  add_index "setupdatalastmoddates", ["r_tenant"], :name => "idx_setupdatalastmoddates_tenantid", :unique => true
  add_index "setupdatalastmoddates", ["r_tenant"], :name => "index_setupdatalastmoddates_on_r_tenant"

  create_table "states", :force => true do |t|
    t.integer  "r_tenant",    :limit => 8,                    :null => false
    t.timestamp "created",                                     :null => false
    t.timestamp "modified",                                    :null => false
    t.integer  "modifiedby",  :limit => 8
    t.string   "displaytext",                                 :null => false
    t.string   "status",                                      :null => false
    t.string   "buttonname",                                  :null => false
    t.boolean  "retired",                  :default => false
  end

  add_index "states", ["modifiedby"], :name => "index_states_on_modifiedby"
  add_index "states", ["r_tenant"], :name => "index_states_on_r_tenant"

  create_table "statesets", :force => true do |t|
    t.integer  "r_tenant",   :limit => 8,                    :null => false
    t.timestamp "created",                                    :null => false
    t.timestamp "modified",                                   :null => false
    t.integer  "modifiedby", :limit => 8
    t.string   "name",                                       :null => false
    t.boolean  "retired",                 :default => false
  end

  add_index "statesets", ["modifiedby"], :name => "index_statesets_on_modifiedby"
  add_index "statesets", ["r_tenant"], :name => "index_statesets_on_r_tenant"

  create_table "statesets_states", :id => false, :force => true do |t|
    t.integer "statesets_id", :limit => 8, :null => false
    t.integer "states_id",    :limit => 8, :null => false
    t.integer "orderidx",                  :null => false
  end

  add_index "statesets_states", ["states_id"], :name => "index_statesets_states_on_states_id"
  add_index "statesets_states", ["states_id"], :name => "statesets_states_states_id_key", :unique => true

  create_table "subproducts", :force => true do |t|
    t.timestamp "created",          :null => false
    t.timestamp "modified",         :null => false
    t.integer  "modifiedby"
    t.integer  "product_id",       :null => false
    t.integer  "masterproduct_id", :null => false
    t.string   "label"
    t.integer  "weight"
  end

  add_index "subproducts", ["masterproduct_id"], :name => "index_subproducts_on_masterproduct_id"

  create_table "tagoptions", :force => true do |t|
    t.integer  "r_tenant",          :limit => 8, :null => false
    t.timestamp "created",                        :null => false
    t.timestamp "modified",                       :null => false
    t.integer  "modifiedby",        :limit => 8
    t.string   "key",                            :null => false
    t.integer  "weight",            :limit => 8, :null => false
    t.string   "text"
    t.string   "resolverclassname"
  end

  create_table "tasks", :force => true do |t|
    t.timestamp "created",        :null => false
    t.timestamp "modified",       :null => false
    t.string   "classname",      :null => false
    t.string   "cronexpression", :null => false
    t.string   "taskgroup"
    t.integer  "taskentityid"
    t.boolean  "enabled",        :null => false
  end

  create_table "tenantlink", :force => true do |t|
    t.integer "r_manufacturer", :limit => 8, :null => false
    t.integer "r_linkedtenant", :limit => 8, :null => false
  end

  add_index "tenantlink", ["r_linkedtenant", "r_manufacturer"], :name => "index_tenantlink_on_r_manufacturer_and_r_linkedtenant", :unique => true

  create_table "unitofmeasures", :force => true do |t|
    t.timestamp "created",                             :null => false
    t.timestamp "modified",                            :null => false
    t.integer  "modifiedby",             :limit => 8
    t.string   "name",                                :null => false
    t.string   "type"
    t.string   "shortname"
    t.boolean  "selectable"
    t.integer  "child_unitofmeasure_id", :limit => 8
  end

  add_index "unitofmeasures", ["child_unitofmeasure_id"], :name => "index_unitofmeasures_on_child_unitofmeasure_id"

  create_table "userrequest", :force => true do |t|
    t.integer  "r_tenant",      :limit => 8,   :null => false
    t.string   "companyname",   :limit => 500
    t.string   "phonenumber",   :limit => 30
    t.integer  "r_useraccount", :limit => 8,   :null => false
    t.text     "comment"
    t.timestamp "modified"
    t.timestamp "created"
    t.integer  "modifiedby",    :limit => 8
  end

  add_index "userrequest", ["modifiedby"], :name => "index_userrequest_on_modifiedby"
  add_index "userrequest", ["r_tenant"], :name => "index_userrequest_on_r_tenant"
  add_index "userrequest", ["r_useraccount"], :name => "index_userrequest_on_r_useraccount"

  create_table "users", :primary_key => "uniqueid", :force => true do |t|
    t.timestamp "datemodified"           
    t.timestamp "datecreated"            
    t.string    "modifiedby",             :limit => 50
    t.string    "userid",                 :limit => 15
    t.string    "firstname",              :limit => 30
    t.string    "lastname",               :limit => 30
    t.string    "emailaddress",           :limit => 50
    t.integer   "r_tenant",               :limit => 8
    t.integer   "r_enduser",              :limit => 8
    t.boolean   "deleted"
    t.string    "hashpassword"
    t.integer   "r_division",             :limit => 8
    t.string    "timezoneid"
    t.integer   "r_organization",         :limit => 8
    t.string    "position"
    t.boolean   "active",                               :default => true
    t.string    "initials"
    t.string    "resetpasswordkey"
    t.string    "hashsecuritycardnumber"
  end

  add_index "users", ["r_division"], :name => "index_users_on_r_division"
  add_index "users", ["r_enduser"], :name => "fki_f_enduser"
  add_index "users", ["r_enduser"], :name => "index_users_on_r_enduser"
  add_index "users", ["r_organization"], :name => "index_users_on_r_organization"
  add_index "users", ["r_tenant", "userid"], :name => "fieldiduser_idx"
  add_index "users", ["r_tenant", "userid"], :name => "uniqueuseridrtenant", :unique => true
  add_index "users", ["r_tenant"], :name => "index_users_on_r_tenant"

end
