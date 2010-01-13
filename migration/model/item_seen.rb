class ItemSeen < ActiveRecord::Base
  set_table_name :seenitstorageitem_itemsseen
  set_primary_key [:seenitstorageitem_id, :element]
end