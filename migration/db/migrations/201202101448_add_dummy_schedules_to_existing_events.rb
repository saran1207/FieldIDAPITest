require 'master_event'
require 'event_schedule'

class AddDummySchedulesToExistingEvents < ActiveRecord::Migration

  def self.up
    # we need to make nextEventDate nullable on event schedule
    #execute("alter table eventschedules modify nextdate datetime default null")

#    MasterEvent.find_each do |master_event|
#
#      if master_event.schedule.nil?
#        event = master_event.event
#
#        schedule = EventSchedule.new
#
#        schedule.tenant_id = event.tenant_id
#        schedule.created = event.created
#        schedule.createdby = event.createdby
#        schedule.modified = event.modified
#        schedule.modifiedby = event.modifiedby
#        schedule.state = master_event.state == 'ACTIVE' ? 'ACTIVE' : 'ARCHIVED'
#        schedule.event_event_id = event.id
#        schedule.completeddate = master_event.date
#        schedule.asset_id = event.asset_id
#        schedule.location = ""
#        schedule.owner_id = master_event.owner_id
#        schedule.status = 'COMPLETED'
#        schedule.eventtype_id=event.type_id
#
#        schedule.save
#      end
#    end

#    add_column(:masterevents, :schedule_id, :integer, :null => false)


    #execute("update masterevents e,eventschedules s set e.schedule_id = s.id where e.event_id = s.event_event_id;")

    add_foreign_key(:masterevents, :eventschedules, :source_column => :schedule_id, :foreign_column => :id)

  end


  def self.down
    EventSchedule.delete_all(:all, :conditions => { :status => "COMPLETE", :nextdate => nil })

    execute("alter table eventschedules modify nextdate datetime not null")
  end

end