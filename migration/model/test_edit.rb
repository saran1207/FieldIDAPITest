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

describe "Asset attributes" do

  #Setup
  before(:all) do

    #Create a connection
    ENV[ "DBCONFIG" ] ||= 'c:\workspace\web\migration\config\database.yml'
    ActiveRecord::Base.configurations = YAML::load( File.open( ENV[ "DBCONFIG" ] ) )
    ActiveRecord::Base.establish_connection( ActiveRecord::Base.configurations[ "development" ] )

    #Read in the csv contents.
    @edit_csv_contents = FasterCSV.read("190TestNewEdits.csv", :quote_char => '"', :col_sep =>',', :row_sep =>:auto)

  end

  it "were set and edited for a newly created local asset (Test #1)" do

    #Check location.
    Product.find_by_serialnumber(@edit_csv_contents[1][1]).location.should == @edit_csv_contents[1][4]

    #Check attributes
    verify_product_attributes(@edit_csv_contents[1][1], [@edit_csv_contents[1][12], @edit_csv_contents[1][13],
        @edit_csv_contents[1][14], @edit_csv_contents[1][15]]).should_not be false

    #Check division
    Product.first(:include => :owner, :conditions => {:serialnumber => @edit_csv_contents[1][1]}).owner.name.should == @edit_csv_contents[1][5]

    #Check purchase and order numbers
    Product.find_by_serialnumber(@edit_csv_contents[1][1]).purchaseorder.should == @edit_csv_contents[1][7]
    verify_order_number(@edit_csv_contents[1][1], @edit_csv_contents[1][8]).should_not be false

    #Check product type.
    Product.first(:include => :productinfo, :conditions => {:serialnumber => @edit_csv_contents[1][1]}).productinfo.name.should == @edit_csv_contents[1][11]

  end

  #changed from rp10 to rp60, due to duplicate on rp10. This test may fail after cleaning DB.
  it "status of an existing online asset was correctly changed (Test #2)" do
    
    verify_product_status(@edit_csv_contents[2][1] ,@edit_csv_contents[2][6])
  end
   
  it "multiple asset edit correctly performed  (Test #3)" do

    Product.find_by_serialnumber(@edit_csv_contents[3][1]).location.should == @edit_csv_contents[3][4]
    Product.find_by_serialnumber(@edit_csv_contents[3][3]).location.should == @edit_csv_contents[3][4]
  end

  it "edit performed right after inspection on the same asset  (Test #4)" do

    verify_product_attributes(@edit_csv_contents[4][1],
      [@edit_csv_contents[4][12],@edit_csv_contents[4][13], @edit_csv_contents[4][14]]).should_not be false
  end

  
  #BUG!!!
  it "edit performed on master and sub during inspection (Test #5)"# do
  #p verify_product_status(@edit_csv_contents[5][1], @edit_csv_contents[5][6])

  #verify_identified_date(@edit_csv_contents[5][1], @edit_csv_contents[5][9])
  #end

  it "multiple single edits queued one after another (Test #6)" do

    verify_product_attributes(@edit_csv_contents[6][1],
      [@edit_csv_contents[6][12],@edit_csv_contents[6][13], @edit_csv_contents[6][14]]).should_not be false

  end

  it "edit performed on master location, propagates to sub (Test #7)" do

    #Join on sub products, ensure this subproduct's location matches the master's, which was edited.
    Product.first(:include => :subProducts, :conditions=>{:serialnumber => @edit_csv_contents[7][1]}).location.should == @edit_csv_contents[7][4]
  end

  #Reusing rp60
  it "reflect the tenant and customer for a newly created asset (Test #8)" do

    #Test Organization and customer name are correct.
    Product.first(:include => :tenant, :conditions =>{:serialnumber => @edit_csv_contents[8][1]}).tenant.name.should ==  @edit_csv_contents[8][16]
  end

end