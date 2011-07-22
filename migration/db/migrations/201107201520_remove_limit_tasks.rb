class RemoveLimitTasks < ActiveRecord::Migration

  def self.up
  	execute "delete from tasks where id in ('DiskUsage', 'LimitUpdate', 'SignUpPackageSync')"
  end

  def self.down
  end

end
