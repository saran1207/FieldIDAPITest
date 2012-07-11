require 'event_schedule'
require 'master_event'
require 'event_group'

class AddSchedulesColumnsToEvents < ActiveRecord::Migration

  def self.up
    execute("alter table masterevents modify column schedule_id bigint(20) null")

    add_column(:masterevents, :nextDate, :datetime)
    add_column(:masterevents, :completedDate, :datetime)
    add_column(:masterevents, :event_state, :string)
    add_column(:masterevents, :project_id, :integer)
    add_foreign_key(:masterevents, :projects, :source_column => :project_id, :foreign_column => :id)

    execute("update masterevents me, eventschedules s set me.event_state = s.status, me.completedDate = s.completedDate, me.nextDate = s.nextDate where me.schedule_id = s.id")

    execute("alter table masterevents modify column status varchar(255) null")

    current_schedule = 1
    total_schedules = EventSchedule.count_by_sql("select count(*) from eventschedules es  left outer join masterevents me on es.id=me.schedule_id where es.status <> 'COMPLETED' and es.state ='ACTIVE' and me.event_id is null;")

    incomplete_schedules = EventSchedule.find_by_sql("select es.* from eventschedules es  left outer join masterevents me on es.id=me.schedule_id where es.status <> 'COMPLETED' and es.state ='ACTIVE' and me.event_id is null;")
    incomplete_schedules.each do |schedule|
      event_group = EventGroup.new
      event_group.created = schedule.created
      event_group.modified = schedule.modified
      event_group.createdby = schedule.createdby
      event_group.modifiedby = schedule.modifiedby
      event_group.tenant_id = schedule.tenant_id

      event = MasterEvent.new
      event.event = Event.new
      event.event_group = event_group

      event.event_state = "OPEN"
      event.status = nil
      event.state = "ACTIVE"
      event.schedule_id = schedule.id
      event.nextDate = schedule.nextdate
      event.event.created = schedule.created
      event.event.modified = schedule.modified
      event.event.createdby = schedule.createdby
      event.event.modifiedby = schedule.modifiedby
      event.project_id = schedule.project_id
      event.location = schedule.location
      event.owner_id = schedule.owner_id
      event.predefinedlocation_id = schedule.predefinedlocation_id
      event.event.type_id = schedule.eventtype_id

      event.event.editable = true

      event.event.tenant_id = schedule.tenant_id
      event.event.asset_id = schedule.asset_id
      event.printable = false

      event.save
      say "Migrated #{current_schedule} of #{total_schedules} schedules" if (current_schedule % 100) == 0
      current_schedule += 1
    end

    execute("alter table masterevents modify column event_state varchar(255) not null")

  end

  def self.down
  end

end