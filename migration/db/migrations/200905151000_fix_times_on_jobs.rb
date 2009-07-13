require "project"
require "user"
require "permissions"
require "permission_action"
require "tzinfo"
class FixTimesOnJobs < ActiveRecord::Migration
  def self.up
    manage_jobs = PermissionAction.find(:first, :conditions => { :mapkey => "managejobs" })
    users_with_jobs_permission = Permissions.find(:all, :conditions => { :r_permissionaction => manage_jobs.uniqueid })
    user_ids_with_jobs = []
    users_with_jobs_permission.each do |permission|
      user_ids_with_jobs << permission.r_fieldiduser
    end
  
    Project.find_each do |project|
      user_with_job = User.find(:first, :conditions => { :r_tenant => project.r_tenant, :uniqueid => user_ids_with_jobs })
      if user_with_job.nil?
        raise Exception.new
      end
      
      time_zone = TZInfo::Timezone.get(user_with_job.timezoneid)
      puts project.projectid
      project.started = time_zone.local_to_utc(project.started) unless project.started.nil?
      project.estimatedcompletion = time_zone.local_to_utc(project.estimatedcompletion) unless project.estimatedcompletion.nil?
      project.actualcompletion = time_zone.local_to_utc(project.actualcompletion) unless project.actualcompletion.nil?
      project.save
    end
      
  end
  
  def self.down
  end
end