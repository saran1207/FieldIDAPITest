require "product"
require "inspection"
require "inspection_book"
require "product_status"
require "organization"
class FixNisProductStatuses < ActiveRecord::Migration
  def self.up
    Inspection.transaction do
      tenant = Organization.find( :first , :conditions => { :name => "nischain" } )
      inspectionBook1 = InspectionBook.find( :first, :conditions => { :title => "GM ROMULUS STAIR/LADDER 2008", :r_tenant => tenant.id  } )
      inspectionBook2 = InspectionBook.find( :first, :conditions => { :title => "GM ROMULUS END EFFECTOR 2008", :r_tenant => tenant.id  } )
      productStatus = ProductStatus.find( :first, :conditions => { :name => "Active", :r_tenant => tenant.id } )
      
      if( inspectionBook1.nil? || inspectionBook2.nil? || productStatus.nil? ) 
        raise Exception( "missing data. " + inspectionBook1.to_s + " " + inspectionBook2.to_s + " " + productStatus.to_s )       
      end
      
      inspections = Inspection.find( :all, :conditions => "( r_inspectionbook =  " + inspectionBook1.uniqueid.to_s + " OR  r_inspectionbook =  " + inspectionBook2.uniqueid.to_s + " ) AND r_tenant = " + tenant.id.to_s    )
      
      for inspection in inspections do
        if !inspection.deleted 
          inspectionDocs = InspectionDoc.find :all, :conditions => { :r_inspection => inspection.uniqueid, :inspectionresult => "Pass", :deleted => false, :r_tenant => tenant.id  }
          if !inspectionDocs.empty?
            puts inspection.r_productserial.to_s + "   " + inspection.uniqueid.to_s
            product = ProductSerial.find( :first, :conditions => { :uniqueid => inspection.r_productserial, :r_tenant => tenant.id } )
            product.r_productstatus = productStatus.uniqueid
            product.save
          end
        end
      end
      
    end
  end
  
  def self.down
    raise ActiveRecord::IrreversibleMigration
  end
end