
class UpdateNullEventsStateToVoid < ActiveRecord::Migration

  def self.up
    # NOTE : a migration for dealing with EventSchedules-->Events can and will insert events with a NULL status.
    # this migration was modified ad hoc to catch the null case.
    execute "update masterevents set status='VOID' where event_state = 'OPEN' and (status='NA' || status is null)"
  end

  def self.down

  end

end