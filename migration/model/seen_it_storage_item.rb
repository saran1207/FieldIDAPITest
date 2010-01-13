require "item_seen"

class SeenItStorageItem < ActiveRecord::Base
  set_table_name :seenitstorageitem
  has_many :item_seen , :foreign_key => :seenitstorageitem_id, :class_name => ItemSeen
  
end