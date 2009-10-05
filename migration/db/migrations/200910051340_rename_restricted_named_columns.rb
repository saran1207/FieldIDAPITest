class RenameRestrictedNamedColumns < ActiveRecord::Migration
  def self.up
    rename_column(:lineitems, :index, :idx)
    rename_column(:tagoptions, :key, :optionkey)
    rename_column(:findproductoption, :key, :identifier)
    rename_column(:configurations, :key, :identifier)
  end
end