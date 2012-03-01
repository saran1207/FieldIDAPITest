require 'event_schedule'

class FixDuplicateScheduleGuids < ActiveRecord::Migration

  def self.up
    add_index(:eventschedules, :mobileguid, :name => "idx_tmp_eventschedules_mobileguid")
    
  	dupGuids = EventSchedule.find_by_sql("SELECT mobileguid FROM eventschedules GROUP BY mobileguid HAVING count(*) > 1").collect {|e|e.mobileguid}
  	
  	allIds = []
  	dupGuids.each do |guid|
  	  ids = EventSchedule.find_by_sql(["SELECT id FROM eventschedules WHERE mobileguid = ?", guid]).collect {|e|e.id}
  	  ids.shift
  	  
  	  allIds.concat(ids) 
	  end
	  
	  EventSchedule.update_all('mobileguid = uuid()', ['id in (?)', allIds])

	  execute("UPDATE eventschedules SET mobileguid = uuid() WHERE mobileguid IS NULL")
	  change_column(:eventschedules, :mobileguid, :string, :null => false)
	  
	  remove_index(:eventschedules, :name => "idx_tmp_eventschedules_mobileguid")
	  add_index(:eventschedules, :mobileguid, :unique => true)
  end

  def self.down
    
  end
  
end