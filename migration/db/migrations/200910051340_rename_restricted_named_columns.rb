class RenameRestrictedNamedColumns < ActiveRecord::Migration
  def self.up
    rename_column(:lineitems, :index, :idx)
    rename_column(:findproductoption, :optionkey, :identifier)
    rename_column(:configurations, :name, :identifier)
  end
end