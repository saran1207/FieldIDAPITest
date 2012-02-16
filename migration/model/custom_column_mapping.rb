require 'column_mapping'

class CustomColumnMapping < ActiveRecord::Base

  set_table_name :custom_column_mappings
  set_primary_key :id

  belongs_to  :column_mapping, :foreign_key => 'column_id',     :class_name => 'ColumnMapping'

end