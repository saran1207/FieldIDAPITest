class DropEventGroupsTable < ActiveRecord::Migration
  def self.up
    execute("alter table masterevents DROP FOREIGN KEY fk_inspectionsmaster_inspectiongroups, drop column group_id")
    execute("drop table eventgroups")
  end

  def self.down

  end
end