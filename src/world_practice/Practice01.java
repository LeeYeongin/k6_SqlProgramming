package world_practice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Practice01 {

	public static void main(String[] args) {
		try (Scanner sc = new Scanner(System.in)) {
			System.out.print("Table Name: ");
			String tname = sc.next();
			test(tname);
		}
	}

	public static void test(String tname) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		try {
			// 드라이버 로드
			Class.forName("com.mysql.cj.jdbc.Driver");

			// 데이터베이스 서버 접속
			String url = "jdbc:mysql://localhost:3306/world";
			String user = "scott";
			String password = "tiger";
			con = DriverManager.getConnection(url, user, password);

			// sql문 생성
			st = con.createStatement();
			rs = st.executeQuery("select * from " + tname + " limit 10");

			ResultSetMetaData meta = rs.getMetaData();
			int count = meta.getColumnCount();
			int width = 0;
			
			while(rs.next()) {
				for (int i=1; i<=count; i++) {
					width = meta.getColumnDisplaySize(i);
					width = (rs.getString(i) != null ? width - rs.getString(i).length() : width - 4);
					System.out.print(rs.getString(i) + (i==count ? "\n" : " ".repeat(width + 3)));
				}	
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 자원 해제
			try {
				if (rs != null)
					rs.close();
				if (con != null)
					con.close();
				if (st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
