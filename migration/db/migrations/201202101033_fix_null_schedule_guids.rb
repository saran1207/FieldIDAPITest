
class FixNullScheduleGuids < ActiveRecord::Migration

  def self.up
  	execute("UPDATE eventschedules SET mobileguid = uuid() WHERE mobileguid IS NULL")
  end

  def self.down
  end
  
end