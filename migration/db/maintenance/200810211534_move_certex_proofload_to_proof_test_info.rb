require "inspection_type"
require "inspection"
require "organization"
class MoveCertexProofloadToProofTestInfo < ActiveRecord::Migration
  def self.up
    Inspection.transaction do
      tenant = Organization.find( :first, :conditions => { :name => "certex" } )
      proofTest = InspectionType.find( :first, :conditions => { :r_tenant => tenant.id, :name => "Pull Test" } ) 
      inspections = Inspection.find( :all, :conditions => { :r_tenant => tenant.id, :type_id => proofTest.id } )
      proofTest.supportedProofTests << InspectionTypeSupportedProofTest.new( { :element => "OTHER" })
      proofTest.save
      for inspection in inspections do
        for infoOption in inspection.infoOptions do
          if infoOption.mapkey == "Proof Load" 
            inspection.peakload = infoOption.element
            inspection.prooftesttype = "OTHER"
            inspection.save
            infoOption.destroy
          end
        end
      end
      for infoField in proofTest.infoFieldNames do
        if infoField.element == "Proof Load"
          infoField.destroy
        end
      end
    end
  end
  
  def self.down
    raise ActiveRecord::IrreversibleMigration
  end
end