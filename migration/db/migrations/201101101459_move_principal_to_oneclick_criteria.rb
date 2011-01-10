class MovePrincipalToOneclickCriteria < ActiveRecord::Migration

  def self.up
    add_column(:oneclick_criteria, :principal, :boolean, :null => false, :default => false)
    execute("update criteria c, oneclick_criteria o set o.principal = c.principal where c.id = o.id")
    remove_column(:criteria, :principal)
  end

  def self.down
    add_column(:criteria, :principal, :boolean, :null => false, :default => false)
    execute("update criteria c, oneclick_criteria o set c.principal = o.principal where c.id = o.id")
    remove_column(:oneclick_criteria, :principal)
  end

end