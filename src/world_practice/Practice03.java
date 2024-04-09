package world_practice;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Practice03 {
	public static void main(String query) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			// 드라이버 로드
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			// 데이터베이스 서버 연결
			String urlString = "jdbc:mysql://localhost:3306/world";
			String user = "scott";
			String password = "tiger";
			con = DriverManager.getConnection(urlString, user, password);
			
			// sql문 생성 및 실행
			st = con.createStatement();
			// 국가코드가 'KOR'인 도시를 찾아 인구수를 역순으로 표시 하세요.
			rs = st.executeQuery("SELECT Name, Population"
								+ "FROM city"
								+ "WHERE CountryCode = 'KOR'"
								+ "ORDER BY Population DESC;");
			
			// sql 결과 정보
			ResultSetMetaData meta = rs.getMetaData();
			int cnt = meta.getColumnCount();
			int width = 0;
			
			// 결과 출력
			while(rs.next()) {
				for(int i=1; i<=cnt; i++) {
					width = meta.getColumnDisplaySize(i);
					width = width - rs.getString(i).length();
					System.out.print(rs.getString(i) + (i==cnt ? "\n" : " ".repeat(width + 1)));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(con != null)
					con.close();
				if(st != null)
					st.close();
				if(rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
	}
}
