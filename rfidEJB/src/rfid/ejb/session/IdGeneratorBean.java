package rfid.ejb.session;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import javax.sql.DataSource;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import com.n4systems.ejb.interceptor.TimingInterceptor;

@Interceptors({TimingInterceptor.class})
@Stateless 
public class IdGeneratorBean implements IdGenerator {
	@Resource (mappedName="java:/RFID_DS")
	DataSource dataSource;

	public String getMaxNo() {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rst = null;

		String maxNo = null;
		String sqlStr = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		String curDayStr = "P" + sdf.format(new Date(System.currentTimeMillis()));
		try {
			conn = dataSource.getConnection();
			sqlStr = "SELECT MAX(SerialNumber) FROM ProductSerial WHERE "
					+ "SUBSTRING(SerialNumber, 1, 7) = '" + curDayStr + "' ";

			stmt = conn.prepareStatement(sqlStr);

			rst = stmt.executeQuery();
			if (rst.next()) {
				if (rst.getString(1) != null) {
					int no = Integer.parseInt(rst.getString(1).substring(7, 12)) + 1;
					if (no < 10) {
						maxNo = "0000" + no;
					} else if (no < 100) {
						maxNo = "000" + no;
					} else if (no < 1000) {
						maxNo = "00" + no;
					} else if (no < 10000) {
						maxNo = "0" + no;
					} else {
						maxNo = "" + no;
					}
				} else {
					maxNo = "00001";
				}
			} else {
				maxNo = "00001";
			}
			rst.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return curDayStr + maxNo;
	}
}
