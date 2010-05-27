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

ActiveRecord::Schema.define(:version => 201005211424) do

  create_table "activesessions", :primary_key => "user_id", :force => true do |t|
    t.string   "sessionid",   :null => false
    t.datetime "lasttouched", :null => false
  end

  create_table "addproducthistory", :primary_key => "uniqueid", :force => true do |t|
    t.integer "r_fieldiduser",   :limit => 20, :null => false
    t.integer "r_producttype",   :limit => 20
    t.integer "r_productstatus", :limit => 20
    t.string  "purchaseorder"
    t.string  "location"
    t.integer "assigneduser_id", :limit => 20
    t.integer "owner_id",        :limit => 20
    t.integer "tenant_id",       :limit => 20
  end

  add_index "addproducthistory", ["assigneduser_id"], :name => "index_addproducthistory_on_assigneduser_id"
  add_index "addproducthistory", ["owner_id"], :name => "fk_addproducthistory_owner"
  add_index "addproducthistory", ["r_fieldiduser"], :name => "addproducthistory_oneuser", :unique => true
  add_index "addproducthistory", ["r_productstatus"], :name => "index_addproducthistory_on_r_productstatus"
  add_index "addproducthistory", ["r_producttype"], :name => "index_addproducthistory_on_r_producttype"
  add_index "addproducthistory", ["tenant_id"], :name => "fk_addproducthistory_tenants"

  create_table "addproducthistory_infooption", :force => true do |t|
    t.integer "r_addproducthistory", :limit => 20, :null => false
    t.integer "r_infooption",        :limit => 20, :null => false
  end

  add_index "addproducthistory_infooption", ["r_addproducthistory"], :name => "index_addproducthistory_infooption_on_r_addproducthistory"
  add_index "addproducthistory_infooption", ["r_infooption"], :name => "index_addproducthistory_infooption_on_r_infooption"

  create_table "addressinfo", :force => true do |t|
    t.datetime "created"
    t.datetime "modified"
    t.string   "streetaddress"
    t.string   "city"
    t.string   "state"
    t.string   "country"
    t.string   "zip"
    t.string   "phone1"
    t.string   "fax1"
    t.integer  "modifiedby",    :limit => 20
    t.string   "phone2"
  end

  add_index "addressinfo", ["modifiedby"], :name => "index_addressinfo_on_modifiedby"

  create_table "alertstatus", :id => false, :force => true do |t|
    t.integer "tenant_id",     :limit => 20
    t.integer "diskspace",     :limit => 20, :null => false
    t.integer "assets",        :limit => 20, :null => false
    t.integer "secondaryorgs", :limit => 10, :null => false
  end

  add_index "alertstatus", ["tenant_id"], :name => "idx_alertstatus_tenantid", :unique => true
  add_index "alertstatus", ["tenant_id"], :name => "index_alertstatus_on_r_tenant"

  create_table "assets", :force => true do |t|
    t.datetime "created",                    :null => false
    t.datetime "modified",                   :null => false
    t.integer  "modifiedby",   :limit => 20
    t.integer  "tenant_id",    :limit => 20, :null => false
    t.string   "serialnumber",               :null => false
  end

  add_index "assets", ["modifiedby"], :name => "fk_assets_users"
  add_index "assets", ["tenant_id"], :name => "fk_assets_tenants"

  create_table "associatedinspectiontypes", :force => true do |t|
    t.datetime "created",                         :null => false
    t.datetime "modified",                        :null => false
    t.integer  "modifiedby",        :limit => 20
    t.integer  "inspectiontype_id", :limit => 20, :null => false
    t.integer  "producttype_id",    :limit => 20, :null => false
    t.integer  "tenant_id",         :limit => 20
  end

  add_index "associatedinspectiontypes", ["inspectiontype_id"], :name => "fk_associatedinspectiontypes_inspectiontypes"
  add_index "associatedinspectiontypes", ["modifiedby"], :name => "fk_associatedinspectiontypes_users"
  add_index "associatedinspectiontypes", ["producttype_id", "inspectiontype_id"], :name => "idx_associatedinspectiontypes_unique", :unique => true
  add_index "associatedinspectiontypes", ["tenant_id"], :name => "fk_associatedinspectiontypes_tenants"

  create_table "autoattributecriteria", :force => true do |t|
    t.integer  "r_producttype", :limit => 20, :null => false
    t.integer  "tenant_id",     :limit => 20
    t.datetime "created"
    t.datetime "modified"
    t.integer  "modifiedby",    :limit => 20
  end

  add_index "autoattributecriteria", ["modifiedby"], :name => "index_autoattributecriteria_on_modifiedby"
  add_index "autoattributecriteria", ["r_producttype"], :name => "index_autoattributecriteria_on_r_producttype"
  add_index "autoattributecriteria", ["tenant_id"], :name => "index_autoattributecriteria_on_r_tenant"

  create_table "autoattributecriteria_inputinfofield", :force => true do |t|
    t.integer "r_infofield",             :limit => 20, :null => false
    t.integer "r_autoattributecriteria", :limit => 20, :null => false
  end

  add_index "autoattributecriteria_inputinfofield", ["r_autoattributecriteria"], :name => "index_autoattributecriteria_inputinfofield_on_r_autoattributecr"
  add_index "autoattributecriteria_inputinfofield", ["r_infofield"], :name => "index_autoattributecriteria_inputinfofield_on_r_infofield"

  create_table "autoattributecriteria_outputinfofield", :force => true do |t|
    t.integer "r_infofield",             :limit => 20, :null => false
    t.integer "r_autoattributecriteria", :limit => 20, :null => false
  end

  add_index "autoattributecriteria_outputinfofield", ["r_autoattributecriteria"], :name => "index_autoattributecriteria_outputinfofield_on_r_autoattributec"
  add_index "autoattributecriteria_outputinfofield", ["r_infofield"], :name => "index_autoattributecriteria_outputinfofield_on_r_infofield"

  create_table "autoattributedefinition", :force => true do |t|
    t.integer  "r_autoattributecriteria", :limit => 20, :null => false
    t.integer  "tenant_id",               :limit => 20
    t.datetime "created"
    t.datetime "modified"
    t.integer  "modifiedby",              :limit => 20
  end

  add_index "autoattributedefinition", ["modifiedby"], :name => "index_autoattributedefinition_on_modifiedby"
  add_index "autoattributedefinition", ["r_autoattributecriteria"], :name => "index_autoattributedefinition_on_r_autoattributecriteria"
  add_index "autoattributedefinition", ["tenant_id"], :name => "index_autoattributedefinition_on_r_tenant"

  create_table "autoattributedefinition_inputinfooption", :force => true do |t|
    t.integer "r_infooption",              :limit => 20, :null => false
    t.integer "r_autoattributedefinition", :limit => 20, :null => false
  end

  add_index "autoattributedefinition_inputinfooption", ["r_autoattributedefinition"], :name => "index_autoattributedefinition_inputinfooption_on_r_autoattribut"
  add_index "autoattributedefinition_inputinfooption", ["r_infooption"], :name => "index_autoattributedefinition_inputinfooption_on_r_infooption"

  create_table "autoattributedefinition_outputinfooption", :force => true do |t|
    t.integer "r_infooption",              :limit => 20, :null => false
    t.integer "r_autoattributedefinition", :limit => 20, :null => false
  end

  add_index "autoattributedefinition_outputinfooption", ["r_autoattributedefinition"], :name => "index_autoattributedefinition_outputinfooption_on_r_autoattribu"
  add_index "autoattributedefinition_outputinfooption", ["r_infooption"], :name => "index_autoattributedefinition_outputinfooption_on_r_infooption"

  create_table "catalogs", :force => true do |t|
    t.datetime "created",                  :null => false
    t.datetime "modified",                 :null => false
    t.integer  "modifiedby", :limit => 20
    t.integer  "tenant_id",  :limit => 20
  end

  add_index "catalogs", ["tenant_id"], :name => "index_catalogs_on_r_tenant", :unique => true

  create_table "catalogs_inspectiontypes", :id => false, :force => true do |t|
    t.integer "catalogs_id",                 :limit => 20, :null => false
    t.integer "publishedinspectiontypes_id", :limit => 20, :null => false
  end

  add_index "catalogs_inspectiontypes", ["catalogs_id", "publishedinspectiontypes_id"], :name => "index_catalogs_inspectiontypes_on_catalogs_id_and_publishedinsp", :unique => true
  add_index "catalogs_inspectiontypes", ["catalogs_id"], :name => "index_catalogs_inspectiontypes_on_catalogs_id"
  add_index "catalogs_inspectiontypes", ["publishedinspectiontypes_id"], :name => "index_catalogs_inspectiontypes_on_publishedinspectiontypes_id", :unique => true

  create_table "catalogs_producttypes", :id => false, :force => true do |t|
    t.integer "catalogs_id",              :limit => 20, :null => false
    t.integer "publishedproducttypes_id", :limit => 20, :null => false
  end

  add_index "catalogs_producttypes", ["catalogs_id", "publishedproducttypes_id"], :name => "index_catalogs_producttypes_on_catalogs_id_and_publishedtypes_i", :unique => true
  add_index "catalogs_producttypes", ["catalogs_id"], :name => "index_catalogs_producttypes_on_catalogs_id"
  add_index "catalogs_producttypes", ["publishedproducttypes_id"], :name => "index_catalogs_producttypes_on_publishedproducttypes_id"
  add_index "catalogs_producttypes", ["publishedproducttypes_id"], :name => "index_catalogs_producttypes_on_publishedtypes_id", :unique => true

  create_table "commenttemplate", :primary_key => "uniqueid", :force => true do |t|
    t.datetime "datecreated"
    t.datetime "datemodified"
    t.string   "modifiedby",   :limit => 50
    t.string   "templateid",   :limit => 50
    t.string   "contents"
    t.integer  "tenant_id",    :limit => 20
  end

  add_index "commenttemplate", ["templateid"], :name => "commenttemplate_idx"
  add_index "commenttemplate", ["tenant_id"], :name => "index_commenttemplate_on_r_tenant"

  create_table "configurations", :force => true do |t|
    t.datetime "created",                  :null => false
    t.datetime "modified",                 :null => false
    t.integer  "modifiedby", :limit => 20
    t.string   "identifier"
    t.string   "value",                    :null => false
    t.integer  "tenantid",   :limit => 20
  end

  add_index "configurations", ["modifiedby"], :name => "index_configurations_on_modifiedby"
  add_index "configurations", ["tenantid"], :name => "index_configurations_on_tenantid"

  create_table "contractpricings", :force => true do |t|
    t.datetime "created",                            :null => false
    t.datetime "modified",                           :null => false
    t.integer  "modifiedby",           :limit => 20
    t.string   "signuppackage"
    t.string   "paymentoption"
    t.float    "priceperuserpermonth"
    t.integer  "externalid",           :limit => 20
  end

  create_table "criteria", :force => true do |t|
    t.integer  "tenant_id",   :limit => 20
    t.datetime "created",                                      :null => false
    t.datetime "modified",                                     :null => false
    t.integer  "modifiedby",  :limit => 20
    t.string   "displaytext",                                  :null => false
    t.integer  "states_id",   :limit => 20,                    :null => false
    t.boolean  "principal",                 :default => false, :null => false
    t.boolean  "retired",                   :default => false
  end

  add_index "criteria", ["modifiedby"], :name => "index_criteria_on_modifiedby"
  add_index "criteria", ["states_id"], :name => "index_criteria_on_states_id"
  add_index "criteria", ["tenant_id"], :name => "index_criteria_on_r_tenant"

  create_table "criteria_deficiencies", :id => false, :force => true do |t|
    t.integer "criteria_id", :limit => 20,  :null => false
    t.string  "text",        :limit => 511, :null => false
    t.integer "orderidx",    :limit => 20,  :null => false
  end

  add_index "criteria_deficiencies", ["criteria_id"], :name => "fk_criteria_deficiencies_criteria"

  create_table "criteria_recommendations", :id => false, :force => true do |t|
    t.integer "criteria_id", :limit => 20,  :null => false
    t.string  "text",        :limit => 511, :null => false
    t.integer "orderidx",    :limit => 20,  :null => false
  end

  add_index "criteria_recommendations", ["criteria_id"], :name => "fk_criteria_recommendations_criteria"

  create_table "criteriaresults", :force => true do |t|
    t.integer  "tenant_id",     :limit => 20
    t.datetime "created",                     :null => false
    t.datetime "modified",                    :null => false
    t.integer  "modifiedby",    :limit => 20
    t.integer  "state_id",      :limit => 20, :null => false
    t.integer  "criteria_id",   :limit => 20, :null => false
    t.integer  "inspection_id", :limit => 20, :null => false
  end

  add_index "criteriaresults", ["criteria_id"], :name => "index_criteriaresults_on_criteria_id"
  add_index "criteriaresults", ["inspection_id"], :name => "index_criteriaresults_on_inspection_id"
  add_index "criteriaresults", ["modifiedby"], :name => "index_criteriaresults_on_modifiedby"
  add_index "criteriaresults", ["state_id"], :name => "index_criteriaresults_on_state_id"
  add_index "criteriaresults", ["tenant_id"], :name => "index_criteriaresults_on_r_tenant"

  create_table "criteriaresults_deficiencies", :id => false, :force => true do |t|
    t.integer "criteriaresults_id", :limit => 20, :null => false
    t.integer "deficiencies_id",    :limit => 20, :null => false
    t.integer "orderidx",           :limit => 20, :null => false
  end

  add_index "criteriaresults_deficiencies", ["criteriaresults_id", "deficiencies_id"], :name => "index_criteriaresults_deficiencies_on_criteriaresults_id_and_de"
  add_index "criteriaresults_deficiencies", ["criteriaresults_id"], :name => "index_criteriaresults_deficiencies_on_criteriaresults_id"
  add_index "criteriaresults_deficiencies", ["deficiencies_id"], :name => "index_criteriaresults_deficiencies_on_deficiencies_id"

  create_table "criteriaresults_recommendations", :id => false, :force => true do |t|
    t.integer "criteriaresults_id", :limit => 20, :null => false
    t.integer "recommendations_id", :limit => 20, :null => false
    t.integer "orderidx",           :limit => 20, :null => false
  end

  add_index "criteriaresults_recommendations", ["criteriaresults_id", "recommendations_id"], :name => "index_criteriaresults_recommendations_on_criteriaresults_id_and"
  add_index "criteriaresults_recommendations", ["criteriaresults_id"], :name => "index_criteriaresults_recommendations_on_criteriaresults_id"
  add_index "criteriaresults_recommendations", ["recommendations_id"], :name => "index_criteriaresults_recommendations_on_recommendations_id"

  create_table "criteriasections", :force => true do |t|
    t.integer  "tenant_id",  :limit => 20
    t.datetime "created",                                     :null => false
    t.datetime "modified",                                    :null => false
    t.integer  "modifiedby", :limit => 20
    t.string   "title",                                       :null => false
    t.boolean  "retired",                  :default => false
  end

  add_index "criteriasections", ["modifiedby"], :name => "index_criteriasections_on_modifiedby"
  add_index "criteriasections", ["tenant_id"], :name => "index_criteriasections_on_r_tenant"

  create_table "criteriasections_criteria", :id => false, :force => true do |t|
    t.integer "criteriasections_id", :limit => 20, :null => false
    t.integer "criteria_id",         :limit => 20, :null => false
    t.integer "orderidx",            :limit => 20, :null => false
  end

  add_index "criteriasections_criteria", ["criteria_id"], :name => "criteriasections_criteria_criteria_id_key", :unique => true
  add_index "criteriasections_criteria", ["criteria_id"], :name => "index_criteriasections_criteria_on_criteria_id"
  add_index "criteriasections_criteria", ["criteriasections_id"], :name => "fk_criteriasections_criteria_criteriasections"

  create_table "downloads", :force => true do |t|
    t.datetime "created",                   :null => false
    t.datetime "modified",                  :null => false
    t.integer  "modifiedby",  :limit => 20
    t.integer  "tenant_id",   :limit => 20, :null => false
    t.string   "name",                      :null => false
    t.string   "contenttype", :limit => 32, :null => false
    t.string   "state",       :limit => 32, :null => false
    t.integer  "user_id",     :limit => 20, :null => false
  end

  add_index "downloads", ["modifiedby"], :name => "fk_downloads_users"
  add_index "downloads", ["tenant_id"], :name => "fk_downloads_tenants"
  add_index "downloads", ["user_id"], :name => "fk_downloads_owner"

  create_table "eulaacceptances", :force => true do |t|
    t.datetime "created",                         :null => false
    t.datetime "modified",                        :null => false
    t.integer  "modifiedby",        :limit => 20
    t.integer  "tenant_id",         :limit => 20
    t.integer  "acceptor_uniqueid", :limit => 20, :null => false
    t.integer  "eula_id",           :limit => 20, :null => false
    t.datetime "date",                            :null => false
  end

  add_index "eulaacceptances", ["acceptor_uniqueid"], :name => "index_eulaacceptances_on_acceptor_uniqueid"
  add_index "eulaacceptances", ["eula_id"], :name => "index_eulaacceptances_on_eula_id"
  add_index "eulaacceptances", ["modifiedby"], :name => "index_eulaacceptances_on_modifiedby"
  add_index "eulaacceptances", ["tenant_id"], :name => "index_eulaacceptances_on_r_tenant"

  create_table "eulas", :force => true do |t|
    t.datetime "created",                     :null => false
    t.datetime "modified",                    :null => false
    t.integer  "modifiedby",    :limit => 20
    t.text     "legaltext",                   :null => false
    t.datetime "effectivedate",               :null => false
    t.string   "version",                     :null => false
  end

  create_table "fileattachments", :force => true do |t|
    t.integer  "tenant_id",  :limit => 20
    t.datetime "created",                  :null => false
    t.datetime "modified",                 :null => false
    t.integer  "modifiedby", :limit => 20
    t.string   "filename"
    t.text     "comments"
  end

  add_index "fileattachments", ["modifiedby"], :name => "index_fileattachments_on_modifiedby"
  add_index "fileattachments", ["tenant_id"], :name => "index_fileattachments_on_r_tenant"

  create_table "findproductoption", :primary_key => "uniqueid", :force => true do |t|
    t.string "identifier"
    t.string "value"
    t.text   "description"
  end

  add_index "findproductoption", ["identifier"], :name => "findproductoption_uniquekey_type", :unique => true

  create_table "findproductoption_manufacture", :primary_key => "uniqueid", :force => true do |t|
    t.integer  "r_findproductoption", :limit => 20,                :null => false
    t.integer  "tenant_id",           :limit => 20
    t.integer  "weight",              :limit => 20,                :null => false
    t.integer  "mobileweight",        :limit => 20, :default => 0
    t.datetime "datemodified"
    t.datetime "datecreated"
  end

  add_index "findproductoption_manufacture", ["r_findproductoption", "tenant_id"], :name => "fpm_unique", :unique => true
  add_index "findproductoption_manufacture", ["tenant_id"], :name => "fk_findproductoption_manufacture_organization"

  create_table "infofield", :primary_key => "uniqueid", :force => true do |t|
    t.string  "name",                             :null => false
    t.integer "r_productinfo",      :limit => 20, :null => false
    t.boolean "required"
    t.integer "weight",             :limit => 20
    t.integer "r_unitofmeasure",    :limit => 20
    t.boolean "usingunitofmeasure"
    t.string  "fieldtype",          :limit => 50
    t.boolean "retired"
  end

  add_index "infofield", ["r_productinfo"], :name => "index_infofield_on_r_productinfo"
  add_index "infofield", ["r_unitofmeasure"], :name => "index_infofield_on_r_unitofmeasure"

  create_table "infooption", :primary_key => "uniqueid", :force => true do |t|
    t.string  "name",                      :null => false
    t.integer "r_infofield", :limit => 20, :null => false
    t.boolean "staticdata"
    t.integer "weight",      :limit => 20
  end

  add_index "infooption", ["r_infofield"], :name => "index_infooption_on_r_infofield"

  create_table "inspectionbooks", :force => true do |t|
    t.integer  "tenant_id",  :limit => 20
    t.datetime "created",                  :null => false
    t.datetime "modified",                 :null => false
    t.integer  "legacyid",   :limit => 20
    t.integer  "modifiedby", :limit => 20
    t.string   "name",                     :null => false
    t.boolean  "open",                     :null => false
    t.integer  "owner_id",   :limit => 20
  end

  add_index "inspectionbooks", ["modifiedby"], :name => "index_inspectionbooks_on_modifiedby"
  add_index "inspectionbooks", ["owner_id"], :name => "fk_inspectionbooks_owner"
  add_index "inspectionbooks", ["tenant_id"], :name => "index_inspectionbooks_on_r_tenant"

  create_table "inspectiongroups", :force => true do |t|
    t.integer  "tenant_id",  :limit => 20
    t.datetime "created",                  :null => false
    t.datetime "modified",                 :null => false
    t.integer  "modifiedby", :limit => 20
    t.string   "mobileguid"
  end

  add_index "inspectiongroups", ["modifiedby"], :name => "index_inspectiongroups_on_modifiedby"
  add_index "inspectiongroups", ["tenant_id"], :name => "index_inspectiongroups_on_r_tenant"

  create_table "inspections", :force => true do |t|
    t.integer  "tenant_id",        :limit => 20
    t.datetime "created",                          :null => false
    t.datetime "modified",                         :null => false
    t.integer  "modifiedby",       :limit => 20
    t.string   "comments",         :limit => 2500
    t.integer  "type_id",          :limit => 20,   :null => false
    t.integer  "product_id",       :limit => 20,   :null => false
    t.integer  "formversion",      :limit => 20,   :null => false
    t.integer  "productstatus_id", :limit => 20
    t.string   "mobileguid"
  end

  add_index "inspections", ["modifiedby"], :name => "index_inspections_on_modifiedby"
  add_index "inspections", ["product_id"], :name => "index_inspections_on_product_id"
  add_index "inspections", ["productstatus_id"], :name => "fk_inspections_productstatus"
  add_index "inspections", ["tenant_id"], :name => "index_inspections_on_r_tenant"
  add_index "inspections", ["type_id"], :name => "index_inspections_on_type_id"

  create_table "inspections_fileattachments", :id => false, :force => true do |t|
    t.integer "inspections_id", :limit => 20, :null => false
    t.integer "attachments_id", :limit => 20, :null => false
  end

  add_index "inspections_fileattachments", ["attachments_id"], :name => "index_inspections_fileattachments_on_attachments_id"
  add_index "inspections_fileattachments", ["attachments_id"], :name => "inspections_fileattachments_attachments_id_key", :unique => true
  add_index "inspections_fileattachments", ["inspections_id"], :name => "index_inspections_fileattachments_on_inspections_id"

  create_table "inspections_infooptionmap", :id => false, :force => true do |t|
    t.integer "inspections_id", :limit => 20, :null => false
    t.string  "element"
    t.string  "mapkey",                       :null => false
  end

  add_index "inspections_infooptionmap", ["inspections_id"], :name => "fk_inspections_infooptionmap_inspections"

  create_table "inspectionschedules", :force => true do |t|
    t.integer  "tenant_id",                :limit => 20
    t.datetime "created",                                :null => false
    t.datetime "modified",                               :null => false
    t.integer  "modifiedby",               :limit => 20
    t.integer  "product_id",               :limit => 20, :null => false
    t.datetime "nextdate",                               :null => false
    t.integer  "inspectiontype_id",        :limit => 20
    t.datetime "completeddate"
    t.string   "status",                                 :null => false
    t.integer  "inspection_inspection_id", :limit => 20
    t.string   "state",                                  :null => false
    t.integer  "project_id",               :limit => 20
    t.string   "location"
    t.integer  "owner_id",                 :limit => 20
    t.string   "mobileguid"
  end

  add_index "inspectionschedules", ["inspection_inspection_id"], :name => "index_inspectionschedules_on_inspection_inspection_id", :unique => true
  add_index "inspectionschedules", ["inspectiontype_id"], :name => "index_inspectionschedules_on_inspectiontype_id"
  add_index "inspectionschedules", ["modifiedby"], :name => "index_inspectionschedules_on_modifiedby"
  add_index "inspectionschedules", ["owner_id"], :name => "fk_inspectionschedules_owner"
  add_index "inspectionschedules", ["product_id"], :name => "index_inspectionschedules_on_product_id"
  add_index "inspectionschedules", ["project_id"], :name => "index_inspectionschedules_on_project_id"
  add_index "inspectionschedules", ["tenant_id"], :name => "index_inspectionschedules_on_r_tenant"

  create_table "inspectionsmaster", :id => false, :force => true do |t|
    t.integer  "inspection_id",      :limit => 20, :null => false
    t.string   "location"
    t.datetime "date",                             :null => false
    t.boolean  "printable",                        :null => false
    t.string   "prooftesttype"
    t.string   "peakload"
    t.string   "duration"
    t.string   "status",                           :null => false
    t.integer  "inspector_uniqueid", :limit => 20, :null => false
    t.integer  "group_id",           :limit => 20, :null => false
    t.integer  "book_id",            :limit => 20
    t.string   "state",                            :null => false
    t.string   "peakloadduration"
    t.integer  "owner_id",           :limit => 20, :null => false
  end

  add_index "inspectionsmaster", ["book_id"], :name => "fk_inspectionsmaster_inspectionbooks"
  add_index "inspectionsmaster", ["group_id"], :name => "index_inspectionsmaster_on_group_id"
  add_index "inspectionsmaster", ["inspection_id"], :name => "index_inspectionsmaster_on_inspection_id"
  add_index "inspectionsmaster", ["inspector_uniqueid"], :name => "index_inspectionsmaster_on_inspector_uniqueid"
  add_index "inspectionsmaster", ["owner_id"], :name => "fk_inspectionsmaster_owner"
  add_index "inspectionsmaster", ["state"], :name => "index_inspectionsmaster_on_state"

  create_table "inspectionsmaster_inspectionssub", :id => false, :force => true do |t|
    t.integer "inspectionsmaster_inspection_id", :limit => 20, :null => false
    t.integer "subinspections_inspection_id",    :limit => 20, :null => false
    t.integer "orderidx",                        :limit => 20, :null => false
  end

  add_index "inspectionsmaster_inspectionssub", ["inspectionsmaster_inspection_id"], :name => "fk_masterinspections_inspection"
  add_index "inspectionsmaster_inspectionssub", ["subinspections_inspection_id"], :name => "index_inspectionsmaster_inspectionssub_on_subinspections_inspec", :unique => true

  create_table "inspectionssub", :id => false, :force => true do |t|
    t.integer "inspection_id", :limit => 20, :null => false
    t.string  "name"
  end

  add_index "inspectionssub", ["inspection_id"], :name => "index_inspectionssub_on_inspection_id"

  create_table "inspectiontypegroups", :force => true do |t|
    t.integer  "tenant_id",              :limit => 20
    t.datetime "created",                              :null => false
    t.datetime "modified",                             :null => false
    t.integer  "modifiedby",             :limit => 20
    t.string   "name",                                 :null => false
    t.string   "reporttitle"
    t.integer  "printout_id",            :limit => 20
    t.integer  "observationprintout_id", :limit => 20
  end

  add_index "inspectiontypegroups", ["modifiedby"], :name => "index_inspectiontypegroups_on_modifiedby"
  add_index "inspectiontypegroups", ["observationprintout_id"], :name => "index_inspectiontypegroups_on_observationprintout_id"
  add_index "inspectiontypegroups", ["printout_id"], :name => "index_inspectiontypegroups_on_printout_id"
  add_index "inspectiontypegroups", ["tenant_id"], :name => "index_inspectiontypegroups_on_r_tenant"

  create_table "inspectiontypes", :force => true do |t|
    t.integer  "tenant_id",     :limit => 20
    t.datetime "created",                                        :null => false
    t.datetime "modified",                                       :null => false
    t.integer  "modifiedby",    :limit => 20
    t.string   "name",                                           :null => false
    t.integer  "group_id",      :limit => 20,                    :null => false
    t.string   "description"
    t.boolean  "printable",                   :default => false, :null => false
    t.integer  "legacyeventid", :limit => 20
    t.boolean  "retired",                     :default => false
    t.boolean  "master",                                         :null => false
    t.integer  "formversion",   :limit => 20,                    :null => false
    t.string   "state",                                          :null => false
    t.string   "archivedName"
  end

  add_index "inspectiontypes", ["group_id"], :name => "index_inspectiontypes_on_group_id"
  add_index "inspectiontypes", ["modifiedby"], :name => "index_inspectiontypes_on_modifiedby"
  add_index "inspectiontypes", ["tenant_id"], :name => "index_inspectiontypes_on_r_tenant"

  create_table "inspectiontypes_criteriasections", :id => false, :force => true do |t|
    t.integer "inspectiontypes_id", :limit => 20, :null => false
    t.integer "sections_id",        :limit => 20, :null => false
    t.integer "orderidx",           :limit => 20, :null => false
  end

  add_index "inspectiontypes_criteriasections", ["inspectiontypes_id"], :name => "fk_inspectiontypes_criteriasections_inspectiontypes"
  add_index "inspectiontypes_criteriasections", ["sections_id"], :name => "index_inspectiontypes_criteriasections_on_sections_id"
  add_index "inspectiontypes_criteriasections", ["sections_id"], :name => "inspectiontypes_criteriasections_sections_id_key", :unique => true

  create_table "inspectiontypes_infofieldnames", :id => false, :force => true do |t|
    t.integer "inspectiontypes_id", :limit => 20, :null => false
    t.string  "element"
    t.integer "orderidx",           :limit => 20, :null => false
  end

  add_index "inspectiontypes_infofieldnames", ["inspectiontypes_id"], :name => "fk_inspectiontypes_infofieldnames_inspectiontypes"

  create_table "inspectiontypes_supportedprooftests", :id => false, :force => true do |t|
    t.integer "inspectiontypes_id", :limit => 20, :null => false
    t.string  "element"
  end

  add_index "inspectiontypes_supportedprooftests", ["inspectiontypes_id"], :name => "index_inspectiontypes_supportedprooftests_on_inspectiontypes_id"

  create_table "instructionalvideos", :force => true do |t|
    t.datetime "created",                  :null => false
    t.datetime "modified",                 :null => false
    t.integer  "modifiedby", :limit => 20
    t.string   "name",                     :null => false
    t.string   "url",                      :null => false
  end

  create_table "jobsites", :force => true do |t|
    t.integer  "tenant_id",  :limit => 20
    t.datetime "created",                  :null => false
    t.datetime "modified",                 :null => false
    t.integer  "modifiedby", :limit => 20
    t.string   "name",                     :null => false
    t.integer  "r_customer", :limit => 20
    t.integer  "r_division", :limit => 20
  end

  add_index "jobsites", ["modifiedby"], :name => "index_jobsites_on_modifiedby"
  add_index "jobsites", ["r_customer"], :name => "index_jobsites_on_r_customer"
  add_index "jobsites", ["r_division"], :name => "index_jobsites_on_r_division"
  add_index "jobsites", ["tenant_id"], :name => "index_jobsites_on_r_tenant"

  create_table "lineitems", :force => true do |t|
    t.integer  "tenant_id",   :limit => 20
    t.datetime "created",                    :null => false
    t.datetime "modified",                   :null => false
    t.integer  "modifiedby",  :limit => 20
    t.integer  "order_id",    :limit => 20,  :null => false
    t.integer  "idx",         :limit => 20
    t.string   "description", :limit => 512
    t.integer  "quantity",    :limit => 20,  :null => false
    t.string   "lineid"
    t.string   "productcode",                :null => false
  end

  add_index "lineitems", ["idx"], :name => "index_lineitems_on_index"
  add_index "lineitems", ["lineid"], :name => "index_lineitems_on_lineid"
  add_index "lineitems", ["modified"], :name => "index_lineitems_on_modified"
  add_index "lineitems", ["modifiedby"], :name => "fk_lineitems_users"
  add_index "lineitems", ["order_id"], :name => "index_lineitems_on_order_id"
  add_index "lineitems", ["productcode"], :name => "index_lineitems_on_productcode"
  add_index "lineitems", ["tenant_id"], :name => "fk_lineitems_tenants"

  create_table "messagecommands", :force => true do |t|
    t.datetime "created",                   :null => false
    t.datetime "modified",                  :null => false
    t.integer  "modifiedby",  :limit => 20
    t.string   "commandtype", :limit => 50, :null => false
    t.boolean  "processed",                 :null => false
    t.integer  "createdby",   :limit => 20, :null => false
  end

  add_index "messagecommands", ["modifiedby"], :name => "fk_messagecommands_users"

  create_table "messagecommands_paramaters", :primary_key => "mapkey", :force => true do |t|
    t.integer "messagecommands_id", :limit => 20, :null => false
    t.string  "element"
  end

  create_table "messages", :force => true do |t|
    t.datetime "created",                    :null => false
    t.datetime "modified",                   :null => false
    t.integer  "modifiedby", :limit => 20
    t.integer  "tenant_id",  :limit => 20,   :null => false
    t.integer  "owner_id",   :limit => 20,   :null => false
    t.string   "sender",                     :null => false
    t.string   "receiver",                   :null => false
    t.string   "subject",    :limit => 1000, :null => false
    t.text     "body",                       :null => false
    t.boolean  "unread",                     :null => false
    t.integer  "command_id", :limit => 20,   :null => false
  end

  add_index "messages", ["command_id"], :name => "fk_messages_messagecommands"
  add_index "messages", ["modifiedby"], :name => "fk_messages_users"
  add_index "messages", ["owner_id"], :name => "fk_messages_org_base"
  add_index "messages", ["tenant_id"], :name => "fk_messages_tenants"

  create_table "notificationsettings", :force => true do |t|
    t.datetime "created",                       :null => false
    t.datetime "modified",                      :null => false
    t.integer  "modifiedby",      :limit => 20
    t.integer  "tenant_id",       :limit => 20
    t.string   "frequency",                     :null => false
    t.string   "name",                          :null => false
    t.string   "periodend",                     :null => false
    t.string   "periodstart",                   :null => false
    t.integer  "user_id",         :limit => 20, :null => false
    t.integer  "owner_id",        :limit => 20
    t.boolean  "includeoverdue",                :null => false
    t.boolean  "includeupcoming",               :null => false
  end

  add_index "notificationsettings", ["modifiedby"], :name => "index_notificationsettings_on_modifiedby"
  add_index "notificationsettings", ["owner_id"], :name => "fk_notificationsettings_owner"
  add_index "notificationsettings", ["tenant_id"], :name => "index_notificationsettings_on_r_tenant"
  add_index "notificationsettings", ["user_id"], :name => "index_notificationsettings_on_user_id"

  create_table "notificationsettings_addresses", :id => false, :force => true do |t|
    t.integer "notificationsettings_id", :limit => 20, :null => false
    t.string  "addr",                                  :null => false
    t.integer "orderidx",                :limit => 20, :null => false
  end

  add_index "notificationsettings_addresses", ["notificationsettings_id"], :name => "index_notificationsettings_addresses_on_notificationsettings_id"

  create_table "notificationsettings_inspectiontypes", :id => false, :force => true do |t|
    t.integer "notificationsettings_id", :limit => 20, :null => false
    t.integer "inspectiontype_id",       :limit => 20, :null => false
    t.integer "orderidx",                :limit => 20, :null => false
  end

  add_index "notificationsettings_inspectiontypes", ["notificationsettings_id"], :name => "index_notificationsettings_inspectiontypes_on_notificationsetti"

  create_table "notificationsettings_producttypes", :id => false, :force => true do |t|
    t.integer "notificationsettings_id", :limit => 20, :null => false
    t.integer "producttype_id",          :limit => 20, :null => false
    t.integer "orderidx",                :limit => 20, :null => false
  end

  add_index "notificationsettings_producttypes", ["notificationsettings_id"], :name => "index_notificationsettings_producttypes_on_notificationsettings"

  create_table "observations", :force => true do |t|
    t.integer  "tenant_id",  :limit => 20
    t.datetime "created",                    :null => false
    t.datetime "modified",                   :null => false
    t.integer  "modifiedby", :limit => 20
    t.string   "type",                       :null => false
    t.string   "text",       :limit => 1000, :null => false
    t.string   "state",                      :null => false
  end

  add_index "observations", ["modifiedby"], :name => "index_observations_on_modifiedby"
  add_index "observations", ["tenant_id"], :name => "index_observations_on_r_tenant"

  create_table "ordermapping", :primary_key => "uniqueid", :force => true do |t|
    t.string "organizationid"
    t.string "externalsourceid"
    t.string "orderkey"
    t.string "sourceorderkey"
  end

  add_index "ordermapping", ["externalsourceid", "orderkey", "organizationid"], :name => "ordermapping_uniquekeys_idx", :unique => true
  add_index "ordermapping", ["externalsourceid", "organizationid"], :name => "ordermapping_quicklookup"

  create_table "orders", :force => true do |t|
    t.integer  "tenant_id",   :limit => 20
    t.datetime "created",                    :null => false
    t.datetime "modified",                   :null => false
    t.integer  "modifiedby",  :limit => 20
    t.string   "ordernumber",                :null => false
    t.string   "ordertype",                  :null => false
    t.datetime "orderdate"
    t.string   "description", :limit => 512
    t.string   "ponumber"
    t.integer  "owner_id",    :limit => 20
  end

  add_index "orders", ["modifiedby"], :name => "index_orders_on_modifiedby"
  add_index "orders", ["ordernumber"], :name => "index_orders_on_ordernumber"
  add_index "orders", ["ordertype"], :name => "index_orders_on_ordertype"
  add_index "orders", ["owner_id"], :name => "fk_orders_owner"
  add_index "orders", ["tenant_id"], :name => "index_orders_on_r_tenant"

  create_table "org_base", :force => true do |t|
    t.datetime "created",                      :null => false
    t.datetime "modified",                     :null => false
    t.integer  "modifiedby",     :limit => 20
    t.integer  "tenant_id",      :limit => 20, :null => false
    t.string   "name",                         :null => false
    t.integer  "addressinfo_id", :limit => 20
    t.integer  "secondary_id",   :limit => 20
    t.integer  "customer_id",    :limit => 20
    t.integer  "division_id",    :limit => 20
    t.string   "global_id",      :limit => 36
  end

  add_index "org_base", ["addressinfo_id"], :name => "fk_org_base_addressinfo"
  add_index "org_base", ["customer_id"], :name => "index_org_base_on_customer_id"
  add_index "org_base", ["division_id"], :name => "index_org_base_on_division_id"
  add_index "org_base", ["global_id"], :name => "index_org_base_on_external_id", :unique => true
  add_index "org_base", ["modifiedby"], :name => "fk_org_base_users"
  add_index "org_base", ["secondary_id"], :name => "index_org_base_on_secondary_id"
  add_index "org_base", ["tenant_id"], :name => "fk_org_base_tenants"

  create_table "org_connections", :force => true do |t|
    t.datetime "created",                   :null => false
    t.datetime "modified",                  :null => false
    t.integer  "modifiedby",  :limit => 20
    t.integer  "vendor_id",   :limit => 20, :null => false
    t.integer  "customer_id", :limit => 20, :null => false
  end

  add_index "org_connections", ["customer_id"], :name => "fk_org_connection_customer"
  add_index "org_connections", ["modifiedby"], :name => "fk_org_connections_users"
  add_index "org_connections", ["vendor_id", "customer_id"], :name => "idx_org_connections_unique", :unique => true

  create_table "org_customer", :primary_key => "org_id", :force => true do |t|
    t.integer "parent_id",    :limit => 20, :null => false
    t.string  "code"
    t.string  "contactemail"
    t.string  "contactname"
    t.integer "legacy_id",    :limit => 20
    t.integer "linked_id",    :limit => 20
  end

  add_index "org_customer", ["linked_id"], :name => "fk_customer_linked_org"
  add_index "org_customer", ["parent_id"], :name => "fk_org_customer_parent"

  create_table "org_division", :primary_key => "org_id", :force => true do |t|
    t.integer "parent_id",    :limit => 20, :null => false
    t.string  "code"
    t.string  "contactemail"
    t.string  "contactname"
    t.integer "legacy_id",    :limit => 20
    t.integer "linked_id",    :limit => 20
  end

  add_index "org_division", ["linked_id"], :name => "fk_division_linked_org"
  add_index "org_division", ["parent_id"], :name => "fk_org_division_parent"

  create_table "org_extendedfeatures", :id => false, :force => true do |t|
    t.integer "org_id",  :limit => 20, :null => false
    t.string  "feature",               :null => false
  end

  add_index "org_extendedfeatures", ["org_id"], :name => "fk_org_extendedfeatures_org_base"

  create_table "org_primary", :primary_key => "org_id", :force => true do |t|
    t.integer "asset_limit",              :limit => 20,   :null => false
    t.integer "diskspace_limit",          :limit => 20,   :null => false
    t.integer "user_limit",               :limit => 20,   :null => false
    t.string  "serialnumberformat",                       :null => false
    t.boolean "usingserialnumber",                        :null => false
    t.string  "website",                  :limit => 2056
    t.string  "certificatename"
    t.string  "defaulttimezone",                          :null => false
    t.string  "dateformat",                               :null => false
    t.integer "externalid",               :limit => 20
    t.integer "org_limit",                :limit => 20,   :null => false
    t.string  "externalpassword"
    t.string  "externalusername"
    t.boolean "autopublish"
    t.boolean "autoaccept",                               :null => false
    t.boolean "plansandpricingavailable",                 :null => false
    t.integer "defaultvendorcontext",     :limit => 20
  end

  create_table "org_secondary", :primary_key => "org_id", :force => true do |t|
    t.integer "primaryorg_id",   :limit => 20, :null => false
    t.string  "certificatename"
    t.string  "defaulttimezone",               :null => false
  end

  add_index "org_secondary", ["primaryorg_id"], :name => "fk_org_secondary_primaryorg"

  create_table "populatorlog", :primary_key => "uniqueid", :force => true do |t|
    t.datetime "timelogged"
    t.text     "logmessage"
    t.string   "logstatus",  :limit => 100
    t.string   "logtype",    :limit => 100
    t.integer  "tenant_id",  :limit => 20
  end

  add_index "populatorlog", ["tenant_id"], :name => "index_populatorlog_on_r_tenant"

  create_table "printouts", :force => true do |t|
    t.datetime "created",                           :null => false
    t.datetime "modified",                          :null => false
    t.text     "description",                       :null => false
    t.string   "name",               :limit => 100, :null => false
    t.string   "pdftemplate"
    t.boolean  "custom"
    t.string   "type"
    t.integer  "modifiedby",         :limit => 20
    t.integer  "tenant_id",          :limit => 20
    t.boolean  "withsubinspections"
  end

  add_index "printouts", ["custom"], :name => "index_printouts_on_custom"
  add_index "printouts", ["modifiedby"], :name => "index_printouts_on_modifiedby"
  add_index "printouts", ["tenant_id"], :name => "index_printouts_on_tenant_id"
  add_index "printouts", ["type"], :name => "index_printouts_on_type"

  create_table "productattachments", :force => true do |t|
    t.datetime "created",                  :null => false
    t.datetime "modified",                 :null => false
    t.integer  "modifiedby", :limit => 20
    t.integer  "tenant_id",  :limit => 20
    t.integer  "product_id", :limit => 20, :null => false
    t.text     "comment"
    t.string   "filename"
  end

  add_index "productattachments", ["product_id"], :name => "index_productattachments_on_product_id"
  add_index "productattachments", ["tenant_id"], :name => "fk_productattachments_tenants"

  create_table "productcodemapping", :primary_key => "uniqueid", :force => true do |t|
    t.integer "tenant_id",         :limit => 20
    t.string  "productcode"
    t.integer "r_productinfo",     :limit => 20, :null => false
    t.string  "customerrefnumber"
  end

  add_index "productcodemapping", ["r_productinfo"], :name => "index_productcodemapping_on_r_productinfo"
  add_index "productcodemapping", ["tenant_id"], :name => "fk_productcodemapping_organization"

  create_table "productcodemapping_infooption", :primary_key => "uniqueid", :force => true do |t|
    t.integer "r_infooption",         :limit => 20, :null => false
    t.integer "r_productcodemapping", :limit => 20, :null => false
    t.integer "r_tenant",             :limit => 20
  end

  add_index "productcodemapping_infooption", ["r_infooption"], :name => "index_productcodemapping_infooption_on_r_infooption"
  add_index "productcodemapping_infooption", ["r_productcodemapping"], :name => "index_productcodemapping_infooption_on_r_productcodemapping"

  create_table "products", :force => true do |t|
    t.datetime "created",                                :null => false
    t.datetime "modified"
    t.string   "rfidnumber",             :limit => 46
    t.integer  "tenant_id",              :limit => 20
    t.integer  "shoporder_id",           :limit => 20
    t.string   "serialnumber",           :limit => 50
    t.string   "comments",               :limit => 2047
    t.integer  "type_id",                :limit => 20
    t.integer  "productstatus_uniqueid", :limit => 20
    t.string   "mobileguid",             :limit => 36
    t.string   "customerrefnumber"
    t.integer  "identifiedby_uniqueid",  :limit => 20
    t.integer  "customerorder_id",       :limit => 20
    t.datetime "lastinspectiondate"
    t.string   "purchaseorder"
    t.datetime "identified",                             :null => false
    t.string   "location"
    t.integer  "modifiedby",             :limit => 20
    t.integer  "assigneduser_id",        :limit => 20
    t.string   "state",                                  :null => false
    t.string   "archivedserialnumber"
    t.integer  "owner_id",               :limit => 20,   :null => false
    t.boolean  "published",                              :null => false
    t.integer  "linked_id",              :limit => 20
    t.boolean  "countstowardslimit",                     :null => false
    t.integer  "network_id",             :limit => 20
  end

  add_index "products", ["assigneduser_id"], :name => "index_products_on_assigneduser_id"
  add_index "products", ["countstowardslimit"], :name => "index_products_on_countstowardslimit"
  add_index "products", ["customerorder_id"], :name => "index_products_on_customerorder_id"
  add_index "products", ["identifiedby_uniqueid"], :name => "index_products_on_identifiedby_uniqueid"
  add_index "products", ["linked_id"], :name => "fk_linked_product_id"
  add_index "products", ["mobileguid"], :name => "index_products_on_mobileguid"
  add_index "products", ["modifiedby"], :name => "index_products_on_modifiedby"
  add_index "products", ["network_id"], :name => "index_products_on_network_id"
  add_index "products", ["owner_id"], :name => "fk_products_owner"
  add_index "products", ["productstatus_uniqueid"], :name => "index_products_on_productstatus_uniqueid"
  add_index "products", ["published"], :name => "index_products_on_published"
  add_index "products", ["purchaseorder"], :name => "index_productserial_on_purchaseorder"
  add_index "products", ["rfidnumber"], :name => "index_products_on_rfidnumber"
  add_index "products", ["serialnumber"], :name => "index_products_on_serialnumber"
  add_index "products", ["shoporder_id"], :name => "index_products_on_shoporder_id"
  add_index "products", ["shoporder_id"], :name => "productserial_rordermaster_idx"
  add_index "products", ["state"], :name => "index_products_on_state"
  add_index "products", ["tenant_id"], :name => "smart_search_customer_ref_number"
  add_index "products", ["tenant_id"], :name => "smart_search_rfid"
  add_index "products", ["tenant_id"], :name => "smart_search_serial_number"
  add_index "products", ["type_id"], :name => "index_products_on_type_id"

  create_table "productserial_infooption", :primary_key => "uniqueid", :force => true do |t|
    t.integer "r_productserial", :limit => 20, :null => false
    t.integer "r_infooption",    :limit => 20, :null => false
  end

  add_index "productserial_infooption", ["r_infooption"], :name => "index_productserial_infooption_on_r_infooption"
  add_index "productserial_infooption", ["r_productserial"], :name => "index_productserial_infooption_on_r_productserial"

  create_table "productserialextension", :primary_key => "uniqueid", :force => true do |t|
    t.integer "tenant_id",      :limit => 20
    t.string  "extensionkey"
    t.string  "extensionlabel"
  end

  add_index "productserialextension", ["extensionkey", "tenant_id"], :name => "productserialextension_keypermanufacture", :unique => true
  add_index "productserialextension", ["tenant_id"], :name => "index_productserialextension_on_r_tenant"

  create_table "productserialextensionvalue", :primary_key => "uniqueid", :force => true do |t|
    t.integer "r_productserial",          :limit => 20,  :null => false
    t.integer "r_productserialextension", :limit => 20,  :null => false
    t.string  "extensionvalue",           :limit => 512
  end

  add_index "productserialextensionvalue", ["r_productserial", "r_productserialextension"], :name => "productserialextensionvalue_oneperproduct", :unique => true
  add_index "productserialextensionvalue", ["r_productserial"], :name => "index_productserialextensionvalue_on_r_productserial"
  add_index "productserialextensionvalue", ["r_productserialextension"], :name => "index_productserialextensionvalue_on_r_productserialextension"

  create_table "productstatus", :primary_key => "uniqueid", :force => true do |t|
    t.string   "name"
    t.integer  "tenant_id",    :limit => 20
    t.datetime "datecreated"
    t.datetime "datemodified"
    t.string   "modifiedby"
  end

  add_index "productstatus", ["tenant_id"], :name => "index_productstatus_on_r_tenant"

  create_table "producttypegroups", :force => true do |t|
    t.datetime "created",                  :null => false
    t.datetime "modified",                 :null => false
    t.integer  "modifiedby", :limit => 20
    t.integer  "tenant_id",  :limit => 20
    t.string   "name",       :limit => 40, :null => false
    t.integer  "orderidx",   :limit => 20, :null => false
  end

  add_index "producttypegroups", ["name", "tenant_id"], :name => "index_producttypegroups_on_r_tenant_and_name", :unique => true
  add_index "producttypegroups", ["tenant_id"], :name => "fk_producttypegroups_tenants"

  create_table "producttypes", :force => true do |t|
    t.string   "cautionurl"
    t.string   "instructions",               :limit => 2047
    t.string   "name",                                       :null => false
    t.string   "warnings",                   :limit => 2047
    t.datetime "created"
    t.datetime "modified"
    t.integer  "tenant_id",                  :limit => 20
    t.boolean  "hasmanufacturecertificate"
    t.string   "manufacturecertificatetext", :limit => 2001
    t.string   "descriptiontemplate"
    t.string   "imagename"
    t.integer  "modifiedby",                 :limit => 20
    t.string   "state",                                      :null => false
    t.string   "archivedname"
    t.integer  "group_id",                   :limit => 20
  end

  add_index "producttypes", ["group_id"], :name => "index_producttypes_on_group_id"
  add_index "producttypes", ["modifiedby"], :name => "index_producttypes_on_modifiedby"
  add_index "producttypes", ["name", "tenant_id"], :name => "productinfo_itemnumber_key", :unique => true
  add_index "producttypes", ["state"], :name => "index_producttypes_on_state"
  add_index "producttypes", ["tenant_id"], :name => "index_producttypes_on_r_tenant"

  create_table "producttypes_fileattachments", :id => false, :force => true do |t|
    t.integer "producttypes_id", :limit => 20, :null => false
    t.integer "attachments_id",  :limit => 20, :null => false
  end

  add_index "producttypes_fileattachments", ["attachments_id"], :name => "index_producttypes_fileattachments_on_attachments_id"
  add_index "producttypes_fileattachments", ["attachments_id"], :name => "producttypes_fileattachments_attachments_id_key", :unique => true
  add_index "producttypes_fileattachments", ["producttypes_id"], :name => "index_producttypes_fileattachments_on_producttypes_id"

  create_table "producttypes_producttypes", :id => false, :force => true do |t|
    t.integer "producttypes_id", :limit => 20, :null => false
    t.integer "subtypes_id",     :limit => 20, :null => false
  end

  add_index "producttypes_producttypes", ["producttypes_id"], :name => "fk_producttypes_producttypes_master_type"
  add_index "producttypes_producttypes", ["subtypes_id"], :name => "index_producttypes_producttypes_on_subtypes_id"

  create_table "producttypeschedules", :force => true do |t|
    t.integer  "tenant_id",         :limit => 20
    t.datetime "created",                         :null => false
    t.datetime "modified",                        :null => false
    t.integer  "modifiedby",        :limit => 20
    t.integer  "frequency",         :limit => 20
    t.integer  "producttype_id",    :limit => 20, :null => false
    t.boolean  "autoschedule"
    t.integer  "inspectiontype_id", :limit => 20
    t.integer  "owner_id",          :limit => 20
  end

  add_index "producttypeschedules", ["inspectiontype_id"], :name => "index_producttypeschedules_on_inspectiontype_id"
  add_index "producttypeschedules", ["modifiedby"], :name => "index_producttypeschedules_on_modifiedby"
  add_index "producttypeschedules", ["owner_id"], :name => "fk_producttypeschedules_owner"
  add_index "producttypeschedules", ["producttype_id"], :name => "index_producttypeschedules_on_producttype_id"
  add_index "producttypeschedules", ["tenant_id"], :name => "index_producttypeschedules_on_r_tenant"

  create_table "projects", :force => true do |t|
    t.datetime "created",                             :null => false
    t.datetime "modified",                            :null => false
    t.integer  "modifiedby",          :limit => 20
    t.integer  "tenant_id",           :limit => 20
    t.string   "projectid",                           :null => false
    t.string   "name",                                :null => false
    t.string   "status",                              :null => false
    t.datetime "started"
    t.datetime "estimatedcompletion"
    t.datetime "actualcompletion"
    t.string   "duration"
    t.boolean  "retired"
    t.string   "description",         :limit => 1001
    t.string   "workperformed",       :limit => 1001
    t.string   "ponumber"
    t.boolean  "open"
    t.boolean  "eventjob",                            :null => false
    t.integer  "owner_id",            :limit => 20
  end

  add_index "projects", ["name"], :name => "index_projects_on_name"
  add_index "projects", ["owner_id"], :name => "fk_projects_owner"
  add_index "projects", ["projectid", "tenant_id"], :name => "index_projects_on_r_tenant_and_projectid", :unique => true
  add_index "projects", ["status"], :name => "index_projects_on_status"
  add_index "projects", ["tenant_id"], :name => "index_projects_on_r_tenant"

  create_table "projects_fileattachments", :id => false, :force => true do |t|
    t.integer "projects_id", :limit => 20, :null => false
    t.integer "notes_id",    :limit => 20, :null => false
  end

  add_index "projects_fileattachments", ["notes_id"], :name => "index_projects_fileattachments_on_notes_id"
  add_index "projects_fileattachments", ["notes_id"], :name => "projects_fileattachments_notes_id_key", :unique => true
  add_index "projects_fileattachments", ["projects_id"], :name => "fk_projects_fileattachments_projects"

  create_table "projects_products", :id => false, :force => true do |t|
    t.integer "projects_id", :limit => 20, :null => false
    t.integer "products_id", :limit => 20, :null => false
    t.integer "orderidx",    :limit => 20
  end

  add_index "projects_products", ["products_id", "projects_id"], :name => "index_projects_products_on_projects_id_and_products_id"
  add_index "projects_products", ["products_id"], :name => "index_projects_products_on_products_id"
  add_index "projects_products", ["projects_id"], :name => "index_projects_products_on_projects_id"

  create_table "projects_users", :id => false, :force => true do |t|
    t.integer "projects_id",        :limit => 20, :null => false
    t.integer "resources_uniqueid", :limit => 20, :null => false
  end

  add_index "projects_users", ["projects_id", "resources_uniqueid"], :name => "index_projects_users_on_projects_id_and_resources_uniqueid", :unique => true
  add_index "projects_users", ["projects_id"], :name => "index_projects_users_on_projects_id"
  add_index "projects_users", ["resources_uniqueid"], :name => "index_projects_users_on_resources_uniqueid"

  create_table "promocode_extendedfeatures", :force => true do |t|
    t.integer "promocode_id", :limit => 20, :null => false
    t.string  "feature",                    :null => false
  end

  add_index "promocode_extendedfeatures", ["promocode_id"], :name => "fk_promocode_extendedfeatures_promocodes"

  create_table "promocodes", :force => true do |t|
    t.datetime "created",                           :null => false
    t.datetime "modified",                          :null => false
    t.integer  "modifiedby",          :limit => 20
    t.string   "code",                              :null => false
    t.integer  "diskspace_limit",     :limit => 20, :null => false
    t.integer  "asset_limit",         :limit => 20
    t.integer  "secondary_org_limit", :limit => 20, :null => false
  end

  add_index "promocodes", ["code"], :name => "index_promocodes_on_code", :unique => true

  create_table "requesttransactions", :force => true do |t|
    t.string   "name",                     :null => false
    t.string   "type",                     :null => false
    t.datetime "created",                  :null => false
    t.datetime "modified",                 :null => false
    t.integer  "modifiedby", :limit => 20
    t.integer  "tenant_id",  :limit => 20
  end

  add_index "requesttransactions", ["modifiedby"], :name => "fk_requesttransactions_users"
  add_index "requesttransactions", ["name"], :name => "index_requesttransactions_on_name", :unique => true
  add_index "requesttransactions", ["tenant_id"], :name => "fk_requesttransactions_organization"

  create_table "savedreports", :force => true do |t|
    t.datetime "created",                     :null => false
    t.datetime "modified",                    :null => false
    t.integer  "modifiedby",    :limit => 20
    t.integer  "tenant_id",     :limit => 20
    t.string   "name",                        :null => false
    t.integer  "user_id",       :limit => 20
    t.string   "sortcolumn",                  :null => false
    t.string   "sortdirection"
    t.string   "sharedbyname"
  end

  add_index "savedreports", ["modifiedby"], :name => "index_savedreports_on_modifiedby"
  add_index "savedreports", ["tenant_id"], :name => "index_savedreports_on_r_tenant"
  add_index "savedreports", ["user_id"], :name => "index_savedreports_on_owner_uniqueid"

  create_table "savedreports_columns", :id => false, :force => true do |t|
    t.integer "savedreports_id", :limit => 20, :null => false
    t.string  "element"
    t.integer "idx",             :limit => 20
  end

  add_index "savedreports_columns", ["savedreports_id"], :name => "index_savedreports_columns_on_savedreports_id"

  create_table "savedreports_criteria", :id => false, :force => true do |t|
    t.integer "savedreports_id", :limit => 20, :null => false
    t.string  "element"
    t.string  "mapkey",                        :null => false
  end

  add_index "savedreports_criteria", ["savedreports_id"], :name => "fk_savedreports_criteria_savedreports"

  create_table "seenitstorageitem", :force => true do |t|
    t.datetime "created",                  :null => false
    t.datetime "modified",                 :null => false
    t.integer  "modifiedby", :limit => 20
    t.integer  "userid",     :limit => 20
  end

  add_index "seenitstorageitem", ["modifiedby"], :name => "fk_seenitstorageitem_users"
  add_index "seenitstorageitem", ["userid"], :name => "index_seenitstorageitem_on_userid", :unique => true

  create_table "seenitstorageitem_itemsseen", :primary_key => "element", :force => true do |t|
    t.integer "seenitstorageitem_id", :limit => 20, :default => 0, :null => false
  end

  create_table "serialnumbercounter", :primary_key => "uniqueid", :force => true do |t|
    t.integer  "tenant_id",     :limit => 20
    t.integer  "counter",       :limit => 20
    t.string   "decimalformat"
    t.integer  "daystoreset",   :limit => 20
    t.datetime "lastreset"
  end

  add_index "serialnumbercounter", ["tenant_id"], :name => "one_counter_per_man", :unique => true

  create_table "setupdatalastmoddates", :id => false, :force => true do |t|
    t.integer  "tenant_id",       :limit => 20
    t.datetime "producttypes",                  :null => false
    t.datetime "inspectiontypes",               :null => false
    t.datetime "autoattributes",                :null => false
    t.datetime "owners",                        :null => false
    t.datetime "jobs"
  end

  add_index "setupdatalastmoddates", ["tenant_id"], :name => "idx_setupdatalastmoddates_tenantid", :unique => true
  add_index "setupdatalastmoddates", ["tenant_id"], :name => "index_setupdatalastmoddates_on_r_tenant"

  create_table "signupreferrals", :force => true do |t|
    t.datetime "signupdate",                       :null => false
    t.integer  "referral_tenant_id", :limit => 20, :null => false
    t.integer  "referred_tenant_id", :limit => 20, :null => false
    t.integer  "referral_user_id",   :limit => 20, :null => false
  end

  add_index "signupreferrals", ["referral_tenant_id"], :name => "signupreferrals_referral_tenant"
  add_index "signupreferrals", ["referral_user_id"], :name => "signupreferrals_referral_user"
  add_index "signupreferrals", ["referred_tenant_id"], :name => "index_signupreferrals_on_referred_tenant_id", :unique => true

  create_table "states", :force => true do |t|
    t.integer  "tenant_id",   :limit => 20
    t.datetime "created",                                      :null => false
    t.datetime "modified",                                     :null => false
    t.integer  "modifiedby",  :limit => 20
    t.string   "displaytext",                                  :null => false
    t.string   "status",                                       :null => false
    t.string   "buttonname",                                   :null => false
    t.boolean  "retired",                   :default => false
  end

  add_index "states", ["modifiedby"], :name => "index_states_on_modifiedby"
  add_index "states", ["tenant_id"], :name => "index_states_on_r_tenant"

  create_table "statesets", :force => true do |t|
    t.integer  "tenant_id",  :limit => 20
    t.datetime "created",                                     :null => false
    t.datetime "modified",                                    :null => false
    t.integer  "modifiedby", :limit => 20
    t.string   "name",                                        :null => false
    t.boolean  "retired",                  :default => false
  end

  add_index "statesets", ["modifiedby"], :name => "index_statesets_on_modifiedby"
  add_index "statesets", ["tenant_id"], :name => "index_statesets_on_r_tenant"

  create_table "statesets_states", :id => false, :force => true do |t|
    t.integer "statesets_id", :limit => 20, :null => false
    t.integer "states_id",    :limit => 20, :null => false
    t.integer "orderidx",     :limit => 20, :null => false
  end

  add_index "statesets_states", ["states_id"], :name => "index_statesets_states_on_states_id"
  add_index "statesets_states", ["states_id"], :name => "statesets_states_states_id_key", :unique => true
  add_index "statesets_states", ["statesets_id"], :name => "fk_statesets_states_statesets"

  create_table "subproducts", :force => true do |t|
    t.datetime "created",                        :null => false
    t.datetime "modified",                       :null => false
    t.integer  "modifiedby",       :limit => 20
    t.integer  "product_id",       :limit => 20, :null => false
    t.integer  "masterproduct_id", :limit => 20, :null => false
    t.string   "label"
    t.integer  "weight",           :limit => 20
  end

  add_index "subproducts", ["masterproduct_id"], :name => "index_subproducts_on_masterproduct_id"
  add_index "subproducts", ["product_id"], :name => "fk_subproducts_subproduct"

  create_table "tagoptions", :force => true do |t|
    t.integer  "tenant_id",         :limit => 20
    t.datetime "created",                         :null => false
    t.datetime "modified",                        :null => false
    t.integer  "modifiedby",        :limit => 20
    t.string   "optionkey"
    t.integer  "weight",            :limit => 20, :null => false
    t.string   "text"
    t.string   "resolverclassname"
  end

  add_index "tagoptions", ["modifiedby"], :name => "fk_tagoptions_users"
  add_index "tagoptions", ["tenant_id"], :name => "fk_tagoptions_organization"

  create_table "tasks", :force => true do |t|
    t.datetime "created",        :null => false
    t.datetime "modified",       :null => false
    t.string   "classname",      :null => false
    t.string   "cronexpression", :null => false
    t.string   "taskgroup"
    t.boolean  "enabled",        :null => false
  end

  create_table "tenants", :force => true do |t|
    t.string "name", :null => false
  end

  create_table "typedorgconnections", :force => true do |t|
    t.datetime "created",                        :null => false
    t.datetime "modified",                       :null => false
    t.integer  "modifiedby",       :limit => 20
    t.integer  "tenant_id",        :limit => 20, :null => false
    t.integer  "owner_id",         :limit => 20, :null => false
    t.integer  "connectedorg_id",  :limit => 20, :null => false
    t.string   "connectiontype",                 :null => false
    t.integer  "orgconnection_id", :limit => 20
  end

  add_index "typedorgconnections", ["connectedorg_id"], :name => "fk_typedorgconnection_connectedorg"
  add_index "typedorgconnections", ["modifiedby"], :name => "fk_typedorgconnections_users"
  add_index "typedorgconnections", ["orgconnection_id"], :name => "fk_typedorgconnections_org_connections"
  add_index "typedorgconnections", ["owner_id", "connectedorg_id", "connectiontype"], :name => "index_typedorgconnection_unique", :unique => true
  add_index "typedorgconnections", ["tenant_id"], :name => "fk_typedorgconnections_tenants"

  create_table "unitofmeasures", :force => true do |t|
    t.datetime "created",                              :null => false
    t.datetime "modified",                             :null => false
    t.integer  "modifiedby",             :limit => 20
    t.string   "name",                                 :null => false
    t.string   "type"
    t.string   "shortname"
    t.boolean  "selectable"
    t.integer  "child_unitofmeasure_id", :limit => 20
  end

  add_index "unitofmeasures", ["child_unitofmeasure_id"], :name => "index_unitofmeasures_on_child_unitofmeasure_id"

  create_table "userrequest", :force => true do |t|
    t.integer  "tenant_id",     :limit => 20
    t.string   "companyname"
    t.string   "phonenumber",   :limit => 30
    t.integer  "r_useraccount", :limit => 20, :null => false
    t.text     "comment"
    t.datetime "modified"
    t.datetime "created"
    t.integer  "modifiedby",    :limit => 20
    t.string   "city"
  end

  add_index "userrequest", ["modifiedby"], :name => "index_userrequest_on_modifiedby"
  add_index "userrequest", ["r_useraccount"], :name => "index_userrequest_on_r_useraccount"
  add_index "userrequest", ["tenant_id"], :name => "index_userrequest_on_r_tenant"

  create_table "users", :primary_key => "uniqueid", :force => true do |t|
    t.datetime "datemodified"
    t.datetime "datecreated"
    t.string   "modifiedby",             :limit => 50
    t.string   "userid",                 :limit => 15
    t.string   "firstname",              :limit => 30
    t.string   "lastname",               :limit => 30
    t.string   "emailaddress",           :limit => 50
    t.integer  "tenant_id",              :limit => 20
    t.boolean  "deleted"
    t.string   "hashpassword"
    t.string   "timezoneid"
    t.string   "position"
    t.boolean  "active",                               :default => true
    t.string   "initials"
    t.string   "resetpasswordkey"
    t.string   "hashsecuritycardnumber"
    t.boolean  "system",                                                 :null => false
    t.boolean  "admin"
    t.integer  "permissions",            :limit => 20,                   :null => false
    t.string   "archiveduserid",         :limit => 15
    t.integer  "externalid",             :limit => 20
    t.integer  "owner_id",               :limit => 20
    t.string   "referralkey",            :limit => 10,                   :null => false
  end

  add_index "users", ["owner_id"], :name => "fk_users_owner"
  add_index "users", ["referralkey"], :name => "index_users_on_referralkey", :unique => true
  add_index "users", ["tenant_id", "userid"], :name => "fieldiduser_idx"
  add_index "users", ["tenant_id", "userid"], :name => "uniqueuseridrtenant", :unique => true
  add_index "users", ["tenant_id"], :name => "index_users_on_r_tenant"

end
