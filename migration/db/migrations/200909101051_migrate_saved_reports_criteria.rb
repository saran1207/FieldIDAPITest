require "saved_report"
require "base_org"
require "customer_org"
require "division_org"

class MigrateSavedReportsCriteria < ActiveRecord::Migration
  def self.up
    SavedReport.find_each do |report|
      
      divCrit = nil
      custCrit = nil
      report.criteria.each do |crit|
        case crit.mapkey
          when "divisionId" then divCrit = crit
          when "customerId" then custCrit = crit
        end
      end
      
      if (!divCrit.nil?)
        create_new_criteria(report, find_division_org_id(divCrit))
        divCrit.destroy
        
        if (!custCrit.nil?)
          custCrit.destroy
        end
      elsif (!custCrit.nil?)
        create_new_criteria(report, find_customer_org_id(custCrit))
        custCrit.destroy
      end
      
    end
  end
  
  def self.down
  end

  def self.create_new_criteria(savedReport, orgId)
    orgCrit = SavedReportCriteria.new
    orgCrit.savedReport = savedReport
    orgCrit.element = orgId
    orgCrit.mapkey = "ownerId"
    orgCrit.save
  end
  
  def self.find_division_org_id(crit)
    return DivisionOrg.find_by_legacy_id(crit.element).baseOrg.id
  end
  
  def self.find_customer_org_id(crit)
    return CustomerOrg.find_by_legacy_id(crit.element).baseOrg.id
  end
  
end