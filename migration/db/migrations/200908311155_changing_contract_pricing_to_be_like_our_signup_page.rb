class ChangingContractPricingToBeLikeOurSignupPage < ActiveRecord::Migration
  def self.up
    drop_table(:contractpricings)
    create_table :contractpricings do |t|
      create_abstract_entity_fields_on(t)
      t.string :netsuiterecordid
      t.string :signuppackage
      t.string :paymentoption
      t.float  :priceperuserpermonth
    end
    
  end
  
  def self.down
    drop_table(:contractpricings)
    create_table :contractpricings do |t|
      create_abstract_entity_fields_on(t)
      t.string :netsuiterecordid
      t.string :price
      t.string :syncid
      t.string :contractlength
    end
  end
end