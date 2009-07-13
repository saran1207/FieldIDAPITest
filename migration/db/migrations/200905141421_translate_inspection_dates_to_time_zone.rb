require "inspectionmaster"
require "tzinfo"
class  TranslateInspectionDatesToTimeZone < ActiveRecord::Migration
  def self.up
    Inspection.find_in_batches() do |inspections|
      for inspection in inspections 
      
        inspector_time_zone = TZInfo::Timezone.get(inspection.inspector.timezoneid)
        date = inspection.date
        
        if date.hour == 0 && date.min == 0 && date.sec == 0 
          date = date.advance(:hours => 12)
        end
        begin 
          inspection.date = inspector_time_zone.local_to_utc(date)
        rescue TZInfo::AmbiguousTime
          inspection.date = inspector_time_zone.local_to_utc(date.advance(:hours =>2))
        end
        
        inspection.save
      end
    end
    
  end
  
  def self.down
  end
end
