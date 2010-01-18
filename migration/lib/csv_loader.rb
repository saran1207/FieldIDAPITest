require "csv"

class CSVLoader
	
	def initialize (file_path)
		@filePath = file_path
	end

	def load()
		rowMaps = []
		firstRow = true

		headers = []
		CSV::Reader.parse(File.new(@filePath, 'r')).each do |row|
			if (firstRow)
				headers = row
				firstRow = false
			else
				rowMaps << convertRowToMap(headers, row)
			end
		end
		return rowMaps
	end

	private
	def convertRowToMap(headers, row)
		headerFieldMap = {}
		headers.each_index do |i|
			headerFieldMap[headers[i]] = row[i]
		end
		return headerFieldMap
	end

end