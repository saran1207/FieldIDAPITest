class AddTriggerToEvent < ActiveRecord::Migration

  def self.up
    execute("alter table masterevents add column trigger_event_id bigint")
    execute ("alter table masterevents add foreign key trigger_event_fk (trigger_event_id) references masterevents (event_id)")
  end

  def self.down
    execute("alter table masterevents drop column trigger_event_id")
  end

end