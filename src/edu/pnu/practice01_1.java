package edu.pnu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class practice01_1 {
	
	// world의 city table 가져오기
	public static void main(String[] args) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			String driver = "com.mysql.cj.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/world";
			String user = "scott";
			String password = "tiger";
			
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
			
			st = con.createStatement();
			rs = st.executeQuery("select * from city");
			
			// queyresult의 정보를 가져오기
			ResultSetMetaData meta = rs.getMetaData();
			int count = meta.getColumnCount();
			int width = 0;
			
			while(rs.next()) {
				// 방법 1
				for (int i=1; i<=count; i++) {
					width = meta.getColumnDisplaySize(i);
					width = width - rs.getString(i).length();
					System.out.print(rs.getString(i) + (i==count ? "\n" : " ".repeat(width + 1)));
				}

				// 방법 2
//				System.out.print(rs.getString("id")+","); 
//				System.out.print(rs.getString("name")+",");
//				System.out.print(rs.getString("countrycode")+",");
//				System.out.print(rs.getString("district")+",");
//				System.out.print(rs.getString("population")+"\n");
			}
			
		} catch (Exception e) {
			System.out.println("연결 실패: " + e.getMessage());
		} finally {
			// 자원 해제
			try {				
				if (rs != null)
					rs.close();
				if (con != null)
					con.close();
				if (st != null)
					st.close();
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
	}
}
