class SetEventStateOnMastereventsNotNull < ActiveRecord::Migration

  def self.up
    
    execute("alter table masterevents modify column event_state varchar(255) not null;")

  end

  def self.down
  end

end
  