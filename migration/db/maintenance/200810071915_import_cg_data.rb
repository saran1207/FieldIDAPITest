require "product"
require "product_type"


=begin 

  This migration is a one off data import for CG.  It works off a csv file which was converted from an excel document.

  Here is now the original doc was converted:

  1.) In Excel (or oocalc), the doc from cg was saved to csv (with quoted strings).  we'll call this file cg_data_raw.csv
  2.) Next we needed to extract only the fields which we will be updating.  In this step we also concat the date field by 
        seperating the month and year fields by a '/' and convert the 2 digit years to 4 digit years by prepending 20 to the year.
      
        The following command was used:
          cat cg_data_raw.csv |awk 'BEGIN { FS = "," } ; $8 !~ /^$/ || $9 !~ /^$/ || $10 !~ /^$/ || $11 !~ /^$/  { printf "%s,%s,%s,%s/20%s\n", $2,$8,$9,$10,$11 }'|tr -d "\"" > cg_data.csv

  3.) The title fields on the first line need to be renamed to the fields that they will update.  Eg/ "Reel/ID" becomes reelid, "COMMENT TAG" becomes comments and so on.
        The titles for the info option columns, match the exact names of the InfoFields which they are for.  Eg/ "COMMENT MODEL" becomes "Model No.".  This was done by hand in vi
  4.) Some of the date fields created in step 2 were malformed.  These needed to be fixed in vi.
        - If the month and year field were empty, the resulting date field was '/20'.  We used the following sed statement in vi to clear these entries
            :%s/\/20$//g
            - this statement simple clears out any chars like '/20' that are the last chars on the line
        - Some of the years given in the original file were acutally single digit years.  The resulting year will be something like /208.  Another sed statement in vi was used to resolve this.
            :%s/\/20\([0-9]\)$/\/200\1/g
            - this statement looks for strings like '/207' which end the line, captures the last number ('7') and replaces it with '/200' re-adding the last character ('/2007')   

=end
class ImportCgData < ActiveRecord::Migration
  DATA_FILE_NAME="db/imports/data/200810071915_cg_data.csv"
  
  SERIAL_FIELD=0
  COMMENT_FIELD=1
  FIRST_INFO_OPTION = 2
  
  def self.up
    ProductSerial.transaction do
      
      # first we should check if our datafile exists
      if !File.exist?(DATA_FILE_NAME)
        raise "Could not find data file at [" + DATA_FILE_NAME + "]"
      end
      
      dataFile = File.open(DATA_FILE_NAME)
      
      # we should limit our product searches by tenant and product type
      tenantId = 132385
      productTypeId = 18
      
      # lets find our product type so we can resolve the infoFields
      productType = ProductType.find(:first, :conditions => ["r_tenant = :tenantId and uniqueid = :typeId", {:tenantId => tenantId, :typeId => productTypeId}])
      
      if productType.nil?
        raise "Cannot find ProductType with uniqueid [" + productTypeId + "] and tenantId [" + tenantId + "]"
      end
      
      puts "Found ProductType [" + productType.displayString + "]"
      
      # split the title line
      titles = dataFile.readline.split(',')
      
      # clear off any whitespace from the title elements
      titles.each { |title| title.strip! }
      
      # we take the first title field to be the field used to resolve the product (this should be either serialnumber or reelid)
      serialNumberField = titles[0]
      
      infoFields = Array.new
      # lets resolve the infoFields for each of these titles
      for i in (FIRST_INFO_OPTION..(titles.size-1))
        
        # try and resolve an info field by it's name
        field = productType.findInfoFieldByName(titles[i])
        
        if field.nil?
          raise "Cannot find InfoField on ProductType [" + productType.displayString + "] named [" + titles[i] + "]"
        end
        
        # add it to our info field list
        puts "Found InfoField [" + field.displayString + "]"
        infoFields << field
      end
      
      # walk the file
      dataFile.each { |line|
        
        fields = line.split(',')
        
        # the first field is always the serial
        serial = fields[SERIAL_FIELD].strip
        comments = fields[COMMENT_FIELD].strip
        
        product = ProductSerial.find(:first, :conditions => ["r_tenant = :tenantId and r_productinfo = :typeId and " + serialNumberField + " = :serial", {:tenantId => tenantId, :typeId => productTypeId, :serial => serial}])
        
        # if you can't find a product, then skip it
        if product.nil?
          puts "WARNING: No product found for Tenant [" + tenantId.to_s + "] and ReelId [" + serial.to_s + "]. Skipping this product..."
          next
        end
        
        puts "Found Product [" + product.displayString + "]"
    
        puts "Updating Comments: [" + comments + "]"
        product.comments = comments
        
        # now we get the value of each column and create our info options
        for i in (0..(titles.size-FIRST_INFO_OPTION-1))
          # we've started indexing at 0 so we need to shift the field
          fieldNum = i + FIRST_INFO_OPTION
          
          # make sure we decode our nulls =:<
          name = (fields[fieldNum].nil?) ? "" : fields[fieldNum].strip
          
          # we don't want to add empty data, if name is 0 length then skip it
          if name.length == 0
            puts "WARNING: Field value is 0 length .  Skipping this field..."
            next
          end
          
          # we need to make sure that there is not already an info option on the product
          infoOption = product.findInfoOptionByInfoField(infoFields[i])
          
          # if the info option already exists, then we will skip this field.  We do not want to change or destroy any data.
          if !infoOption.nil?
            puts "WARNING: InfoOption [" + infoOption.displayString + "] for InfoField [" +  infoOption.infoField.displayString + "] already exists on product.  Skipping this field..."
            next
          end
          
          # get the infoOption weight from the ProductSerial
          nextWeight = product.getNextInfoOptionWeight()
          
          # let create the new info option.  Note that we can only handle updating non-static data right now
          infoOption = InfoOption.create(
                                        :name => name,
                                        :infoField => infoFields[i],
                                        :staticdata => false,
                                        :weight => nextWeight
                                       )
          
          puts "Created InfoOption: [" + infoOption.displayString + "] weight [" + nextWeight.to_s + "] for InfoField [" + infoFields[i].displayString + "]"
          
          # now lets add our info option to the product
          product.infoOptions << infoOption
          
        end # for i in (0..(titles.size-FIRST_INFO_OPTION-1))
        
        # update the changes to the product
        product.save
        
      } # dataFile.each { |line|
      
    end # ProductSerial.transaction do
  end # def self.up
  
  def self.down
    ProductSerial.transaction do
      # nothing to do here ...
    end
  end
end