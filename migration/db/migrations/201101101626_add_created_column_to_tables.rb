class AddCreatedColumnToTables < ActiveRecord::Migration

  CREATED_BY_TABLES = [:eventtypes, :subassets,:instructionalvideos,
        :eulas, :addressinfo, :contractpricings, :promocodes, :unitofmeasures,
        :autoattributedefinition, :catalogs, :tagoptions, :criteriaresults, :predefined_location_levels, :observations,
        :autoattributecriteria, :assettypegroups, :states, :associatedeventtypes, :userrequest, :downloads,
        :org_base, :assettypes, :predefinedlocations, :eventforms, :savedreports, :eventschedules, :assets,
        :notificationsettings, :typedorgconnections, :projects, :users, :assettypeschedules, :eventbooks, :orders,
        :criteria, :events, :lineitems, :printouts, :requesttransactions, :assetattachments, :eventgroups,
        :eulaacceptances, :fileattachments, :statesets, :eventtypegroups, :criteriasections, :configurations, :seenitstorageitem, :org_connections]

  def self.up
    CREATED_BY_TABLES.each { |table| make_created_by_column(table) }
  end

  def self.down
    CREATED_BY_TABLES.each { |table| drop_created_by_column(table) }
  end

  def self.make_created_by_column(table)
    add_column(table, :createdby, :integer, :null => true)
    execute("alter table #{table} add foreign key fk_created_by_user (createdby) references users(id) on update no action on delete no action")
  end

  def self.drop_created_by_column(table)
    execute("alter table #{table} drop foreign key fk_created_by_user")
    remove_column(table, :createdby)
  end


end
