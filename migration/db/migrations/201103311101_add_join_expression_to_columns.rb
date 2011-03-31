require 'column_mapping'

class AddJoinExpressionToColumns < ActiveRecord::Migration

  def self.up
    add_column(:column_mappings, :join_expression, :string)
    ColumnMapping.update_all({ :join_expression => "book" }, {:path_expression=>"book.name"})
    ColumnMapping.update_all({ :join_expression => "book" }, {:path_expression=>"status.displayName"})
    ColumnMapping.update_all({ :join_expression => "asset.shopOrder.order" }, {:path_expression=>"asset.shopOrder.order.orderNumber"} )
    ColumnMapping.update_all({ :join_expression => "asset.identifiedBy" }, {:path_expression=>"asset.identifiedBy.displayName"})
    ColumnMapping.update_all({ :join_expression => "owner.customerOrg" }, {:path_expression=>"owner.customerOrg.name"} )
    ColumnMapping.update_all({ :join_expression => "owner.divisionOrg" }, {:path_expression=>"owner.divisionOrg.name"} )
    ColumnMapping.update_all({ :join_expression => "owner.divisionOrg" }, {:path_expression=>"owner.seciondaryOrg.name"})
    ColumnMapping.update_all({ :join_expression => "owner.secondaryOrg" }, {:path_expression=>"owner.seciondaryOrg.name"} )
    ColumnMapping.update_all({ :join_expression => "asset.type.group" }, {:path_expression=>"asset.type.group.name"} )
    ColumnMapping.update_all({ :join_expression => "assetStatus" }, {:path_expression=>"assetStatus.name"})

    ColumnMapping.update_all({ :join_expression => "shopOrder.order" }, {:path_expression=>"shopOrder.order.orderNumber"} )
    ColumnMapping.update_all({ :join_expression => "identifiedBy" }, {:path_expression=>"identifiedBy.displayName"} )
    ColumnMapping.update_all({ :join_expression => "type.group" }, {:path_expression=>"type.group.name"}  )

    ColumnMapping.update_all({ :join_expression => "type.group" }, {:path_expression=>"asset.shopOrder.order"} )
    ColumnMapping.update_all({ :join_expression => "asset.assetStatus" }, {:path_expression=>"asset.assetStatus.name"} )
  end

  def self.down
    remove_column(:column_mappings, :join_expression)
  end
  
end