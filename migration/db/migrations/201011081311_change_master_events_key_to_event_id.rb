class ChangeMasterEventsKeyToEventId < ActiveRecord::Migration

  def self.up
    change_column(:masterevents, :inspection_id, :index_inspectionsmaster_on_inspection_id, :fk_inspectionsmaster_inspections, :event_id, :index_masterevents_on_event_id, :fk_inspectionsmaster_inspections, :event_id, :events, :id, "bigint (20) not null")
  end

  def self.down
    change_column(:masterevents, :event_id, :index_masterevents_on_event_id, :fk_inspectionsmaster_inspections, :inspection_id, :index_inspectionsmaster_on_inspection_id, :fk_inspectionsmaster_inspections, :inspection_id, :events, :id, "bigint (20) not null")
  end

  def self.change_column(table, old_column_name, old_index_name, old_fk_name, new_column_name, new_index_name, new_fk_name, fk_column, fk_target_table, fk_target_column, column_def = "BIGINT(20)", unique=false)
    execute "alter table #{table} drop foreign key #{old_fk_name}" unless old_fk_name.nil?
    execute "alter table #{table} drop key #{old_index_name}" unless old_index_name.nil?
    execute "alter table #{table} change column #{old_column_name} #{new_column_name} #{column_def}"
    add_index table, [new_column_name], :name => new_index_name, :unique => unique unless new_index_name.nil?
    execute "alter table #{table} add foreign key #{new_fk_name} (#{fk_column}) references #{fk_target_table} (#{fk_target_column}) on delete no action on update no action" unless new_fk_name.nil?
  end

end