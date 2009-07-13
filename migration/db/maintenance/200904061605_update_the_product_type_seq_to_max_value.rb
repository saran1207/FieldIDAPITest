require "product_type"
class UpdateTheProductTypeSeqToMaxValue < ActiveRecord::Migration
  def self.up
      execute "select setval('producttypes_id_seq', (select MAX(id) + 1 from producttypes))"
  end
  
end