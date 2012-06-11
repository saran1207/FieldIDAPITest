class FixDuplicateScheduleRows < ActiveRecord::Migration

  def self.up
    execute "delete from eventschedules where state<>'RETIRED' and status='COMPLETED' and id not in (select schedule_id from masterevents)"
  end

  def self.down
  end

end