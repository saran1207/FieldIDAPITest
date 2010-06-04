require 'faster_csv'
require 'test_helpers'
require 'product'
require 'abstract_inspection'
require 'inspection_info_option'
require 'sub_product'
require 'criteria_result'
require 'inspection_schedule'
require 'criteriaresults_deficiencies'
require 'inspection_file_attachment'
require 'sub_inspections'
require 'inspectionmaster'
require "product_status"
require 'yaml'

describe "Asset attributes" do

  #Setup
  before(:all) do

    #Create a connection
    ENV[ "DBCONFIG" ] ||= 'c:\workspace\web\migration\config\database.yml'
    ActiveRecord::Base.configurations = YAML::load( File.open( ENV[ "DBCONFIG" ] ) )
    ActiveRecord::Base.establish_connection( ActiveRecord::Base.configurations[ "development" ] )

    #Read in the yaml contents.
    TESTDATA = YAML::load(File.read('c:\workspace\web\migration\model\test_config.yml'))

  end

  it "were set and edited for a newly created local asset (Test #1)" do
    test_data = TESTDATA['AttributeTests']['Test1']

    #Check location.
    Product.find_by_serialnumber(test_data['SerialNumber']).location.should == test_data['Location']

    #Check attributes
    verify_product_attributes(test_data['SerialNumber'], [test_data['Attribute1'], test_data['Attribute2'],
        test_data['Attribute3'], test_data['Attribute4']]).should_not be false

    #Check division
    Product.first(:include => :owner, :conditions => {:serialnumber => test_data['SerialNumber']}).owner.name.should == test_data['Division']

    #Check purchase and order numbers
    Product.find_by_serialnumber( test_data['SerialNumber']).purchaseorder.should ==  test_data['PurchaseOrder']
    verify_order_number(test_data['SerialNumber'], test_data['OrderNumber']).should_not be false

    #Check product type.
    Product.first(:include => :productinfo, :conditions => {:serialnumber => test_data['SerialNumber']}).productinfo.name.should == test_data['ProductType']

  end

  #changed from rp10 to rp60, due to duplicate on rp10. This test may fail after cleaning DB.
  it "status of an existing online asset was correctly changed (Test #2)" do
    
    test_data = TESTDATA['AttributeTests']['Test2']
    verify_product_status(test_data['SerialNumber'] , test_data['Status'])
  end
   
  it "multiple asset edit correctly performed  (Test #3)" do
    
 
    test_data = TESTDATA['AttributeTests']['Test3']
    Product.find_by_serialnumber(test_data['SerialNumber']).location.should == test_data['Location']
    Product.find_by_serialnumber(test_data['MultiEditComponent1']).location.should == test_data['Location']
    Product.find_by_serialnumber(test_data['MultiEditComponent2']).location.should == test_data['Location']
  end

  it "edit performed right after inspection on the same asset  (Test #4)" do
    test_data = TESTDATA['AttributeTests']['Test4']
  
    verify_product_attributes(test_data['SerialNumber'],
      [test_data['Attribute1'],test_data['Attribute2'], test_data['Attribute3']]).should_not be false
  end

  
  #BUG!!!
  it "edit performed on master and sub during inspection (Test #5)"# do
  #p verify_product_status(@edit_csv_contents[5][1], @edit_csv_contents[5][6])

  #verify_identified_date(@edit_csv_contents[5][1], @edit_csv_contents[5][9])
  #end

  it "multiple single edits queued one after another (Test #6)" do
    test_data = TESTDATA['AttributeTests']['Test6']
    verify_product_attributes(test_data['SerialNumber'],
      [test_data['Attribute1'], test_data['Attribute2'], test_data['Attribute3']]).should_not be false

  end

  it "edit performed on master location, propagates to sub (Test #7)" do
    test_data = TESTDATA['AttributeTests']['Test7']
    #Join on sub products, ensure this subproduct's location matches the master's, which was edited.
    Product.first(:include => :subProducts, :conditions=>{:serialnumber => test_data['SerialNumber']}).location.should == test_data['Location']
  end

  #Reusing rp60
  it "reflect the tenant and customer for a newly created asset (Test #8)" do
    test_data = TESTDATA['AttributeTests']['Test8']
    #Test Organization and customer name are correct.
    Product.first(:include => :tenant, :conditions =>{:serialnumber => test_data['SerialNumber']}).tenant.name.should ==  test_data['Tenant']
  end

end