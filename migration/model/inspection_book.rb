require "organization"
require "user"
require "inspection"
require "customer"

class InspectionBook < ActiveRecord::Base
  set_table_name :inspectionbooks

  belongs_to  :tenant,        :foreign_key => 'r_tenant',           :class_name => 'Organization'
  belongs_to  :modifiedBy,    :foreign_key => 'modifiedby',         :class_name => 'User'
  belongs_to  :customer,      :foreign_key => 'customer_uniqueid',  :class_name => 'Customer'
  has_many    :inspections,   :foreign_key => 'book_id',            :class_name => 'Inspection'
  
  def self.findOrCreate(tenant, legacyBook, customer)
    
    if legacyBook.nil?
      puts "Legacy inspection book was null"
      return nil
    end
    
    if customer.nil?
      book = InspectionBook.find(:first, :conditions => ["r_tenant = :tenantId and name = :bookName and customer_uniqueid is null", {:tenantId => tenant.id, :bookName => legacyBook.title}])
    else
      book = InspectionBook.find(:first, :conditions => ["r_tenant = :tenantId and name = :bookName and customer_uniqueid = :customerid", {:tenantId => tenant.id, :bookName => legacyBook.title, :customerid => customer.uniqueid}]) 
    end
      
    if book.nil?
      puts "Creating New InspectionBook for: [" +  tenant.displayString + "] name [" + legacyBook.title + "] open [" + legacyBook.isOpen.to_s + "]"

      book = InspectionBook.create :tenant => tenant, :legacyid => legacyBook.uniqueid, :customer => customer, :name => legacyBook.title, :created => legacyBook.datecreated, :modified => legacyBook.datemodified, :open => legacyBook.isOpen
      
    else
      puts "Found InspectionBook: " + book.displayString
    end
      
    return book
  end
  
  def displayString
    "#{name} (#{id.to_s})"
  end
  
end
