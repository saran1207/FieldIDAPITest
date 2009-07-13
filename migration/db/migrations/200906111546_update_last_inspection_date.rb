class UpdateLastInspectionDate < ActiveRecord::Migration
  def self.up
    execute "update products set lastinspectiondate = (select max(date) from inspectionsmaster left join inspections on(inspectionsmaster.inspection_id = inspections.id) where product_id = products.id)"
  end
  
  def self.down
  end
end