
class UpdateScheduledEventsStateToVoid< ActiveRecord::Migration

  def self.up
    execute "update masterevents set status='VOID' where event_state = 'OPEN' and status='NA'"
  end

  def self.down

  end

end