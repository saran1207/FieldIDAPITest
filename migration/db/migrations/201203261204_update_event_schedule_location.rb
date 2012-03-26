
class UpdateEventScheduleLocation < ActiveRecord::Migration

  def self.up
    execute "update masterevents me, eventschedules es set es.location = me.location, es.predefinedlocation_id = me.predefinedlocation_id where es.nextdate is null and me.schedule_id = es.id;"
  end

  def self.down

  end

end