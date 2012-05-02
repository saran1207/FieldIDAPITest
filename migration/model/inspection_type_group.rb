require 'inspection_type'
class InspectionTypeGroup < ActiveRecord::Base
    set_table_name :inspectiontypegroups
    has_many :inspection_type, :foreign_key => 'group_id'
    
    def self.findOrCreate(tenant, name)
        typeGroup = InspectionTypeGroup.find(:first, :conditions => ["r_tenant = :tenantId and name = :groupName", {:tenantId => tenant.id, :groupName => self.resolveName(name)}])
        
        if typeGroup.nil?
          puts "Creating New InspectionTypeGroup for: [" +  tenant.displayString + "] named [" + name + "]"
          
          now = Time.now
          typeGroup = InspectionTypeGroup.create :r_tenant => tenant.id, :name => self.resolveName(name), :reporttitle => self.resolveReportTitle(name),:created => now, :modified => now
        else
          puts "Found InspectionTypeGroup: " + typeGroup.displayString
        end
        
        typeGroup
    end
    
    def self.findByLegacyName(tenant, name)
      return InspectionTypeGroup.find(:first, :conditions => ["r_tenant = :tenantId and name = :groupName", {:tenantId => tenant.id, :groupName => self.resolveName(name)}])
    end
    
    def self.resolveName(oldName)
      case oldName
        when "visualinspection"
          return "Visual Inspection"
        when "repair"
          return "Repair"
        when "prooftest"
          return "Proof Test"
        else raise "No EventType mapping found for [" + oldName + "]"
      end
    end
    
    def self.resolveReportTitle(oldName)
      case oldName
        when "visualinspection"
          return "Visual Inspection"
        when "prooftest"
          return "Inspection Certificate"
        else return "Inspection Report"
      end
    end
    
    def displayString
      "#{name} (#{id.to_s})"
    end
end