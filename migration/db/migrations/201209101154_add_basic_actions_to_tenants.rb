require 'tenant'
require 'event_type_group'
require 'event_type'

class AddBasicActionsToTenants < ActiveRecord::Migration

  def self.up
    tenants = Tenant.find(:all)
    tenants.each do |tenant|
      group = EventTypeGroup.new
      group.tenant_id = tenant.id
      group.name = "Actions"
      group.action = 1
      group.created = Time.now
      group.modified = Time.now
      group.save

      event_type = EventType.new
      event_type.tenant_id = tenant.id
      event_type.name = "Corrective Action"
      event_type.group_id = group.id
      event_type.created = Time.now
      event_type.modified = Time.now
      event_type.master = false
      event_type.formversion = 0
      event_type.assignedtoavailable = 0
      event_type.state = "ACTIVE"
      event_type.display_section_totals = 0
      event_type.save
    end
  end

  def self.down
    execute ("delete from eventtypes where group_id in (select id from eventtypegroups where action = 1)")
    execute ("delete from eventtypegroups where action = 1")
  end

end