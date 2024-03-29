package edu.pnu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class QueryByStatment {

	public static void main(String[] args) {
		
		Connection con = null;
		try {
			String driver = "com.mysql.cj.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/world"; // world는 DB 이름
			String username = "scott";
			String password = "tiger";
			
			// 드라이버 로딩 (Driver.class의 인스턴스 생성 요청)
			Class.forName(driver);
			// 데이터베이스 서버 접속
			con = DriverManager.getConnection(url, username, password);
			
			Statement st = con.createStatement(); // 질의문 객체 생성
			// executeQuery: ResultSet으로 반환, executeUpdate: insert, update, delete 실행시 사용
			// query 결과는 db 서버에 session형태로 생성 (ResultSet은 현재 빈 껍데기)
			ResultSet rs = st.executeQuery("select id, name, countrycode, district, population from city limit 10");
			
			// 이때 서버로부터 하나씩 값을 가져옴(첫번째를 읽고 두번째 읽을때는 첫번째 정보를 덮어쓰고 두번째를 저장)
			while(rs.next()) { // cursor processing. cursor의 시작과 끝은 빈행
				// XXX getXXX(String column명) or getXXX(int index) -> sql의 index는 1부터 시작
				// 커서가위치한레코드의컬럼값을반환함(XXX는데이터타입)
				System.out.print(rs.getString("id")+","); 
				System.out.print(rs.getString("name")+",");
				System.out.print(rs.getString("countrycode")+",");
				System.out.print(rs.getString("district")+",");
				System.out.print(rs.getString("population")+"\n");
			}
			
			rs.close();
			st.close();
			con.close();
		} catch (Exception e) {
			System.out.println("연결 실패 : " + e.getMessage());
		}

	}

}
