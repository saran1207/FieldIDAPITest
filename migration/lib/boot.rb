require "rubygems"
require "./lib/create_environment"


ENV[ "ENVIRONMENT" ] ||= "development" 
ENV["ENVIRONMENT"] += "_non_java" unless RUBY_PLATFORM =~ /java/
puts ENV["ENVIRONMENT"]
ENV[ "DBCONFIG" ] ||= 'config/database.yml'
puts ENV[ "DBCONFIG" ]
ActiveRecord::Base.configurations = YAML::load( File.open( ENV[ "DBCONFIG" ] ) ) 
ActiveRecord::Base.logger = Logger.new(File.open('log/database.log', 'a'))


ActiveRecord::Base.establish_connection( ActiveRecord::Base.configurations[ ENV["ENVIRONMENT"] ] )