require 'column_mapping'

class AddJoinExpressionToRemainingColumns < ActiveRecord::Migration

  def self.up
    ColumnMapping.update_all({ :join_expression => "owner.secondaryOrg" }, {:path_expression=>"owner.internalOrg.name"} )
    ColumnMapping.update_all({ :join_expression => "asset.shopOrder" }, {:path_expression=>"asset.shopOrder.description"} )
  end

  def self.down
    ColumnMapping.update_all({ :join_expression => nil }, {:path_expression=>"owner.internalOrg.name"} )
    ColumnMapping.update_all({ :join_expression => nil }, {:path_expression=>"asset.shopOrder.description"} )
  end

end