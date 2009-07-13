class TruncateTimesInNextdate < ActiveRecord::Migration
  
  def self.up

	execute("update inspectionschedules set nextdate = date_trunc('day', nextdate)");
     
  end
  
  def self.down
  
  end
  
end