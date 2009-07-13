class PrintOut < ActiveRecord::Base
  set_inheritance_column nil # this way the active record modle doesn't try to use the type column as multi inheritance.
  set_table_name :printouts
end