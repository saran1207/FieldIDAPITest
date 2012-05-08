class RemoveDateColumnFromEvent < ActiveRecord::Migration

  def self.up
    execute "alter table masterevents drop column date"
  end

  def self.down
    execute "alter table masterevents add column date datetime default null"
    execute "update masterevents me, eventschedules es set me.date = es.completedDate where me.schedule_id = es.id"
    execute "alter table masterevents modify column date datetime not null"
  end

end