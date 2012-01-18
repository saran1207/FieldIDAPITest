require 'saved_report'
require 'new_saved_report'
require 'date'

class MigrateSavedReportsToNewFormat < ActiveRecord::Migration

  def self.up

    fields_to_not_copy = [:fromDate,:toDate,:dateRange,:serialNumber, :predefinedLocationId]

    create_table :saved_reports do |t|
      create_abstract_entity_fields_on(t)
      t.string :rfidNumber
      t.string :identifier
      t.string :referenceNumber
      t.string :purchaseOrderNumber
      t.string :orderNumber
      t.string :dateRange
      t.date :fromDate
      t.date :toDate
      t.integer :assetTypeGroup
      t.integer :assetType
      t.integer :assetStatus
      t.integer :eventBook
      t.integer :jobSite
      t.integer :eventTypeGroup
      t.integer :eventTypeId
      t.integer :performedBy
      t.integer :jobId
      t.integer :ownerId
      t.string :status, :length => 10
      t.string :location
      t.integer :predefinedLocation_id
      t.integer :assignedUser
      t.boolean :includeSafetyNetwork, :null => false

      t.string :sortDirection, :length => 10
      t.integer :sortColumnId
    end

    execute "create table saved_reports_columns (saved_report_id bigint(21), column_id varchar(255), idx bigint(20))"

    SavedReport.find(:all).each do |saved_report|
      new_saved_report = NewSavedReport.new
      new_saved_report.id = saved_report.id
      new_saved_report.created = saved_report.created
      new_saved_report.createdby = saved_report.createdby
      new_saved_report.modified = saved_report.modified
      new_saved_report.modifiedby = saved_report.modifiedby
      new_saved_report.sortColumnId = saved_report.sort_column_id.to_i unless saved_report.sort_column_id.nil?
      new_saved_report.sortDirection = saved_report.sortdirection.upcase
      new_saved_report.includeSafetyNetwork = false
      new_saved_report.dateRange = 'CUSTOM'
      new_saved_report.location = ""

      saved_report.criteria.each do |criteria|
        field_name = criteria.mapkey.intern
        field_value = criteria.element
        unless fields_to_not_copy.include? field_name
          new_saved_report[field_name] = field_value
        end

        if field_name == :serialNumber
          new_saved_report.identifier = field_value
        end

        if field_name == :fromDate
          new_saved_report.fromDate = DateTime.strptime(field_value, '%Y-%m-%d')
        end
        
        if field_name == :toDate
          new_saved_report.toDate = DateTime.strptime(field_value, '%Y-%m-%d')
        end

        if field_name == :predefinedLocationId
          new_saved_report.predefinedLocation_id = field_value
        end
      end

      new_saved_report.save
    end

    add_foreign_key(:saved_reports_columns, :saved_reports, :source_column => :saved_report_id, :foreign_column => :id)
    execute "insert into saved_reports_columns (select savedreports_id,element,idx from savedreports_columns)"
  end

  def self.down
    drop_table :saved_reports_columns
    drop_table :saved_reports
  end

end