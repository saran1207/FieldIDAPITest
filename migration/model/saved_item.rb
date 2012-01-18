class SavedItem < ActiveRecord::Base
    set_table_name :saved_items
    set_inheritance_column :dummy_column_not_used
end