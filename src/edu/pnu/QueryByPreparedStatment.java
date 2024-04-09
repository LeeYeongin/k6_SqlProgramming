package edu.pnu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class QueryByPreparedStatment {
	public static void main(String[] args) {
		Connection con = null;
		try {
			String driver = "com.mysql.cj.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/world";
			
			Class.forName(driver);
			con = DriverManager.getConnection(url, "scott", "tiger");
			
			PreparedStatement pt = con.prepareStatement("select id, name, countrycode, district, population from city where countrycode=?");
			pt.setString(1, "KOR");
			ResultSet rs = pt.executeQuery();
			
			while(rs.next()) {
				System.out.print(rs.getInt("id")+", ");
				System.out.print(rs.getString("name")+", ");
				System.out.print(rs.getString("countrycode")+", ");
				System.out.print(rs.getString("district")+", ");
				System.out.print(rs.getInt("population")+"\n");
			}
			rs.close();
			pt.close();
			con.close();
		}catch (Exception e) {
			System.out.println("연결 실패: " + e.getMessage());
		}
	}
}
