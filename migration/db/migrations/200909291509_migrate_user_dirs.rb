class MigrateUserDirs < ActiveRecord::Migration
  
  def self.up
    `mkdir -p /var/fieldid/private/users`
    `cd /var/fieldid/private/images && for tenant in *; do mv ${tenant}/users /var/fieldid/private/users/${tenant}; done`
  end

  def self.down
  end

end