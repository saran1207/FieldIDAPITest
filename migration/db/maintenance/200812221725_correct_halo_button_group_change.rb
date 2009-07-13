require "criteria_result"
class CorrectHaloButtonGroupChange < ActiveRecord::Migration
  
  def self.up
    CriteriaResult.transaction do 
      results = CriteriaResult.find( :all, :conditions => { :r_tenant => 10802300, :state_id => 102 } )
      for result in results
        if( allowedResult?( result ) )
          result.state_id = 235
          result.save
        end
      end
      
      results = CriteriaResult.find( :all, :conditions => { :r_tenant => 10802300, :state_id => 103 } )
      for result in results
        if( allowedResult?( result ) )
          result.state_id = 102
          result.save
        end
      end
    end
  end
  
  private
    def self.allowedResult?( result )
      targetInspections = [ 798, 803, 801, 802, 927, 941, 943, 945, 946, 948 ]
      if( targetInspections.include?( result.inspection_id ) )
        return true
      end
      raise Exception.new( result.id.to_s + " is not in our set of target inspections" )
    end
end