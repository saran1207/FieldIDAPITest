require "product"
class FixHaloMultipleProducts < ActiveRecord::Migration
  def self.up
    ProductSerial.transaction do
      setOfGuids = [ 
          '042ca7bb-c376-4bec-a65c-064dafa5c1ac',
          '2dbfc96c-731c-49a2-9939-ccd10cea66cd',
          'a1d9aacd-cc7d-4122-9aa0-ec595dac4a00',
          '674f1bbd-942e-48be-ad63-68cf881f2683',
          'bb36d96d-3999-47de-8ea5-ed0842a596fa',
          '8903ca3d-e98a-4d03-af78-046c90e815a7',
          '332ed2f3-2ac6-484b-b4f4-bea077c7e3d4',
          'cf15718c-99fd-4b0a-b76a-4ffd65014c27',
          '5b87c7e3-1b75-47ca-a848-328b8360e5ae',
          'e46927ee-e899-47be-99e8-3835d3d889bb',
          'ed29a07e-1f31-4c63-9b0a-26952ff17b96',
          '8926af17-3aac-4c0d-b3cb-ee30b82538a0',
          'e9b894c8-da83-482e-83ce-07c77c99c304',
          'a31f813f-970f-47ac-bf8b-aff104ae7b0b',
          '8029db22-603f-4454-a3fe-45dd7b84e162',
          '4a047d53-d7c0-4cbc-9618-0441c899f7aa',
          '50f82bf1-204f-4050-ad47-5d00f1469e73',
          '436a6125-3b7a-4e1e-85d1-6397b72855f3',
          '8ca44d32-7b12-4aa6-bd7e-d2c65262cbd9',
          '9a12b2d1-4927-40ca-9f01-afa4be4aed79',
          '6f351dd8-4235-4825-839d-81ac71939631',
          'ac567ce0-7c93-4e04-a470-851fed95807e',
          '08ee4ea6-9d83-4209-ae2a-4b686379179f',
          '030ec3b9-b51b-4c4d-9db3-db3fcf7b30db',
          '59b0585a-d033-4ba1-b0aa-519c03feffb6']
          
       for guid in setOfGuids do
         products = ProductSerial.find :all, :conditions => { :mobileguid => guid, :r_tenant => 10802300 }, :order => :datecreated
         if !products.empty?  
           product1 = products[0]
           product2 = products[1]
           if product1.rfidnumber.empty?
             product1.rfidnumber = product2.rfidnumber
             
             
           end
           product2.mobileguid = ""
           product2.rfidnumber = ""
           puts product1.serialnumber + "  " + product2.serialnumber
           
           product1.save
           product2.save
         end
       end
    end
  end
  
  def self.down
    raise ActiveRecord::IrreversibleMigration
  end
end