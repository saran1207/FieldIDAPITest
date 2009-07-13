require 'inspectionmaster'
require 'inspection_type'

class MigrateChargeField < ActiveRecord::Migration
  def self.up
    # unirope, certex, allway, peakworks
    tenants = Array[ 2, 10802350, 10802301, 15511485 ]
    
    tenants.each do |tenantId|
      puts "Working on Tenant: " + tenantId.to_s
      
      types = InspectionType.find(:all, :conditions => ['r_tenant = ?', tenantId]) 
      types.each do |type|
        puts "\tWorking on Inspection Type: " + type.name
        
        # if there are already info fields, we'll add this to the end of the list
        weight = InspectionTypeInfoFieldName.count(:conditions => ['inspectiontypes_id = ?', type.id])
        
        # create the info field name
        InspectionTypeInfoFieldName.create(:inspectiontypes_id => type.id, :orderidx => weight, :element => 'Charge') 
        
        # now we're going to find all the inspections for this type
        # add move the charge value from the inspection to the infofield
        
        inspections = AbstractInspection.find(:all, :conditions => ['r_tenant = ? and type_id = ?', tenantId, type.id])
        
        inspections.each do |inspection|
          charge = formatCharge(Inspection.find(inspection.id).charge)
          
          chargeCount = InspectionInfoOption.count(:conditions => ['inspections_id = ? and mapkey = ?', inspection.id, 'Charge'])
          
          if chargeCount == 0
            InspectionInfoOption.create(:inspections_id => inspection.id, :mapkey => 'Charge', :element => charge)
          else
            puts "\t\tCharge already exists for Inspection: " + inspection.id.to_s
          end
        end        
      end
    end
    
  end
  
  def self.down
  end

  def self.formatCharge(charge)
    return sprintf('%.2f', charge/100.0)
  end
  
end