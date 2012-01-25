class CreateSavedSearchesTable < ActiveRecord::Migration

  def self.up
    create_table :saved_searches do |t|
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
      t.integer :jobSite
      t.integer :performedBy
      t.integer :jobId
      t.integer :ownerId
      t.string :status, :length => 10
      t.string :location
      t.integer :predefinedLocation_id
      t.integer :assignedUser

      t.string :sortDirection, :length => 10
      t.integer :sortColumnId
    end

    add_column(:saved_items, :search_id, :integer)
    add_foreign_key(:saved_items, :saved_searches, :source_column => :search_id, :foreign_column => :id)

    execute "create table saved_searches_columns (saved_search_id bigint(21), column_id varchar(255), idx bigint(20))"
    add_foreign_key(:saved_searches_columns, :saved_searches, :source_column => :saved_search_id, :foreign_column => :id)
  end

  def self.down
    drop_table :saved_reports_columns

    drop_foreign_key(:saved_items , :saved_searches, :source_column => :search_id, :foreign_column => :id, :name => "fk_saved_items_saved_searches")
    remove_column(:saved_items, :search_id)
    drop_table :saved_searches
  end

end