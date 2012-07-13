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
  end

  def self.down
  end

end