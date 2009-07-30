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

ActiveRecord::Schema.define(:version => 200907271130) do

  create_table "addproducthistory", :primary_key => "uniqueid", :force => true do |t|
    t.integer "r_fieldiduser",                  :null => false
    t.integer "r_division"
    t.integer "r_producttype"
    t.integer "r_owner"
    t.integer "r_productstatus"
    t.string  "purchaseorder",   :limit => 255
    t.string  "location",        :limit => 255
    t.integer "r_jobsite",       :limit => nil
    t.integer "assigneduser_id", :limit => nil
  end


  create_table "addproducthistory_infooption", :force => true do |t|
    t.integer "r_addproducthistory", :null => false
    t.integer "r_infooption",        :null => false
  end


  create_table "addressinfo", :force => true do |t|
    t.datetime "created"
    t.datetime "modified"
    t.string   "streetaddress", :limit => 255
    t.string   "city",          :limit => 255
    t.string   "state",         :limit => 255
    t.string   "country",       :limit => 255
    t.string   "zip",           :limit => 255
    t.string   "phone1",        :limit => 255
    t.string   "fax1",          :limit => 255
    t.integer  "modifiedby"
    t.string   "phone2",        :limit => 255
  end


  create_table "alertstatus", :id => false, :force => true do |t|
    t.integer "r_tenant",  :null => false
    t.integer "diskspace", :null => false
  end


  create_table "autoattributecriteria", :force => true do |t|
    t.integer  "r_producttype", :null => false
    t.integer  "r_tenant",      :null => false
    t.datetime "created"
    t.datetime "modified"
    t.integer  "modifiedby"
  end


  create_table "autoattributecriteria_inputinfofield", :force => true do |t|
    t.integer "r_infofield",             :null => false
    t.integer "r_autoattributecriteria", :null => false
  end


  create_table "autoattributecriteria_outputinfofield", :force => true do |t|
    t.integer "r_infofield",             :null => false
    t.integer "r_autoattributecriteria", :null => false
  end


  create_table "autoattributedefinition", :force => true do |t|
    t.integer  "r_autoattributecriteria", :null => false
    t.integer  "r_tenant",                :null => false
    t.datetime "created"
    t.datetime "modified"
    t.integer  "modifiedby"
  end


  create_table "autoattributedefinition_inputinfooption", :force => true do |t|
    t.integer "r_infooption",              :null => false
    t.integer "r_autoattributedefinition", :null => false
  end


  create_table "autoattributedefinition_outputinfooption", :force => true do |t|
    t.integer "r_infooption",              :null => false
    t.integer "r_autoattributedefinition", :null => false
  end


  create_table "catalogs", :force => true do |t|
    t.datetime "created",                   :null => false
    t.datetime "modified",                  :null => false
    t.integer  "modifiedby", :limit => nil
    t.integer  "r_tenant",   :limit => nil, :null => false
  end


  create_table "catalogs_inspectiontypes", :id => false, :force => true do |t|
    t.integer "catalogs_id",                 :limit => nil, :null => false
    t.integer "publishedinspectiontypes_id", :limit => nil, :null => false
  end


  create_table "catalogs_producttypes", :id => false, :force => true do |t|
    t.integer "catalogs_id",              :limit => nil, :null => false
    t.integer "publishedproducttypes_id", :limit => nil, :null => false
  end


  create_table "commenttemplate", :primary_key => "uniqueid", :force => true do |t|
    t.timestamp "datecreated" 
    t.timestamp "datemodified"
    t.string    "modifiedby",   :limit => 50
    t.string    "templateid",   :limit => 50
    t.string    "contents",     :limit => 255
    t.integer   "r_tenant"
  end


  create_table "configurations", :force => true do |t|
    t.datetime "created",                   :null => false
    t.datetime "modified",                  :null => false
    t.integer  "modifiedby"
    t.string   "key",        :limit => 255, :null => false
    t.string   "value",      :limit => 255, :null => false
    t.integer  "tenantid"
  end


  create_table "criteria", :force => true do |t|
    t.integer  "r_tenant",                                      :null => false
    t.datetime "created",                                       :null => false
    t.datetime "modified",                                      :null => false
    t.integer  "modifiedby"
    t.string   "displaytext", :limit => 255,                    :null => false
    t.integer  "states_id",                                     :null => false
    t.boolean  "principal",                  :default => false, :null => false
    t.boolean  "retired",                    :default => false
  end


  create_table "criteria_deficiencies", :id => false, :force => true do |t|
    t.integer "criteria_id",                :null => false
    t.string  "text",        :limit => 511, :null => false
    t.integer "orderidx",    :limit => nil, :null => false
  end

  create_table "criteria_recommendations", :id => false, :force => true do |t|
    t.integer "criteria_id",                :null => false
    t.string  "text",        :limit => 511, :null => false
    t.integer "orderidx",    :limit => nil, :null => false
  end

  create_table "criteriaresults", :force => true do |t|
    t.integer  "r_tenant",      :null => false
    t.datetime "created",       :null => false
    t.datetime "modified",      :null => false
    t.integer  "modifiedby"
    t.integer  "state_id",      :null => false
    t.integer  "criteria_id",   :null => false
    t.integer  "inspection_id", :null => false
  end


  create_table "criteriaresults_deficiencies", :id => false, :force => true do |t|
    t.integer "criteriaresults_id",                :null => false
    t.integer "deficiencies_id",                   :null => false
    t.integer "orderidx",           :limit => nil, :null => false
  end


  create_table "criteriaresults_recommendations", :id => false, :force => true do |t|
    t.integer "criteriaresults_id",                :null => false
    t.integer "recommendations_id",                :null => false
    t.integer "orderidx",           :limit => nil, :null => false
  end


  create_table "criteriasections", :force => true do |t|
    t.integer  "r_tenant",                                     :null => false
    t.datetime "created",                                      :null => false
    t.datetime "modified",                                     :null => false
    t.integer  "modifiedby"
    t.string   "title",      :limit => 255,                    :null => false
    t.boolean  "retired",                   :default => false
  end


  create_table "criteriasections_criteria", :id => false, :force => true do |t|
    t.integer "criteriasections_id",                :null => false
    t.integer "criteria_id",                        :null => false
    t.integer "orderidx",            :limit => nil, :null => false
  end


  create_table "customers", :force => true do |t|
    t.timestamp "created"
    t.timestamp "modified"
    t.string    "name",           :limit => 255, :null => false
    t.integer   "r_tenant"
    t.string    "contactemail",   :limit => 255
    t.string    "customerid",     :limit => 255, :null => false
    t.integer   "modifiedby",     :limit => nil
    t.string    "contactname",    :limit => 255
    t.integer   "addressinfo_id", :limit => nil
  end


  create_table "divisions", :force => true do |t|
    t.string   "name",           :limit => 255
    t.integer  "customer_id",                   :null => false
    t.datetime "created"
    t.datetime "modified"
    t.integer  "modifiedby",     :limit => nil
    t.integer  "r_tenant",       :limit => nil, :null => false
    t.string   "contactname"
    t.string   "contactemail"
    t.integer  "addressinfo_id"
    t.string   "divisionid"
  end


  create_table "eulaacceptances", :force => true do |t|
    t.datetime "created",           :null => false
    t.datetime "modified",          :null => false
    t.integer  "modifiedby"
    t.integer  "r_tenant",          :null => false
    t.integer  "acceptor_uniqueid", :null => false
    t.integer  "eula_id",           :null => false
    t.datetime "date",              :null => false
  end


  create_table "eulas", :force => true do |t|
    t.datetime "created",       :null => false
    t.datetime "modified",      :null => false
    t.integer  "modifiedby"
    t.text     "legaltext",     :null => false
    t.datetime "effectivedate", :null => false
    t.string   "version",       :null => false
  end

  create_table "fileattachments", :force => true do |t|
    t.integer  "r_tenant",                  :null => false
    t.datetime "created",                   :null => false
    t.datetime "modified",                  :null => false
    t.integer  "modifiedby"
    t.string   "filename",   :limit => 255
    t.text     "comments"
  end


  create_table "findproductoption", :primary_key => "uniqueid", :force => true do |t|
    t.string "key",         :limit => 255
    t.string "value",       :limit => 255
    t.text   "description"
  end


  create_table "findproductoption_manufacture", :primary_key => "uniqueid", :force => true do |t|
    t.integer   "r_findproductoption",                               :null => false
    t.integer   "r_tenant",                                          :null => false
    t.integer   "weight",                                            :null => false
    t.integer   "mobileweight",        :limit => nil, :default => 0
    t.timestamp "datemodified"
    t.datetime  "datecreated"
  end


  create_table "infofield", :primary_key => "uniqueid", :force => true do |t|
    t.string  "name",               :limit => 255, :null => false
    t.integer "r_productinfo",                     :null => false
    t.boolean "required"
    t.integer "weight"
    t.integer "r_unitofmeasure"
    t.boolean "usingunitofmeasure"
    t.string  "fieldtype",          :limit => 50
    t.boolean "retired"
  end


  create_table "infooption", :primary_key => "uniqueid", :force => true do |t|
    t.string  "name",        :limit => 255, :null => false
    t.integer "r_infofield",                :null => false
    t.boolean "staticdata"
    t.integer "weight"
  end


  create_table "inspectionbooks", :force => true do |t|
    t.integer  "r_tenant",                   :null => false
    t.datetime "created",                    :null => false
    t.datetime "modified",                   :null => false
    t.integer  "legacyid"
    t.integer  "modifiedby"
    t.string   "name",        :limit => 255, :null => false
    t.integer  "customer_id"
    t.boolean  "open",                       :null => false
  end


  create_table "inspectiongroups", :force => true do |t|
    t.integer  "r_tenant",                  :null => false
    t.datetime "created",                   :null => false
    t.datetime "modified",                  :null => false
    t.integer  "modifiedby"
    t.string   "mobileguid", :limit => 255
  end


  create_table "inspections", :force => true do |t|
    t.integer  "r_tenant",                    :null => false
    t.datetime "created",                     :null => false
    t.datetime "modified",                    :null => false
    t.integer  "modifiedby"
    t.string   "comments",    :limit => 2500
    t.integer  "type_id",                     :null => false
    t.integer  "product_id",                  :null => false
    t.integer  "formversion", :limit => nil,  :null => false
  end


  create_table "inspections_fileattachments", :id => false, :force => true do |t|
    t.integer "inspections_id", :null => false
    t.integer "attachments_id", :null => false
  end


  create_table "inspections_infooptionmap", :id => false, :force => true do |t|
    t.integer "inspections_id",                :null => false
    t.string  "element",        :limit => 255
    t.string  "mapkey",         :limit => 255, :null => false
  end

  create_table "inspectionschedules", :force => true do |t|
    t.integer  "r_tenant",                                :null => false
    t.datetime "created",                                 :null => false
    t.datetime "modified",                                :null => false
    t.integer  "modifiedby"
    t.integer  "product_id",                              :null => false
    t.datetime "nextdate",                                :null => false
    t.integer  "inspectiontype_id",        :limit => nil
    t.datetime "completeddate"
    t.string   "status",                   :limit => 255, :null => false
    t.integer  "inspection_inspection_id", :limit => nil
    t.string   "state",                    :limit => 255, :null => false
    t.integer  "project_id",               :limit => nil
    t.integer  "division_id",              :limit => nil
    t.integer  "customer_id",              :limit => nil
    t.integer  "jobsite_id",               :limit => nil
    t.string   "location",                 :limit => 255
  end


  create_table "inspectionsmaster", :id => false, :force => true do |t|
    t.integer  "inspection_id",                     :null => false
    t.string   "location",           :limit => 255
    t.datetime "date",                              :null => false
    t.boolean  "printable",                         :null => false
    t.string   "prooftesttype",      :limit => 255
    t.string   "peakload",           :limit => 255
    t.string   "duration",           :limit => 255
    t.string   "status",             :limit => 255, :null => false
    t.integer  "division_id"
    t.integer  "inspector_uniqueid",                :null => false
    t.integer  "group_id",                          :null => false
    t.integer  "book_id"
    t.integer  "customer_id"
    t.integer  "jobsite_id"
    t.integer  "organization_id",                   :null => false
    t.string   "state",              :limit => 255, :null => false
    t.string   "peakloadduration",   :limit => 255
  end


  create_table "inspectionsmaster_inspectionssub", :id => false, :force => true do |t|
    t.integer "inspectionsmaster_inspection_id",                :null => false
    t.integer "subinspections_inspection_id",                   :null => false
    t.integer "orderidx",                        :limit => nil, :null => false
  end


  create_table "inspectionssub", :id => false, :force => true do |t|
    t.integer "inspection_id",                :null => false
    t.string  "name",          :limit => 255
  end


  create_table "inspectiontypegroups", :force => true do |t|
    t.integer  "r_tenant",                              :null => false
    t.datetime "created",                               :null => false
    t.datetime "modified",                              :null => false
    t.integer  "modifiedby"
    t.string   "name",                   :limit => 255, :null => false
    t.string   "reporttitle",            :limit => 255
    t.integer  "printout_id",            :limit => nil
    t.integer  "observationprintout_id", :limit => nil
  end


  create_table "inspectiontypes", :force => true do |t|
    t.integer  "r_tenant",                                        :null => false
    t.datetime "created",                                         :null => false
    t.datetime "modified",                                        :null => false
    t.integer  "modifiedby"
    t.string   "name",          :limit => 255,                    :null => false
    t.integer  "group_id",                                        :null => false
    t.string   "description",   :limit => 255
    t.boolean  "printable",                    :default => false, :null => false
    t.integer  "legacyeventid"
    t.boolean  "retired",                      :default => false
    t.boolean  "master",                                          :null => false
    t.integer  "formversion",   :limit => nil,                    :null => false
  end


  create_table "inspectiontypes_criteriasections", :id => false, :force => true do |t|
    t.integer "inspectiontypes_id",                :null => false
    t.integer "sections_id",                       :null => false
    t.integer "orderidx",           :limit => nil, :null => false
  end


  create_table "inspectiontypes_infofieldnames", :id => false, :force => true do |t|
    t.integer "inspectiontypes_id",                :null => false
    t.string  "element",            :limit => 255
    t.integer "orderidx",           :limit => nil, :null => false
  end

  create_table "inspectiontypes_supportedprooftests", :id => false, :force => true do |t|
    t.integer "inspectiontypes_id",                :null => false
    t.string  "element",            :limit => 255
  end


  create_table "instructionalvideos", :force => true do |t|
    t.datetime "created",                   :null => false
    t.datetime "modified",                  :null => false
    t.integer  "modifiedby"
    t.string   "name",       :limit => 255, :null => false
    t.string   "url",        :limit => 255, :null => false
  end

  create_table "jobsites", :force => true do |t|
    t.integer  "r_tenant",                  :null => false
    t.datetime "created",                   :null => false
    t.datetime "modified",                  :null => false
    t.integer  "modifiedby"
    t.string   "name",       :limit => 255, :null => false
    t.integer  "r_customer"
    t.integer  "r_division"
  end


  create_table "legacybuttonstatemappings", :id => false, :force => true do |t|
    t.integer "buttonstateid", :null => false
    t.integer "r_tenant",      :null => false
    t.integer "criteria_id",   :null => false
    t.integer "state_id",      :null => false
  end

  create_table "lineitems", :force => true do |t|
    t.integer  "r_tenant",                   :null => false
    t.datetime "created",                    :null => false
    t.datetime "modified",                   :null => false
    t.integer  "modifiedby"
    t.integer  "order_id",                   :null => false
    t.integer  "index",                      :null => false
    t.string   "description", :limit => 512
    t.integer  "quantity",    :limit => nil, :null => false
    t.string   "lineid",      :limit => 255
    t.string   "productcode", :limit => 255, :null => false
  end


  create_table "notificationsettings", :force => true do |t|
    t.datetime "created",                     :null => false
    t.datetime "modified",                    :null => false
    t.integer  "modifiedby",   :limit => nil
    t.integer  "r_tenant",     :limit => nil, :null => false
    t.string   "frequency",    :limit => 255, :null => false
    t.string   "name",         :limit => 255, :null => false
    t.string   "periodend",    :limit => 255, :null => false
    t.string   "periodstart",  :limit => 255, :null => false
    t.boolean  "usingjobsite",                :null => false
    t.integer  "user_id",      :limit => nil, :null => false
  end


  create_table "notificationsettings_addresses", :id => false, :force => true do |t|
    t.integer "notificationsettings_id", :limit => nil, :null => false
    t.string  "addr",                    :limit => 255, :null => false
    t.integer "orderidx",                :limit => nil, :null => false
  end


  create_table "notificationsettings_inspectiontypes", :id => false, :force => true do |t|
    t.integer "notificationsettings_id", :limit => nil, :null => false
    t.integer "inspectiontype_id",       :limit => nil, :null => false
    t.integer "orderidx",                :limit => nil, :null => false
  end


  create_table "notificationsettings_owner", :force => true do |t|
    t.integer "notificationsettings_id", :limit => nil, :null => false
    t.integer "customer_id",             :limit => nil
    t.integer "division_id",             :limit => nil
    t.integer "jobsite_id",              :limit => nil
  end


  create_table "notificationsettings_producttypes", :id => false, :force => true do |t|
    t.integer "notificationsettings_id", :limit => nil, :null => false
    t.integer "producttype_id",          :limit => nil, :null => false
    t.integer "orderidx",                :limit => nil, :null => false
  end


  create_table "observations", :force => true do |t|
    t.integer  "r_tenant",                   :null => false
    t.datetime "created",                    :null => false
    t.datetime "modified",                   :null => false
    t.integer  "modifiedby"
    t.string   "type",       :limit => 255,  :null => false
    t.string   "text",       :limit => 1000, :null => false
    t.string   "state",      :limit => 255,  :null => false
  end


  create_table "ordermapping", :primary_key => "uniqueid", :force => true do |t|
    t.string "organizationid",   :limit => 255
    t.string "externalsourceid", :limit => 255
    t.string "orderkey",         :limit => 255
    t.string "sourceorderkey",   :limit => 255
  end


  create_table "orders", :force => true do |t|
    t.integer  "r_tenant",                   :null => false
    t.datetime "created",                    :null => false
    t.datetime "modified",                   :null => false
    t.integer  "modifiedby"
    t.string   "ordernumber", :limit => 255, :null => false
    t.string   "ordertype",   :limit => 255, :null => false
    t.datetime "orderdate"
    t.integer  "customer_id"
    t.integer  "division_id"
    t.string   "description", :limit => 512
    t.string   "ponumber",    :limit => 255
  end


  create_table "organization", :force => true do |t|
    t.string   "displayname",          :limit => 255
    t.string   "certificatename",      :limit => 255
    t.datetime "created"
    t.datetime "modified"
    t.string   "type",                 :limit => 255
    t.integer  "parent_id"
    t.integer  "r_addressinfo"
    t.string   "name",                 :limit => 255
    t.boolean  "usingserialnumber",                    :default => true, :null => false
    t.string   "adminemail",           :limit => 255
    t.string   "serialnumberformat",   :limit => 255
    t.integer  "modifiedby"
    t.integer  "r_tenant"
    t.string   "fidac",                :limit => 8
    t.string   "accountdiscriminator", :limit => 255
    t.string   "dateformat",           :limit => 255
    t.string   "website",              :limit => 2056
    t.integer  "diskspace_limit"
    t.integer  "user_limit"
  end


  create_table "organization_extendedfeatures", :id => false, :force => true do |t|
    t.integer "organization_id",                :null => false
    t.string  "element",         :limit => 255
  end


  create_table "populatorlog", :primary_key => "uniqueid", :force => true do |t|
    t.timestamp "timelogged"
    t.text      "logmessage"
    t.string    "logstatus",  :limit => 100
    t.string    "logtype",    :limit => 100
    t.integer   "r_tenant"
  end


  create_table "printouts", :force => true do |t|
    t.datetime "created",                           :null => false
    t.datetime "modified",                          :null => false
    t.text     "description",                       :null => false
    t.string   "name",               :limit => 100, :null => false
    t.string   "pdftemplate",        :limit => 255
    t.boolean  "custom"
    t.string   "type",               :limit => 255
    t.integer  "modifiedby",         :limit => nil
    t.integer  "tenant_id",          :limit => nil
    t.boolean  "withsubinspections"
  end


  create_table "productattachments", :force => true do |t|
    t.datetime "created",    :null => false
    t.datetime "modified",   :null => false
    t.integer  "modifiedby"
    t.integer  "r_tenant",   :null => false
    t.integer  "product_id", :null => false
    t.text     "comment"
    t.string   "filename"
  end


  create_table "productcodemapping", :primary_key => "uniqueid", :force => true do |t|
    t.integer "r_tenant",                         :null => false
    t.string  "productcode",       :limit => 255
    t.integer "r_productinfo",                    :null => false
    t.string  "customerrefnumber", :limit => 255
  end


  create_table "productcodemapping_infooption", :primary_key => "uniqueid", :force => true do |t|
    t.integer "r_infooption",         :null => false
    t.integer "r_productcodemapping", :null => false
    t.integer "r_tenant"
  end


  create_table "products", :force => true do |t|
    t.timestamp "created",                    :null => false
    t.timestamp "modified"             
    t.string    "rfidnumber",             :limit => 46
    t.integer   "r_tenant"
    t.integer   "shoporder_id"
    t.string    "serialnumber",           :limit => 50
    t.string    "comments",               :limit => 2047
    t.integer   "owner_id"
    t.integer   "type_id"
    t.integer   "division_id"
    t.integer   "productstatus_uniqueid"
    t.string    "mobileguid",             :limit => 36
    t.string    "customerrefnumber",      :limit => 255
    t.integer   "organization_id"
    t.integer   "identifiedby_uniqueid"
    t.integer   "customerorder_id"
    t.datetime  "lastinspectiondate"
    t.string    "purchaseorder",          :limit => 255
    t.string    "uuid",                   :limit => 255
    t.string    "linkeduuid",             :limit => 255
    t.datetime  "identified",                             :null => false
    t.string    "location",               :limit => 255
    t.integer   "jobsite_id",             :limit => nil
    t.integer   "modifiedby",             :limit => nil
    t.integer   "assigneduser_id",        :limit => nil
    t.string    "state",                  :limit => 255,  :null => false
    t.string    "archivedserialnumber",   :limit => 255
  end


  create_table "productserial_infooption", :primary_key => "uniqueid", :force => true do |t|
    t.integer "r_productserial", :null => false
    t.integer "r_infooption",    :null => false
  end


  create_table "productserialextension", :primary_key => "uniqueid", :force => true do |t|
    t.integer "r_tenant",                      :null => false
    t.string  "extensionkey",   :limit => 255
    t.string  "extensionlabel", :limit => 255
  end


  create_table "productserialextensionvalue", :primary_key => "uniqueid", :force => true do |t|
    t.integer "r_productserial",                         :null => false
    t.integer "r_productserialextension",                :null => false
    t.string  "extensionvalue",           :limit => 512
  end


  create_table "productstatus", :primary_key => "uniqueid", :force => true do |t|
    t.string    "name",         :limit => 255
    t.integer   "r_tenant"
    t.timestamp "datecreated"
    t.timestamp "datemodified"
    t.string    "modifiedby",   :limit => 255
  end


  create_table "producttypegroups", :force => true do |t|
    t.datetime "created",                   :null => false
    t.datetime "modified",                  :null => false
    t.integer  "modifiedby", :limit => nil
    t.integer  "r_tenant",   :limit => nil, :null => false
    t.string   "name",       :limit => 40,  :null => false
    t.integer  "orderidx",   :limit => nil, :null => false
  end


  create_table "producttypes", :force => true do |t|
    t.string    "cautionurl",                 :limit => 255
    t.string    "instructions",               :limit => 2047
    t.string    "name",                       :limit => 255,  :null => false
    t.string    "warnings",                   :limit => 2047
    t.timestamp "created"
    t.timestamp "modified"
    t.integer   "r_tenant"
    t.boolean   "hasmanufacturecertificate"
    t.string    "manufacturecertificatetext", :limit => 2001
    t.string    "descriptiontemplate",        :limit => 255
    t.string    "imagename",                  :limit => 255
    t.integer   "modifiedby",                 :limit => nil
    t.string    "state",                      :limit => 255,  :null => false
    t.string    "archivedname",               :limit => 255
    t.integer   "group_id",                   :limit => nil
  end


  create_table "producttypes_fileattachments", :id => false, :force => true do |t|
    t.integer "producttypes_id", :null => false
    t.integer "attachments_id",  :null => false
  end


  create_table "producttypes_inspectiontypes", :id => false, :force => true do |t|
    t.integer "producttypes_id",    :null => false
    t.integer "inspectiontypes_id", :null => false
  end

  create_table "producttypes_producttypes", :id => false, :force => true do |t|
    t.integer "producttypes_id", :null => false
    t.integer "subtypes_id",     :null => false
  end


  create_table "producttypeschedules", :force => true do |t|
    t.integer  "r_tenant",                         :null => false
    t.datetime "created",                          :null => false
    t.datetime "modified",                         :null => false
    t.integer  "modifiedby"
    t.integer  "frequency"
    t.integer  "producttype_id",                   :null => false
    t.integer  "customer_id"
    t.boolean  "autoschedule"
    t.integer  "inspectiontype_id", :limit => nil
  end


  create_table "projects", :force => true do |t|
    t.datetime "created",                             :null => false
    t.datetime "modified",                            :null => false
    t.integer  "modifiedby"
    t.integer  "r_tenant",                            :null => false
    t.string   "projectid",           :limit => 255,  :null => false
    t.string   "name",                :limit => 255,  :null => false
    t.integer  "customer_id"
    t.integer  "division_id"
    t.string   "status",              :limit => 255,  :null => false
    t.datetime "started"
    t.datetime "estimatedcompletion"
    t.datetime "actualcompletion"
    t.string   "duration",            :limit => 255
    t.boolean  "retired"
    t.integer  "jobsite_id",          :limit => nil
    t.string   "description",         :limit => 1001
    t.string   "workperformed",       :limit => 1001
    t.string   "ponumber",            :limit => 255
    t.boolean  "open"
    t.boolean  "eventjob",                            :null => false
  end


  create_table "projects_fileattachments", :id => false, :force => true do |t|
    t.integer "projects_id", :null => false
    t.integer "notes_id",    :null => false
  end


  create_table "projects_products", :id => false, :force => true do |t|
    t.integer "projects_id",                :null => false
    t.integer "products_id",                :null => false
    t.integer "orderidx",    :limit => nil
  end


  create_table "projects_users", :id => false, :force => true do |t|
    t.integer "projects_id",        :limit => nil, :null => false
    t.integer "resources_uniqueid", :limit => nil, :null => false
  end


  create_table "requesttransactions", :force => true do |t|
    t.string   "name",       :limit => 255, :null => false
    t.string   "type",       :limit => 255, :null => false
    t.datetime "created",                   :null => false
    t.datetime "modified",                  :null => false
    t.integer  "modifiedby"
    t.integer  "r_tenant",                  :null => false
  end


  create_table "savedreports", :force => true do |t|
    t.datetime "created",                       :null => false
    t.datetime "modified",                      :null => false
    t.integer  "modifiedby",     :limit => nil
    t.integer  "r_tenant",       :limit => nil, :null => false
    t.string   "name",           :limit => 255, :null => false
    t.integer  "owner_uniqueid", :limit => nil, :null => false
    t.string   "sortcolumn",     :limit => 255, :null => false
    t.string   "sortdirection",  :limit => 255
    t.string   "sharedbyname",   :limit => 255
  end


  create_table "savedreports_columns", :id => false, :force => true do |t|
    t.integer "savedreports_id", :limit => nil, :null => false
    t.string  "element",         :limit => 255
    t.integer "idx",             :limit => nil
  end


  create_table "savedreports_criteria", :id => false, :force => true do |t|
    t.integer "savedreports_id", :limit => nil, :null => false
    t.string  "element",         :limit => 255
    t.string  "mapkey",          :limit => 255, :null => false
  end

  create_table "serialnumbercounter", :primary_key => "uniqueid", :force => true do |t|
    t.integer  "r_tenant",                     :null => false
    t.integer  "counter"
    t.string   "decimalformat", :limit => 255
    t.integer  "daystoreset"
    t.datetime "lastreset"
  end


  create_table "setupdatalastmoddates", :id => false, :force => true do |t|
    t.integer  "r_tenant",        :limit => nil, :null => false
    t.datetime "producttypes",                   :null => false
    t.datetime "inspectiontypes",                :null => false
    t.datetime "autoattributes",                 :null => false
    t.datetime "owners",                         :null => false
    t.datetime "jobs"
  end


  create_table "states", :force => true do |t|
    t.integer  "r_tenant",                                      :null => false
    t.datetime "created",                                       :null => false
    t.datetime "modified",                                      :null => false
    t.integer  "modifiedby"
    t.string   "displaytext", :limit => 255,                    :null => false
    t.string   "status",      :limit => 255,                    :null => false
    t.string   "buttonname",  :limit => 255,                    :null => false
    t.boolean  "retired",                    :default => false
  end


  create_table "statesets", :force => true do |t|
    t.integer  "r_tenant",                                     :null => false
    t.datetime "created",                                      :null => false
    t.datetime "modified",                                     :null => false
    t.integer  "modifiedby"
    t.string   "name",       :limit => 255,                    :null => false
    t.boolean  "retired",                   :default => false
  end


  create_table "statesets_states", :id => false, :force => true do |t|
    t.integer "statesets_id",                :null => false
    t.integer "states_id",                   :null => false
    t.integer "orderidx",     :limit => nil, :null => false
  end


  create_table "subproducts", :force => true do |t|
    t.datetime "created",                         :null => false
    t.datetime "modified",                        :null => false
    t.integer  "modifiedby",       :limit => nil
    t.integer  "product_id",       :limit => nil, :null => false
    t.integer  "masterproduct_id", :limit => nil, :null => false
    t.string   "label",            :limit => 255
    t.integer  "weight",           :limit => nil
  end


  create_table "tagoptions", :force => true do |t|
    t.integer  "r_tenant",                         :null => false
    t.datetime "created",                          :null => false
    t.datetime "modified",                         :null => false
    t.integer  "modifiedby"
    t.string   "key",               :limit => 255, :null => false
    t.integer  "weight",                           :null => false
    t.string   "text",              :limit => 255
    t.string   "resolverclassname", :limit => 255
  end

  create_table "tasks", :force => true do |t|
    t.datetime "created",                       :null => false
    t.datetime "modified",                      :null => false
    t.string   "classname",      :limit => 255, :null => false
    t.string   "cronexpression", :limit => 255, :null => false
    t.string   "taskgroup",      :limit => 255
    t.integer  "taskentityid",   :limit => nil
    t.boolean  "enabled",                       :null => false
  end

  create_table "tenantlink", :force => true do |t|
    t.integer "r_manufacturer", :null => false
    t.integer "r_linkedtenant", :null => false
  end


  create_table "unitofmeasures", :force => true do |t|
    t.datetime "created",                               :null => false
    t.datetime "modified",                              :null => false
    t.integer  "modifiedby"
    t.string   "name",                   :limit => 255, :null => false
    t.string   "type",                   :limit => 255
    t.string   "shortname",              :limit => 255
    t.boolean  "selectable"
    t.integer  "child_unitofmeasure_id"
  end


  create_table "userrequest", :force => true do |t|
    t.integer  "r_tenant",                    :null => false
    t.string   "companyname"
    t.string   "phonenumber",   :limit => 30
    t.integer  "r_useraccount",               :null => false
    t.text     "comment"
    t.datetime "modified"
    t.datetime "created"
    t.integer  "modifiedby"
  end


  create_table "users", :primary_key => "uniqueid", :force => true do |t|
    t.timestamp "datemodified"
    t.timestamp "datecreated"
    t.string    "modifiedby",             :limit => 50
    t.string    "userid",                 :limit => 15
    t.string    "firstname",              :limit => 30
    t.string    "lastname",               :limit => 30
    t.string    "emailaddress",           :limit => 50
    t.integer   "r_tenant"
    t.integer   "r_enduser"
    t.boolean   "deleted"
    t.string    "hashpassword",           :limit => 255
    t.integer   "r_division"
    t.string    "timezoneid",             :limit => 255
    t.integer   "r_organization"
    t.string    "position",               :limit => 255
    t.boolean   "active",                                :default => true
    t.string    "initials",               :limit => 255
    t.string    "resetpasswordkey",       :limit => 255
    t.string    "hashsecuritycardnumber", :limit => 255
    t.boolean   "system",                                                  :null => false
    t.boolean   "admin"
    t.integer   "permissions",                                             :null => false
  end


end
