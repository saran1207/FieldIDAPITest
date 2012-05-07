class FixCompletedDatesToMatchEvents < ActiveRecord::Migration

  def self.up
    execute "update eventschedules es, masterevents me set es.completeddate = me.date where es.id = me.schedule_id"
  end

  def self.down
  end

end