package edu.pnu;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {

	public static void main(String[] args) {
		try {
			String driver = "com.mysql.cj.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/world"; // world는 DB 이름
			String username = "scott";
			String password = "tiger";
			
			// 드라이버 로딩 (Driver.class의 인스턴스 생성 요청)
			Class.forName(driver);
			// 데이터베이스 서버 접속
			Connection con = DriverManager.getConnection(url, username, password);
			
			System.out.println("연결 성공");
			con.close();
		} catch (Exception e) {
			System.out.println("연결 실패");
			System.out.println(e.getMessage());
		}

	}

}
