 #Note: Load up fieldidDumpCreatedJobAccess.sql dump that contains jobs functionality prior to running this script, if db is fresh. 
require 'project'
require
 #Setup
 describe "A job" do
  before(:all) do

    #Create a connection
    ENV[ "DBCONFIG" ] ||= 'c:\workspace\web\migration\config\database.yml'
    ActiveRecord::Base.configurations = YAML::load( File.open( ENV[ "DBCONFIG" ] ) )
    ActiveRecord::Base.establish_connection( ActiveRecord::Base.configurations[ "development" ] )

    #Read in the csv contents.


    TESTDATA = YAML::load(File.read('c:\workspace\web\migration\model\test_config.yml'))
  end

   it "is correctly downloaded and completed for single asset"
   
   it "is correctly downloaded and complete for master with sub"

 
 end