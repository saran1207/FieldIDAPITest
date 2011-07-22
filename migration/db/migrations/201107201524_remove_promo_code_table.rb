class RemovePromoCodeTable < ActiveRecord::Migration

  def self.up
  	drop_foreign_key(:promocodes , :users, :source_column => :createdby, :foreign_column => :id, :name => "promocodes_ibfk_1")
  	drop_table(:promocode_extendedfeatures)
  	drop_table(:promocodes)
  end

  def self.down
  end

end
