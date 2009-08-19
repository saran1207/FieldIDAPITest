class InsertInitialSignupPackages < ActiveRecord::Migration
	def self.up
    	execute("insert into tasks (created, modified, classname, cronexpression, taskgroup, enabled) values (now(), now(), 'com.n4systems.taskscheduling.task.SignupPackageSyncTask', '0 0 * * *', 'default', true);")
    	
    	execute("delete from signuppackages;");
    	execute("delete from contractpricings;")
		execute("insert into signuppackages (created, modified, syncid, name, diskspace_limit, user_limit, asset_limit) values (now(), now(), 'FIDFREE', 'Free', 5, 1, 5);");	      		
		execute("insert into signuppackages (created, modified, syncid, name, diskspace_limit, user_limit, asset_limit) values (now(), now(), 'FIDBASIC', 'Basic', 50, 5, 250);");	      		
		execute("insert into signuppackages (created, modified, syncid, name, diskspace_limit, user_limit, asset_limit) values (now(), now(), 'FIDPLUS', 'Plus', 1000, -1, -1);");	      		
		execute("insert into signuppackages (created, modified, syncid, name, diskspace_limit, user_limit, asset_limit) values (now(), now(), 'FIDENTERPRISE', 'Enterprise', 1000, -1, -1);");	      		
		execute("insert into signuppackages (created, modified, syncid, name, diskspace_limit, user_limit, asset_limit) values (now(), now(), 'FIDUNLIMITED', 'Unlimited', 1000, -1, -1);");	
		
		execute("delete from signuppackages_extendedfeatures;")
		execute("insert into signuppackages_extendedfeatures (signuppackage_id, element) values ((select id from signuppackages where syncid='FIDBASIC' limit 1), 'EmailAlerts');")
		execute("insert into signuppackages_extendedfeatures (signuppackage_id, element) values ((select id from signuppackages where syncid='FIDPLUS' limit 1), 'EmailAlerts');")
		execute("insert into signuppackages_extendedfeatures (signuppackage_id, element) values ((select id from signuppackages where syncid='FIDENTERPRISE' limit 1), 'EmailAlerts');")
		execute("insert into signuppackages_extendedfeatures (signuppackage_id, element) values ((select id from signuppackages where syncid='FIDUNLIMITED' limit 1), 'EmailAlerts');")
		execute("insert into signuppackages_extendedfeatures (signuppackage_id, element) values ((select id from signuppackages where syncid='FIDPLUS' limit 1), 'Projects');")
		execute("insert into signuppackages_extendedfeatures (signuppackage_id, element) values ((select id from signuppackages where syncid='FIDENTERPRISE' limit 1), 'Projects');")
		execute("insert into signuppackages_extendedfeatures (signuppackage_id, element) values ((select id from signuppackages where syncid='FIDUNLIMITED' limit 1), 'Projects');")
		execute("insert into signuppackages_extendedfeatures (signuppackage_id, element) values ((select id from signuppackages where syncid='FIDENTERPRISE' limit 1), 'Branding');")
		execute("insert into signuppackages_extendedfeatures (signuppackage_id, element) values ((select id from signuppackages where syncid='FIDUNLIMITED' limit 1), 'Branding');")
		execute("insert into signuppackages_extendedfeatures (signuppackage_id, element) values ((select id from signuppackages where syncid='FIDENTERPRISE' limit 1), 'PartnerCenter');")
		execute("insert into signuppackages_extendedfeatures (signuppackage_id, element) values ((select id from signuppackages where syncid='FIDUNLIMITED' limit 1), 'PartnerCenter');")
		execute("insert into signuppackages_extendedfeatures (signuppackage_id, element) values ((select id from signuppackages where syncid='FIDENTERPRISE' limit 1), 'Compliance');")
		execute("insert into signuppackages_extendedfeatures (signuppackage_id, element) values ((select id from signuppackages where syncid='FIDUNLIMITED' limit 1), 'Compliance');")
	end
	
	def self.down
		execute("delete from tasks where classname='com.n4systems.taskscheduling.task.SignupPackageSyncTask'")
    	execute("delete from signuppackages")
		execute("delete from signuppackages_extendedfeatures;")	
	end
end