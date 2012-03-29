class FixOrphanedSchedules < ActiveRecord::Migration

  def self.up
    execute "delete from eventschedules where status = 'COMPLETED' and nextdate is null and id not in (select schedule_id from masterevents)"
  end

  def self.down
  end

end