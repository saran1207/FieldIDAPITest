require 'event'
require 'master_event'
require 'event_group'

class SetTenantIdsForMasterEventGroup < ActiveRecord::Migration
  def self.up

    MasterEvent.find_by_sql("select * from masterevents
                              where event_id in
                                               (select event_id
                                                from eventgroups join masterevents on id = group_id
                                                where tenant_id is null)").each do |masterevent|
      event = Event.find(masterevent.event_id)
      group = EventGroup.find(masterevent.group_id)
      if group.tenant_id.nil?
        group.tenant_id = event.tenant_id
        group.save
      end
    end
  end

  def self.down

  end
end