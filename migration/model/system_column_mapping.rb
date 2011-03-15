require 'column_mapping'

class SystemColumnMapping < ActiveRecord::Base
  set_table_name :system_column_mappings
  set_primary_key :id

  belongs_to  :column_mapping, :foreign_key => 'column_id',     :class_name => 'ColumnMapping'

end