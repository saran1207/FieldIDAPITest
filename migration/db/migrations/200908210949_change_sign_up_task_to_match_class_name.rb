class ChangeSignUpTaskToMatchClassName < ActiveRecord::Migration
	def self.up
		execute("update tasks set classname='com.n4systems.taskscheduling.task.SignUpPackageSyncTask' where classname='com.n4systems.taskscheduling.task.SignupPackageSyncTask';")
	end

	def self.down
		execute("update tasks set classname='com.n4systems.taskscheduling.task.SignupPackageSyncTask' where classname='com.n4systems.taskscheduling.task.SignUpPackageSyncTask';")
	end
end	