class ColumnMapping < ActiveRecord::Base
  set_table_name :column_mappings
  belongs_to  :column_mapping_group, :foreign_key => 'group_id',     :class_name => 'ColumnMappingGroup'
  has_many  :column_layouts
  has_many :active_column_mappings
end